package com.luoyurui.webmaster.mapper;

import com.luoyurui.webmaster.entity.SmsUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsUserMapperTest {

    @Resource
    private SmsUserMapper userMapper;

    @Test
    public void findById(){
        SmsUser smsUser = userMapper.selectByPrimaryKey(1);
        System.out.println(smsUser);
    }
}