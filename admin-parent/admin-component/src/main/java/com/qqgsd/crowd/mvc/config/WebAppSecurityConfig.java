package com.qqgsd.crowd.mvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * SpringSecurity配置类
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 加密密码
     */
    @Bean
    public BCryptPasswordEncoder getPasswordEncoder(){
        return  new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {

        // 基于数据库的验证
        builder
                .userDetailsService(userDetailsService)
                .passwordEncoder(getPasswordEncoder())
                ;
    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security
                .authorizeRequests()                                            // 给请求授权
                .antMatchers("/admin/to/login/page.html",           // 放行登录请求和静态资源
                                        "/bootstrap/**",
                                        "/crowd/**",
                                        "/css/**",
                                        "/fonts/**",
                                        "/img/**",
                                        "/jquery/**",
                                        "/layer/**",
                                        "/script/**",
                                        "/ztree/**")
                .permitAll()                                                    // 允许无权限访问
                .anyRequest()                                                   // 其他请求
                .authenticated()                                                // 认证后访问
                .and()
                .csrf()
                .disable()                                                      // 禁用csrf
                .formLogin()                                                    // 开启表单登录的功能
                .loginPage("/admin/to/login/page.html")                         // 登录页面
                .loginProcessingUrl("/admin/do/login.html")                     // 登录提交的表单
                .defaultSuccessUrl("/admin/to/main/page.html")                  // 登录成功后去的页面
                .usernameParameter("loginAcct")                                 // 账号请求参数
                .passwordParameter("userPwd")                                   // 密码请求参数
                .and()
                .logout()                                                       // 开启退出功能
                .logoutUrl("/admin/do/logout.html")                             // 退出登录的地址
                .logoutSuccessUrl("/admin/to/login/page.html")                  // 退出成功后去的页面
                ;
    }
}


