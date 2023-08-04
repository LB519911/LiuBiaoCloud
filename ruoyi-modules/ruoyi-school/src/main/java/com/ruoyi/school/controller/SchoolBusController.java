package com.ruoyi.school.controller;

import com.google.common.collect.Lists;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.PageDomain;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.core.web.page.TableSupport;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.school.domain.School;
import com.ruoyi.school.service.IApSchoolService;
import com.ruoyi.school.service.ISchoolService;
import com.ruoyi.workflow.api.RemoteWorkFlowService;
import com.ruoyi.workflow.api.model.GroupTaskRequestBody;
import com.ruoyi.workflow.api.model.ProcessDefinitionPojo;
import com.ruoyi.workflow.api.model.StartProcessInstanceByIdRequestBody;
import com.ruoyi.workflow.api.model.TaskBusinessKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * 分校成立Controller
 *
 * @author 刘彪
 * @date 2023-08-03
 */
@RestController
@RequestMapping("/school")
public class SchoolBusController extends BaseController {
    //工作流流程信息
    public static final String FXCL = "fxcl";
    public static final String XTJ = "xtj";
    public static final String DEFINITION_NAME = "分校成立流程";
    @Autowired
    private ISchoolService schoolService;
    @Autowired
    private RemoteWorkFlowService remoteWorkFlowService;
    @Autowired
    private IApSchoolService apSchoolService;

    public static final int SUCCESS_CODE = 200;

    /**
     * 分校成立发起流程
     */
    @RequiresPermissions("school:school:startAp")
    @Log(title = "分校成立发起流程", businessType = BusinessType.UPDATE)
    @GetMapping("/startFlow/{id}")
    public AjaxResult startFlow(@PathVariable("id") String id) {
        //查询流程信息
        R<ProcessDefinitionPojo> processDefinitions = remoteWorkFlowService
                .getDeployLatestListByName(DEFINITION_NAME, XTJ, FXCL);

        if (SUCCESS_CODE != processDefinitions.getCode()) {
            return error(processDefinitions.getMsg());
        }

        try {
            School school = new School();
            school.setId(id);
            school.setWorkflowStatus(1L);

            //发起流程请求参数
            StartProcessInstanceByIdRequestBody requestBody = new StartProcessInstanceByIdRequestBody();
            //向工作流设置任务主键
            requestBody.setBusinessKey(id);
            //向工作流设置流程定义ID
            requestBody.setProcessDefinitionId(processDefinitions.getData().getId());
            //向工作流设置流程定义KEY
            requestBody.setProcessDefinitionKey(FXCL);
            //向工作流设置流程变量（审批人、流程走向参数请放到这个里，自己建表，此处为了演示直接写到map）
            HashMap<String, Object> vars = new HashMap();
            //主管组
            vars.put("zg_group", "zg_group");
            //财务负责人组
            vars.put("cwfzr_group", "cwfzr_group");
            requestBody.setGlobalVar(vars);
            //向工作流设置租户ID
            requestBody.setTenantId(XTJ);

            //发起流程
            R<String> startProcessInstanceInfo = remoteWorkFlowService.startProcessInstanceByKey(requestBody);
            if (SUCCESS_CODE != startProcessInstanceInfo.getCode()) {
                return error(startProcessInstanceInfo.getMsg());
            }

            //工作流发起成功后会返回instanceID，持久化到数据库
            school.setWorkflowId(startProcessInstanceInfo.getData());
            //发起人是本身
            school.setWorkflowTaskNode("发起人");
            schoolService.updateSchool(school);
        } catch (Exception e) {
            return error(e.getMessage());
        }
        return success(schoolService.selectSchoolById(id));
    }

    /**
     * 查看审批进度
     */
    @RequiresPermissions("school:school:hiAp")
    @Log(title = "分校成立发起流程", businessType = BusinessType.UPDATE)
    @GetMapping("/hiFlow/{id}")
    public R<String> HiFlow(@PathVariable("id") String id) {
        //根据业务ID找到流程实例ID
        School school = schoolService.selectSchoolById(id);
        //调用流程图
        R<String> processImageBase64 = remoteWorkFlowService.getProcessImageBase64(school.getWorkflowId());

        if (processImageBase64.getCode() != SUCCESS_CODE) {
            return R.fail(processImageBase64.getMsg());
        }
        return R.ok(processImageBase64.getData());
    }

    /**
     * 查询分校成立审批列表
     */
    @RequiresPermissions("school:school:apList")
    @GetMapping("/apList")
    public TableDataInfo apList() {
        //获取流程里的业务主键
        GroupTaskRequestBody groupTaskRequestBody = new GroupTaskRequestBody();
        //流程定义KEY
        groupTaskRequestBody.setProcessDefinitionKey(FXCL);
        //审批组ID
        groupTaskRequestBody.setTaskAssigneeGroup("zg_group");
        groupTaskRequestBody.setTenantId(XTJ);
        //分页信息
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum() - 1;
        Integer pageSize = pageDomain.getPageSize();
        groupTaskRequestBody.setCurrentPage(pageNum);
        groupTaskRequestBody.setMaxResults(pageSize);

        //得到审批列表
        R<TaskBusinessKeys> taskLists = remoteWorkFlowService.findGroupTaskList(groupTaskRequestBody);

        startPage();
        List<School> list = apSchoolService.selectSchoolList(taskLists.getData().getBusinessKeys());
        return getDataTable(list);
    }
}
