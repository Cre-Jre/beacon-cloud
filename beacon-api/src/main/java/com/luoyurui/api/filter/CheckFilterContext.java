package com.luoyurui.api.filter;

import com.luoyurui.common.model.StandardSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RefreshScope
public class CheckFilterContext {

    //spring的ioc会将对象全部放到map中
    //基于4.x中spring提供的泛型注解，基于map只拿到需要的类型对象即可
    @Autowired
    private Map<String, CheckFilter> checkFiltersMap;

    /**
     * 拿去naocs的顺序
     */
    @Value("${filters:apikey,ip,sign,template}")
    private String filters;

    public void check(StandardSubmit submit) {
        //1.将获取道德filters基于,进行切分
        String[] filterArray = filters.split(",");
        //2.遍历当前数组
        for (String filter : filterArray) {
            CheckFilter checkFilter = checkFiltersMap.get(filter);
            checkFilter.check(submit);
        }
    }
}
