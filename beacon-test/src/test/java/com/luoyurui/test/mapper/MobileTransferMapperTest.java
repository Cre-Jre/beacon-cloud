package com.luoyurui.test.mapper;

import com.luoyurui.test.client.CacheClient;
import com.luoyurui.test.entity.MobileTransfer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MobileTransferMapperTest {

    @Autowired
    private MobileTransferMapper mapper;

    @Autowired
    private CacheClient cacheClient;

    @Test
    public void findAll() {
        List<MobileTransfer> list = mapper.findAll();

        for (MobileTransfer mobileTransfer : list) {
            cacheClient.set("transfer:" + mobileTransfer.getTransferNumber(),mobileTransfer.getNowIsp());
        }
    }
}