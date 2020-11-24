package com.mrfsong.open.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ZkClientTest {


    @Test
    public void execute() throws Exception {
        ZooKeeper zk = new ZooKeeper("127.0.0.1:3181,127.0.0.1:3182,127.0.0.1:3183", 3000, new Watcher()
        {
            // 监控所有被触发的事件
            public void process(WatchedEvent event)
            {
                log.info(event.toString());
            }
        });

        zk.create("/zkTest","hello".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zk.setData("/zkTest","hello2".getBytes(),-1);

        zk.getChildren("/zTest",false);

        TimeUnit.SECONDS.sleep(30);
        zk.close();

    }



    @Test
    public void testElection() {
        ExecutorService executorService = Executors.newCachedThreadPool();
    }
}
