package com.ruoyi.sorkflow.api.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liubiao
 */
@Data
public class ProcessDefinitionPojo implements Serializable {
    private static final long serialVersionUID = 1L;
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
