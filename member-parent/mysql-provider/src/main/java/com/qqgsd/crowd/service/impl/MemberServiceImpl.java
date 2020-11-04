package com.qqgsd.crowd.service.impl;

import com.qqgsd.crowd.entity.po.MemberPO;
import com.qqgsd.crowd.entity.po.MemberPOExample;
import com.qqgsd.crowd.mapper.MemberPOMapper;
import com.qqgsd.crowd.service.api.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberPOMapper memberPOMapper;

    @Override
    public MemberPO getMemberPOByLoginAct(String loginAct) {
        MemberPOExample example=new MemberPOExample();
        MemberPOExample.Criteria criteria = example.createCriteria();
        criteria.andLoginacctEqualTo(loginAct);
        List<MemberPO> list = memberPOMapper.selectByExample(example);
        if (list==null||list.size()==0)
            return null;
        return list.get(0);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    @Override
    public void saveMember(MemberPO memberPO) {
        memberPOMapper.insertSelective(memberPO);
    }
}
