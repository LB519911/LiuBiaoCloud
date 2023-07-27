package com.ruoyi.workflow.service.activiti.impl;

import com.ruoyi.workflow.service.activiti.ProcessImageService;
import com.ruoyi.workflow.service.activiti.manager.ProcessImageManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

/**
 * 流程图service实现类
 *
 * @author liubiao
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ProcessImageServiceImpl implements ProcessImageService {

    private final ProcessImageManager processImageManager;

    /**
     * 根据流程实例Id获取流程图
     *
     * @param procInstId 流程实例id
     * @return inputStream
     * @throws Exception exception
     */
    @Override
    public InputStream getFlowImgByProcInstId(String procInstId) throws Exception {
        return processImageManager.getFlowImgByProcInstId(procInstId);
    }
}
