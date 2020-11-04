package com.qqgsd.crowd.handler;

import com.qqgsd.crowd.api.MysqlRemoteService;
import com.qqgsd.crowd.config.OSSProperties;
import com.qqgsd.crowd.constant.CrowdConstant;
import com.qqgsd.crowd.entity.vo.*;
import com.qqgsd.crowd.util.CrowdUtil;
import com.qqgsd.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class ProjectConsumerHandler {

    @Autowired
    private OSSProperties ossProperties;
    @Autowired
    private MysqlRemoteService mysqlRemoteService;

    /**
     * 上传项目信息
     * @param projectVO projectVO
     * @param headerPicture 上传的头图
     * @param detailPictureList 详细图片
     */
    @RequestMapping(value = "/create/project/information")
    public String saveProjectBasicInfo(ProjectVO projectVO, MultipartFile headerPicture,
                                       List<MultipartFile> detailPictureList, HttpSession session,
                                       ModelMap modelMap) throws IOException {
        boolean headerPictureEmpty = headerPicture.isEmpty();
        if (!headerPictureEmpty){
            ResultEntity<String> uploadHeaderResult = CrowdUtil.uploadFileToOss(ossProperties.getEndPoint(), ossProperties.getAccessKeyId(),
                    ossProperties.getAccessKeySecret(), headerPicture.getInputStream(),
                    ossProperties.getBucketName(), ossProperties.getBucketDomain(),
                    Objects.requireNonNull(headerPicture.getOriginalFilename()));

            String result = uploadHeaderResult.getResult();
            if (ResultEntity.SUCCESS.equals(result)){
                String headerPicturePath = uploadHeaderResult.getData();
                projectVO.setHeaderPicturePath(headerPicturePath);
            }
            else{
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_HEADER_PIC_UPLOAD_FAILED);
                return "project-launch";
            }
        }

        List<String> detailPicturePathList=new ArrayList<>();
        for (MultipartFile detailPicture : detailPictureList) {
            if (!detailPicture.isEmpty()){
                ResultEntity<String> detailUploadHeaderResult = CrowdUtil.uploadFileToOss(ossProperties.getEndPoint(), ossProperties.getAccessKeyId(),
                        ossProperties.getAccessKeySecret(), detailPicture.getInputStream(),
                        ossProperties.getBucketName(), ossProperties.getBucketDomain(),
                        Objects.requireNonNull(detailPicture.getOriginalFilename()));
                String detailResult = detailUploadHeaderResult.getResult();
                if (ResultEntity.SUCCESS.equals(detailResult)){
                    String detailPicturePath = detailUploadHeaderResult.getData();
                    detailPicturePathList.add(detailPicturePath);
                }
            }

            projectVO.setDetailPicturePathList(detailPicturePathList);
        }

        session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT,projectVO);
        return "redirect:http://localhost/project/return/info/page";
    }

    /**
     * 上传回报图片
     */
    @ResponseBody
    @RequestMapping(value = "/create/upload/return/picture.json")
    public ResultEntity<String> uploadReturnPicture(@RequestParam("returnPicture") MultipartFile returnPicture) throws IOException {
        return CrowdUtil.uploadFileToOss(ossProperties.getEndPoint(), ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(), returnPicture.getInputStream(),
                ossProperties.getBucketName(), ossProperties.getBucketDomain(),
                Objects.requireNonNull(returnPicture.getOriginalFilename()));
    }

    @ResponseBody
    @RequestMapping(value = "/create/save/return.json")
    public ResultEntity<String> saveReturn(ReturnVO returnVO,HttpSession session){
        try {
            ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);
            if (projectVO==null)
                return ResultEntity.failed(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT_MISSING);
            List<ReturnVO> returnVOList = projectVO.getReturnVOList();
            if (returnVOList==null){
                returnVOList=new ArrayList<>();
                projectVO.setReturnVOList(returnVOList);
            }
            returnVOList.add(returnVO);
            session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT,projectVO);
            return ResultEntity.successWithoutData();
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResultEntity.failed(exception.getMessage());
        }
    }

    @RequestMapping(value = "/create/confirm")
    public String saveConfirm(HttpSession session, MemberConfirmInfoVO memberConfirmInfoVO,ModelMap modelMap){
        ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);
        if (projectVO==null){
            throw new RuntimeException(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT_MISSING);
        }
        projectVO.setMemberConfirmInfoVO(memberConfirmInfoVO);
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        Integer memberID = memberLoginVO.getId();
        ResultEntity<String> saveResult=mysqlRemoteService.saveProjectVORemote(projectVO,memberID);
        String result = saveResult.getResult();
        if (ResultEntity.FAILED.equals(result)){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,saveResult.getMessage());
            return "project-confirm";
        }
        session.removeAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);
        return "redirect:http://localhost/project/create/success";
    }

    @RequestMapping(value = "/get/project/detail/{projectId}")
    public String getProjectDetail(@PathVariable("projectId") Integer projectId, Model model){
        ResultEntity<DetailProjectVO> resultEntity = mysqlRemoteService.getDetailProjectVORemote(projectId);
        String result = resultEntity.getResult();
        if (ResultEntity.SUCCESS.equals(result)){
            DetailProjectVO detailProjectVO = resultEntity.getData();
            model.addAttribute("detailProjectVO",detailProjectVO);
        }
        return "project-detail";
    }
}
