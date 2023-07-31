package com.ruoyi.sorkflow.api.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liubiao
 */
@Data
public class BusinessKeyInfo implements Serializable {
    private static final long serialVersionUID = 1L;

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