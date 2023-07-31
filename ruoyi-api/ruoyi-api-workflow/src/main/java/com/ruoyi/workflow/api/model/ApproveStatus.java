package com.ruoyi.workflow.api.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liubiao
 */
@Data
public class ApproveStatus implements Serializable {
    private static final long serialVersionUID = 1L;
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
