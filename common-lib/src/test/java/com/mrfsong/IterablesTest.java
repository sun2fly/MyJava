package com.mrfsong;

import com.mrfsong.common.iter.Iterables;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * ${DESCRIPTION}
 * </P>
 *
 * @Author songfei20
 * @Date 2018/10/31 16:33
 * @Version 1.0
 */
@Slf4j
public class IterablesTest {
    @Test
    public void exec(){
        List<String> list = Arrays.asList("a", "b", "b", "c", "c", "c", "d", "d", "d", "f", "f", "g");

        Iterables.forEach(list, (index, str) -> log.info(index + " -> " + str));
    }

}
