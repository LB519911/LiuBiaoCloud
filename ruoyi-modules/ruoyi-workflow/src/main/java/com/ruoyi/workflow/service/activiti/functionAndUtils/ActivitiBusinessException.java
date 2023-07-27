package com.ruoyi.workflow.service.activiti.functionAndUtils;

/**
 * @author liubiao
 */
public class ActivitiBusinessException extends ActivitiBaseException {

    public ActivitiBusinessException() {
    }

    public ActivitiBusinessException(ActivitiCodeEnum codeEnum) {
        super(codeEnum.getCode(), codeEnum.description());
    }

    public ActivitiBusinessException(ActivitiCodeEnum codeEnum, String msg) {
        super(codeEnum.getCode(), codeEnum.description() + "," + msg);
    }

    public ActivitiBusinessException(int codeInt, String errorMsg) {
        super(codeInt, errorMsg);
    }
}
