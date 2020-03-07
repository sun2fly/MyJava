package com.mrfsong.spring;

import com.mrfsong.spring.bean.PrototypeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

/**
 * <p>
 *      Spring源码调试,学习
 * </P>
 *
 * @Author songfei20
 * @Date 2019/10/30
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class SpringTest {

    @Autowired
    private PrototypeService prototypeService;

    @Test
    public void getBean() {

        assertNotNull(prototypeService);
    }

    @Test
    public void aopTest() {
        String hello = prototypeService.sayHello("felix");
        log.info("==================== [{}] ====================",hello);
    }





}
