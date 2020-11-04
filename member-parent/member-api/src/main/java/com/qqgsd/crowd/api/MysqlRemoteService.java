package com.qqgsd.crowd.api;

import com.qqgsd.crowd.entity.po.MemberPO;
import com.qqgsd.crowd.entity.vo.*;
import com.qqgsd.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "crowd-mysql")
public interface MysqlRemoteService {

    /**
     * 根据用户名查询用户
     * @param loginAct 登录名
     * @return 查询的用户
     */
    @RequestMapping(value = "/get/memberPO/by/loginAct/remote")
    ResultEntity<MemberPO> getMemberPOByLoginActRemote(@RequestParam("loginAct") String loginAct);

    /**
     * 保存member对象
     */
    @RequestMapping(value = "/save/member/remote")
    ResultEntity<String> saveMember(@RequestBody MemberPO memberPO);

    /**
     * 保存projectVO
     */
    @RequestMapping(value = "/save/project/vo/remote")
    ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO, @RequestParam("memberID") Integer memberID);

    @RequestMapping("/get/portal/type/project/data/remote")
    ResultEntity<List<PortalTypeVO>> getPortalTypeProjectDataRemote();

    @RequestMapping(value = "/get/project/detail/remote/{projectId}")
    ResultEntity<DetailProjectVO> getDetailProjectVORemote(@PathVariable("projectId") Integer projectId);

    @RequestMapping(value = "/get/order/projectVO/remote")
    ResultEntity<OrderProjectVO> getOrderProjectVORemote(@RequestParam("projectId") Integer projectId,
                                                         @RequestParam("returnId") Integer returnId);

    @RequestMapping(value = "/get/addressVO/remote")
    ResultEntity<List<AddressVO>> getAddressVORemote(@RequestParam("memberId") Integer memberId);

    @RequestMapping(value = "/save/address/remote")
    ResultEntity<String> saveAddress(@RequestBody AddressVO addressVO);

    @RequestMapping(value = "/save/order/remote")
    ResultEntity<String> saveOrderRemote(@RequestBody OrderVO orderVO);
}
