package com.ruoyi.school.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.PageDomain;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.core.web.page.TableSupport;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.school.domain.School;
import com.ruoyi.school.service.IApSchoolService;
import com.ruoyi.school.service.ISchoolService;
import com.ruoyi.workflow.api.RemoteWorkFlowService;
import com.ruoyi.workflow.api.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public static final String TERMINATE_END = "terminateEndEvent".toLowerCase();
    public static final String CANCEL_END = "cancelEnd".toLowerCase();
    public static final String EXCEPTION_END = "exceptionEnd".toLowerCase();
    public static final String END = "end".toLowerCase();
    public static final String TO = "to".toLowerCase();
    public static final String BACK = "back".toLowerCase();
    public static final String NEXT = "next".toLowerCase();
    public static final String GO = "go".toLowerCase();

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
            ArrayList<Object> zgs = Lists.newArrayList();
            zgs.add("zg_group");
            //超级用户
            zgs.add("super_group");
            vars.put("zg_group", zgs);

            //主管组
            ArrayList<Object> cws = Lists.newArrayList();
            cws.add("cw_group");
            //超级用户
            cws.add("super_group");
            vars.put("cwfzr_group", cws);
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
    @Log(title = "查看审批进度", businessType = BusinessType.OTHER)
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
     * 查看审批进度
     */
    @RequiresPermissions("school:school:hiAp")
    @Log(title = "查看审批进度", businessType = BusinessType.OTHER)
    @GetMapping("/HiFlowText/{id}")
    public AjaxResult HiFlowText(@PathVariable("id") String id) {
        //根据业务ID找到流程实例ID
        School school = schoolService.selectSchoolById(id);
        //调用历史
        R<List<BusinessKeysAndProcessInfos>> his = remoteWorkFlowService.getBusinessKeysAndProcessInfosWithVar(
                "",
                XTJ,
                "",
                FXCL,
                school.getWorkflowId(),
                "");

        if (SUCCESS_CODE != his.getCode()) {
            return error(his.getMsg());
        }
        return success(his.getData());
    }

    /**
     * 查看审批进度
     */
    @RequiresPermissions("school:school:hiAp")
    @Log(title = "取消发起审批", businessType = BusinessType.OTHER)
    @GetMapping("/suspendProcessInstance/{id}")
    public AjaxResult SuspendProcessInstance(@PathVariable("id") String id) {
        //根据业务ID找到流程实例ID
        School school = schoolService.selectSchoolById(id);
        //取消
        R<String> remoteR = remoteWorkFlowService.suspendProcessInstance(
                XTJ,
                "",
                FXCL,
                id);

        if (SUCCESS_CODE != remoteR.getCode()) {
            return error(remoteR.getMsg());
        }

        //更新业务状态
        school.setWorkflowStatus(0L);
        school.setWorkflowTaskNode("");
        school.setWorkflowId("");
        schoolService.updateSchool(school);
        return success();
    }

    /**
     * 查询分校成立审批列表
     */
    @RequiresPermissions("school:school:apList")
    @Log(title = "查询分校成立审批列表", businessType = BusinessType.OTHER)
    @GetMapping("/apList")
    public TableDataInfo apList() {
        //获取流程里的业务主键
        GroupTaskRequestBody groupTaskRequestBody = new GroupTaskRequestBody();
        //流程定义KEY
        groupTaskRequestBody.setProcessDefinitionKey(FXCL);
        groupTaskRequestBody.setTenantId(XTJ);
        //分页信息
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum() - 1;
        Integer pageSize = pageDomain.getPageSize();
        groupTaskRequestBody.setCurrentPage(pageNum);
        groupTaskRequestBody.setMaxResults(pageSize);

        String username = SecurityUtils.getUsername();
        //审批组ID,这个里需要找到这个人在那个审批组，自己建表，此处为了演示直接使用if判断
        if (username.startsWith("zg")) {
            groupTaskRequestBody.setTaskAssigneeGroup("zg_group");
        }
        //审批组ID,这个里需要找到这个人在那个审批组，自己建表，此处为了演示直接使用if判断
        if (username.startsWith("cw")) {
            groupTaskRequestBody.setTaskAssigneeGroup("cw_group");
        }
        //审批组ID,这个里需要找到这个人在那个审批组，自己建表，此处为了演示直接使用if判断
        if (username.startsWith("admin")) {
            groupTaskRequestBody.setTaskAssigneeGroup("super_group");
        }

        //得到审批列表
        R<TaskBusinessKeys> taskLists = remoteWorkFlowService.findGroupTaskList(groupTaskRequestBody);

        startPage();
        List<School> list = apSchoolService.selectSchoolList(taskLists.getData().getBusinessKeys());
        return getDataTable(list);
    }

    /**
     * 审批
     */
    @RequiresPermissions("school:school:apList")
    @GetMapping("/apSchool/{id}/{to}")
    @Log(title = "审批", businessType = BusinessType.UPDATE)
    public R<String> apSchool(@PathVariable("id") String id, @PathVariable("to") String to) {
        //根据业务主键查询流程实例ID
        School school = schoolService.selectSchoolById(id);
        if (school == null) {
            return R.fail("没有该校区");
        }

        //完成任务准备参数
        TaskCompleteRequestBody taskCompleteRequestBody = new TaskCompleteRequestBody();
        //流程定义KEY
        taskCompleteRequestBody.setProcessDefinitionKey(FXCL);
        taskCompleteRequestBody.setProcessDefinitionId("");
        String username = SecurityUtils.getUsername();
        //审批组ID,这个里需要找到这个人在那个审批组，自己建表，此处为了演示直接使用if判断
        if (username.startsWith("zg")) {
            taskCompleteRequestBody.setTaskAssigneeGroup("zg_group");
        }
        //审批组ID,这个里需要找到这个人在那个审批组，自己建表，此处为了演示直接使用if判断
        if (username.startsWith("cw")) {
            taskCompleteRequestBody.setTaskAssigneeGroup("cw_group");
        }
        //审批组ID,这个里需要找到这个人在那个审批组，自己建表，此处为了演示直接使用if判断
        if (username.startsWith("admin")) {
            taskCompleteRequestBody.setTaskAssigneeGroup("super_group");
        }
        //审批人
        taskCompleteRequestBody.setTaskAssignee(username);
        //设置业务主键
        taskCompleteRequestBody.setBusinessKey(id);
        //租户ID
        taskCompleteRequestBody.setTenantId(XTJ);
        //审批意见、审批通过或不通过
        HashMap<String, Object> vars = Maps.newHashMap();
        vars.put("approvalComment", "这里是我的审批意见，还能设置各种附件");
        vars.put("to", to);
        taskCompleteRequestBody.setLocalVars(vars);

        //完成任务
        R<TaskCompleteResponseBody> taskCompleteResponseBodyR = remoteWorkFlowService.taskClaimAndComplete(taskCompleteRequestBody);
        if (taskCompleteResponseBodyR.getCode() != SUCCESS_CODE) {
            return R.fail(taskCompleteResponseBodyR.getMsg());
        }

        //更新审批人node名称，这一步可选
        school.setWorkflowTaskNode(taskCompleteResponseBodyR.getData().getApproveStatus().getActivityId());

        //判断流程是否结束
        if (taskCompleteResponseBodyR.getData().getApproveStatus().isEnd()) {
            if (taskCompleteResponseBodyR.getData().getApproveStatus().getEndType().equals(TERMINATE_END)) {
                //中途取消或者驳回
                school.setWorkflowStatus(2L);
            }

            if (taskCompleteResponseBodyR.getData().getApproveStatus().getEndType().equals(END)) {
                //正常结束
                school.setWorkflowStatus(3L);
            }
        }

        schoolService.updateSchool(school);
        return R.ok();
    }
}
