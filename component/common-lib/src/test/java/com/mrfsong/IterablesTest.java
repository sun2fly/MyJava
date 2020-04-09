package com.mrfsong;

import com.mrfsong.common.iter.Iterables;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

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
    public void forEach(){
        List<String> list = Arrays.asList("a", "b", "b", "c", "c", "c", "d", "d", "d", "f", "f", "g");

        Iterables.forEach(list, (index, str) -> log.info(index + " -> " + str));

        //查看对象内部信息
        log.info(ClassLayout.parseInstance(list).toPrintable());

        //查看对象外部信息：包括引用的对象
        log.info(GraphLayout.parseInstance(list).toPrintable());

        //查看对象占用空间总大小
        log.info(String.valueOf(GraphLayout.parseInstance(list).totalSize()).concat(" bytes"));

    }

}
