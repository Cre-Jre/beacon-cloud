package com.luoyurui.cache.controller;

import com.luoyurui.cache.config.RedisConfig;
import com.msb.framework.redis.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private RedisTemplate redisTemplate;

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

    //管道测试
    @PostMapping("/test/pipeline")
    public String pipeline() {
        Map<String, Object> map = new HashMap<>();
        map.put("1888888", "北京 北京，移动");
        map.put("1888889", "北京 北京，电信");
        redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    // 将key和value转换为字节数组（根据RedisTemplate的序列化设置可能不需要）
                    byte[] keyBytes = redisTemplate.getKeySerializer().serialize(entry.getKey());
                    byte[] valueBytes = redisTemplate.getValueSerializer().serialize(entry.getValue());
                    // 使用set命令存入数据[citation:3]
                    connection.set(keyBytes, valueBytes);
                }
                // 必须返回null[citation:3]
                return null;
            }
        });
        return "ok";
        }
}
