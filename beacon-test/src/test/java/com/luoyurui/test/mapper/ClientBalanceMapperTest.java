package com.luoyurui.test.mapper;

import com.luoyurui.test.client.CacheClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ClientBalanceMapperTest {

    @Autowired
    private ClientBalanceMapper mapper;

    @Autowired
    private CacheClient cacheClient;

    @Test
    public void findByClientId() {
        Long balance = mapper.findByClientId(1L);
        System.out.println(balance);

        cacheClient.set("client_balance:1",balance);
    }
}