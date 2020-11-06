package com.mrfsong.struct;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void testShift(){

        int capacity = 10;
        int shiftR = (capacity >> 1);
        int shiftL = (capacity << 1);
        log.info("shifL：{}" ,shiftL);
        log.info("shiftR：{}" ,shiftR);
        List<String> list = new ArrayList<>();
        list.add("111");
        list.set(0,"222");
        log.info(list.get(0));

    }
}
