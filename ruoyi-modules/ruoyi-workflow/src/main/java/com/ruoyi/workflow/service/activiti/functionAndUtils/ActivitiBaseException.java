package com.ruoyi.workflow.service.activiti.functionAndUtils;

/**
 * 基本异常
 *
 * @author liubiao
 */
public abstract class ActivitiBaseException extends RuntimeException {

    private ActivitiR<?> r;

    ActivitiBaseException() {
        super();
    }

    ActivitiBaseException(int codeInt, String msg) {
        super(msg);
        this.r = RFactory.newResult(ActivitiCodeEnum.getEnumByCode(codeInt), msg);
    }

    public int getCodeInt() {
        return r.getCode();
    }

    public Object getResult() {
        return r.getResult();
    }

    public String getMsg() {
        return r.getMessage();
    }

    public ActivitiR<?> getReturnMsg() {
        return r;
    }

}
