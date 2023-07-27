package com.ruoyi.workflow.controller;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.workflow.service.activiti.ProcessImageService;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.io.IOUtils;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 刘彪
 */
@Log4j2
@RestController
@RequestMapping("/workflow")
@Transactional(rollbackFor = Exception.class)
public class WorkFlowController extends BaseController {

    public static final String TERMINATE_END = "terminateEndEvent".toLowerCase();
    public static final String CANCEL_END = "cancelEnd".toLowerCase();
    public static final String EXCEPTION_END = "exceptionEnd".toLowerCase();
    public static final String END = "end".toLowerCase();
    public static final String TO = "to".toLowerCase();
    public static final String BACK = "back".toLowerCase();
    public static final String NEXT = "next".toLowerCase();
    public static final String GO = "go".toLowerCase();

    public static final String YYYY_MM_DD_HH_MM_SS = "YYYY-MM-dd HH:mm:ss";

    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private HistoryService historyService;
    @Resource
    private TaskService taskService;
    @Resource
    private ProcessImageService processImageService;

    //start *****************************部署部分开始*********************************

    /**
     * 部署
     *
     * @param definitionName 流程定义名称
     * @param tenantId       机构ID
     * @param bpmn           文件
     * @return R 返回
     * @throws IOException 异常
     */
    @PostMapping("/deploy")
    public AjaxResult deploy(@RequestParam String definitionName,
                             @RequestParam String tenantId,
                             MultipartFile bpmn) throws IOException {
        if (bpmn == null || bpmn.isEmpty()) {
            return AjaxResult.error("没有bpmn文件!");
        }

        Deployment deploy = repositoryService.createDeployment()
                .tenantId(tenantId)
                .addInputStream(definitionName + ".bpmn", bpmn.getInputStream())
                .name(definitionName)
                .deploy();

        if (deploy == null) {
            return AjaxResult.error("部署不成功!");
        }
        return AjaxResult.success("部署成功!");
    }
    //end *****************************部署部分结束*********************************


    //start *****************************查看部署部分开始*********************************

    /**
     * 根据部署名称查询流程定义信息，降序排列
     *
     * @param definitionName       流程定义名称
     * @param currentPage          当前页 0 开始
     * @param maxResults           页内条数
     * @param tenantId             租户ID
     * @param processDefinitionKey 流程KEY
     * @return ProcessDefinitionPojoPage 分页信息
     */
    @GetMapping("/getDeployListByName")
    public AjaxResult getDeployListByName(@RequestParam String definitionName,
                                          @RequestParam Integer currentPage,
                                          @RequestParam Integer maxResults,
                                          @RequestParam String tenantId,
                                          @RequestParam String processDefinitionKey) {
        //返回值

        ProcessDefinitionPojoPage result = new ProcessDefinitionPojoPage();
        ArrayList<ProcessDefinitionPojo> processDefinitionPojoList = Lists.newArrayList();

        //分页信息
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .processDefinitionTenantId(tenantId)
                .processDefinitionName(definitionName);

        if (!"".equals(processDefinitionKey)) {
            processDefinitionQuery.processDefinitionKey(processDefinitionKey);
        }

        //分页信息
        long totalRecords = processDefinitionQuery.count();

        int pageSize = maxResults;
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize) - 1;

        List<ProcessDefinition> processDefinitions = processDefinitionQuery
                .orderByProcessDefinitionVersion()
                .desc()
                .listPage(currentPage * pageSize, pageSize);

        //分页信息
        boolean hasNextPage = (currentPage < totalPages) && processDefinitions.size() > 0;
        boolean hasPreviousPage = currentPage > 0;

        for (ProcessDefinition processDefinition : processDefinitions) {
            ProcessDefinitionPojo processDefinitionPojo = new ProcessDefinitionPojo();
            BeanUtils.copyProperties(processDefinition, processDefinitionPojo);
            //是否停用
            processDefinitionPojo.setSuspended(processDefinition.isSuspended());
            processDefinitionPojoList.add(processDefinitionPojo);
        }

        result.setProcessDefinitionPojoList(processDefinitionPojoList);
        result.setCurrentPage(Long.valueOf(currentPage));
        result.setHasNextPage(hasNextPage);
        result.setHasPreviousPage(hasPreviousPage);
        result.setPageSize((long) pageSize);

