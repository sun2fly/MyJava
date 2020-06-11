package com.mrfsong.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * <p>
 *      ClassLoader测试
 * </P>
 *
 * @Author songfei20
 * @Date 2020/5/8
 */
@Slf4j
public class ClassLoaderTest {

    @Test
    public void testExtDirs() {
        String classPath = System.getProperty("java.class.path");
        String extDirs = System.getProperty("java.ext.dirs");
        log.info("class path: {} , ext dirs: {}" , classPath , extDirs);

//        System.setProperty("java.ext.dirs","D:\\ext\\lib"); // no effect

        // Determines all of the current system properties
        /*Properties properties = System.getProperties();
        Enumeration<?> propertyNames = properties.propertyNames();
        while(propertyNames.hasMoreElements()){
            log.info("property : {}" , propertyNames.nextElement());
        }*/

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Class<?> aClass = contextClassLoader.loadClass("com.alibaba.fastjson.JSONObject");
            aClass.newInstance();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }
}
