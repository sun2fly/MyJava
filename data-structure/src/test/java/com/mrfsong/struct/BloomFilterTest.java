package com.mrfsong.struct;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 布隆过滤器测试
 */
@Slf4j
public class BloomFilterTest {

    @Test
    public void testWithGuava() throws Exception {
        BloomFilter<Integer> filter = BloomFilter.create(
                Funnels.integerFunnel(),
                500,
                0.01);





    }

}
