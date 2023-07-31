package com.ruoyi.sorkflow.api.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author liubiao
 */
@Data
public class BusinessKeysAndProcessInfos implements Serializable {
    private static final long serialVersionUID = 1L;
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
