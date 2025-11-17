package com.luoyurui.moitor.task;


import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

@Component
public class TestJob {


    @XxlJob("test")
    public void test(){
        System.out.println("xxxxxxxxxxxxxxxxxx");
    }
}
