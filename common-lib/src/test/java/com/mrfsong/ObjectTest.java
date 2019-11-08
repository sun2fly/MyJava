package com.mrfsong;

import com.github.jsonzou.jmockdata.JMockData;
import com.github.jsonzou.jmockdata.MockConfig;
import com.github.jsonzou.jmockdata.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

import java.util.List;

/**
 * <p>
 *
 * </P>
 *
 * @Author songfei20
 * @Date 2019/10/30
 */
@Slf4j
public class ObjectTest {

    private final List<String> valList = JMockData.mock(new TypeReference<List<String>>(){},new MockConfig().sizeRange(100, 100));


    @Test
    public void testMethodParam() {
        String strValue = String.join(",",valList);

        log.info("Internal of List : {}",ClassLayout.parseInstance(valList).toPrintable());
        log.info("Internal of String : {}",ClassLayout.parseInstance(strValue).toPrintable());

        log.info("External of List : {}",GraphLayout.parseInstance(valList).toPrintable());
        log.info("External of String : {}",GraphLayout.parseInstance(strValue).toPrintable());

        log.info("List total size :　{}",GraphLayout.parseInstance(valList).totalSize());
        log.info("String total size : {}",GraphLayout.parseInstance(strValue).totalSize());



    }



}
