package com.mrfsong.struct;

import com.mrfsong.struct.cache.LRUCache;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2017/12/04 21:01
 **/
@Slf4j
public class LRUCacheTest {

    private LRUCache<String , String> cache;

    @Before
    public void setUp() throws Exception {
        cache = new LRUCache<String, String>(3);
    }

    @Test
    public void put() throws Exception {
        cache.put("1" , "abc");
        cache.put("2" , "abcd");
        cache.put("3" , "abcde");
        cache.put("4" , "abcdef");

        log.debug("Cache size : {}" , cache.size());



    }


}
