package com.mrfsong.cache.guava;


import com.google.common.base.Strings;
import com.google.common.cache.*;
import com.mrfsong.cache.guava.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.openjdk.jol.info.GraphLayout;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * <p>
 *      ehcache用法测试
 * </p>
 *
 * @Author: Felix
 * @Created: 2020/08/10 10:15
 */
@Slf4j
public class GuavaCacheTest {


    @Test
    public void withKeyString() throws Exception{

        int size = 100;
        LoadingCache<String, String> localCache = CacheBuilder.newBuilder()
                .initialCapacity(10)
                .maximumSize(size)
                .concurrencyLevel(3)
                .recordStats()
                .removalListener((RemovalListener<String, String>) notification -> {
                    log.warn("key:[{}] is removed ...",notification.getKey());


                    switch (notification.getCause()) {
                        //The entry was manually removed by the user
                        case EXPLICIT:
                            break;

                        //The entry itself was not actually removed, but its value was replaced by the user
                        case REPLACED:
                            break;

                        //The entry was removed automatically because its key or value was garbage-collected
                        case COLLECTED:
                            break;

                        //The entry's expiration timestamp has passed
                        case EXPIRED:
                            break;

                        //The entry was evicted due to size constraints
                        case SIZE:
                            break;

                        default:
                            break;

                    }
                })
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .build(new CacheLoader<String, String>() {
                    //默认的数据加载实现,当调用get取值的时候,如果key没有对应的值,就调用这个方法进行加载，load操作时为单线程阻塞操作
                    @Override
                    public String load(String key) throws Exception {
                        log.warn("key:[{}] not exists , begin to loading ..." , key);
                        return "initVal_" + (System.currentTimeMillis());
                    }
                });


        String val = localCache.getIfPresent("123");
        Assert.assertNull(val);
        val = localCache.get("123");
        log.info("val ======> {}" , val);
        Assert.assertFalse(Strings.isNullOrEmpty(val));

        IntStream.range(1, size).forEach(element -> {
            localCache.put(""+element,"" + System.currentTimeMillis());
        });

        log.warn("finish init {} element ......" , size);
        log.warn("total size : {}", GraphLayout.parseInstance(localCache).totalSize());

//        Thread.sleep(1000 * 60 * 5L);

        /*log.info("Internal of List : {}", ClassLayout.parseInstance(valList).toPrintable());
        log.info("Internal of String : {}",ClassLayout.parseInstance(strValue).toPrintable());

        log.info("External of List : {}", GraphLayout.parseInstance(valList).toPrintable());
        log.info("External of String : {}",GraphLayout.parseInstance(strValue).toPrintable());

        log.info("List total size :　{}",GraphLayout.parseInstance(valList).totalSize());*/



    }

    @Test
    public void withKeyObject() throws Exception {

        LoadingCache<User, String> localCache = CacheBuilder.newBuilder()
                .initialCapacity(1)
                .maximumSize(1)
                .concurrencyLevel(3)
                .recordStats()

                //时间监听会阻塞put方法、直到监听时间处理完毕
                .removalListener((RemovalListener<User, String>) notification -> {
                    log.warn("key:[{}] is removed ,cause : {}",notification.getKey() , notification.getCause());

                    try {
                        Thread.sleep(1000 * 3L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    switch (notification.getCause()) {
                        //The entry was manually removed by the user
                        case EXPLICIT:
                            break;

                        //The entry itself was not actually removed, but its value was replaced by the user
                        case REPLACED:
                            break;

                        //The entry was removed automatically because its key or value was garbage-collected
                        case COLLECTED:
                            break;

                        //The entry's expiration timestamp has passed
                        case EXPIRED:
                            break;

                        //The entry was evicted due to size constraints
                        case SIZE:
                            break;

                        default:
                            break;

                    }
                })
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .build(new CacheLoader<User, String>() {
                    //默认的数据加载实现,当调用get取值的时候,如果key没有对应的值,就调用这个方法进行加载
                    @Override
                    public String load(User key) throws Exception {
                        log.warn("key:[{}] not exists , begin to loading ..." , key);
                        return "initVal_" + (System.currentTimeMillis());
                    }
                });


        localCache.put(new User("u1",28),"hello");
        localCache.put(new User("u2",30),"hello2");
        log.info("================== Finish ==================");




    }

    /**
     * 默认情况下，监听器方法是被同步调用的（在移除缓存的那个线程中执行）。如果监听器方法比较耗时，会导致调用者线程阻塞时间变长;
     * 需要使用RemovalListeners.asynchronous方式开启异步监听
     * @throws Exception
     */
    @Test
    public void withAsyncListener() throws Exception {
        RemovalListener<User, String> removalListener = notification -> {
            try {
                Thread.sleep(1000 * 10L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("Async removal listener , key : {} , cause : {}", notification.getKey().toString(), notification.getCause());
        };

        //注册异步监听器
        RemovalListener<User, String> asyncRemovalListener = RemovalListeners.asynchronous(removalListener, Executors.newSingleThreadExecutor());
        LoadingCache<User, String> localCache = CacheBuilder.newBuilder()
                .initialCapacity(1)
                .maximumSize(10)
                .concurrencyLevel(3)
                .recordStats()

                //时间监听会阻塞put方法、直到监听时间处理完毕
                .removalListener(asyncRemovalListener)
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .expireAfterAccess(1,TimeUnit.HOURS)
                .refreshAfterWrite(10,TimeUnit.MINUTES)

                //weakKeys和weakValues方法指定Cache只保存对缓存记录key和value的弱引用。这样当没有其他强引用指向key和value时，key和value对象就会被垃圾回收器回收
                .weakKeys()
                .weakValues()
                .build(new CacheLoader<User, String>() {
                    //默认的数据加载实现,当调用get取值的时候,如果key没有对应的值,就调用这个方法进行加载
                    @Override
                    public String load(User key) throws Exception {
                        log.warn("key:[{}] not exists , begin to loading ..." , key);
                        return "initVal_" + (System.currentTimeMillis());
                    }
                });

        //异步Loading
        CacheLoader<User, String> asyncReloading = CacheLoader.asyncReloading(new CacheLoader<User, String>() {
            @Override
            public String load(User key) throws Exception {
                return null;
            }
        },Executors.newSingleThreadExecutor());


        localCache.put(new User("u1",28),"hello");
        localCache.put(new User("u2",30),"hello2");

        //TODO 此处返回NULL
        String localCacheIfPresent = localCache.getIfPresent(new User("u1", 28));
        Assert.assertEquals("hello",localCacheIfPresent);
        log.info("================== Cache stats:{} ==================",localCache.stats().toString());


    }

    @Test
    public void testHashCode() throws Exception {
        User u1 = new User("u1",28);
        log.info("U1 hashCode : {}" ,u1.hashCode());
        User u2 = new User("u1",28);
        log.info("U2 hashCode : {}" ,u2.hashCode());

        Assert.assertEquals(u1,u2);
    }


}
