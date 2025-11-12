package com.luoyurui.strategy.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 获取手机号归属地和运营商工具
 */
@Component
public class MobileOperatorUtil {


    @Autowired
    private RestTemplate restTemplate ;

    private final String url1 = "https://cx.shouji.360.cn/phonearea.php?number=";

    @Autowired
    private ObjectMapper objectMapper;

    private final String CODE = "code";
    private final String DATA = "data";
    private final String PROVINCE = "province";
    private final String CITY = "city";
    private final String SP = "sp";
    private final String SPACE =  " ";
    private final String SEPARATE =  ",";

    /**
     * 根据手机号前七位获取手机号信息
     * @param mobile
     * @return
     */
    public String getMobileInfoBy360(String mobile) {
        //1.根据请求获取信息
        String mobileInfoJson = restTemplate.getForObject(url1 + mobile, String.class);
        //2.解析JSON    {"code":0,"data":{"province":"\u4e91\u5357","city":"\u6606\u660e","sp":"\u79fb\u52a8"}}
        Map map = null;
        try {
            map = objectMapper.readValue(mobileInfoJson, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Integer code = (Integer) map.get(CODE);
        if (code != 0) {
            return null;
        }
        Map<String, String> areaAndOp = (Map<String, String>) map.get(DATA);
        String province = areaAndOp.get(PROVINCE);
        String city = areaAndOp.get(CITY);
        String sp = areaAndOp.get(SP);
        if (StringUtils.isEmpty(province) &&StringUtils.isEmpty(city) &&StringUtils.isEmpty(sp) ) {
            return null;
        }
        //3.封装为 省  市， 运营商
        return province + SPACE + city + SEPARATE + sp;
    }

}
