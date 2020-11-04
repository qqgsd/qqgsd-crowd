package com.qqgsd.crowd.service.api;

import com.qqgsd.crowd.entity.po.MemberPO;

public interface MemberService {

    /**
     * 根据用户名查询用户
     * @param loginAct 登录名
     * @return 查询的用户
     */
    MemberPO getMemberPOByLoginAct(String loginAct);

    /**
     * 保存member对象
     */
    void saveMember(MemberPO memberPO);
}
