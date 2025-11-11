package com.luoyurui.cache.controller;

import com.luoyurui.cache.config.RedisConfig;
import com.msb.framework.redis.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class TestController {

    @Autowired
    private RedisClient redisClient;

    //写测试
    @PostMapping("/test/set/{key}")
    public String set(@PathVariable String key, @RequestBody Map map) {

        redisClient.hSet(key, map);
//        redisTemplate.opsForHash().putAll(key, map);
        return "1";
    }

    //读测试
    @GetMapping("/test/get/{key}")
    public Map get(@PathVariable String key) {
        Map<String, Object> result = redisClient.hGetAll(key);
//        Map<Object, Object> result = redisTemplate.opsForHash().entries(key);
        return result;
    }
}
