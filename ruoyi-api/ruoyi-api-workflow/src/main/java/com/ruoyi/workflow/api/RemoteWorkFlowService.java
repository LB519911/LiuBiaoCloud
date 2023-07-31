package com.ruoyi.workflow.api;

import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.workflow.api.factory.RemoteWorkFlowFallbackFactory;
import com.ruoyi.workflow.api.model.*;
import com.ruoyi.workflow.api.model.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 工作流服务
 *
 * @author liubiao
 */
@FeignClient(contextId = "remoteWorkFlowService", value = ServiceNameConstants.WORKFLOW_SERVICE, fallbackFactory = RemoteWorkFlowFallbackFactory.class)
public interface RemoteWorkFlowService {

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
    public R<String> deploy(@RequestParam String definitionName, @RequestParam String tenantId, MultipartFile bpmn);

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
    public R<ProcessDefinitionPojoPage> getDeployListByName(@RequestParam String definitionName,
                                                            @RequestParam Integer currentPage,
                                                            @RequestParam Integer maxResults,
                                                            @RequestParam String tenantId,
                                                            @RequestParam String processDefinitionKey);

    /**
     * 根据部署名称查询最新的流程定义信息
     *
     * @param definitionName       流程定义名称
     * @param tenantId             租户ID
     * @param processDefinitionKey 流程KEY
     * @return ProcessDefinitionPojo
     */
    @GetMapping("/deployLatestListByName")
    public R<ProcessDefinitionPojo> getDeployLatestListByName(@RequestParam String definitionName,
                                                              @RequestParam String tenantId,
                                                              @RequestParam String processDefinitionKey);

    /**
     * @param definitionName       流程定义名称
     * @param tenantId             租户ID
     * @param processDefinitionKey 流程KEY
     * @param processDefinitionId  流程定义ID
     */
    @GetMapping("/getDeployImage")
    public R<String> getDeployImage(@RequestParam String definitionName,
                                    @RequestParam String tenantId,
                                    @RequestParam String processDefinitionKey,
                                    @RequestParam String processDefinitionId,
                                    HttpServletResponse httpServletResponse);

    /**
     * 根据流程定义ID删除所有信息,危险！
     *
     * @param processDefinitionId 流程定义ID
     * @return String
     */
    @GetMapping("/deleteProcessDefinitionAllInfo")
    public R<String> deleteProcessDefinitionAllInfo(@RequestParam String processDefinitionId,
                                                    @RequestParam String tenantId);

    /**
     * 根据流程名称删除所有信息,危险！
     *
     * @param processDefinitionName 流程定义名称
     * @return String
     */
    @GetMapping("/deleteProcessDefinitionByName")
    public R<String> deleteProcessDefinitionByName(@RequestParam String processDefinitionName,
                                                   @RequestParam String tenantId);

    /**
     * 根据流程Key删除所有信息,危险！
     *
     * @param processDefinitionKey 流程定义Key
     * @return String
     */
    @GetMapping("/deleteProcessDefinitionByKey")
    public R<String> deleteProcessDefinitionByKey(@RequestParam String processDefinitionKey,
                                                  @RequestParam String tenantId);

    /**
     * 发起任务
     *
     * @param requestBody 请求体
     * @return String
     */
    @PostMapping("/startProcessInstanceByKey")
    public R<String> startProcessInstanceByKey(@RequestBody StartProcessInstanceByIdRequestBody requestBody);

    /**
     * 查询组任务
     *
     * @param requestBody 请求
     * @return R<TaskBusinessKeys> 结果
     */
    @PostMapping("/findGroupTaskList")
    public R<TaskBusinessKeys> findGroupTaskList(@RequestBody GroupTaskRequestBody requestBody);

    /**
     * 查询候选任务
     *
     * @param requestBody 请求
     * @return R<TaskBusinessKeys> 结果
     */
    @PostMapping("/findTaskCandidateUserList")
    public R<TaskBusinessKeys> findTaskCandidateUserList(@RequestBody GroupTaskRequestBody requestBody);

    /**
     * 拾取组任务
     *
     * @param requestBody 请求
     * @return String
     */
    @PostMapping("/claimTaskFormGroup")
    public R<String> claimTaskFormGroup(@RequestBody GroupTaskRequestBody requestBody);

    /**
     * 拾取候选任务
     *
     * @param requestBody 请求
     * @return String
     */
    @PostMapping("/claimTaskFromCandidateUser")
    public R<String> claimTaskFromCandidateUser(@RequestBody GroupTaskRequestBody requestBody);

