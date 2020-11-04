package com.qqgsd.crowd.handler;

import com.qqgsd.crowd.constant.CrowdConstant;
import com.qqgsd.crowd.entity.po.MemberPO;
import com.qqgsd.crowd.service.api.MemberService;
import com.qqgsd.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberProviderHandler {

    @Autowired
    private MemberService memberService;

    /**
     * 根据用户名查询用户
     * @param loginAct 登录名
     * @return 查询结果
     */
    @RequestMapping(value = "/get/memberPO/by/loginAct/remote")
    ResultEntity<MemberPO> getMemberPOByLoginActRemote(@RequestParam("loginAct") String loginAct){
        try {
            MemberPO memberPO=memberService.getMemberPOByLoginAct(loginAct);
            return ResultEntity.successWithData(memberPO);
        } catch (Exception exception) {
            return ResultEntity.failed(exception.getMessage());
        }
    }

    /**
     * 保存member对象
     */
    @RequestMapping(value = "/save/member/remote")
    public ResultEntity<String> saveMember(@RequestBody MemberPO memberPO){
        try {
            memberService.saveMember(memberPO);
            return ResultEntity.successWithoutData();
        } catch (Exception exception) {
            exception.printStackTrace();
            if (exception instanceof DuplicateKeyException)
                return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            return ResultEntity.failed(exception.getMessage());
        }
    }

}
