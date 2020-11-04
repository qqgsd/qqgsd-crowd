package com.qqgsd.crowd.mvc.interceptor;

import com.qqgsd.crowd.constant.CrowdConstant;
import com.qqgsd.crowd.entity.Admin;
import com.qqgsd.crowd.exception.AccessForbiddenException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录检查拦截器
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 1.获取session对象
        HttpSession session = request.getSession();

        // 2.尝试获取admin对象
        Admin admin= (Admin) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);

        // 3.判断admin是否为空
        if (admin==null){

            // 4.抛出异常
            throw new AccessForbiddenException(CrowdConstant.MESSAGE_ACCESS_FORBIDDEN);
        }
        // 放行
        return true;
    }
}