    /**
     * 归还组任务
     *
     * @param requestBody 请求
     * @return String
     */
    @PostMapping("/setAssigneeToGroupTask")
    public R<String> setAssigneeToGroupTask(@RequestBody GroupTaskRequestBody requestBody);

    /**
     * 归还候选任务
     *
     * @param requestBody 请求
     * @return String
     */
    @PostMapping("/setAssigneeToTaskCandidateUser")
    public R<String> setAssigneeToTaskCandidateUser(@RequestBody GroupTaskRequestBody requestBody);

    /**
     * 转交任务（不推荐）
     *
     * @param requestBody 请求
     * @return String
     */
    @PostMapping("/setAssigneeToCandidateUser")
    public R<String> setAssigneeToCandidateUser(@RequestBody GroupTaskRequestBody requestBody);

    /**
     * 查询可审批列表
     *
     * @param requestBody 请求他
     * @return TaskBusinessKeys  返回
     */
    @PostMapping("/getTaskBusinessKeys")
    public R<TaskBusinessKeys> getTaskBusinessKeys(@RequestBody TaskCompleteRequestBody requestBody);

    /**
     * 审批任务
     *
     * @param requestBody 请求体
     * @return TaskCompleteResponseBody 返回值
     */
    @PostMapping("/taskComplete")
    public R<TaskCompleteResponseBody> taskComplete(@RequestBody TaskCompleteRequestBody requestBody);

    /**
     * 拾取组任务和审批任务
     *
     * @param requestBody 请求体
     * @return TaskCompleteResponseBody 返回值
     */
    @PostMapping("/taskClaimAndComplete")
    public R<TaskCompleteResponseBody> taskClaimAndComplete(@RequestBody TaskCompleteRequestBody requestBody);

    /**
     * 拾取候选任务和审批任务
     *
     * @param requestBody 请求体
     * @return TaskCompleteResponseBody 返回值
     */
    @PostMapping("/taskCandidateUserClaimAndComplete")
    public R<TaskCompleteResponseBody> taskCandidateUserClaimAndComplete(@RequestBody TaskCompleteRequestBody requestBody);

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
    public R<List<BusinessKeysAndProcessInfos>> getBusinessKeysAndProcessInfos(@RequestParam String assignee,
                                                                               @RequestParam String tenantId,
                                                                               @RequestParam String processDefinitionId,
                                                                               @RequestParam String processDefinitionKey,
                                                                               @RequestParam String processInstanceId,
                                                                               @RequestParam String businessKey);

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
    public R<List<BusinessKeysAndProcessInfos>> getBusinessKeysAndProcessInfosWithVar(@RequestParam String assignee,
                                                                                      @RequestParam String tenantId,
                                                                                      @RequestParam String processDefinitionId,
                                                                                      @RequestParam String processDefinitionKey,
                                                                                      @RequestParam String processInstanceId,
                                                                                      @RequestParam String businessKey);

    /**
     * 获取流程进度图片
     *
     * @param processInstanceId   流程实例ID
     * @param httpServletResponse 结果
     */
    @GetMapping(value = "/getProcessImage")
    public void getProcessImage(@RequestParam String processInstanceId,
                                HttpServletResponse httpServletResponse);

    /**
     * @param tenantId    租户ID
     * @param businessKey 业务主键
     * @return R<List < BusinessKeyInfo>> 结果
     */
    @GetMapping(value = "/getBusinessKeyInfos")
    public R<List<BusinessKeyInfo>> getBusinessKeyInfos(@RequestParam String tenantId,
                                                        @RequestParam String processDefinitionId,
                                                        @RequestParam String processDefinitionKey,
                                                        @RequestParam String businessKey);

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
    public R<String> suspendProcessInstance(
            @RequestParam String tenantId,
            @RequestParam String processDefinitionId,
            @RequestParam String processDefinitionKey,
            @RequestParam String businessKey);

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
    public R<String> suspendProcessDefinition(
            @RequestParam String tenantId,
            @RequestParam String processDefinitionId,
            @RequestParam String processDefinitionKey,
            @RequestParam Boolean cascade);

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
    public R<String> notSuspendProcessInstance(
            @RequestParam String tenantId,
            @RequestParam String processDefinitionId,
            @RequestParam String processDefinitionKey,
            @RequestParam String businessKey);

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
    public R<String> notSuspendProcessDefinition(
            @RequestParam String tenantId,
            @RequestParam String processDefinitionId,
            @RequestParam String processDefinitionKey,
            @RequestParam Boolean cascade);
}
