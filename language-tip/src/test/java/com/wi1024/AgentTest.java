package com.wi1024;

import java.lang.instrument.Instrumentation;

/**
 * <p>
 * ${DESCRIPTION}
 * </P>
 *
 * @Author songfei20
 * @Date 2018/8/25 15:32
 * @Version 1.0
 */
public class AgentTest {

    public static void agentmain(String args, Instrumentation inst){
        Class[] classes = inst.getAllLoadedClasses();
        for(Class cls :classes){
            System.out.println(cls.getName());
        }
    }


}
