package com.ruoyi.workflow.api.model;

import com.google.common.collect.Maps;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author liubiao
 */
@Data
public class StartProcessInstanceByIdRequestBody implements Serializable {
    private static final long serialVersionUID = 1L;
    String businessKey;
    String processDefinitionId;
    String processDefinitionKey;
    String definitionName;
    String tenantId;
    HashMap<String, Object> globalVar = Maps.newHashMap();
}
