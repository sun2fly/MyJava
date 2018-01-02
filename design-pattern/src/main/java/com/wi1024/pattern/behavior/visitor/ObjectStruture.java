package com.wi1024.pattern.behavior.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/02 15:19
 **/
public class ObjectStruture {

    public static List<Element> getList() {
        List<Element> list = new ArrayList<Element>();
        Random random = new Random();
        for(int i=0;i<10;i++){
            int a = random.nextInt(100);
            if(a > 50){
                list.add(new AConcreteElement());
            }else {
                list.add(new BConcreteElement());
            }
        }
        return list;
    }
}
