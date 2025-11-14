package com.luoyurui.strategy.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@FeignClient(value = "beacon-cache")
public interface BeaconCacheClient {


    @GetMapping("/cache/hget/{key}/{field}")
    String hget(@PathVariable(value = "key")String key,@PathVariable(value = "field")String field);

    @GetMapping("/cache/hget/{key}/{field}")
    Integer hgetInteger(@PathVariable(value = "key")String key,@PathVariable(value = "field")String field);

    @GetMapping(value = "/cache/get/{key}")
    String get(@PathVariable(value = "key")String key);

    @PostMapping(value = "/cache/sinterstr/{key}/{sinterKey}")
    Set<Object> sinterstr(@PathVariable(value = "key")String key, @PathVariable(value = "sinterKey")String sinterKey, @RequestBody String... value);

    @GetMapping("/cache/smember/{key}")
    Set smember(@PathVariable(value = "key")String key);

    @GetMapping("/cache/smember/{key}")
    Set<Map> smemberMap(@PathVariable(value = "key")String key);


    @PostMapping(value = "/cache/zadd/{key}/{score}/{member}")
    Boolean zAdd(@PathVariable(value = "key")String key,
                            @PathVariable (value = "score")Long score,
                            @PathVariable (value = "member")Object member);

    @GetMapping(value = "/cache/zrangebyscorecount/{key}/{start}/{end}")
    int zRangeByScoreCount(@PathVariable(value = "key")String key,
                                  @PathVariable(value = "start")Double start,
                                  @PathVariable(value = "end")Double end);

    @DeleteMapping(value = "/cache/zremove/{key}/{member}")
    void zRemove(@PathVariable(value = "key") String key,@PathVariable(value = "member") String member);

    @PostMapping(value = "cache/hincrby/{key}/{field}/{number}")
    Long hIncrBy(@PathVariable(value = "key") String key,
                        @PathVariable(value = "field") String field,
                        @PathVariable(value = "number") Long number);

    @GetMapping("/cache/hgetall/{key}")
    Map hGetAll(@PathVariable(value = "key") String key);
}
