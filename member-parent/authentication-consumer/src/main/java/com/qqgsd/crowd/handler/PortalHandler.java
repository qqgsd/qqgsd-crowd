package com.qqgsd.crowd.handler;

import com.qqgsd.crowd.api.MysqlRemoteService;
import com.qqgsd.crowd.constant.CrowdConstant;
import com.qqgsd.crowd.entity.vo.PortalTypeVO;
import com.qqgsd.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class PortalHandler {

    @Autowired
    private MysqlRemoteService mysqlRemoteService;

    @RequestMapping(value = "/")
    public String showPortalPage(ModelMap modelMap){
        ResultEntity<List<PortalTypeVO>> resultEntity = mysqlRemoteService.getPortalTypeProjectDataRemote();
        String result = resultEntity.getResult();
        if (ResultEntity.SUCCESS.equals(result)){
            List<PortalTypeVO> list = resultEntity.getData();
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_PORTAL_DATA,list);
        }
        return "portal";
    }
}
