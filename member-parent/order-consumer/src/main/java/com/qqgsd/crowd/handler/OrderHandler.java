package com.qqgsd.crowd.handler;

import com.qqgsd.crowd.api.MysqlRemoteService;
import com.qqgsd.crowd.constant.CrowdConstant;
import com.qqgsd.crowd.entity.vo.AddressVO;
import com.qqgsd.crowd.entity.vo.MemberLoginVO;
import com.qqgsd.crowd.entity.vo.OrderProjectVO;
import com.qqgsd.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class OrderHandler {

    @Autowired
    private MysqlRemoteService mysqlRemoteService;

    @RequestMapping(value = "/confirm/return/info/{projectId}/{returnId}")
    public String showReturnConfirmInfo(@PathVariable("projectId") Integer projectId,
                                        @PathVariable("returnId") Integer returnId,
                                        HttpSession session){
        ResultEntity<OrderProjectVO> resultEntity=mysqlRemoteService.getOrderProjectVORemote(projectId,returnId);
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())){
            OrderProjectVO orderProjectVO = resultEntity.getData();
            session.setAttribute("orderProjectVO",orderProjectVO);
        }
        return "confirm-return";
    }

    @RequestMapping(value = "/confirm/order/{returnCount}")
    public String showConfirmOrderInfo(@PathVariable("returnCount") Integer returnCount,
                                       HttpSession session){

        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");
        orderProjectVO.setReturnContent(returnCount);
        session.setAttribute("orderProjectVO",orderProjectVO);
        MemberLoginVO memberLoginVo = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        Integer memberId = memberLoginVo.getId();
        ResultEntity<List<AddressVO>> resultEntity=mysqlRemoteService.getAddressVORemote(memberId);
        if (ResultEntity.SUCCESS.equals(resultEntity.getMessage())){
            List<AddressVO> list = resultEntity.getData();
            session.setAttribute("addressVOList",list);
        }
        return "confirm-order";
    }

    @RequestMapping(value = "/save/address")
    public String saveAddress(AddressVO addressVO,HttpSession session){

        ResultEntity<String> resultEntity=mysqlRemoteService.saveAddress(addressVO);
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");
        Integer returnCount = orderProjectVO.getReturnCount();
        return "redirect:http://localhost/orderconfirm/order/"+returnCount;
    }

}
