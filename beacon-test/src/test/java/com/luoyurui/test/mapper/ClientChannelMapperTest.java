package com.luoyurui.test.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luoyurui.test.client.CacheClient;
import com.luoyurui.test.entity.Channel;
import com.luoyurui.test.entity.ClientChannel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ClientChannelMapperTest {

    @Autowired
    private CacheClient cacheClient;

    @Autowired
    private ClientChannelMapper mapper;

    @Test
    public void findAll() throws JsonProcessingException {
        List<ClientChannel> list = mapper.findAll();
        for (ClientChannel clientChannel : list) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map map = objectMapper.readValue(objectMapper.writeValueAsString(clientChannel), Map.class);
            cacheClient.sadd("client_channel:" + clientChannel.getClientId(), map);
        }
    }
}