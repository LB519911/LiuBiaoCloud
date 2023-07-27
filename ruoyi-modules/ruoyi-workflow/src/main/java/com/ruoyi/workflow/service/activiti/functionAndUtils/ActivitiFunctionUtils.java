package com.ruoyi.workflow.service.activiti.functionAndUtils;

import com.ruoyi.workflow.service.activiti.functionAndUtils.handler.ActivitiThrowExceptionHandler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author liubiao
 * @date 2021/12/16-14:38
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActivitiFunctionUtils {

    /**
     * 如果isError是true，则抛出错误
     *
     * @param isError 是否错误
     * @return ThrowExceptionHandler
     */
    public static ActivitiThrowExceptionHandler isWrong(boolean isError) {
        return message -> {
            if (isError) {
                // 打出错误信息日志，并报错
                log.error(message);
                throw new ActivitiBusinessException(ActivitiCodeEnum.ERROR, message);
            }
        };
    }

}
