package com.qqgsd.crowd.service.api;

import com.qqgsd.crowd.entity.Auth;

import java.util.List;
import java.util.Map;

public interface AuthService {
    /**
     * 获取所以的权限
     * @return 权限列表
     */
    List<Auth> getAllAuth();

    /**
     * 根据角色获取权限
     * @param roleId 角色id
     * @return 权限列表
     */
    List<Integer> getAssignedAuthIdByRoleId(Integer roleId);

    /**
     * 保存角色权限映射
     */
    void saveRoleAuthRelationship(Map<String, List<Integer>> map);

    /**
     * 根据用户id查询以获取权限的名称
     * @param adminId 用户id
     * @return 权限名称
     */
    List<String> getAssignedAuthNameByAdminId(Integer adminId);
}
