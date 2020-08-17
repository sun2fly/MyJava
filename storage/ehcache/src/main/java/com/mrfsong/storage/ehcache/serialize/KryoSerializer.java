package com.mrfsong.storage.ehcache.serialize;

import com.mrfsong.storage.ehcache.Serializer;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *      kryo序列化实现
 * </P>
 *
 * @Author songfei20
 * @Date 2020/4/27
 */
@Slf4j
public class KryoSerializer<T> implements Serializer<T> {

    @Override
    public byte[] serialize(T t) throws Exception {
        return new byte[0];
    }

    @Override
    public T deSerialize(byte[] bytes) throws Exception {
        return null;
    }
}
