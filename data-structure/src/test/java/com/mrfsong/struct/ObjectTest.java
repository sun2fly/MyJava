package com.mrfsong.struct;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * <p>
 * 测试
 * </P>
 *
 * @Author songfei20
 * @Date 2019/11/28
 */
@Slf4j
public class ObjectTest {

    @Test
    public void testMod() {
        int minBit = Integer.MIN_VALUE % 10;
        int maxBit = Integer.MAX_VALUE % 10;
        log.info("Max bit :{} , Min bit : {}" , maxBit ,minBit);
    }
}
