package com.qqgsd.crowd.test;

import com.qqgsd.crowd.entity.Admin;
import com.qqgsd.crowd.entity.Role;
import com.qqgsd.crowd.mapper.AdminMapper;
import com.qqgsd.crowd.mapper.RoleMapper;
import com.qqgsd.crowd.service.api.AdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml","classpath:spring-persist-tx.xml"})
public class CrowdTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void testRoleSave(){

        for (int i=0;i<235;i++){
            roleMapper.insert(new Role(i+1,"role"+i+1));
        }
    }

    @Test
    public void testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }

    @Test
    public void testInsertAdmin() {
        Admin admin = new Admin(2, "qqgsd", "123456", "许明辉", "a@a.cm", null);
        int insert = adminMapper.insert(admin);
        System.out.println(insert);
    }

    @Test
    public void textTx(){
        Admin admin = new Admin(2, "qqgsd", "123456", "许明辉", "a@a.cm", null);
        adminService.saveAdmin(admin);
    }

    @Test
    public void text(){
        for (int i = 2; i < 240; i++) {
            adminService.saveAdmin(new Admin(i, "loginAcct"+i, "userPwd"+i, "userName"+i, "email"+i, null));
        }
    }
}
