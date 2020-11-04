package com.qqgsd.crowd.test;

import com.qqgsd.crowd.entity.po.MemberPO;
import com.qqgsd.crowd.mapper.MemberPOMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisTest {

    @Autowired
    private DataSource dataSource;

    private final Logger logger = LoggerFactory.getLogger(MybatisTest.class);

    @Autowired
    private MemberPOMapper memberPOMapper;

    @Test
    public void testConnection() throws SQLException {

        Connection connection = dataSource.getConnection();
        logger.debug(connection.toString());
    }

    @Test
    public void testMapper(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String source="123123";
        String encode = passwordEncoder.encode(source);
        MemberPO memberPO = new MemberPO(null, "qqgsd", encode, "许明辉", "2642088533@qq.com", 1, 1, "许明辉", "123123", 2);
        memberPOMapper.insert(memberPO);
    }

}
