package com.ruoyi.sorkflow.api.factory;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.sorkflow.api.RemoteWorkFlowService;
import com.ruoyi.sorkflow.api.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 工作流服务降级处理
 *
 * @author liubiao
 */
@Component
public class RemoteWorkFlowFallbackFactory implements FallbackFactory<RemoteWorkFlowService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteWorkFlowFallbackFactory.class);

    @Override
    public RemoteWorkFlowService create(Throwable throwable) {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteWorkFlowService() {

            @Override
            public R<String> deploy(String definitionName, String tenantId, MultipartFile bpmn) {
                return R.fail("失败:" + throwable.getMessage());
            }

            @Override
            public R<ProcessDefinitionPojoPage> getDeployListByName(String definitionName, Integer currentPage, Integer maxResults, String tenantId, String processDefinitionKey) {
                return R.fail("失败:" + throwable.getMessage());
            }

            @Override
            public R<ProcessDefinitionPojo> getDeployLatestListByName(String definitionName, String tenantId, String processDefinitionKey) {
                return R.fail("失败:" + throwable.getMessage());
            }

            @Override
            public R<String> getDeployImage(String definitionName, String tenantId, String processDefinitionKey, String processDefinitionId, HttpServletResponse httpServletResponse) {
                return R.fail("失败:" + throwable.getMessage());
            }

            @Override
            public R<String> deleteProcessDefinitionAllInfo(String processDefinitionId, String tenantId) {
                return R.fail("失败:" + throwable.getMessage());
            }

            @Override
            public R<String> deleteProcessDefinitionByName(String processDefinitionName, String tenantId) {
                return R.fail("失败:" + throwable.getMessage());
            }

            @Override
            public R<String> deleteProcessDefinitionByKey(String processDefinitionKey, String tenantId) {
                return R.fail("失败:" + throwable.getMessage());
            }

            @Override
            public R<String> startProcessInstanceByKey(StartProcessInstanceByIdRequestBody requestBody) {
                return R.fail("失败:" + throwable.getMessage());
            }

            @Override
            public R<TaskBusinessKeys> findGroupTaskList(GroupTaskRequestBody requestBody) {
                return R.fail("失败:" + throwable.getMessage());
            }

            @Override
            public R<TaskBusinessKeys> findTaskCandidateUserList(GroupTaskRequestBody requestBody) {
                return R.fail("失败:" + throwable.getMessage());
            }

            @Override
            public R<String> claimTaskFormGroup(GroupTaskRequestBody requestBody) {
                return R.fail("失败:" + throwable.getMessage());
            }

            @Override
            public R<String> claimTaskFromCandidateUser(GroupTaskRequestBody requestBody) {
                return R.fail("失败:" + throwable.getMessage());
            }

            @Override
            public R<String> setAssigneeToGroupTask(GroupTaskRequestBody requestBody) {
                return R.fail("失败:" + throwable.getMessage());
            }

            @Override
            public R<String> setAssigneeToTaskCandidateUser(GroupTaskRequestBody requestBody) {
                return R.fail("失败:" + throwable.getMessage());
            }

            @Override
            public R<String> setAssigneeToCandidateUser(GroupTaskRequestBody requestBody) {
                return R.fail("失败:" + throwable.getMessage());
            }

            @Override
            public R<TaskBusinessKeys> getTaskBusinessKeys(TaskCompleteRequestBody requestBody) {
                return R.fail("失败:" + throwable.getMessage());
            }

            @Override
            public R<TaskCompleteResponseBody> taskComplete(TaskCompleteRequestBody requestBody) {
                return R.fail("失败:" + throwable.getMessage());
            }

            @Override
            public R<TaskCompleteResponseBody> taskClaimAndComplete(TaskCompleteRequestBody requestBody) {
                return R.fail("失败:" + throwable.getMessage());
            }

            @Override
            public R<TaskCompleteResponseBody> taskCandidateUserClaimAndComplete(TaskCompleteRequestBody requestBody) {
                return R.fail("失败:" + throwable.getMessage());
            }

            @Override
            public R<List<BusinessKeysAndProcessInfos>> getBusinessKeysAndProcessInfos(String assignee, String tenantId, String processDefinitionId, String processDefinitionKey, String processInstanceId, String businessKey) {
                return R.fail("失败:" + throwable.getMessage());
            }

            @Override
            public R<List<BusinessKeysAndProcessInfos>> getBusinessKeysAndProcessInfosWithVar(String assignee, String tenantId, String processDefinitionId, String processDefinitionKey, String processInstanceId, String businessKey) {
                return R.fail("失败:" + throwable.getMessage());
            }

            @Override
            public void getProcessImage(String processInstanceId, HttpServletResponse httpServletResponse) {

            }

            @Override
            public R<List<BusinessKeyInfo>> getBusinessKeyInfos(String tenantId, String processDefinitionId, String processDefinitionKey, String businessKey) {
                return R.fail("失败:" + throwable.getMessage());
            }

            @Override
            public R<String> suspendProcessInstance(String tenantId, String processDefinitionId, String processDefinitionKey, String businessKey) {
                return R.fail("失败:" + throwable.getMessage());
            }

            @Override
            public R<String> suspendProcessDefinition(String tenantId, String processDefinitionId, String processDefinitionKey, Boolean cascade) {
                return R.fail("失败:" + throwable.getMessage());
            }

            @Override
            public R<String> notSuspendProcessInstance(String tenantId, String processDefinitionId, String processDefinitionKey, String businessKey) {
                return R.fail("失败:" + throwable.getMessage());
            }

            @Override
            public R<String> notSuspendProcessDefinition(String tenantId, String processDefinitionId, String processDefinitionKey, Boolean cascade) {
                return R.fail("失败:" + throwable.getMessage());
            }
        };
    }
}
