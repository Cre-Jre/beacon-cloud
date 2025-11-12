package com.luoyurui.test.mapper;

import com.luoyurui.test.client.CacheClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MobileDiertyWordMapperTest {

    @Autowired
    private CacheClient cacheClient;

    @Autowired
    private MobileDiertyWordMapper mapper;

    @Test
    public void findDirtyWord() {
        List<String> dirtyWords = mapper.findDirtyWord();

        cacheClient.saddStr("dirty_word",dirtyWords.toArray(new String[]{}));
    }
}