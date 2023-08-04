package com.ruoyi.workflow.api;

import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.workflow.api.factory.RemoteWorkFlowFallbackFactory;
import com.ruoyi.workflow.api.model.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
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
    @PostMapping(value = "/workflow/deploy", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<String> deploy(@RequestParam("definitionName") String definitionName, @RequestParam("tenantId") String tenantId, @RequestPart(value = "bpmn") MultipartFile bpmn);

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
    @GetMapping("/workflow/getDeployListByName")
    public R<ProcessDefinitionPojoPage> getDeployListByName(@RequestParam("definitionName") String definitionName,
                                                            @RequestParam("currentPage") Integer currentPage,
                                                            @RequestParam("maxResults") Integer maxResults,
                                                            @RequestParam("tenantId") String tenantId,
                                                            @RequestParam("processDefinitionKey") String processDefinitionKey);

    /**
     * 根据部署名称查询最新的流程定义信息
     *
     * @param definitionName       流程定义名称
     * @param tenantId             租户ID
     * @param processDefinitionKey 流程KEY
     * @return ProcessDefinitionPojo
     */
    @GetMapping("/workflow/deployLatestListByName")
    public R<ProcessDefinitionPojo> getDeployLatestListByName(@RequestParam("definitionName") String definitionName,
                                                              @RequestParam("tenantId") String tenantId,
                                                              @RequestParam("processDefinitionKey") String processDefinitionKey);

    /**
     * @param definitionName       流程定义名称
     * @param tenantId             租户ID
     * @param processDefinitionKey 流程KEY
     * @param processDefinitionId  流程定义ID
     */
    @GetMapping("/workflow/getDeployImage")
    public R<String> getDeployImage(@RequestParam("definitionName") String definitionName,
                                    @RequestParam("tenantId") String tenantId,
                                    @RequestParam("processDefinitionKey") String processDefinitionKey,
                                    @RequestParam("processDefinitionId") String processDefinitionId,
                                    HttpServletResponse httpServletResponse);

    /**
     * 获取流程进度Base64图片
     *
     * @param processInstanceId 流程实例ID
     */
    @GetMapping(value = "/workflow/getProcessImageBase64")
    public R<String> getProcessImageBase64(@RequestParam("processInstanceId") String processInstanceId);

    /**
     * 根据流程定义ID删除所有信息,危险！
     *
     * @param processDefinitionId 流程定义ID
     * @return String
     */
    @GetMapping("/workflow/deleteProcessDefinitionAllInfo")
    public R<String> deleteProcessDefinitionAllInfo(@RequestParam("processDefinitionId") String processDefinitionId,
                                                    @RequestParam("tenantId") String tenantId);

    /**
     * 根据流程名称删除所有信息,危险！
     *
     * @param processDefinitionName 流程定义名称
     * @return String
     */
    @GetMapping("/workflow/deleteProcessDefinitionByName")
    public R<String> deleteProcessDefinitionByName(@RequestParam("processDefinitionName") String processDefinitionName,
                                                   @RequestParam("tenantId") String tenantId);

    /**
     * 根据流程Key删除所有信息,危险！
     *
     * @param processDefinitionKey 流程定义Key
     * @return String
     */
    @GetMapping("/workflow/deleteProcessDefinitionByKey")
    public R<String> deleteProcessDefinitionByKey(@RequestParam("processDefinitionKey") String processDefinitionKey,
                                                  @RequestParam("tenantId") String tenantId);

    /**
     * 发起任务
     *
     * @param requestBody 请求体
     * @return String
     */
    @PostMapping("/workflow/startProcessInstanceByKey")
    public R<String> startProcessInstanceByKey(@RequestBody StartProcessInstanceByIdRequestBody requestBody);

    /**
     * 查询组任务
     *
     * @param requestBody 请求
     * @return R<TaskBusinessKeys> 结果
     */
    @PostMapping("/workflow/findGroupTaskList")
    public R<TaskBusinessKeys> findGroupTaskList(@RequestBody GroupTaskRequestBody requestBody);

    /**
     * 查询候选任务
     *
     * @param requestBody 请求
     * @return R<TaskBusinessKeys> 结果
     */
    @PostMapping("/workflow/findTaskCandidateUserList")
    public R<TaskBusinessKeys> findTaskCandidateUserList(@RequestBody GroupTaskRequestBody requestBody);

    /**
     * 拾取组任务
     *
     * @param requestBody 请求
     * @return String
     */
    @PostMapping("/workflow/claimTaskFormGroup")
    public R<String> claimTaskFormGroup(@RequestBody GroupTaskRequestBody requestBody);

    /**
     * 拾取候选任务
     *
     * @param requestBody 请求
     * @return String
     */
    @PostMapping("/workflow/claimTaskFromCandidateUser")
    public R<String> claimTaskFromCandidateUser(@RequestBody GroupTaskRequestBody requestBody);

    /**
     * 归还组任务
     *
     * @param requestBody 请求
     * @return String
     */
    @PostMapping("/workflow/setAssigneeToGroupTask")
    public R<String> setAssigneeToGroupTask(@RequestBody GroupTaskRequestBody requestBody);

    /**
     * 归还候选任务
     *
     * @param requestBody 请求
     * @return String
     */
    @PostMapping("/workflow/setAssigneeToTaskCandidateUser")
    public R<String> setAssigneeToTaskCandidateUser(@RequestBody GroupTaskRequestBody requestBody);

    /**
     * 转交任务（不推荐）
     *
     * @param requestBody 请求
     * @return String
     */
    @PostMapping("/workflow/setAssigneeToCandidateUser")
    public R<String> setAssigneeToCandidateUser(@RequestBody GroupTaskRequestBody requestBody);

    /**
     * 查询可审批列表
     *
     * @param requestBody 请求他
     * @return TaskBusinessKeys  返回
     */
    @PostMapping("/workflow/getTaskBusinessKeys")
    public R<TaskBusinessKeys> getTaskBusinessKeys(@RequestBody TaskCompleteRequestBody requestBody);

    /**
     * 审批任务
     *
     * @param requestBody 请求体
     * @return TaskCompleteResponseBody 返回值
     */
    @PostMapping("/workflow/taskComplete")
    public R<TaskCompleteResponseBody> taskComplete(@RequestBody TaskCompleteRequestBody requestBody);

    /**
     * 拾取组任务和审批任务
     *
     * @param requestBody 请求体
     * @return TaskCompleteResponseBody 返回值
     */
    @PostMapping("/workflow/taskClaimAndComplete")
    public R<TaskCompleteResponseBody> taskClaimAndComplete(@RequestBody TaskCompleteRequestBody requestBody);

    /**
     * 拾取候选任务和审批任务
     *
     * @param requestBody 请求体
     * @return TaskCompleteResponseBody 返回值
     */
    @PostMapping("/workflow/taskCandidateUserClaimAndComplete")
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
    @GetMapping("/workflow/getBusinessKeysAndProcessInfos")
    public R<List<BusinessKeysAndProcessInfos>> getBusinessKeysAndProcessInfos(@RequestParam("assignee") String assignee,
                                                                               @RequestParam("tenantId") String tenantId,
                                                                               @RequestParam("processDefinitionId") String processDefinitionId,
                                                                               @RequestParam("processDefinitionKey") String processDefinitionKey,
                                                                               @RequestParam("processInstanceId") String processInstanceId,
                                                                               @RequestParam("businessKey") String businessKey);

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
    @GetMapping("/workflow/getBusinessKeysAndProcessInfosWithVar")
    public R<List<BusinessKeysAndProcessInfos>> getBusinessKeysAndProcessInfosWithVar(@RequestParam("assignee") String assignee,
                                                                                      @RequestParam("tenantId") String tenantId,
                                                                                      @RequestParam("processDefinitionId") String processDefinitionId,
                                                                                      @RequestParam("processDefinitionKey") String processDefinitionKey,
                                                                                      @RequestParam("processInstanceId") String processInstanceId,
                                                                                      @RequestParam("businessKey") String businessKey);

    /**
     * 获取流程进度图片
     *
     * @param processInstanceId   流程实例ID
     * @param httpServletResponse 结果
     */
    @GetMapping(value = "/workflow/getProcessImage")
    public void getProcessImage(@RequestParam("processInstanceId") String processInstanceId,
                                HttpServletResponse httpServletResponse);

    /**
     * @param tenantId    租户ID
     * @param businessKey 业务主键
     * @return R<List < BusinessKeyInfo>> 结果
     */
    @GetMapping(value = "/workflow/getBusinessKeyInfos")
    public R<List<BusinessKeyInfo>> getBusinessKeyInfos(@RequestParam("tenantId") String tenantId,
                                                        @RequestParam("processDefinitionId") String processDefinitionId,
                                                        @RequestParam("processDefinitionKey") String processDefinitionKey,
                                                        @RequestParam("businessKey") String businessKey);

    /**
     * 暂停流程实例
     *
     * @param tenantId             机构ID
     * @param processDefinitionId  流程定义ID
     * @param processDefinitionKey 流程定义KEY
     * @param businessKey          业务主键
     * @return R 返回
     */
    @GetMapping("/workflow/suspendProcessInstance")
    public R<String> suspendProcessInstance(
            @RequestParam("tenantId") String tenantId,
            @RequestParam("processDefinitionId") String processDefinitionId,
            @RequestParam("processDefinitionKey") String processDefinitionKey,
            @RequestParam("businessKey") String businessKey);

    /**
     * 暂停流程定义
     *
     * @param tenantId             机构ID
     * @param processDefinitionId  流程定义ID
     * @param processDefinitionKey 流程定义KEY
     * @param cascade              是否连同实例也暂停
     * @return R 返回
     */
    @GetMapping("/workflow/suspendProcessDefinition")
    public R<String> suspendProcessDefinition(
            @RequestParam("tenantId") String tenantId,
            @RequestParam("processDefinitionId") String processDefinitionId,
            @RequestParam("processDefinitionKey") String processDefinitionKey,
            @RequestParam("cascade") Boolean cascade);

    /**
     * 重新开启流程实例
     *
     * @param tenantId             机构ID
     * @param processDefinitionId  流程定义ID
     * @param processDefinitionKey 流程定义KEY
     * @param businessKey          业务主键
     * @return R 返回
     */
    @GetMapping("/workflow/notSuspendProcessInstance")
    public R<String> notSuspendProcessInstance(
            @RequestParam("tenantId") String tenantId,
            @RequestParam("processDefinitionId") String processDefinitionId,
            @RequestParam("processDefinitionKey") String processDefinitionKey,
            @RequestParam("businessKey") String businessKey);

    /**
     * 重新开启流程定义
     *
     * @param tenantId             机构ID
     * @param processDefinitionId  流程定义ID
     * @param processDefinitionKey 流程定义KEY
     * @param cascade              是否连同实例也暂停
     * @return R 返回
     */
    @GetMapping("/workflow/notSuspendProcessDefinition")
    public R<String> notSuspendProcessDefinition(
            @RequestParam("tenantId") String tenantId,
            @RequestParam("processDefinitionId") String processDefinitionId,
            @RequestParam("processDefinitionKey") String processDefinitionKey,
            @RequestParam("cascade") Boolean cascade);
}
