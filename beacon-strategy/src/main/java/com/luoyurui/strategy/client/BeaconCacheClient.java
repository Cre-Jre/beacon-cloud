package com.luoyurui.strategy.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.Set;

@FeignClient(value = "beacon-cache")
public interface BeaconCacheClient {


    @GetMapping("/cache/hget/{key}/{field}")
    String hget(@PathVariable(value = "key")String key,@PathVariable(value = "field")String field);

    @GetMapping(value = "/cache/get/{key}")
    String get(@PathVariable(value = "key")String key);

    @PostMapping(value = "/cache/sinterstr/{key}/{sinterKey}")
    Set<Object> sinterstr(@PathVariable(value = "key")String key, @PathVariable(value = "sinterKey")String sinterKey, @RequestBody String... value);

    @GetMapping("/cache/smember/{key}")
    Set smember(@PathVariable(value = "key")String key);
}
