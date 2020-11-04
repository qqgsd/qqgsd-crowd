package com.qqgsd.crowd.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qqgsd.crowd.constant.CrowdConstant;
import com.qqgsd.crowd.entity.Admin;
import com.qqgsd.crowd.entity.AdminExample;
import com.qqgsd.crowd.exception.LoginAcctAlreadyInUseException;
import com.qqgsd.crowd.exception.LoginFailedException;
import com.qqgsd.crowd.mapper.AdminMapper;
import com.qqgsd.crowd.service.api.AdminService;
import com.qqgsd.crowd.util.CrowdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class AdminServiceImpl implements AdminService {

    // admin的dao层
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void saveAdmin(Admin admin) {
        // 1.密码加密
        String userPwd = admin.getUserPwd();
        String md5 = bCryptPasswordEncoder.encode(userPwd);
        admin.setUserPwd(md5);
        // 2.创建时间
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = simpleDateFormat.format(date);

        admin.setCreateTime(createTime);
        try {
            adminMapper.insert(admin);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof DuplicateKeyException){
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);

            }
        }
    }

    @Override
    public List<Admin> getAll() {
        return adminMapper.selectByExample(new AdminExample());
    }

    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPwd) {

        // 1.根据登录账号查询admin对象
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        // 封装查询条件
        criteria.andLoginAcctEqualTo(loginAcct);
        List<Admin> adminList = adminMapper.selectByExample(adminExample);
        // 2.判断admin是否为空
        if (adminList==null||adminList.size()==0)
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);

        if (adminList.size()>1)
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        // 如果admin为空抛异常
        Admin admin = adminList.get(0);
        if (admin==null) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        // 3.比较密码
        // 数据库中的密码
        String userPwdDB = admin.getUserPwd();
        // 表单中的密码
        String userPwdForm = CrowdUtil.md5(userPwd);
        if (!Objects.equals(userPwdDB,userPwdForm))
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        return admin;
    }

    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {

        // 1.开启分页功能
        PageHelper.startPage(pageNum,pageSize);

        // 2.执行查询
        List<Admin> adminList = adminMapper.selectAdminByKeyword(keyword);

        // 3.封装到PageInfo对象中
        return new PageInfo<>(adminList);
    }

    @Override
    public void remove(Integer adminId) {
        adminMapper.deleteByPrimaryKey(adminId);
    }

    @Override
    public Admin getAdminById(Integer adminId) {

        return adminMapper.selectByPrimaryKey(adminId);
    }

    @Override
    public void update(Admin admin) {
        try {
            adminMapper.updateByPrimaryKeySelective(admin);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof DuplicateKeyException)
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
        }
    }

    @Override
    public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {
       // 清空
        adminMapper.deleteRelationship(adminId);
       // 保存
        if (roleIdList!=null&&roleIdList.size()>0)
            adminMapper.insertRelationship(adminId,roleIdList);
    }

    @Override
    public Admin getAdminByLoginAcct(String username) {
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andLoginAcctEqualTo(username);
        return adminMapper.selectByExample(adminExample).get(0);
    }

}
