package com.qqgsd.crowd.mvc.config;

import com.qqgsd.crowd.entity.Admin;
import com.qqgsd.crowd.entity.Role;
import com.qqgsd.crowd.service.api.AdminService;
import com.qqgsd.crowd.service.api.AuthService;
import com.qqgsd.crowd.service.api.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CrowdUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据账号查询admin对象
        Admin admin=adminService.getAdminByLoginAcct(username);

        // 根据adminId查询角色信息
        List<Role> assignedRole = roleService.getAssignedRole(admin.getId());

        // 根据adminId查询权限信息
        List<String> assignedAuthNameByAdminId = authService.getAssignedAuthNameByAdminId(admin.getId());

        List<GrantedAuthority> grantedAuthorities=new ArrayList<>();

        // 存入角色信息
        for (Role role : assignedRole) {
            // 前缀
            String roleName="ROLE_"+role.getName();
            grantedAuthorities.add(new SimpleGrantedAuthority(roleName));
        }

        // 存入权限信息
        for (String auth : assignedAuthNameByAdminId) {
            grantedAuthorities.add(new SimpleGrantedAuthority(auth));
        }
        return new SecurityAdmin(admin, grantedAuthorities);
    }
}
