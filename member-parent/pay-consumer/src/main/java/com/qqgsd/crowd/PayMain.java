package com.qqgsd.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class PayMain {

    public static void main(String[] args) {
        SpringApplication.run(PayMain.class,args);
    }
}
