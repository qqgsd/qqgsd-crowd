package com.qqgsd.crowd.service.api;

import com.qqgsd.crowd.entity.vo.DetailProjectVO;
import com.qqgsd.crowd.entity.vo.PortalTypeVO;
import com.qqgsd.crowd.entity.vo.ProjectVO;

import java.util.List;

public interface ProjectService {
    void saveProject(ProjectVO projectVO, Integer memberID);

    List<PortalTypeVO> getPortalTypeVO();

    DetailProjectVO getDetailProjectVO(Integer id);
}
