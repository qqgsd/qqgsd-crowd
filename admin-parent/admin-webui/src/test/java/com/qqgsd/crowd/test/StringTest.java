package com.qqgsd.crowd.test;

import com.qqgsd.crowd.util.CrowdUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml","classpath:spring-web-mvc.xml","classpath:spring-persist-tx.xml"})
public class StringTest {

    @Autowired
    public BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void testMd5(){
        System.err.println(bCryptPasswordEncoder.encode("123456"));
    }
}
