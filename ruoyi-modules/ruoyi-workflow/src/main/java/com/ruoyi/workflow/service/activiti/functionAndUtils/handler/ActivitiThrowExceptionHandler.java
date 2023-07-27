package com.ruoyi.workflow.service.activiti.functionAndUtils.handler;

/**
 * @author 刘彪
 * @date 2021/12/16-14:56
 */
@FunctionalInterface
public interface ActivitiThrowExceptionHandler {

    /**
     * 抛出异常信息
     *
     * @param message 异常信息
     */
    void throwCustomException(String message);
}
