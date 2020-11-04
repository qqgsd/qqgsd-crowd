package com.qqgsd.crowd.service.api;

import com.qqgsd.crowd.entity.Menu;

import java.util.List;

public interface MenuService {

    /**
     * 获取所有权限菜单信息
     * @return 菜单列表
     */
    List<Menu> getAll();

    /**
     * 保存添加的Menu节点
     * @param menu 要保存的Menu节点
     */
    void saveMenu(Menu menu);

    /**
     * 更新Menu
     * @param menu 要更新的Menu的id
     */
    void updateById(Menu menu);

    /**
     * 删除Menu对象
     * @param id 要删除Menu的id
     */
    void deleteById(Integer id);
}
