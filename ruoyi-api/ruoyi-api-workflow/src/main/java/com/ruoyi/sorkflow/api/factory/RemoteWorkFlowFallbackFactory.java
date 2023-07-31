package com.ruoyi.sorkflow.api.factory;

import com.ruoyi.sorkflow.api.RemoteWorkFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 工作流服务降级处理
 *
 * @author liubiao
 */
@Component
public class RemoteWorkFlowFallbackFactory implements FallbackFactory<RemoteWorkFlowService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteWorkFlowFallbackFactory.class);

    @Override
    public RemoteWorkFlowService create(Throwable throwable) {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteWorkFlowService() {
        };
    }
}
