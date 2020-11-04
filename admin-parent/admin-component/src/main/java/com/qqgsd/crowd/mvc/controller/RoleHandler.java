package com.qqgsd.crowd.mvc.controller;

import com.github.pagehelper.PageInfo;
import com.qqgsd.crowd.entity.Role;
import com.qqgsd.crowd.service.api.RoleService;
import com.qqgsd.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoleHandler {

    @Autowired
    private RoleService roleService;


    /**
     * 获取分页数据
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @param keyword 关键字
     * @return 分页数据
     */
    @PreAuthorize(value = "hasRole('SE - 软件工程师')")
    @RequestMapping(value = "/role/get/page/info.json")
    public ResultEntity<PageInfo<Role>> getPageInfo(
                    @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                    @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
                    @RequestParam(value = "keyword",defaultValue = "") String keyword){

        // 获取分页数据
        PageInfo<Role> pageInfo = roleService.getPageInfo(pageNum, pageSize, keyword);

        return ResultEntity.successWithData(pageInfo);
    }

    /**
     * 保存角色
     * @return 成功
     */
    @RequestMapping(value = "role/save.json")
    public ResultEntity<String> saveRole(Role role){
        roleService.saveRole(role);
        return ResultEntity.successWithoutData();
    }

    /**
     * 更新角色
     * @param role 更新的角色
     * @return 成功
     */
    @RequestMapping(value = "role/update.json")
    public ResultEntity<String> updateRole(Role role){
        roleService.updateRole(role);
        return ResultEntity.successWithoutData();
    }

    /**
     * 根据id列表删除角色
     * @param roleIdList roleId列表（json格式）
     * @return 成功
     */
    @RequestMapping(value = "role/remove/by/role/id/array.json")
    public ResultEntity<String> removeByRoleIdArray(@RequestBody List<Integer> roleIdList){
        roleService.removeRole(roleIdList);
        return ResultEntity.successWithoutData();
    }
}
