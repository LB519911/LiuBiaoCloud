package com.ruoyi.sorkflow.api;

import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.sorkflow.api.factory.RemoteWorkFlowFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 工作流服务
 *
 * @author liubiao
 */
@FeignClient(contextId = "remoteWorkFlowService", value = ServiceNameConstants.WORKFLOW_SERVICE, fallbackFactory = RemoteWorkFlowFallbackFactory.class)
public interface RemoteWorkFlowService {

    /**
     * 部署
     *
     * @param definitionName 流程定义名称
     * @param tenantId       机构ID
     * @param bpmn           文件
     * @return R 返回
     * @throws IOException 异常
     */
    @PostMapping("/deploy")
    public R<String> deploy(@RequestParam String definitionName, @RequestParam String tenantId, MultipartFile bpmn);
}
