package com.luoyurui.strategy.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;
import java.util.Set;

@FeignClient(value = "beacon-cache")
public interface BeaconCacheClient {


    @GetMapping("/cache/hget/{key}/{field}")
    String hget(@PathVariable(value = "key")String key,@PathVariable(value = "field")String field);
}
