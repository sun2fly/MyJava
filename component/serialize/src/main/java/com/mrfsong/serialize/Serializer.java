package com.mrfsong.serialize;

/**
 * <p>
 * 序列化接口
 * </P>
 *
 * @Author songfei20
 * @Date 2020/4/27
 */
public interface Serializer<T> {

    /**
     * 序列化
     * @param t
     * @return
     * @throws Exception
     */
    byte[] serialize(T t) throws Exception;

    /**
     * 反序列化
     * @param bytes
     * @return
     * @throws Exception
     */
    T deSerialize(byte[] bytes) throws Exception;

}
