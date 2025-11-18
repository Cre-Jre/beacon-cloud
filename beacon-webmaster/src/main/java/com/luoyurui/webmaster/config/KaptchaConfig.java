package com.luoyurui.webmaster.config;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 验证码生成
 */

@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha kaptcha(){
        //1.直接构建DefaultKaptcha
        DefaultKaptcha kaptcha = new DefaultKaptcha();

        //2.设置配置信息
        Properties properties = new Properties();
        // 验证码生成位数为4位。
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH,"4");
        Config config = new Config(properties);
        kaptcha.setConfig(config);

        //3.返回对象
        return kaptcha;
    }
}
