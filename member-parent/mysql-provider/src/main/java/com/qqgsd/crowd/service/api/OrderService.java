package com.qqgsd.crowd.service.api;

import com.qqgsd.crowd.entity.vo.AddressVO;
import com.qqgsd.crowd.entity.vo.OrderProjectVO;
import com.qqgsd.crowd.entity.vo.OrderVO;

import java.util.List;

public interface OrderService {
    OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId);

    List<AddressVO> getAddressVOList(Integer memberId);

    void saveAddress(AddressVO addressVO);

    void saveOrder(OrderVO orderVO);
}
