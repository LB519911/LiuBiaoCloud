package com.ruoyi.school.controller;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.school.domain.School;
import com.ruoyi.school.service.ISchoolService;
import com.ruoyi.workflow.api.RemoteWorkFlowService;
import com.ruoyi.workflow.api.model.ProcessDefinitionPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 分校成立Controller
 *
 * @author 刘彪
 * @date 2023-08-03
 */
@RestController
@RequestMapping("/school")
public class SchoolBusController extends BaseController {
    @Autowired
    private ISchoolService schoolService;
    @Autowired
    private RemoteWorkFlowService remoteWorkFlowService;

    /**
     * 分校成立发起流程
     */
    @RequiresPermissions("school:school:startAp")
    @Log(title = "分校成立发起流程", businessType = BusinessType.UPDATE)
    @GetMapping("/startFlow/{id}")
    public AjaxResult startFlow(@PathVariable("id") String id) {
        //查询流程信息
        R<ProcessDefinitionPojo> processDefinitions = remoteWorkFlowService
                .getDeployLatestListByName("x", "xtj", "x");

        School school = new School();
        school.setId(id);
        school.setWorkflowStatus(1L);
        try {
            schoolService.updateSchool(school);
        } catch (Exception e) {
            return error(e.getMessage());
        }
        return success(schoolService.selectSchoolById(id));
    }
}
