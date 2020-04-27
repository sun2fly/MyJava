package com.mrfsong.storage.rocks.serialize;

import com.mrfsong.storage.rocks.Serializer;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Protocol Buffer序列化
 * </P>
 *
 * @Author songfei20
 * @Date 2020/4/27
 */
@Slf4j
public class ProtocolBufferSerializer<T> implements Serializer<T> {
    @Override
    public byte[] serialize(T t) throws Exception {
        return new byte[0];
    }

    @Override
    public T deSerialize(byte[] bytes) throws Exception {
        return null;
    }
}
