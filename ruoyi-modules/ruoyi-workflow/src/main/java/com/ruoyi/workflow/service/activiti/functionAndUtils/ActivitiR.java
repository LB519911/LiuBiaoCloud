package com.ruoyi.workflow.service.activiti.functionAndUtils;

import lombok.Data;

import java.io.Serializable;

/**
 * @param <T>
 * @author liubiao
 */
@Data
public class ActivitiR<T> implements Serializable {

    private int code;
    private T result;
    private String message;

    public ActivitiR(int code, T result, String message) {
        this.code = code;
        this.result = result;
        this.message = message;
    }

}
