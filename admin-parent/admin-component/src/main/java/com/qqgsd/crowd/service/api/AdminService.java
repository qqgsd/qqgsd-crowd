package com.qqgsd.crowd.service.api;

import com.github.pagehelper.PageInfo;
import com.qqgsd.crowd.entity.Admin;

import java.util.List;

public interface AdminService {

    /**
     * 保存添加的admin用户
     */
    void saveAdmin(Admin admin);

    List<Admin> getAll();

    /**
     * 根据用户名和密码查询用户
     * @param loginAcct 用户名
     * @param userPwd 密码
     * @return 如果可以查询到返回admin对象，否则抛出异常
     */
    Admin getAdminByLoginAcct(String loginAcct, String userPwd);

    /**
     * 分页功能
     * @param keyword 查询关键字
     * @param pageNum 当前页
     * @param pageSize 当前页数量
     * @return PageInfo
     */
    PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize);

    /**
     * 根据id删除admin用户
     * @param adminId id
     */
    void remove(Integer adminId);

    /**
     * 根据id获取admin用户
     * @param adminId 用户id
     * @return 获取到的用户
     */
    Admin getAdminById(Integer adminId);

    /**
     * 更新用户
     */
    void update(Admin admin);

    /**
     * 保存用户角色关系
     * @param adminId 用户id
     * @param roleIdList 当前用户拥有的角色
     */
    void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList);

    /**
     * 根据用户名查询amin对象
     * @param username 用户名
     * @return admin对象
     */
    Admin getAdminByLoginAcct(String username);
}
