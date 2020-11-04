package com.qqgsd.crowd.service.api;

import com.github.pagehelper.PageInfo;
import com.qqgsd.crowd.entity.Role;

import java.util.List;

public interface RoleService {

    /**
     * 获取角色分页
     */
    PageInfo<Role> getPageInfo(Integer pageNum,Integer pageSize,String keyword);

    /**
     * 保存角色
     * @param role 角色
     */
    void saveRole(Role role);

    /**
     * 修改角色
     * @param role 新角色名称
     */
    void updateRole(Role role);

    /**
     * 删除角色（批量和单条）
     * @param roleIdList 角色id列表
     */
    void removeRole(List<Integer> roleIdList);

    /**
     * 查询已分配的角色
     * @param adminId 用户id
     * @return 已分配的角色列表
     */
    List<Role> getAssignedRole(Integer adminId);

    /**
     * 查询未分配的角色
     * @param adminId 用户id
     * @return 未分配的角色列表
     */
    List<Role> getUnAssignedRole(Integer adminId);
}
