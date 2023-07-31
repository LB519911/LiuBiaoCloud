package com.ruoyi.workflow.api.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liubiao
 */
@Data
public class GroupTaskRequestBody implements Serializable {
    private static final long serialVersionUID = 1L;
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
