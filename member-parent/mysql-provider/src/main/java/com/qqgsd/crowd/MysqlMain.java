package com.qqgsd.crowd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.qqgsd.crowd.mapper")
@SpringBootApplication
public class MysqlMain {

    public static void main(String[] args) {
        SpringApplication.run(MysqlMain.class,args);
    }
}
