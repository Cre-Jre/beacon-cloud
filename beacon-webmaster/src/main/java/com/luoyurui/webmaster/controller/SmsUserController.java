package com.luoyurui.webmaster.controller;

import com.luoyurui.common.constant.WebMasterConstants;
import com.luoyurui.common.enums.ExceptionEnums;
import com.luoyurui.common.util.R;
import com.luoyurui.common.vo.ResultVO;
import com.luoyurui.webmaster.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/sys")
@Slf4j
public class SmsUserController {


    @PostMapping("/login")
    public ResultVO login(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
//        * 1、请求参数的非空校验
        if (bindingResult.hasErrors()) {
            // 参数不合法，响应对应的JSON信息
            log.info("【认证操作】参数不合法，userDTO = {}", userDTO);
            return R.error(ExceptionEnums.PARAMETER_ERROR);
        }
//        * 2、基于验证码校验请求是否合理
        String realKaptcha = (String) SecurityUtils.getSubject().getSession().getAttribute(WebMasterConstants.KAPTCHA);
        if (!userDTO.getCaptcha().equalsIgnoreCase(realKaptcha)) {
            log.info("【认证操作】验证码不正确，kapacha = {}，realKaptcha = {}", userDTO.getCaptcha(), realKaptcha);
            return R.error(ExceptionEnums.KAPACHA_ERROR);
        }
//        * 3、基于用户名和密码做Shiro的认证操作
        UsernamePasswordToken token = new UsernamePasswordToken(userDTO.getUsername(),userDTO.getPassword());
        token.setRememberMe(userDTO.getRememberMe());
        try {
            SecurityUtils.getSubject().login(token);
        } catch (AuthenticationException e) {
//        * 4、根据Shiro的认证，返回响应信息
            log.info("【认证操作】用户名或密码错误，ex = {}", e.getMessage());
            return R.error(ExceptionEnums.AUTHEN_ERROR);
        }
        // 到这，代表认证成功
        return R.ok();
    }


}
