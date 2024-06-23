package com.example.smartstudy.config;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

//为redis key 统一加上前缀
public class RedisKeySerializer implements RedisSerializer<String> {

    /**
     * 编码格式
     */
    private final Charset charset;

    /**
     * 前缀
     */
    private final String PREFIX_KEY = "SmartStudy:";

    public RedisKeySerializer() {
        this(Charset.forName("UTF8"));
    }

    public RedisKeySerializer(Charset charset) {
        this.charset = charset;
    }

    /**
     * Serialize the given object to binary data.
     *
     * @param s object to serialize. Can be {@literal null}.
     * @return the equivalent binary data. Can be {@literal null}.
     */
    @Override
    public byte[] serialize(String cacheKey) throws SerializationException {
        String key = PREFIX_KEY + cacheKey;
        return key.getBytes(charset);
    }

    /**
     * Deserialize an object from the given binary data.
     *
     * @param bytes object binary representation. Can be {@literal null}.
     * @return the equivalent object instance. Can be {@literal null}.
     */
    @Override
    public String deserialize(byte[] bytes) throws SerializationException {
        String cacheKey = new String(bytes, charset);
        int indexOf = cacheKey.indexOf(PREFIX_KEY);
        if (indexOf == -1) {
            cacheKey = PREFIX_KEY + cacheKey;
        }
        return (cacheKey.getBytes() == null ? null : cacheKey);
    }
}