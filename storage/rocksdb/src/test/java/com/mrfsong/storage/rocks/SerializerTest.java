package com.mrfsong.storage.rocks;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.collect.Lists;
import com.mrfsong.storage.rocks.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 *
 * </P>
 *
 * @Author songfei20
 * @Date 2020/4/28
 */
@Slf4j
public class SerializerTest {

    //每个线程的 Kryo 实例
    private static final ThreadLocal<Kryo> kryoLocal = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();

            /**
             * 不要轻易改变这里的配置！更改之后，序列化的格式就会发生变化，
             * 上线的同时就必须清除 Redis 里的所有缓存，
             * 否则那些缓存再回来反序列化的时候，就会报错
             */
            //支持对象循环引用（否则会栈溢出）
            kryo.setReferences(true); //默认值就是 true，添加此行的目的是为了提醒维护者，不要改变这个配置

            //不强制要求注册类（注册行为无法保证多个 JVM 内同一个类的注册编号相同；而且业务系统中大量的 Class 也难以一一注册）
            kryo.setRegistrationRequired(false); //默认值就是 false，添加此行的目的是为了提醒维护者，不要改变这个配置

            //Fix the NPE bug when deserializing Collections.
            /*((Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy())
                    .setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());*/

            kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
            ((Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy()).setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());


            return kryo;
        }
    };

    @Test
    public void testKryo() throws FileNotFoundException {
        List<User> list = Lists.newArrayList();
        list.add(new User("A1",20));
        list.add(new User("A2",30));
        list.add(new User("A3",40));

        String filePath = "D:\\data\\test.data";
        try(FileOutputStream fileOutputStream = new FileOutputStream(filePath,true);){
            Output output = new Output(fileOutputStream);
            Kryo kryo = kryoLocal.get();
            kryo.writeClassAndObject(output, list);
            output.flush();

        }catch (IOException e) {
            e.printStackTrace();
        }

        try(FileInputStream fileInputStream = new FileInputStream(filePath);){
            Input input = new Input(fileInputStream);
            Object o = kryoLocal.get().readClassAndObject(input);
            log.info(o.getClass().getName());

        }catch (IOException e) {

        }







    }

}
