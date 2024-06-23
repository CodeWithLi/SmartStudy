package com.example.smartstudy.controller;

import com.example.smartstudy.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@RestController
//用户获取好友之间信息的控制器：
public class PullMessageController {

    private final RedisUtil redis;

    @Autowired
    public PullMessageController(RedisUtil redis) {
        this.redis = redis;
    }

    @PostMapping("/pullMsg")
    public List<Object> pullMsg(@RequestParam("from") Long from, @RequestParam("to") Long to) {
        // 根据两人的 id 生成键，并到 redis 中获取
        String key = LongStream.of(from, to)
                .sorted()
                .mapToObj(String::valueOf)
                .collect(Collectors.joining("-"));
        return redis.get(key);
    }

}
