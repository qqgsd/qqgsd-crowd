package com.qqgsd.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringBootApplication
public class ZuulMain {

    public static void main(String[] args) {
        SpringApplication.run(ZuulMain.class,args);
    }
}
