package com.qqgsd.crowd.mvc.controller;

import com.qqgsd.crowd.entity.Auth;
import com.qqgsd.crowd.entity.Role;
import com.qqgsd.crowd.service.api.AdminService;
import com.qqgsd.crowd.service.api.AuthService;
import com.qqgsd.crowd.service.api.RoleService;
import com.qqgsd.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class AssignHandler {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

    /**
     * 前往修改角色页面
     * @return 角色修改页面
     */
    @RequestMapping(value = "assign/to/assign/role/page.html")
    public ModelAndView toAssignRolePage(@RequestParam("adminId") Integer adminId){

        ModelAndView mv = new ModelAndView();

        // 1.查询已分配的角色
        List<Role> assignedRoleList=roleService.getAssignedRole(adminId);

        // 2.查询未分配的角色
        List<Role> unassignedRoleList=roleService.getUnAssignedRole(adminId);

        mv.addObject("assignedRoleList",assignedRoleList);
        mv.addObject("unassignedRoleList",unassignedRoleList);
        mv.setViewName("admin/role/assign-role");
        return mv;
    }

    /**
     * 保存用户角色修改
     * @param adminId 用户的id
     * @param pageNum 当前页
     * @param keyword 查寻关键字
     * @param roleIdList 更改后的角色id
     * @return 返回用户维护页面
     */
    @RequestMapping(value = "assign/do/role/assign.html")
    public String  saveAdminRoleRelationship(
                                @RequestParam("adminId") Integer adminId,
                                @RequestParam("pageNum") Integer pageNum,
                                @RequestParam("keyword") String keyword,

                                // 允许取消所有用户的所有角色,允许roleIdList为空
                                @RequestParam(value = "roleIdList",required = false) List<Integer> roleIdList
                                ){

        adminService.saveAdminRoleRelationship(adminId,roleIdList);
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }

    /**
     * 获取所有权限
     */
    @ResponseBody
    @RequestMapping(value = "assign/get/all/auth.json")
    public ResultEntity<List<Auth>> getAllAuth(){
        List<Auth> authList=authService.getAllAuth();
        return ResultEntity.successWithData(authList);
    }

    /**
     * 根据角色获取权限
     * @param roleId 角色id
     * @return 权限列表
     */
    @ResponseBody
    @RequestMapping(value = "assign/getAssigned/authId/byRoleId.json")
    public ResultEntity<List<Integer>> getAssignedAuthIdByRoleId(@RequestParam("roleId") Integer roleId){
        List<Integer> authIdList=authService.getAssignedAuthIdByRoleId(roleId);
        return ResultEntity.successWithData(authIdList);
    }

    @ResponseBody
    @RequestMapping(value = "assign/do/roleAssign/auth.json")
    public ResultEntity<String> saveRoleAuthRelationship(@RequestBody Map<String,List<Integer>> map){
        authService.saveRoleAuthRelationship(map);
        return ResultEntity.successWithoutData();
    }
}
