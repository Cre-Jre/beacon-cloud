package com.luoyurui.webmaster.service.impl;

import com.luoyurui.webmaster.entity.SmsUser;
import com.luoyurui.webmaster.entity.SmsUserExample;
import com.luoyurui.webmaster.mapper.SmsUserMapper;
import com.luoyurui.webmaster.service.SmsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsUserServiceImpl implements SmsUserService {

    @Autowired
    private SmsUserMapper smsUserMapper;

    @Override
    public SmsUser findByUserName(String username) {
        //1.封装查询条件
        SmsUserExample example = new SmsUserExample();
        SmsUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        //2.基于userMapper查询
        List<SmsUser> list = smsUserMapper.selectByExample(example);
        return list != null ? list.get(0) : null;
    }
}
