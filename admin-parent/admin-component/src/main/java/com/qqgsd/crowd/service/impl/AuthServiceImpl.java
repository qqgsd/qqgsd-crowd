package com.qqgsd.crowd.service.impl;

import com.qqgsd.crowd.entity.Auth;
import com.qqgsd.crowd.entity.AuthExample;
import com.qqgsd.crowd.mapper.AuthMapper;
import com.qqgsd.crowd.service.api.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthMapper authMapper;

    @Override
    public List<Auth> getAllAuth() {
        return authMapper.selectByExample(new AuthExample());
    }

    @Override
    public List<Integer> getAssignedAuthIdByRoleId(Integer roleId) {
        return authMapper.selectAssignedAuthIdByRoleId(roleId);
    }

    @Override
    public void saveRoleAuthRelationship(Map<String, List<Integer>> map) {
        List<Integer> authIdArray = map.get("authIdArray");
        Integer roleId = map.get("roleId").get(0);
        // 清空旧关系
        authMapper.deleteRelationship(roleId);

        // 添加新关系
        if (authIdArray!=null&&authIdArray.size()>0){
            authMapper.insertRelationship(roleId,authIdArray);
        }
    }

    @Override
    public List<String> getAssignedAuthNameByAdminId(Integer adminId) {
        return authMapper.selectAssignedAuthNameByAdminId(adminId);
    }
}
