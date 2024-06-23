package com.example.smartstudy.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public List<Object> get(String key) {
        // 获取信箱中所有的信息
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    public void set(String key, Object value) {
        // 向正在发送信息的任意两人的信箱中中添加信息
        redisTemplate.opsForList().rightPush(key, value);
    }

}

