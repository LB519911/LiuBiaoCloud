package com.ruoyi.sorkflow.api;

import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.sorkflow.api.factory.RemoteWorkFlowFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 工作流服务
 *
 * @author liubiao
 */
@FeignClient(contextId = "remoteWorkFlowService", value = ServiceNameConstants.WORKFLOW_SERVICE, fallbackFactory = RemoteWorkFlowFallbackFactory.class)
public interface RemoteWorkFlowService {
}
