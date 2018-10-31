package com.mrfsong.pattern.creat.singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/09 15:01
 **/
public class Multition {

    private static int maxNumOfObj = 3;

    private static Map<Integer , Multition> multitionMap = new HashMap<Integer , Multition>();

    private static int index ;

    static {
        for(int i=0;i<maxNumOfObj; i++){
            multitionMap.put(i , new Multition());
        }
    }

    private Multition() {}

    public static Multition getInstance() {
        Random random = new Random();
        index = random.nextInt(maxNumOfObj);
        return multitionMap.get(index);
    }

}
