package com.luoyurui.search.utils;

import com.luoyurui.common.model.StandardReport;

import java.time.LocalDateTime;

public class SearchUtils {

    //索引前缀
    public static String INDEX = "sms_submit_log_";

    public static ThreadLocal<StandardReport> reportThreadLocal = new ThreadLocal<>();

    public static void set(StandardReport report){
        reportThreadLocal.set(report);
    }

    public static StandardReport get() {
        return reportThreadLocal.get();
    }

    public static void remove() {
        reportThreadLocal.remove();
    }

//获取年份信息
    public static String getYear(){
        return LocalDateTime.now().getYear() + "";
    }
}
