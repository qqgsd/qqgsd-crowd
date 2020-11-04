package com.qqgsd.crowd.mvc.controller;

import com.qqgsd.crowd.entity.Menu;
import com.qqgsd.crowd.service.api.MenuService;
import com.qqgsd.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MenuHandler {

    @Autowired
    private MenuService menuService;

    /**
     *
     * 获取Menu树
     * @return 返回Menu根节点
     */
    @RequestMapping(value = "/menu/get/whole/tree.json")
    public ResultEntity<Menu> getWholeTree(){
        // 获取所有Menu对象
        List<Menu> menuList = menuService.getAll();

        // 根节点
        Menu root=null;

        // 存储id和Menu的对应关系便于查找父节点
        Map<Integer,Menu> menuMap=new HashMap<>();

        // 填充menuMap
        for (Menu menu : menuList) {
            menuMap.put(menu.getId(), menu);
        }

        // 查找根节点，组装父子节点
        for (Menu menu : menuList) {
            Integer pid = menu.getPid();
            if (pid==null){   // 查找根节点
                root=menu;
                continue;
            }

            // 把子节点放到父节点中
            Menu parent = menuMap.get(pid);
            parent.getChildren().add(menu);

        }
        return ResultEntity.successWithData(root);
    }

    /**
     * 保存添加的Menu节点
     * @param menu 要保存的Menu节点
     */
    @RequestMapping(value = "menu/save/node.json")
    public ResultEntity<String> saveMenu(Menu menu){
        menuService.saveMenu(menu);
        return ResultEntity.successWithoutData();
    }

    /**
     * 更新Menu
     * @param menu 要更新的Menu的id
     */
    @RequestMapping(value = "menu/edit/node.json")
    public ResultEntity<String> updateMenu(Menu menu){
        menuService.updateById(menu);
        return ResultEntity.successWithoutData();
    }

    /**
     * 删除Menu对象
     * @param id 要删除Menu的id
     */
    @RequestMapping(value = "menu/remove/node.json")
    public ResultEntity<String> deleteMenu(Integer id){
        menuService.deleteById(id);
        return ResultEntity.successWithoutData();
    }
}
