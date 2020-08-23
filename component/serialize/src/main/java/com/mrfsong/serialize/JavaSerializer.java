package com.mrfsong.serialize;

import java.io.*;

/**
 * <p>
 * java默认序列化
 * </P>
 *
 * @Author songfei20
 * @Date 2020/4/27
 */
public class JavaSerializer<T> implements Serializer<T> {
    @Override
    public byte[] serialize(T t) throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(t);
            return baos.toByteArray();
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public T deSerialize(byte[] bytes) throws Exception {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            Object o = ois.readObject();
            if(o != null){
                return (T)o;
            }
            return null;
        } catch (IOException | ClassNotFoundException e) {
            throw e;
        }
    }
}
