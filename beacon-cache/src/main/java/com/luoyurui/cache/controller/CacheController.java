package com.luoyurui.cache.controller;

import com.msb.framework.redis.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
public class CacheController {

    @Autowired
    private RedisClient redisClient;

    @PostMapping(value = "/cache/hmset/{key}")
    public void hmset(@PathVariable(value = "key")String key, @RequestBody Map<String, Object> map){
        log.info("【缓存模块】 hmset方法，存储key = {}，存储value = {}",key,map);
        redisClient.hSet(key, map);
    }

    @PostMapping(value = "/cache/set/{key}")
    public void set(@PathVariable(value = "key")String key, @RequestParam(value = "value") String value){
        log.info("【缓存模块】 set方法，存储key = {}，存储value = {}",key,value);
        redisClient.set(key, value);
    }

    @PostMapping(value = "/cache/sadd/{key}")
    public void sadd(@PathVariable(value = "key")String key, @RequestBody Map<String,Object>... value){
        log.info("【缓存模块】 sadd方法，存储key = {}，存储value = {}", key, value);
        redisClient.sAdd(key,value);
    }

    @GetMapping("/cache/hgetall/{key}")
    public Map hGetAll(@PathVariable(value = "key") String key){
        log.info("【缓存模块】 hGetAll方法，存储key = {}",key);
        Map<String, Object> stringObjectMap = redisClient.hGetAll(key);
        log.info("【缓存模块】 hGetAll方法，存储key = {}，存储value = {}",key,stringObjectMap);
        return stringObjectMap;
    }

    @GetMapping("/cache/hget/{key}/{field}")
    public Object hget(@PathVariable(value = "key") String key, @PathVariable(value = "field") String field) {
        log.info("【缓存模块】 hget方法，获取key ={}，field = {}的数据", key,field);
        Object value = redisClient.hGet(key, field);
        log.info("【缓存模块】 hGetAll方法，获取key ={}，field = {} 的数据 value = {}", key,field,value);
        return value;
    }
}
