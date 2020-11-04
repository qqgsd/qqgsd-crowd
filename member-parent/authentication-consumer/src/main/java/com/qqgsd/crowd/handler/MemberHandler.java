package com.qqgsd.crowd.handler;

import com.qqgsd.crowd.api.MysqlRemoteService;
import com.qqgsd.crowd.constant.CrowdConstant;
import com.qqgsd.crowd.entity.po.MemberPO;
import com.qqgsd.crowd.entity.vo.MemberLoginVO;
import com.qqgsd.crowd.entity.vo.MemberVO;
import com.qqgsd.crowd.util.ResultEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class MemberHandler {

    @Autowired
    private MysqlRemoteService mysqlRemoteService;

    /**
     * 用户注册
     * @param memberVO 用户注册信息
     * @return 返回登陆页面
     */
    @RequestMapping("/auth/do/member/register")
    public ModelAndView register(MemberVO memberVO){
        ModelAndView mv = new ModelAndView();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String userpswd = memberVO.getUserpswd();
        String encode = passwordEncoder.encode(userpswd);
        memberVO.setUserpswd(encode);
        MemberPO memberPO = new MemberPO();
        BeanUtils.copyProperties(memberVO,memberPO);
        ResultEntity<String> saveMemberResult = mysqlRemoteService.saveMember(memberPO);
        if (ResultEntity.FAILED.equals(saveMemberResult.getResult())) {

            mv.addObject(CrowdConstant.ATTR_NAME_MESSAGE,saveMemberResult.getMessage());
            mv.setViewName("member-regist");
            return mv;
        }
        mv.setViewName("redirect:http://localhost/auth/member/to/login/page");
        return mv;
    }

    /**
     * 登录
     * @param loginacct 账号
     * @param userpswd 密码
     */
    @RequestMapping(value = "/auth/member/do/login")
    public String login(@RequestParam("loginacct") String loginacct,
                        @RequestParam("userpswd") String userpswd,
                        ModelMap modelMap,
                        HttpSession session){

        ResultEntity<MemberPO> resultEntity = mysqlRemoteService.getMemberPOByLoginActRemote(loginacct);
        if (ResultEntity.FAILED.equals(resultEntity.getResult())){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,resultEntity.getMessage());
            return "member-login";
        }
        MemberPO memberPO = resultEntity.getData();
        if (memberPO==null){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }
        String memberPOUserpswd = memberPO.getUserpswd();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean matchesResult = passwordEncoder.matches(userpswd, memberPOUserpswd);
        if (!matchesResult){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }
        MemberLoginVO memberLoginVO = new MemberLoginVO(memberPO.getId(),memberPO.getUsername(),memberPO.getEmail());
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER,memberLoginVO);
        return "redirect:http://localhost/auth/member/to/center/page";
    }

    /**
     * 退出登录
     */
    @RequestMapping(value = "/auth/member/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:http://localhost/";
    }
}
