package com.qqgsd.crowd.mvc.config;

import com.qqgsd.crowd.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

/**
 * 考虑到 User 对象中仅仅包含账号和密码，为了能够获取到原始的 Admin 对象，专门创建
 * 这个类对 User 类进行扩展
 */
public class SecurityAdmin extends User {

    private final Admin originalAdmin;

    /**
     *
     * @param originalAdmin 原始admin对象
     * @param authorities 创建角色、权限信息集合
     */
    public SecurityAdmin(Admin originalAdmin, List<GrantedAuthority> authorities){
        super(originalAdmin.getLoginAcct(),originalAdmin.getUserPwd(),authorities);
        this.originalAdmin=originalAdmin;
    }

    public Admin getOriginalAdmin() {
        return originalAdmin;
    }
}
