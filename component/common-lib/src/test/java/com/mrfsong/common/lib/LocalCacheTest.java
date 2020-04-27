package com.mrfsong.common.lib;

import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.openjdk.jol.info.GraphLayout;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * <p>
 * 本地缓存测试用例
 * </P>
 *
 * @Author songfei20
 * @Date 2020/4/9
 */
@Slf4j
public class LocalCacheTest {

    @Test
    public void testGuavaCache() throws Exception{

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
                    //默认的数据加载实现,当调用get取值的时候,如果key没有对应的值,就调用这个方法进行加载
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


}
