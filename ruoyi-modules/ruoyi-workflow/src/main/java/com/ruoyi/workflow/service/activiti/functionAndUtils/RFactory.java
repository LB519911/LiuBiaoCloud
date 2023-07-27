package com.ruoyi.workflow.service.activiti.functionAndUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * @author liubiao
 */
public final class RFactory {

    /**
     * 根据Code创建有返回消息的result
     *
     * @param codeEnum 状态Code体
     * @return ReturnMsg
     */
    public static ActivitiR<JSONObject> newResult(ActivitiCodeEnum codeEnum) {
        return new ActivitiR<>(codeEnum.getCode(), new JSONObject(), codeEnum.description());
    }

    /**
     * 根据Code创建有返回消息的result
     *
     * @param codeEnum 状态Code体
     * @return ReturnMsg
     */
    public static ActivitiR<JSONObject> newResult(ActivitiCodeEnum codeEnum, String msg) {
        return new ActivitiR<>(codeEnum.getCode(), new JSONObject(), codeEnum.description() + msg);
    }

    /**
     * 创建无返回消息的成功的result
     *
     * @return ReturnMsg
     */
    public static ActivitiR<JSONObject> newSuccess() {
        return new ActivitiR<>(ActivitiCodeEnum.SUCCESS.getCode(), new JSONObject(), "成功");
    }

    /**
     * 创建成功有返回的result
     *
     * @param result 返回信息
     * @return ReturnMsg
     */
    public static ActivitiR<?> newSuccess(Object result) {
        return new ActivitiR<>(ActivitiCodeEnum.SUCCESS.getCode(), result, "成功");
    }

    /**
     * 创建无返回消息的错误的result
     *
     * @param msg 错误消息
     * @return {@link ActivitiR}
     */
    public static ActivitiR<JSONObject> newError(String msg) {
        return new ActivitiR<>(ActivitiCodeEnum.ERROR.getCode(), new JSONObject(), ActivitiCodeEnum.ERROR.description() + "," + msg);
    }

    /**
     * 创建错误有返回的result
     *
     * @param result 返回信息
     * @return {@link ActivitiR}
     */
    public static ActivitiR<?> newError(Object result) {
        return new ActivitiR<>(ActivitiCodeEnum.ERROR.getCode(), result, ActivitiCodeEnum.ERROR.description());
    }

    /**
     * 创建错误有返回的result
     *
     * @param result 返回信息
     * @param msg    错误消息
     * @return {@link ActivitiR}
     */
    public static ActivitiR<?> newError(Object result, String msg) {
        return new ActivitiR<>(ActivitiCodeEnum.ERROR.getCode(), result, ActivitiCodeEnum.ERROR.description() + "," + msg);
    }

}
