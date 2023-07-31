package com.ruoyi.workflow.api.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liubiao
 */
@Data
public class TaskCompleteResponseBody implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 是否结束
     */
    ApproveStatus approveStatus;
}
