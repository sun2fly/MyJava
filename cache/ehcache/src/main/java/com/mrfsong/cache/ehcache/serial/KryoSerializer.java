package com.mrfsong.cache.ehcache.serial;


import lombok.extern.slf4j.Slf4j;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.SerializerException;

import java.nio.ByteBuffer;

@Slf4j
public class KryoSerializer implements Serializer {
    @Override
    public ByteBuffer serialize(Object object) throws SerializerException {
        return null;
    }

    @Override
    public Object read(ByteBuffer binary) throws ClassNotFoundException, SerializerException {
        return null;
    }

    @Override
    public boolean equals(Object object, ByteBuffer binary) throws ClassNotFoundException, SerializerException {
        return false;
    }
}
