package com.ruoyi.workflow.service.activiti.functionAndUtils;

/**
 * 枚举基类
 *
 * @author 刘彪
 */
public interface ActivitiBaseEnum {

    /**
     * name
     *
     * @return name
     */
    String name();

    /**
     * 描述
     *
     * @return 描述
     */
    String description();

    /**
     * 获取code值
     *
     * @return codeInt
     */
    default int getCode() {
        return -1;
    }
}
