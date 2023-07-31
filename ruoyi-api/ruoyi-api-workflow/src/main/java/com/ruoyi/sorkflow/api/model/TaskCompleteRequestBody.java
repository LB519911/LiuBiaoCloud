package com.ruoyi.sorkflow.api.model;

import com.google.common.collect.Maps;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author liubiao
 */
@Data
public class TaskCompleteRequestBody implements Serializable {
    private static final long serialVersionUID = 1L;
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
