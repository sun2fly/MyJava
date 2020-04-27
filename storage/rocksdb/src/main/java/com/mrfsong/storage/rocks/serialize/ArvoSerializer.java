package com.mrfsong.storage.rocks.serialize;

import com.mrfsong.storage.rocks.Serializer;

/**
 * <p>
 * arvo 序列化
 * </P>
 *
 * @Author songfei20
 * @Date 2020/4/27
 */
public class ArvoSerializer<T> implements Serializer<T> {
    @Override
    public byte[] serialize(T t) throws Exception {
        //todo
        return new byte[0];
    }

    @Override
    public T deSerialize(byte[] bytes) throws Exception {
        //todo
        return null;
    }
}
