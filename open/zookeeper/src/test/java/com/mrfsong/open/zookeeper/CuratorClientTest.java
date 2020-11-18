package com.mrfsong.open.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CuratorClientTest {

    private static final String PATH = "/test";
    private static final String PATH_EPHEMERAL = "/test/eph";
    private static final String PATH_PERSISTENT = "/test/persist";
    private static final String PATH_SEQUENTIAL = "/test/seq";
    private static final String PATH_LEADER = "/test/leader";
    private static final String ZK_SERVER_URL = "127.0.0.1:2181";

    private CuratorFramework client;
    private ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);

    @Before
    public void prepare() {

        client = getClient(ZK_SERVER_URL, retryPolicy);

    }

    @After
    public void destory() {
        if (client != null && client.isStarted()) {
            client.close();
        }
    }

    @Test
    public void execute() throws Exception {

        //创建节点
        client.start();

        //创建普通节点 （默认：CreateMode#PERSISTENT）
        create(PATH, "test".getBytes());

        createEphemeral(PATH_EPHEMERAL, "EPHEMERAL".getBytes());
        createEphemeralSequential(PATH_SEQUENTIAL, "EPHEMERAL SEQUENTIAL".getBytes());

        setData(PATH, "test2".getBytes());


        TimeUnit.MINUTES.sleep(5);
        client.close();

    }

    /**
     * 通过LeaderSelectorListener可以对领导权进行控制， 在适当的时候释放领导权，这样每个节点都有可能获得领导权。
     * 而LeaderLatch则一直持有leadership， 除非调用close方法，否则它不会释放领导权。
     */
    @Test
    public void testLeader() {

        List<LeaderSelector> selectors = new ArrayList<>();
        List<CuratorFramework> clients = new ArrayList<>();

        try {

            for (int i = 0; i < 10; i++) {

                CuratorFramework client = getClient(ZK_SERVER_URL, retryPolicy);
                client.start();
                clients.add(client);


                final String name = "client#" + i;

                LeaderSelector leaderSelector = new LeaderSelector(client, PATH, new LeaderSelectorListenerAdapter() {
                    @Override
                    public void takeLeadership(CuratorFramework client) throws Exception {
                        log.warn(name + ":I am leader.");
                        Thread.sleep(2000);
                    }

                });


                leaderSelector.autoRequeue();
                leaderSelector.start();
                selectors.add(leaderSelector);
            }

            Thread.sleep(Integer.MAX_VALUE);

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            for (CuratorFramework client : clients) {

                CloseableUtils.closeQuietly(client);

            }


            for (LeaderSelector selector : selectors) {

                CloseableUtils.closeQuietly(selector);

            }


        }


    }

    /**
     * 随机从候选着中选出一台作为leader，选中之后除非调用close()释放leadship，否则其他的后选择无法成为leader
     */
    @Test
    public void testLeaderLatch() {
        List<LeaderLatch> latchList = new ArrayList<>();
        List<CuratorFramework> clients = new ArrayList<>();
        try {
            for (int i = 0; i < 10; i++) {
                CuratorFramework client = getClient(ZK_SERVER_URL, retryPolicy);
                client.start();
                clients.add(client);


                final LeaderLatch leaderLatch = new LeaderLatch(client, PATH, "client#" + i);
                leaderLatch.addListener(new LeaderLatchListener() {
                    @Override
                    public void isLeader() {
                        log.warn(leaderLatch.getId() + ":I am leader. I am doing jobs!");
                    }

                    @Override
                    public void notLeader() {
                        log.warn(leaderLatch.getId() + ":I am not leader. I will do nothing!");
                    }
                });

                latchList.add(leaderLatch);
                leaderLatch.start();
            }

            Thread.sleep(1000 * 60);

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            for (CuratorFramework client : clients) {
                CloseableUtils.closeQuietly(client);
            }

            for (LeaderLatch leaderLatch : latchList) {
                CloseableUtils.closeQuietly(leaderLatch);

            }

        }
    }

    private CuratorFramework getClient(String addrString, RetryPolicy retryPolicy) {
        return CuratorFrameworkFactory.builder()
                .connectString(addrString)
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(300_000)
                .sessionTimeoutMs(120_000)
                .build();
    }

    private void create(String path, byte[] payload) throws Exception {
        // this will create the given ZNode with the given data
        client.create().creatingParentsIfNeeded().forPath(path, payload);
    }

    private void createEphemeral(String path, byte[] payload) throws Exception {
        // this will create the given EPHEMERAL ZNode with the given data
        client.create().withMode(CreateMode.EPHEMERAL).forPath(path, payload);
    }

    private String createEphemeralSequential(String path, byte[] payload) throws Exception {
        // this will create the given EPHEMERAL-SEQUENTIAL ZNode with the given data using Curator protection.

        /*
            Protection Mode:

            It turns out there is an edge case that exists when creating sequential-ephemeral nodes. The creation
            can succeed on the server, but the server can crash before the created node name is returned to the
            client. However, the ZK session is still valid so the ephemeral node is not deleted. Thus, there is no
            way for the client to determine what node was created for them.

            Even without sequential-ephemeral, however, the create can succeed on the sever but the client (for various
            reasons) will not know it. Putting the create builder into protection mode works around this. The name of
            the node that is created is prefixed with a GUID. If node creation fails the normal retry mechanism will
            occur. On the retry, the parent path is first searched for a node that has the GUID in it. If that node is
            found, it is assumed to be the lost node that was successfully created on the first try and is returned to
            the caller.
         */
        return client.create().withProtection().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path, payload);
    }

    private void setData(String path, byte[] payload) throws Exception {
        // set data for the given node
        client.setData().forPath(path, payload);
    }

    private void setDataAsync(String path, byte[] payload) throws Exception {
        // this is one method of getting event/async notifications
        CuratorListener listener = new CuratorListener() {
            @Override
            public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
                // examine event for details
                log.info("Event Type : {}", event.getType().name());
            }
        };
        client.getCuratorListenable().addListener(listener);

        // set data for the given node asynchronously. The completion notification
        // is done via the CuratorListener.
        client.setData().inBackground().forPath(path, payload);
    }

    private void setDataAsyncWithCallback(BackgroundCallback callback, String path, byte[] payload) throws Exception {
        // this is another method of getting notification of an async completion
        client.setData().inBackground(callback).forPath(path, payload);
    }

    private void delete(CuratorFramework client, String path) throws Exception {
        // delete the given node
        client.delete().forPath(path);
    }

    private void guaranteedDelete(String path) throws Exception {
        // delete the given node and guarantee that it completes

        /*
            Guaranteed Delete

            Solves this edge case: deleting a node can fail due to connection issues. Further, if the node was
            ephemeral, the node will not get auto-deleted as the session is still valid. This can wreak havoc
            with lock implementations.


            When guaranteed is set, Curator will record failed node deletions and attempt to delete them in the
            background until successful. NOTE: you will still get an exception when the deletion fails. But, you
            can be assured that as long as the CuratorFramework instance is open attempts will be made to delete
            the node.
         */

        client.delete().guaranteed().forPath(path);
    }

    private List<String> watchedGetChildren(String path) throws Exception {
        /**
         * Get children and set a watcher on the node. The watcher notification will come through the
         * CuratorListener (see setDataAsync() above).
         */
        return client.getChildren().watched().forPath(path);
    }

    private List<String> watchedGetChildren(String path, Watcher watcher) throws Exception {
        /**
         * Get children and set the given watcher on the node.
         */
        return client.getChildren().usingWatcher(watcher).forPath(path);
    }


}
