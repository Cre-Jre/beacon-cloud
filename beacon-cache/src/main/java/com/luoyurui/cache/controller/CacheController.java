package com.luoyurui.cache.controller;

import com.msb.framework.redis.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@Slf4j
public class CacheController {

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private RedisTemplate redisTemplate;

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
        log.info("【缓存模块】 hget方法，获取key ={}，field = {} 的数据 value = {}", key,field,value);
        return value;
    }

    @GetMapping("/cache/smember/{key}")
    public Set smember(@PathVariable(value = "key")String key) {
        log.info("【缓存模块】 smember，获取key ={}", key);
        Set<Object> values = redisClient.sMembers(key);
        log.info("【缓存模块】 smember方法，获取key ={} 的数据 value = {}", key,values);
        return values;
    }

    @PostMapping("/cache/pipeline/string")
    public void pipelineString(@RequestBody Map<String,String> map){
        log.info("【缓存模块】 pipelineString，获取到存储的数据，map的长度 ={}的数据", map.size());
        redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                for (Map.Entry<String, String> entry : map.entrySet()) {
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
    }
}
