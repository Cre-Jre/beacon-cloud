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

    @GetMapping(value = "/cache/get/{key}")
    public Object get(@PathVariable(value = "key")String key) {
        log.info("【缓存模块】 get方法，获取key ={}", key);
        Object value = redisTemplate.opsForValue().get(key);
        log.info("【缓存模块】 get方法，获取key ={} 的数据 value = {}", key,value);
        return key == null ? null : value;
    }

    //...可以存多个
    @PostMapping(value = "/cache/saddstr/{key}")
    public void saddStr(@PathVariable(value = "key")String key, @RequestBody String... value){
        log.info("【缓存模块】 saddStr方法，存储key = {}，存储value = {}", key, value);
        redisTemplate.opsForSet().add(key, value);
    }

    @PostMapping(value = "/cache/sinterstr/{key}/{sinterKey}")
    public Set<Object> sinterstr(@PathVariable(value = "key")String key, @PathVariable(value = "sinterKey")String sinterKey, @RequestBody String... value){
        log.info("【缓存模块】 sinterstr的交集方法，存储key = {}， sinterKey = {}, 存储value = {}", key,sinterKey, value);
        //1.存储数据到set
        redisTemplate.opsForSet().add(key, value);
        //2.需要将key和sinterKey做交集操作，并拿到返回的set
        Set<Object> result = redisTemplate.opsForSet().intersect(key, sinterKey);
        //3.将key删除
        redisTemplate.delete(key);

        return result;
    }

    @PostMapping(value = "/cache/zadd/{key}/{score}/{member}")
    public Boolean zAdd(@PathVariable(value = "key")String key,
                     @PathVariable (value = "score")Long score,
                     @PathVariable (value = "member")Object member){
        log.info("【缓存模块】 zAdd，存储key = {}，score = {}，member = {}", key, score, member);
        Boolean add = redisTemplate.opsForZSet().add(key, member, score);
        return add;
    }

    @GetMapping(value = "/cache/zrangebyscorecount/{key}/{start}/{end}")
    public int zRangeByScoreCount(@PathVariable(value = "key")String key,
                                @PathVariable(value = "start")Double start,
                                @PathVariable(value = "end")Double end) {
        log.info("【缓存模块】 zRangeByScoreCount，获取key ={}，start = {}，end = {}", key, start, end);
        Set values = redisTemplate.opsForZSet().rangeByScoreWithScores(key, start, end);
        if (values != null) {
            return values.size();
        }
        return 0;
    }

    @DeleteMapping(value = "/cache/zremove/{key}/{member}")
    public void zRemove(@PathVariable(value = "key") String key,@PathVariable(value = "member") String member) {
        log.info("【缓存模块】 zRemove方法，删除key = {},member = {}", key,member);
        redisTemplate.opsForZSet().remove(key, member);
    }

    @PostMapping(value = "cache/hincrby/{key}/{field}/{number}")
    public Long hIncrBy(@PathVariable(value = "key") String key,
                        @PathVariable(value = "field") String field,
                        @PathVariable(value = "number") Long number) {
        log.info("【缓存模块】 hIncrBy自增 key = {},field = {},number = {}", key ,field ,number);
        Long increment = redisTemplate.opsForHash().increment(key, field, number);
        log.info("【缓存模块】 hIncrBy自增 key = {},field = {},number = {},剩余金额为 = {}", key ,field ,number, increment);
        return increment;
    }
}
