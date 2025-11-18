package com.luoyurui.webmaster.service;

import com.luoyurui.webmaster.entity.SmsUser;

public interface SmsUserService {

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    SmsUser findByUserName(String username);
}
