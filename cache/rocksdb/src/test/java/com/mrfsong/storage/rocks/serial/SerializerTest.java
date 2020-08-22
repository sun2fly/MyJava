package com.mrfsong.storage.ehcache.serial;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mrfsong.storage.ehcache.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

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


    private final List<User> list = Lists.newArrayList();
    private final Map<User,String> map = Maps.newHashMap();
    User user;

    @Before
    public void init() {
        list.add(new User("A1",10));
        list.add(new User("A2",20));
        list.add(new User("A3",30));

        user = new User("A100",100);
        map.put(user,"100");

    }

    @Test
    public void kryoWithClass() throws FileNotFoundException {
        String filePath = "D:\\data\\kryo_with_class.data";
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

            log.info("" + list.equals(o));

        }catch (IOException e) {

        }
    }


    @Test
    public void kryoGeneric() {
        String filePath = "D:\\data\\kryo_generic.data";

        try(FileOutputStream fileOutputStream = new FileOutputStream(filePath,true);){
            Output output = new Output(fileOutputStream);
            Kryo kryo = kryoLocal.get();
            kryo.writeObject(output, list);
            output.flush();

        }catch (IOException e) {
            e.printStackTrace();
        }

        try(FileInputStream fileInputStream = new FileInputStream(filePath);){
            Input input = new Input(fileInputStream);
            ArrayList arrayList = kryoLocal.get().readObject(input, ArrayList.class);

            log.info("" + list.equals(arrayList));

        }catch (IOException e) {

        }
    }


    /**
     * 测试“不同对象（地址不同、内容相同）”,在序列化为byte[]后的表现
     */
    @Test
    public void kryoEntity() {
        String filePath = "D:\\data\\kryo_entity.data";

        try(FileOutputStream fileOutputStream = new FileOutputStream(filePath,true)){
            Output output = new Output(fileOutputStream);
            Kryo kryo = kryoLocal.get();
            kryo.writeObject(output, user);
            output.flush();
            output.close();


        }catch (IOException e) {
            e.printStackTrace();
        }

        try(FileInputStream fileInputStream = new FileInputStream(filePath)){
            Input input = new Input(fileInputStream);
            User user = kryoLocal.get().readObject(input, User.class);
            input.close();

            String str = map.get(user);
            Assert.assertEquals("100",str);

        }catch (IOException e) {

        }
    }


    /**
     * 测试“不同对象（地址不同、内容相同）”序列化为String类型后的表现
     * 结论：“不同对象”在使用相同的序列化工具时，serial后的字符串内容相同
     * @throws Exception
     */
    @Test
    public void testWriteToString() throws Exception {
        Kryo kryo = kryoLocal.get();
        Base64.Encoder urlEncoder = Base64.getUrlEncoder();
        String encodeString;
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Output output = new Output(byteArrayOutputStream)){

            User user = new User("123",123);
            log.info("Origin data : [{}]",user.toString());
            kryo.writeObject(output, user);
            output.flush();

            byte[] bytes = byteArrayOutputStream.toByteArray();
            encodeString = urlEncoder.encodeToString(bytes);
            log.info("Serial to string : [{}]" , encodeString);
        }

        Base64.Decoder urlDecoder = Base64.getUrlDecoder();
        byte[] bytes = urlDecoder.decode(encodeString);

        try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            Input input = new Input(byteArrayInputStream);){

            User user = kryo.readObject(input, User.class);
            log.info("Deser from string : [{}]",user.toString());

        }
    }

    @Test
    public void testReadFromString() throws Exception {
        User u1 = new User("u1",1);
        User u2 = new User("u1",1);
        log.info(String.valueOf(u1 == u2));
        log.info(String.valueOf(u1.equals(u2)));
        log.info(u1.hashCode() + " " + u2.hashCode());


    }




}
