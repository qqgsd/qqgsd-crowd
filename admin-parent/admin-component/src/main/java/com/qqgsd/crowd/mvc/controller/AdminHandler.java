package com.qqgsd.crowd.mvc.controller;

import com.github.pagehelper.PageInfo;
import com.qqgsd.crowd.constant.CrowdConstant;
import com.qqgsd.crowd.entity.Admin;
import com.qqgsd.crowd.service.api.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class AdminHandler {

    // admin的service层
    @Autowired
    private AdminService adminService;

    /**
     * 登录功能
     * @param loginAcct 用户名
     * @param userPwd 密码
     * @param session session域
     * @return 后台主页面
     */
    @RequestMapping(value = "/admin/do/login.html")
    public String doLogin(@RequestParam("loginAcct") String loginAcct,
                          @RequestParam("userPwd") String userPwd,
                          HttpSession session){
        // 登录检查
        // 返回admin对象表示登录成功。账号，密码不正确会抛异常
        Admin admin=adminService.getAdminByLoginAcct(loginAcct,userPwd);

        //登录成功将admin对象存入session域
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN,admin);

        // 为避免后台主页面刷新导致表单重复提交，重定向到目标页面
        return "redirect:/admin/to/main/page.html";
    }

    /**
     *  退出登录
     * @param session session域（强制session失效）
     * @return 登录页面
     */
    @RequestMapping(value = "/admin/do/logout.html")
    public String doLogout(HttpSession session){

        // 强制session失效
        session.invalidate();

        return "redirect:/admin/to/login/page.html";
    }

    /**
     * 获取分页页面
     * @param keyword 关键字
     * @param pageNum 当前页
     * @param pageSize 当前页数量
     * @return 分页页面
     */
    @RequestMapping(value = "/admin/get/page.html")
    public ModelAndView getPageInfo(@RequestParam(value = "keyword",defaultValue = "") String keyword,
                                    @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                    @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize){

        ModelAndView mv = new ModelAndView();
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);
        mv.addObject(CrowdConstant.ATTR_NAME_PAGE_INFO, pageInfo);
        mv.setViewName("admin/admin-page");
        return mv;
    }

    /**
     * 删除用户
     * @param adminId 用户id
     * @param pageNum 当前页
     * @param keyword 关键字
     * @return 当前页
     */
    @RequestMapping(value = "admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String remove(@PathVariable(value = "adminId") Integer adminId,
                         @PathVariable(value = "pageNum") Integer pageNum,
                         @PathVariable(value = "keyword") String keyword){
        adminService.remove(adminId);
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }

    /**
     * 保存添加的admin用户
     * @param admin 要保存的用户
     * @return 最后一页
     */
    @RequestMapping(value = "admin/save.html")
    public String save(Admin admin){
        adminService.saveAdmin(admin);
        return "redirect:/admin/get/page.html?pageNum="+Integer.MAX_VALUE;
    }

    /**
     * 获取要修改的admin用户并回显到修改页面
     * @param adminId 用户id
     * @return 修改页面
     */
    @RequestMapping(value = "admin/to/edit/page.html")
    public ModelAndView toEditPage(@RequestParam("adminId") Integer adminId){
        ModelAndView mv = new ModelAndView();
        Admin admin=adminService.getAdminById(adminId);
        mv.addObject("admin",admin);
        mv.setViewName("admin/admin-edit");
        return mv;
    }

    /**
     * 更新admin用户
     * @return 返回分页页面
     */
    @RequestMapping(value = "admin/update.html")
    public String update(Admin admin,
                         @RequestParam("pageNum") Integer pageNum,
                         @RequestParam("keyword") String keyword){
        adminService.update(admin);
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }
}
