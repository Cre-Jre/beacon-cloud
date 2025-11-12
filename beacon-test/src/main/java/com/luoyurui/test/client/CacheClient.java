package com.luoyurui.test.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@FeignClient(value = "beacon-cache")
public interface CacheClient {

    @PostMapping(value = "/cache/hmset/{key}")
    void hmset(@PathVariable(value = "key")String key, @RequestBody Map<String, Object> map);

    @PostMapping(value = "/cache/set/{key}")
    void set(@PathVariable(value = "key")String key, @RequestParam(value = "value") String value);

    @PostMapping(value = "/cache/set/{key}")
    void set(@PathVariable(value = "key")String key, @RequestParam(value = "value") Long value);

    @PostMapping(value = "/cache/sadd/{key}")
    void sadd(@PathVariable(value = "key")String key, @RequestBody Map<String,Object>... maps);

    @PostMapping("/cache/pipeline/string")
    void pipelineString(@RequestBody Map<String,String> map);

    @PostMapping(value = "/cache/saddstr/{key}")
    void saddStr(@PathVariable(value = "key")String key, @RequestBody String... value);
}
