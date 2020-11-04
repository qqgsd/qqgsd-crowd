package com.qqgsd.crowd.service.impl;

import com.qqgsd.crowd.entity.Auth;
import com.qqgsd.crowd.entity.Menu;
import com.qqgsd.crowd.entity.MenuExample;
import com.qqgsd.crowd.mapper.AuthMapper;
import com.qqgsd.crowd.mapper.MenuMapper;
import com.qqgsd.crowd.service.api.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    /*@Autowired
    private AuthMapper authMapper;*/

    @Override
    public List<Menu> getAll() {
        return menuMapper.selectByExample(new MenuExample());
    }

    @Override
    public void saveMenu(Menu menu) {
        menuMapper.insert(menu);
        //authMapper.insert(new Auth(menu.getId(),menu.getName(),menu.getName(),menu.getPid()));
    }

    @Override
    public void updateById(Menu menu) {
        // 有选择的更新
        menuMapper.updateByPrimaryKeySelective(menu);
        //authMapper.updateByPrimaryKeySelective(new Auth(menu.getId(),menu.getName(),menu.getName(),menu.getPid()));
    }

    @Override
    public void deleteById(Integer id) {
        menuMapper.deleteByPrimaryKey(id);
        //authMapper.deleteByPrimaryKey(id);
    }
}