        long pageCount = totalRecords / pageSize;
        result.setPageCount(pageCount);
        if (totalRecords % pageSize > 0) {
            result.setPageCount(pageCount + 1);
        }
        return AjaxResult.success(result);
    }

    /**
     * 根据部署名称查询最新的流程定义信息
     *
     * @param definitionName       流程定义名称
     * @param tenantId             租户ID
     * @param processDefinitionKey 流程KEY
     * @return ProcessDefinitionPojo
     */
    @GetMapping("/deployLatestListByName")
    public AjaxResult getDeployLatestListByName(@RequestParam String definitionName,
                                                @RequestParam String tenantId,
                                                @RequestParam String processDefinitionKey) {
        //返回值
        ProcessDefinitionPojo result = new ProcessDefinitionPojo();

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .processDefinitionTenantId(tenantId)
                .processDefinitionName(definitionName);

        if (!"".equals(processDefinitionKey)) {
            processDefinitionQuery.processDefinitionKey(processDefinitionKey);
        }

        ProcessDefinition processDefinition = processDefinitionQuery
                .active()
                .latestVersion()
                .singleResult();

        if (processDefinition != null) {
            BeanUtils.copyProperties(processDefinition, result);
            return AjaxResult.success(result);
        } else {
            return AjaxResult.error("没有该流程定义");
        }
    }

    /**
     * @param definitionName       流程定义名称
     * @param tenantId             租户ID
     * @param processDefinitionKey 流程KEY
     * @param processDefinitionId  流程定义ID
     */
    @GetMapping("/getDeployImage")
    public AjaxResult getDeployImage(@RequestParam String definitionName,
                                     @RequestParam String tenantId,
                                     @RequestParam String processDefinitionKey,
                                     @RequestParam String processDefinitionId,
                                     HttpServletResponse httpServletResponse) {

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .processDefinitionTenantId(tenantId)
                .processDefinitionName(definitionName)
                .processDefinitionKey(processDefinitionKey);

        if (!"".equals(processDefinitionId)) {
            processDefinitionQuery.processDefinitionId(processDefinitionId);
        }

        ProcessDefinition processDefinition = processDefinitionQuery
                .latestVersion()
                .singleResult();

        if (processDefinition == null) {
            return AjaxResult.error("没有该流程定义");
        }

        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        if (bpmnModel != null && bpmnModel.getLocationMap().size() > 0) {
            DefaultProcessDiagramGenerator ge = new DefaultProcessDiagramGenerator();

            InputStream inputStream = ge.generateDiagram(bpmnModel, "宋体", "宋体", "宋体");

            try (OutputStream outputStream = httpServletResponse.getOutputStream()) {
                byte[] bytes = IOUtils.toByteArray(inputStream);
                httpServletResponse.setContentType("image/svg+xml");
                outputStream.write(bytes);
                outputStream.flush();
                return AjaxResult.success("成功");
            } catch (Exception e) {
                return AjaxResult.error(e.getMessage());
            }
        } else {
            return AjaxResult.error("没有该流程定义");
        }
    }
    //end *****************************查看部署部分结束*********************************


    //start *****************************根据流程定义ID删除所有流程部分开始*********************************

    /**
     * 根据流程定义ID删除所有信息,危险！
     *
     * @param processDefinitionId 流程定义ID
     * @return String
     */
    @GetMapping("/deleteProcessDefinitionAllInfo")
    public AjaxResult deleteProcessDefinitionAllInfo(@RequestParam String processDefinitionId,
                                                     @RequestParam String tenantId) {
        // 删除流程定义及相关数据
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionTenantId(tenantId)
                    .processDefinitionId(processDefinitionId)
                    .singleResult();
            repositoryService.deleteDeployment(processDefinition.getDeploymentId(), true);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success("成功!");
    }

    /**
     * 根据流程名称删除所有信息,危险！
     *
     * @param processDefinitionName 流程定义名称
     * @return String
     */
    @GetMapping("/deleteProcessDefinitionByName")
    public AjaxResult deleteProcessDefinitionByName(@RequestParam String processDefinitionName,
                                                    @RequestParam String tenantId) {
        // 删除流程定义及相关数据
        try {
            List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionTenantId(tenantId)
                    .processDefinitionName(processDefinitionName)
                    .list();

            for (ProcessDefinition processDefinition : processDefinitions) {
                repositoryService.deleteDeployment(processDefinition.getDeploymentId(), true);
            }
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success("成功!");
    }

    /**
     * 根据流程Key删除所有信息,危险！
     *
     * @param processDefinitionKey 流程定义Key
     * @return String
     */
    @GetMapping("/deleteProcessDefinitionByKey")
    public AjaxResult deleteProcessDefinitionByKey(@RequestParam String processDefinitionKey,
                                                   @RequestParam String tenantId) {
        // 删除流程定义及相关数据
        try {
            List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionTenantId(tenantId)
                    .processDefinitionKey(processDefinitionKey)
                    .list();

            for (ProcessDefinition processDefinition : processDefinitions) {
                repositoryService.deleteDeployment(processDefinition.getDeploymentId(), true);
            }
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success("成功!");
    }
    //end *****************************根据流程定义ID删除所有流程部分结束*********************************


    //start *****************************发起审批部分开始*********************************

    /**
     * 发起任务
     *
     * @param requestBody 请求体
     * @return String
     */
    @PostMapping("/startProcessInstanceByKey")
    public AjaxResult startProcessInstanceByKey(@RequestBody StartProcessInstanceByIdRequestBody requestBody) {
        //流程ID为空则默认取最新的流程
        if ("".equals(requestBody.getProcessDefinitionId())) {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey(requestBody.getProcessDefinitionKey())
                    .latestVersion()
                    .active()
                    .singleResult();
            requestBody.setProcessDefinitionId(processDefinition.getId());
        }

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionTenantId(requestBody.getTenantId())
                .processDefinitionKey(requestBody.getProcessDefinitionKey())
                .processDefinitionId(requestBody.getProcessDefinitionId())
                .singleResult();

        if (processDefinition == null) {
            return AjaxResult.error("没有该流程定义!");
        }

        //校验业务主键是否处于正在发起到结束之间,处于的话不能发起任务
        TaskQuery taskQuery = taskService.createTaskQuery()
                .taskTenantId(requestBody.getTenantId())
                .processDefinitionKey(requestBody.getProcessDefinitionKey())
                .processInstanceBusinessKey(requestBody.getBusinessKey())
                .active();

        List<Task> tasks = taskQuery.list();
        if (!tasks.isEmpty()) {
            return AjaxResult.error("该业务ID正在审批当中,禁止重新发起任务");
        }

        ProcessInstance processInstance = runtimeService.startProcessInstanceById(
                processDefinition.getId(),
                requestBody.getBusinessKey(),
                requestBody.getGlobalVar()
        );

        if (processInstance == null) {
            return AjaxResult.success("任务发起失败!");
        }
        return AjaxResult.success(processInstance.getProcessInstanceId());
    }
    //end *****************************发起审批部分结束*********************************


    //start *****************************拾取、交接、归还任务部分开始*********************************

    /**
     * 查询组任务
     *
     * @param requestBody 请求
     * @return AjaxResult 结果
     */
    @PostMapping("/findGroupTaskList")
    public AjaxResult findGroupTaskList(@RequestBody GroupTaskRequestBody requestBody) {
        // 返回值
        TaskBusinessKeys result = new TaskBusinessKeys();
        ArrayList<String> businessKeys = Lists.newArrayList();

        // 分页信息
        int pageSize = requestBody.getMaxResults();
        int currentPage = requestBody.getCurrentPage();

        TaskQuery taskQuery = taskService.createTaskQuery()
                .active()
                .processDefinitionKey(requestBody.getProcessDefinitionKey())
                .taskCandidateGroup(requestBody.getTaskAssigneeGroup())
                .taskTenantId(requestBody.getTenantId());

        if (!"".equals(requestBody.getProcessDefinitionId())) {
            taskQuery.processDefinitionId(requestBody.getProcessDefinitionId());
        }

        long totalRecords = taskQuery.count();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize) - 1;

        List<Task> tasks = taskQuery.listPage(currentPage * pageSize, pageSize);

        // 分页信息
        boolean hasNextPage = (currentPage < totalPages) && tasks.size() > 0;
        boolean hasPreviousPage = currentPage > 0;

        for (Task task : tasks) {
            String businessKey = task.getBusinessKey();
            businessKeys.add(businessKey);
        }

        result.setBusinessKeys(businessKeys);
        result.setCurrentPage((long) currentPage);
        result.setHasNextPage(hasNextPage);
        result.setHasPreviousPage(hasPreviousPage);
        result.setPageSize((long) pageSize);

        long pageCount = totalRecords / pageSize;
        result.setPageCount(pageCount);
        if (totalRecords % pageSize > 0) {
            result.setPageCount(pageCount + 1);
        }

        return AjaxResult.success(result);
    }

    /**
     * 查询候选任务
     *
     * @param requestBody 请求
     * @return AjaxResult 结果
     */
    @PostMapping("/findTaskCandidateUserList")
    public AjaxResult findTaskCandidateUserList(@RequestBody GroupTaskRequestBody requestBody) {
        // 返回值
        TaskBusinessKeys result = new TaskBusinessKeys();
        ArrayList<String> businessKeys = Lists.newArrayList();

        // 分页信息
        int pageSize = requestBody.getMaxResults();
        int currentPage = requestBody.getCurrentPage();

        TaskQuery taskQuery = taskService.createTaskQuery()
                .active()
                .processDefinitionKey(requestBody.getProcessDefinitionKey())
                .taskCandidateUser(requestBody.getCandidateUser())
                .taskTenantId(requestBody.getTenantId());

        if (!"".equals(requestBody.getProcessDefinitionId())) {
            taskQuery.processDefinitionId(requestBody.getProcessDefinitionId());
        }

        long totalRecords = taskQuery.count();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize) - 1;

        List<Task> tasks = taskQuery.listPage(currentPage * pageSize, pageSize);

        // 分页信息
        boolean hasNextPage = (currentPage < totalPages) && tasks.size() > 0;
        boolean hasPreviousPage = currentPage > 0;

        for (Task task : tasks) {
            String businessKey = task.getBusinessKey();
            businessKeys.add(businessKey);
        }

        result.setBusinessKeys(businessKeys);
        result.setCurrentPage((long) currentPage);
        result.setHasNextPage(hasNextPage);
        result.setHasPreviousPage(hasPreviousPage);
        result.setPageSize((long) pageSize);

        long pageCount = totalRecords / pageSize;
        result.setPageCount(pageCount);
        if (totalRecords % pageSize > 0) {
            result.setPageCount(pageCount + 1);
        }

        return AjaxResult.success(result);
    }


    /**
     * 拾取组任务
     *
     * @param requestBody 请求
     * @return String
     */
    @PostMapping("/claimTaskFormGroup")
    public AjaxResult claimTaskFormGroup(@RequestBody GroupTaskRequestBody requestBody) {
        TaskQuery taskQuery = taskService.createTaskQuery()
                .active()
                .processDefinitionKey(requestBody.getProcessDefinitionKey())
                .taskCandidateGroup(requestBody.getTaskAssigneeGroup())
                .processInstanceBusinessKey(requestBody.getBusinessKey())
                .taskTenantId(requestBody.getTenantId());

        if (!"".equals(requestBody.getProcessDefinitionId())) {
            taskQuery.processDefinitionId(requestBody.getProcessDefinitionId());
        }

        Task task = taskQuery.singleResult();

        if (task == null) {
            return AjaxResult.error("没有该任务");
        }

        taskService.claim(task.getId(), requestBody.getTaskAssignee());
        return AjaxResult.success("成功");
    }

    /**
     * 拾取候选任务
     *
     * @param requestBody 请求
     * @return String
     */
    @PostMapping("/claimTaskFromCandidateUser")
    public AjaxResult claimTaskFromCandidateUser(@RequestBody GroupTaskRequestBody requestBody) {
        TaskQuery taskQuery = taskService.createTaskQuery()
                .active()
                .processDefinitionKey(requestBody.getProcessDefinitionKey())
                .taskCandidateUser(requestBody.getCandidateUser())
                .processInstanceBusinessKey(requestBody.getBusinessKey())
                .taskTenantId(requestBody.getTenantId());

        if (!"".equals(requestBody.getProcessDefinitionId())) {
            taskQuery.processDefinitionId(requestBody.getProcessDefinitionId());
        }

        Task task = taskQuery.singleResult();

        if (task == null) {
            return AjaxResult.error("没有该任务");
        }

        taskService.claim(task.getId(), requestBody.getCandidateUser());
        return AjaxResult.success("成功");
    }

    /**
     * 归还组任务
     *
     * @param requestBody 请求
     * @return String
     */
    @PostMapping("/setAssigneeToGroupTask")
    public AjaxResult setAssigneeToGroupTask(@RequestBody GroupTaskRequestBody requestBody) {
        TaskQuery taskQuery = taskService.createTaskQuery()
                .active()
                .processDefinitionKey(requestBody.getProcessDefinitionKey())
                .taskAssignee(requestBody.getTaskAssignee())
                .processInstanceBusinessKey(requestBody.getBusinessKey())
                .taskTenantId(requestBody.getTenantId());

        if (!"".equals(requestBody.getProcessDefinitionId())) {
            taskQuery.processDefinitionId(requestBody.getProcessDefinitionId());
        }

        Task task = taskQuery.singleResult();

        if (task == null) {
            return AjaxResult.error("没有该任务");
        }

        taskService.setAssignee(task.getId(), null);
        return AjaxResult.success("成功");
    }

    /**
     * 归还候选任务
     *
     * @param requestBody 请求
     * @return String
     */
    @PostMapping("/setAssigneeToTaskCandidateUser")
    public AjaxResult setAssigneeToTaskCandidateUser(@RequestBody GroupTaskRequestBody requestBody) {
        TaskQuery taskQuery = taskService.createTaskQuery()
                .active()
                .processDefinitionKey(requestBody.getProcessDefinitionKey())
                .taskAssignee(requestBody.getTaskAssignee())
                .processInstanceBusinessKey(requestBody.getBusinessKey())
                .taskTenantId(requestBody.getTenantId());

        if (!"".equals(requestBody.getProcessDefinitionId())) {
            taskQuery.processDefinitionId(requestBody.getProcessDefinitionId());
        }

        Task task = taskQuery.singleResult();

        if (task == null) {
            return AjaxResult.error("没有该任务");
        }

        taskService.setAssignee(task.getId(), null);
        return AjaxResult.success("成功");
    }


    /**
     * 转交任务（不推荐）
     *
     * @param requestBody 请求
     * @return String
     */
    @PostMapping("/setAssigneeToCandidateUser")
    public AjaxResult setAssigneeToCandidateUser(@RequestBody GroupTaskRequestBody requestBody) {
        TaskQuery taskQuery = taskService.createTaskQuery()
                .active()
                .processDefinitionKey(requestBody.getProcessDefinitionKey())
                .taskAssignee(requestBody.getTaskAssignee())
                .processInstanceBusinessKey(requestBody.getBusinessKey())
                .taskTenantId(requestBody.getTenantId());

        if (!"".equals(requestBody.getProcessDefinitionId())) {
            taskQuery.processDefinitionId(requestBody.getProcessDefinitionId());
        }

        Task task = taskQuery.singleResult();

        if (task == null) {
            return AjaxResult.error("没有该任务");
        }

        taskService.setAssignee(task.getId(), requestBody.getTaskAssigneeAnther());
        return AjaxResult.success("成功");
    }
    //end *****************************拾取、交接、归还任务部分结束*********************************


    //start *****************************查询可审批列表部分开始*********************************

    /**
     * 查询可审批列表
     *
     * @param requestBody 请求他
     * @return TaskBusinessKeys  返回
     */
    @PostMapping("/getTaskBusinessKeys")
    public AjaxResult getTaskBusinessKeys(@RequestBody TaskCompleteRequestBody requestBody) {
        //返回值
        TaskBusinessKeys result = new TaskBusinessKeys();
        ArrayList<String> businessKeys = Lists.newArrayList();

        //query
        TaskQuery taskQuery = taskService.createTaskQuery()
                .active()
                .processDefinitionKey(requestBody.getProcessDefinitionKey())
                .taskTenantId(requestBody.getTenantId())
                .taskAssignee(requestBody.getTaskAssignee());

        if (!"".equals(requestBody.getProcessDefinitionId())) {
            taskQuery.processDefinitionId(requestBody.getProcessDefinitionId());
        }
        //分页信息
        long totalRecords = taskQuery.count();

        int pageSize = requestBody.getMaxResults();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize) - 1;
        int currentPage = requestBody.getCurrentPage();

        List<Task> tasks = taskQuery.listPage(currentPage * pageSize, pageSize);

        //分页信息
        boolean hasNextPage = (currentPage < totalPages) && tasks.size() > 0;
        boolean hasPreviousPage = currentPage > 0;

        for (Task task : tasks) {
            String businessKey = task.getBusinessKey();
            businessKeys.add(businessKey);
        }

        result.setBusinessKeys(businessKeys);
        result.setCurrentPage((long) currentPage);
        result.setHasNextPage(hasNextPage);
        result.setHasPreviousPage(hasPreviousPage);
        result.setPageSize((long) pageSize);

        long pageCount = totalRecords / pageSize;
        result.setPageCount(pageCount);
        if (totalRecords % pageSize > 0) {
            result.setPageCount(pageCount + 1);
        }

        return AjaxResult.success(result);
    }
    //end *****************************查询可审批列表部分结束*********************************


    //start *****************************审批部分(驳回和通过)开始*********************************

    /**
     * 审批任务
     *
     * @param requestBody 请求体
     * @return TaskCompleteResponseBody 返回值
     */
    @PostMapping("/taskComplete")
    public AjaxResult taskComplete(@RequestBody TaskCompleteRequestBody requestBody) {
        TaskCompleteResponseBody responseBody = new TaskCompleteResponseBody();

        TaskQuery taskQuery = taskService.createTaskQuery()
                .active()
                .processInstanceBusinessKey(requestBody.getBusinessKey())
                .processDefinitionKey(requestBody.getProcessDefinitionKey())
                .taskAssignee(requestBody.getTaskAssignee())
                .taskTenantId(requestBody.getTenantId());

        if (!"".equals(requestBody.getProcessDefinitionId())) {
            taskQuery.processDefinitionId(requestBody.getProcessDefinitionId());
        }

        Task task = taskQuery.singleResult();

        if (task != null) {
            try {
                String executionId = task.getExecutionId();
                Execution execution = runtimeService.createExecutionQuery()
                        .executionId(executionId)
                        .executionTenantId(requestBody.getTenantId())
                        .singleResult();

                //局部变量
                taskService.setVariablesLocal(task.getId(), requestBody.getLocalVars());
                taskService.complete(task.getId());

                if ("".equals(requestBody.getProcessDefinitionId())) {
                    requestBody.setProcessDefinitionId(task.getProcessDefinitionId());
                }
                //审批状态
                ApproveStatus approveStatus = getApproveStatus(requestBody.getProcessDefinitionId(), task,
                        execution.getActivityId(), requestBody.getLocalVars(),
                        requestBody.getTenantId(), task.getProcessInstanceId(), requestBody.getProcessDefinitionKey());
                responseBody.setApproveStatus(approveStatus);
            } catch (Exception e) {
                return AjaxResult.error(e.getMessage());
            }
        } else {
            return AjaxResult.error("没有该Task");
        }
        return AjaxResult.success(responseBody);
    }

    /**
     * 拾取组任务和审批任务
     *
     * @param requestBody 请求体
     * @return TaskCompleteResponseBody 返回值
     */
    @PostMapping("/taskClaimAndComplete")
    public AjaxResult taskClaimAndComplete(@RequestBody TaskCompleteRequestBody requestBody) {
        TaskCompleteResponseBody responseBody = new TaskCompleteResponseBody();

        TaskQuery taskQuery = taskService.createTaskQuery()
                .active()
                .processInstanceBusinessKey(requestBody.getBusinessKey())
                .processDefinitionKey(requestBody.getProcessDefinitionKey())
                .taskCandidateGroup(requestBody.getTaskAssigneeGroup())
                .taskTenantId(requestBody.getTenantId());

        if (!"".equals(requestBody.getProcessDefinitionId())) {
            taskQuery.processDefinitionId(requestBody.getProcessDefinitionId());
        }

        Task task = taskQuery.singleResult();

        if (task != null) {
            try {

                //拾取任务
                taskService.claim(task.getId(), requestBody.getTaskAssignee());

                String executionId = task.getExecutionId();
                Execution execution = runtimeService.createExecutionQuery()
                        .executionId(executionId)
                        .executionTenantId(requestBody.getTenantId())
                        .singleResult();

                //局部变量
                taskService.setVariablesLocal(task.getId(), requestBody.getLocalVars());
                taskService.complete(task.getId());

                if ("".equals(requestBody.getProcessDefinitionId())) {
                    requestBody.setProcessDefinitionId(task.getProcessDefinitionId());
                }
                //审批状态
                ApproveStatus approveStatus = getApproveStatus(requestBody.getProcessDefinitionId(), task,
                        execution.getActivityId(), requestBody.getLocalVars(),
                        requestBody.getTenantId(), task.getProcessInstanceId(), requestBody.getProcessDefinitionKey());
                responseBody.setApproveStatus(approveStatus);
            } catch (Exception e) {
                return AjaxResult.error(e.getMessage());
            }
        } else {
            return AjaxResult.error("没有该Task");
        }
        return AjaxResult.success(responseBody);
    }

    /**
     * 拾取候选任务和审批任务
     *
     * @param requestBody 请求体
     * @return TaskCompleteResponseBody 返回值
     */
    @PostMapping("/taskCandidateUserClaimAndComplete")
    public AjaxResult taskCandidateUserClaimAndComplete(@RequestBody TaskCompleteRequestBody requestBody) {
        TaskCompleteResponseBody responseBody = new TaskCompleteResponseBody();

        TaskQuery taskQuery = taskService.createTaskQuery()
                .active()
                .processInstanceBusinessKey(requestBody.getBusinessKey())
                .processDefinitionKey(requestBody.getProcessDefinitionKey())
                .taskCandidateUser(requestBody.getCandidateUser())
                .taskTenantId(requestBody.getTenantId());

        if (!"".equals(requestBody.getProcessDefinitionId())) {
            taskQuery.processDefinitionId(requestBody.getProcessDefinitionId());
        }

        Task task = taskQuery.singleResult();

        if (task != null) {
            try {

                //拾取任务
                taskService.claim(task.getId(), requestBody.getCandidateUser());

                String executionId = task.getExecutionId();
                Execution execution = runtimeService.createExecutionQuery()
                        .executionId(executionId)
                        .executionTenantId(requestBody.getTenantId())
                        .singleResult();

                //局部变量
                taskService.setVariablesLocal(task.getId(), requestBody.getLocalVars());
                taskService.complete(task.getId());

                if ("".equals(requestBody.getProcessDefinitionId())) {
                    requestBody.setProcessDefinitionId(task.getProcessDefinitionId());
                }
                //审批状态
                ApproveStatus approveStatus = getApproveStatus(requestBody.getProcessDefinitionId(), task,
                        execution.getActivityId(), requestBody.getLocalVars(),
                        requestBody.getTenantId(), task.getProcessInstanceId(), requestBody.getProcessDefinitionKey());
                responseBody.setApproveStatus(approveStatus);
            } catch (Exception e) {
                return AjaxResult.error(e.getMessage());
            }
        } else {
            return AjaxResult.error("没有该Task");
        }
        return AjaxResult.success(responseBody);
    }
    //end *****************************审批部分(驳回和通过)结束*********************************


    //start *****************************审批历史部分开始*********************************

    /**
     * 根据审批人查询业务ID和流程信息
     *
     * @param assignee             办理人
     * @param tenantId             租户ID
     * @param processDefinitionId  流程定义ID
     * @param processDefinitionKey 流程定义 KEY
     * @param businessKey          业务主键
     * @return R<List < BusinessKeysAndProcessInfos>> 结果
     */
    @GetMapping("getBusinessKeysAndProcessInfos")
    public AjaxResult getBusinessKeysAndProcessInfos(@RequestParam String assignee,
                                                     @RequestParam String tenantId,
                                                     @RequestParam String processDefinitionId,
                                                     @RequestParam String processDefinitionKey,
                                                     @RequestParam String processInstanceId,
                                                     @RequestParam String businessKey) {
        //返回体
        List<BusinessKeysAndProcessInfos> businessKeysAndProcessInfos = Lists.newArrayList();

        // 查询完成的任务
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService
                .createHistoricTaskInstanceQuery()
                .taskTenantId(tenantId);

        if (!"".equals(assignee)) {
            historicTaskInstanceQuery.taskAssignee(assignee);
        }

        if (!"".equals(processDefinitionKey)) {
            historicTaskInstanceQuery.processDefinitionKey(processDefinitionKey);
        }

        if (!"".equals(processInstanceId)) {
            historicTaskInstanceQuery.processInstanceId(processInstanceId);
        }

        if (!"".equals(processDefinitionId)) {
            historicTaskInstanceQuery.processDefinitionId(processDefinitionId);
        }

        if (!"".equals(businessKey)) {
            historicTaskInstanceQuery.processInstanceBusinessKey(businessKey);
        }

        List<HistoricTaskInstance> tasks = historicTaskInstanceQuery
                .finished()
                .orderByHistoricTaskInstanceEndTime()
                .asc()
                .list();

        for (HistoricTaskInstance task : tasks) {
            // 获取流程实例ID
            String taskProcessInstanceId = task.getProcessInstanceId();
            // 根据流程实例ID查询历史流程实例
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(taskProcessInstanceId)
                    .singleResult();

            //拼装信息
            BusinessKeysAndProcessInfos info = new BusinessKeysAndProcessInfos();
            info.setBusinessKey(historicProcessInstance.getBusinessKey());
            info.setActivityId(assignee);
            info.setProcessDefinitionId(task.getProcessDefinitionId());
            info.setTenantId(tenantId);
            info.setProcessInstanceId(taskProcessInstanceId);
            info.setExecutionId(task.getExecutionId());
            info.setTaskId(task.getId());
            info.setTime(DateUtil.format(task.getTime(), YYYY_MM_DD_HH_MM_SS));
            info.setStartTime(DateUtil.format(task.getStartTime(), YYYY_MM_DD_HH_MM_SS));
            info.setEndTime(DateUtil.format(task.getEndTime(), YYYY_MM_DD_HH_MM_SS));
            info.setClaimTime(DateUtil.format(task.getClaimTime(), YYYY_MM_DD_HH_MM_SS));
            info.setActivityId(task.getAssignee());
            businessKeysAndProcessInfos.add(info);
        }

        return AjaxResult.success(businessKeysAndProcessInfos);
    }

    /**
     * 根据审批人查询业务ID和流程信息(包括运行变量)
     *
     * @param assignee             审批人
     * @param tenantId             租户ID
     * @param processDefinitionId  流程定义ID
     * @param processDefinitionKey 流程定义KEY
     * @param businessKey          业务主键
     * @return List<BusinessKeysAndProcessInfos> 返回体
     */
    @GetMapping("getBusinessKeysAndProcessInfosWithVar")
    public AjaxResult getBusinessKeysAndProcessInfosWithVar(@RequestParam String assignee,
                                                            @RequestParam String tenantId,
                                                            @RequestParam String processDefinitionId,
                                                            @RequestParam String processDefinitionKey,
                                                            @RequestParam String processInstanceId,
                                                            @RequestParam String businessKey) {
        //返回体
        List<BusinessKeysAndProcessInfos> businessKeysAndProcessInfos = Lists.newArrayList();

        // 查询完成的任务
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService
                .createHistoricTaskInstanceQuery()
                .taskTenantId(tenantId);

        if (!"".equals(assignee)) {
            historicTaskInstanceQuery.taskAssignee(assignee);
        }

        if (!"".equals(processDefinitionKey)) {
            historicTaskInstanceQuery.processDefinitionKey(processDefinitionKey);
        }

        if (!"".equals(processInstanceId)) {
            historicTaskInstanceQuery.processInstanceId(processInstanceId);
        }

        if (!"".equals(processDefinitionId)) {
            historicTaskInstanceQuery.processDefinitionId(processDefinitionId);
        }

        if (!"".equals(businessKey)) {
            historicTaskInstanceQuery.processInstanceBusinessKey(businessKey);
        }

        List<HistoricTaskInstance> tasks = historicTaskInstanceQuery
                .finished()
                .orderByHistoricTaskInstanceEndTime()
                .asc()
                .list();

        for (HistoricTaskInstance task : tasks) {
            // 获取流程实例ID
            String taskProcessInstanceId = task.getProcessInstanceId();
            // 根据流程实例ID查询历史流程实例
            HistoricProcessInstance historicProcessInstance = historyService
                    .createHistoricProcessInstanceQuery()
                    .processInstanceId(taskProcessInstanceId)
                    .singleResult();

            //拼装信息
            BusinessKeysAndProcessInfos info = new BusinessKeysAndProcessInfos();
            info.setBusinessKey(historicProcessInstance.getBusinessKey());
            info.setActivityId(assignee);
            info.setProcessDefinitionId(task.getProcessDefinitionId());
            info.setTenantId(tenantId);
            info.setProcessInstanceId(taskProcessInstanceId);
            info.setExecutionId(task.getExecutionId());
            info.setTaskId(task.getId());
            info.setTime(DateUtil.format(task.getTime(), YYYY_MM_DD_HH_MM_SS));
            info.setStartTime(DateUtil.format(task.getStartTime(), YYYY_MM_DD_HH_MM_SS));
            info.setEndTime(DateUtil.format(task.getEndTime(), YYYY_MM_DD_HH_MM_SS));
            info.setClaimTime(DateUtil.format(task.getClaimTime(), YYYY_MM_DD_HH_MM_SS));
            info.setActivityId(task.getAssignee());

            List<HistoricVariableInstance> historicVariableInstanceList = historyService
                    .createHistoricVariableInstanceQuery()
                    .taskId(task.getId())
                    .list();

            //流程变量
            Map<String, Object> var = Maps.newHashMap();
            info.setVar(var);
            for (HistoricVariableInstance hvi : historicVariableInstanceList) {
                /// System.out.println("变量ID:" + hvi.getId());
                /// System.out.println("变量类型:" + hvi.getVariableTypeName());
                /// System.out.println("变量名称:" + hvi.getVariableName());
                /// System.out.println("变量值:" + hvi.getValue());
                var.put(hvi.getVariableName(), hvi.getValue());
            }
            businessKeysAndProcessInfos.add(info);
        }

        return AjaxResult.success(businessKeysAndProcessInfos);
    }
    //end *****************************审批历史部分结束*********************************


    //start *****************************查看审批进度部分开始*********************************

    /**
     * 获取流程进度图片
     *
     * @param processInstanceId   流程实例ID
     * @param httpServletResponse 结果
     */
    @GetMapping(value = "/getProcessImage")
    public void getProcessImage(@RequestParam String processInstanceId,
                                HttpServletResponse httpServletResponse) {
        try (OutputStream outputStream = httpServletResponse.getOutputStream();
             InputStream img = processImageService.getFlowImgByProcInstId(processInstanceId)) {
            httpServletResponse.setContentType("image/svg+xml");
            IOUtils.copy(img, outputStream);
            outputStream.flush();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * @param tenantId    租户ID
     * @param businessKey 业务主键
     * @return R<List < BusinessKeyInfo>> 结果
     */
    @GetMapping(value = "/getBusinessKeyInfos")
    public AjaxResult getBusinessKeyInfos(@RequestParam String tenantId,
                                          @RequestParam String processDefinitionId,
                                          @RequestParam String processDefinitionKey,
                                          @RequestParam String businessKey) {
        //返回结果
        List<BusinessKeyInfo> results = Lists.newArrayList();

        // 查询历史流程实例
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery();
        historicProcessInstanceQuery.processInstanceBusinessKey(businessKey);
        historicProcessInstanceQuery.processInstanceTenantId(tenantId);

        if (!"".equals(processDefinitionId)) {
            historicProcessInstanceQuery.processDefinitionId(processDefinitionId);
        }

        if (!"".equals(processDefinitionKey)) {
            historicProcessInstanceQuery.processDefinitionKey(processDefinitionKey);
        }

        List<HistoricProcessInstance> historicProcessInstances = historicProcessInstanceQuery
                .orderByProcessInstanceStartTime()
                .asc()
                .list();

        for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
            BusinessKeyInfo businessKeyInfo = new BusinessKeyInfo();
            businessKeyInfo.setBusinessKey(historicProcessInstance.getBusinessKey());
            businessKeyInfo.setProcessDefinitionKey(historicProcessInstance.getProcessDefinitionKey());
            businessKeyInfo.setTenantId(tenantId);
            businessKeyInfo.setProcessDefinitionName(historicProcessInstance.getProcessDefinitionName());
            businessKeyInfo.setProcessDefinitionId(historicProcessInstance.getProcessDefinitionId());
            businessKeyInfo.setProcessInstanceId(historicProcessInstance.getId());
            businessKeyInfo.setStartTime(DateUtil.format(historicProcessInstance.getStartTime(), YYYY_MM_DD_HH_MM_SS));
            businessKeyInfo.setEndTime(DateUtil.format(historicProcessInstance.getEndTime(), YYYY_MM_DD_HH_MM_SS));

            if (historicProcessInstance.getEndTime() != null) {
                businessKeyInfo.setEnd(true);
            }

            results.add(businessKeyInfo);
        }
        return AjaxResult.success(results);
    }
    //end *****************************查看审批进度部分结束*********************************


    //start *****************************暂停审批流程部分开始*********************************

    /**
     * 暂停流程实例
     *
     * @param tenantId             机构ID
     * @param processDefinitionId  流程定义ID
     * @param processDefinitionKey 流程定义KEY
     * @param businessKey          业务主键
     * @return R 返回
     */
    @GetMapping("/suspendProcessInstance")
    public AjaxResult suspendProcessInstance(
            @RequestParam String tenantId,
            @RequestParam String processDefinitionId,
            @RequestParam String processDefinitionKey,
            @RequestParam String businessKey) {

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .processDefinitionTenantId(tenantId)
                .processDefinitionKey(processDefinitionKey);

        if (!"".equals(processDefinitionId)) {
            processDefinitionQuery.processDefinitionId(processDefinitionId);
        }

        ProcessDefinition processDefinition = processDefinitionQuery
                .singleResult();

        if (processDefinition == null) {
            return AjaxResult.error("没有该流程定义或者流程定义以终止");
        }

        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery()
                .processDefinitionId(processDefinition.getId())
                .processInstanceTenantId(tenantId)
                .processInstanceBusinessKey(businessKey);

        if (!"".equals(businessKey)) {
            processInstanceQuery.processInstanceBusinessKey(businessKey);
        }

        ProcessInstance processInstance = processInstanceQuery
                .singleResult();

        if (processInstance == null) {
            return AjaxResult.error("没有该流程实例");
        }

        if (processInstance.isSuspended()) {
            //如果是暂停，可以执行激活操作 ,参数：流程定义id
            /// runtimeService.activateProcessInstanceById(processInstance.getId());
            return AjaxResult.error("流程实例已经被暂停禁止重复暂停");
        } else {
            //如果是激活状态，可以暂停，参数：流程定义id
            runtimeService.suspendProcessInstanceById(processInstance.getId());
        }
        return AjaxResult.success("成功");
    }

    /**
     * 暂停流程定义
     *
     * @param tenantId             机构ID
     * @param processDefinitionId  流程定义ID
     * @param processDefinitionKey 流程定义KEY
     * @param cascade              是否连同实例也暂停
     * @return R 返回
     */
    @GetMapping("/suspendProcessDefinition")
    public AjaxResult suspendProcessDefinition(
            @RequestParam String tenantId,
            @RequestParam String processDefinitionId,
            @RequestParam String processDefinitionKey,
            @RequestParam Boolean cascade) {

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .processDefinitionTenantId(tenantId)
                .processDefinitionKey(processDefinitionKey);

        if (!"".equals(processDefinitionId)) {
            processDefinitionQuery.processDefinitionId(processDefinitionId);
        }

        ProcessDefinition processDefinition = processDefinitionQuery
                .singleResult();

        if (processDefinition == null) {
            return AjaxResult.error("没有该流程定义或者流程定义以终止");
        }

        if (processDefinition.isSuspended()) {
            //如果是暂停，可以执行激活操作 ,参数1 ：流程定义id ，参数2：是否激活，参数3：激活时间
            /// repositoryService.activateProcessDefinitionById(
            /// processDefinition.getId(),
            /// cascade,
            /// DateUtil.date()
            /// );
            return AjaxResult.error("流程已经被暂停禁止重复暂停");
        } else {
            //如果是激活状态，可以暂停，参数1 ：流程定义id ，参数2：是否暂停，参数3：暂停时间
            repositoryService.suspendProcessDefinitionById(
                    processDefinition.getId(),
                    cascade,
                    DateUtil.date());
        }

        return AjaxResult.success("成功");
    }
    //end *****************************暂停审批流程部分结束*********************************


    //start *****************************重新开始审批流程部分开始*********************************

    /**
     * 重新开启流程实例
     *
     * @param tenantId             机构ID
     * @param processDefinitionId  流程定义ID
     * @param processDefinitionKey 流程定义KEY
     * @param businessKey          业务主键
     * @return R 返回
     */
    @GetMapping("/notSuspendProcessInstance")
    public AjaxResult notSuspendProcessInstance(
            @RequestParam String tenantId,
            @RequestParam String processDefinitionId,
            @RequestParam String processDefinitionKey,
            @RequestParam String businessKey) {

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .processDefinitionTenantId(tenantId)
                .processDefinitionKey(processDefinitionKey);

        if (!"".equals(processDefinitionId)) {
            processDefinitionQuery.processDefinitionId(processDefinitionId);
        }

        ProcessDefinition processDefinition = processDefinitionQuery
                .active()
                .singleResult();

        if (processDefinition == null) {
            return AjaxResult.error("没有该流程定义或者流程定义以终止");
        }

        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery()
                .processDefinitionId(processDefinition.getId())
                .processInstanceTenantId(tenantId);

        if (!"".equals(businessKey)) {
            processInstanceQuery.processInstanceBusinessKey(businessKey);
        }

        ProcessInstance processInstance = processInstanceQuery
                .singleResult();

        if (processInstance == null) {
            return AjaxResult.error("没有该流程实例");
        }

        if (processInstance.isSuspended()) {
            //如果是暂停，可以执行激活操作 ,参数：流程定义id
            runtimeService.activateProcessInstanceById(processInstance.getId());
        } else {
            //如果是激活状态，可以暂停，参数：流程定义id
            /// runtimeService.suspendProcessInstanceById(processInstance.getId());
            return AjaxResult.error("流程实例已经被开启禁止重复开启");
        }
        return AjaxResult.success("成功");
    }

    /**
     * 重新开启流程定义
     *
     * @param tenantId             机构ID
     * @param processDefinitionId  流程定义ID
     * @param processDefinitionKey 流程定义KEY
     * @param cascade              是否连同实例也暂停
     * @return R 返回
     */
    @GetMapping("/notSuspendProcessDefinition")
    public AjaxResult notSuspendProcessDefinition(
            @RequestParam String tenantId,
            @RequestParam String processDefinitionId,
            @RequestParam String processDefinitionKey,
            @RequestParam Boolean cascade) {

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .processDefinitionTenantId(tenantId)
                .processDefinitionKey(processDefinitionKey);

        if (!"".equals(processDefinitionId)) {
            processDefinitionQuery.processDefinitionId(processDefinitionId);
        }

        ProcessDefinition processDefinition = processDefinitionQuery
                .singleResult();

        if (processDefinition == null) {
            return AjaxResult.error("没有该流程定义或者流程定义以终止");
        }

        if (processDefinition.isSuspended()) {
            //如果是暂停，可以执行激活操作 ,参数1 ：流程定义id ，参数2：是否激活，参数3：激活时间
            repositoryService.activateProcessDefinitionById(
                    processDefinition.getId(),
                    cascade,
                    DateUtil.date()
            );
        } else {
            //如果是激活状态，可以暂停，参数1 ：流程定义id ，参数2：是否暂停，参数3：暂停时间
            /// repositoryService.suspendProcessDefinitionById(
            /// processDefinition.getId(),
            /// cascade,
            /// DateUtil.date());
            return AjaxResult.error("流程已经被开启禁止重复开启");
        }

        return AjaxResult.success("成功");
    }
    //end *****************************重新开始审批流程部分结束*********************************


    //start *****************************工具方法部分开始*********************************

    /**
     * 是否结束
     *
     * @param processDefinitionId  流程定义ID
     * @param task                 task实体
     * @param activityId           执行人
     * @param var                  流程变量
     * @param tenantId             租户ID
     * @param processInstanceId    流程实例ID
     * @param processDefinitionKey 流程定义KEY
     * @return ApproveStatus 执行状态返回值
     */
    private ApproveStatus getApproveStatus(String processDefinitionId, Task task,
                                           String activityId, Map<String, Object> var,
                                           String tenantId, String processInstanceId, String processDefinitionKey) {
        //返回值
        ApproveStatus approveStatus = new ApproveStatus();
        approveStatus.setEnd(false);
        approveStatus.setActivityId(activityId);
        approveStatus.setProcessDefinitionId(processDefinitionId);
        approveStatus.setTenantId(tenantId);
        approveStatus.setProcessInstanceId(processInstanceId);
        approveStatus.setTaskId(task.getId());
        approveStatus.setExecutionId(task.getExecutionId());
        approveStatus.setProcessDefinitionKey(processDefinitionKey);

        // 获取BpmnModel对象
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        // 获取下一个节点的信息
        FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());

        for (SequenceFlow sequenceFlow : flowNode.getOutgoingFlows()) {
            String targetRef = sequenceFlow.getTargetRef();
            if (targetRef.contains(TERMINATE_END)) {
                if (var.get(TO).equals(BACK)) {
                    approveStatus.setEnd(true);
                    approveStatus.setEndType(TERMINATE_END);
                    break;
                }
            } else if (targetRef.contains(CANCEL_END)) {
                if (var.get(TO).equals(BACK)) {
                    approveStatus.setEnd(true);
                    approveStatus.setEndType(CANCEL_END);
                    break;
                }
            } else if (targetRef.contains(EXCEPTION_END)) {
                if (var.get(TO).equals(BACK)) {
                    approveStatus.setEnd(true);
                    approveStatus.setEndType(EXCEPTION_END);
                    break;
                }
            } else if (targetRef.contains(END)) {
                if (var.get(TO).equals(NEXT) || var.get(TO).equals(GO)) {
                    approveStatus.setEnd(true);
                    approveStatus.setEndType(END);
                    break;
                }
            }
        }
        return approveStatus;
    }
    //end *****************************工具方法部分结束*********************************
}

