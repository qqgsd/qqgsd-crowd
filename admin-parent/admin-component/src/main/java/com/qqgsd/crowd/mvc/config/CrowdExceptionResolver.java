package com.qqgsd.crowd.mvc.config;

import com.google.gson.Gson;
import com.qqgsd.crowd.exception.LoginAcctAlreadyInUseException;
import com.qqgsd.crowd.exception.LoginFailedException;
import com.qqgsd.crowd.util.CrowdUtil;
import com.qqgsd.crowd.util.ResultEntity;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 异常处理类
 */
@ControllerAdvice  // 表示当前类是基于注解的异常处理类
public class CrowdExceptionResolver {

    /**
     *  通用的异常处理
     * @param viewName 要返回的视图名称
     * @param exception 异常
     * @return ajax请求返回null，非ajax请求返回视图（viewName）
     */
    private ModelAndView commonResolve(String viewName, Exception exception,
                                       HttpServletRequest request, HttpServletResponse response) {
        // 1.判读请求类型
        boolean judgeRequestType = CrowdUtil.judgeRequestType(request);

        // 2.如果是ajax请求
        if (judgeRequestType){
            // 3.创建resultEntity对象
            ResultEntity<Object> resultEntity = ResultEntity.failed(exception.getMessage());

            // 4.转为json并返回
            Gson gson = new Gson();
            String json = gson.toJson(resultEntity);
            try {
                response.getWriter().write(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        else {
            // 5.不是ajax请求
            ModelAndView mv = new ModelAndView();
            mv.addObject("exception",exception);
            // 6.设置视图名称
            mv.setViewName(viewName);
            return mv;
        }
    }

    /**
     * 处理登录异常
     * @param exception 登录异常
     * @return 返回登录页面
     */
    @ExceptionHandler(value = LoginFailedException.class)
    public ModelAndView resolveLoginFailedException(LoginFailedException exception,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response){

        String viewName="admin/admin-login";

        return commonResolve(viewName,exception,request,response);
    }

    @ExceptionHandler(value = LoginAcctAlreadyInUseException.class)
    public ModelAndView resolveDuplicateKeyException(LoginAcctAlreadyInUseException exception,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response){
        String viewName="admin/admin-add";
        return commonResolve(viewName,exception,request,response);
    }
}
