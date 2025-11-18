package com.luoyurui.webmaster.service.impl;

import com.luoyurui.webmaster.entity.SmsUser;
import com.luoyurui.webmaster.service.SmsUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsUserServiceImplTest {

    @Autowired
    private SmsUserService smsUserService;

    @Test
    public void findByUsername() {
        SmsUser smsUser = smsUserService.findByUserName("admin");
        System.out.println(smsUser);
    }
}