@Data
class StartProcessInstanceByIdRequestBody {
    String businessKey;
    String processDefinitionId;
    String processDefinitionKey;
    String definitionName;
    String tenantId;
    HashMap<String, Object> globalVar = Maps.newHashMap();
}

@Data
class TaskCompleteRequestBody {
    String businessKey;
    String processDefinitionId;
    String taskAssigneeGroup;
    String processDefinitionKey;
    String tenantId;
    String taskAssignee;
    String candidateUser;
    Integer currentPage;
    Integer maxResults;
    HashMap<String, Object> localVars = Maps.newHashMap();
}

@Data
class GroupTaskRequestBody {
    String taskId;
    String businessKey;
    String processDefinitionId;
    String processDefinitionKey;
    String taskAssigneeGroup;
    String candidateUser;
    String tenantId;
    String taskAssignee;
    String taskAssigneeAnther;
    Integer currentPage;
    Integer maxResults;
}

@Data
class TaskCompleteResponseBody {
    /**
     * 是否结束
     */
    ApproveStatus approveStatus;
}

@Data
class ProcessDefinitionPojo {
    String id;
    String name;
    String description;
    String key;
    Integer version;
    String deploymentId;
    String resourceName;
    Integer suspensionState;
    Integer revision;
    boolean isInserted;
    boolean isUpdated;
    boolean isDeleted;
    boolean isSuspended;
    String tenantId;
}

