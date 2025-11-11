package com.luoyurui.test.mapper;

import com.luoyurui.test.client.CacheClient;
import com.luoyurui.test.entity.MobileArea;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MobileAreaMapperTest {

    @Autowired
    private CacheClient cacheClient;

    @Autowired
    private MobileAreaMapper mapper;

    @Test
    public void findAllMobileArea() {
        List<MobileArea> list = mapper.findAllMobileArea();
        Map map = new HashMap(list.size());
        for (MobileArea mobileArea : list) {
            map.put("phase:" + mobileArea.getMobileNumber(),mobileArea.getMobileArea() + "," + mobileArea.getMobileType());
        }
        cacheClient.pipelineString(map);
    }
}