package com.mrfsong.common;

import com.mrfsong.common.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Mock数据测试
 */
@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class MockTest {

    @Test
    public void mockObject() throws Exception {
        List<String> list = Mockito.mock(ArrayList.class);
        User user = Mockito.mock(User.class);
        log.info(list.toString());


    }


   /* public static void main(String[] args) {
        List<String> list = Mockito.mock(ArrayList.class);
        log.info(list.toString());
    }*/

}