@Data
class TaskBusinessKeys {
    List<String> businessKeys;
    Long currentPage;
    Long pageSize;
    Long pageCount;
    boolean hasNextPage;
    boolean hasPreviousPage;
}

@Data
class ProcessDefinitionPojoPage {
    List<ProcessDefinitionPojo> processDefinitionPojoList;
    Long currentPage;
    Long pageSize;
    Long pageCount;
    boolean hasNextPage;
    boolean hasPreviousPage;
}

@Data
class ApproveStatus {
    boolean isEnd;
    String activityId;
    String endType;
    String processDefinitionId;
    String processDefinitionKey;
    String tenantId;
    String processInstanceId;
    String executionId;
    String taskId;
}

@Data
class BusinessKeysAndProcessInfos {
    String businessKey;
    String activityId;
    String processDefinitionId;
    String processDefinitionKey;
    String tenantId;
    String processInstanceId;
    String executionId;
    String taskId;
    String time;
    String startTime;
    String endTime;
    String claimTime;
    Map<String, Object> var;
}

@Data
class BusinessKeyInfo {
    String businessKey;
    String processDefinitionId;
    String processDefinitionKey;
    String processDefinitionName;
    String tenantId;
    String processInstanceId;
    String endTime;
    String startTime;
    boolean isEnd = false;
}