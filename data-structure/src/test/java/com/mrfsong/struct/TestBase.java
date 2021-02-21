package com.mrfsong.struct;

import com.github.jsonzou.jmockdata.JMockData;
import com.github.jsonzou.jmockdata.MockConfig;
import com.github.jsonzou.jmockdata.TypeReference;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


/**
 * @Auther: songfei20
 * @Date: 2021/1/20 20:04
 * @Description:
 */
@RunWith(Parameterized.class)
public abstract class TestBase {

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[1000][0];
    }

    protected int[] intArray = JMockData.mock(new TypeReference<int[]>(){} , MockConfig.newInstance());


}