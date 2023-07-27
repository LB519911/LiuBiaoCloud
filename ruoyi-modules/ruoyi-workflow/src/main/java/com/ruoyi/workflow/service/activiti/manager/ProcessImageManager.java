package com.ruoyi.workflow.service.activiti.manager;

import com.ruoyi.workflow.service.activiti.impl.imageConfig.CustomProcessDiagramGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.Execution;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liubiao
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessImageManager {

    private final RepositoryService repositoryService;
    private final ProcessHistoryManager processHistoryManager;
    private final ProcessInstanceManager processInstanceManager;


    public InputStream getFlowImgByProcInstId(String procInstId) throws Exception {
        InputStream imageStream = null;
        try {
            // 通过流程实例ID获取历史流程实例
            HistoricProcessInstance historicProcessInstance = processHistoryManager.getHistoricProcessInstance(procInstId);

            // 通过流程实例ID获取流程中已经执行的节点，按照执行先后顺序排序
            List<HistoricActivityInstance> historicActivityInstanceList = processHistoryManager.getHistoricActivityInstancesAsc(procInstId);

            // 将已经执行的节点放入高亮显示节点集合
            List<String> highLightedActivityIdList = historicActivityInstanceList.stream()
                    .map(HistoricActivityInstance::getActivityId)
                    .collect(Collectors.toList());

            // 通过流程实例ID获取流程中正在执行的节点
            List<Execution> runningActivityInstanceList = processInstanceManager.getRunningActivityInstance(procInstId);
            List<String> runningActivityIdList = new ArrayList<>();
            for (Execution execution : runningActivityInstanceList) {
                if (!StringUtils.isEmpty(execution.getActivityId())) {
                    runningActivityIdList.add(execution.getActivityId());
                }
            }

            // 定义流程画布生成器
            CustomProcessDiagramGenerator processDiagramGenerator = new CustomProcessDiagramGenerator();

            // 获取流程定义Model对象
            BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());

            // 获取已经流经的流程线，需要高亮显示流程已经发生流转的线id集合
            // List<String> highLightedFlowsIds = getHighLightedFlows(bpmnModel, historicActivityInstanceList);
            List<String> highLightedFlowsIds = getHighLightedFlowsByIncomingFlows(bpmnModel, historicActivityInstanceList);

            // 根据runningActivityIdList获取runningActivityFlowsIds
            List<String> runningActivityFlowsIds = getRunningActivityFlowsIds(bpmnModel, runningActivityIdList, historicActivityInstanceList);

            // 使用默认配置获得流程图表生成器，并生成追踪图片字符流
            imageStream = processDiagramGenerator.generateDiagramCustom(
                    bpmnModel,
                    highLightedActivityIdList, runningActivityIdList, highLightedFlowsIds, runningActivityFlowsIds,
                    "宋体", "微软雅黑", "黑体");
            return imageStream;
        } catch (Exception e) {
            log.error("通过流程实例ID【{}】获取流程图时出现异常！", e.getMessage());
            throw new Exception("通过流程实例ID" + procInstId + "获取流程图时出现异常！", e);
        } finally {
            if (imageStream != null) {
                imageStream.close();
            }
        }
    }

    /**
     * @param bpmnModel                    bpmnModel
     * @param historicActivityInstanceList historicActivityInstanceList
     * @return HighLightedFlows
     */
    public List<String> getHighLightedFlowsByIncomingFlows(BpmnModel bpmnModel,
                                                           List<HistoricActivityInstance> historicActivityInstanceList) {
        //historicActivityInstanceList 是 流程中已经执行的历史活动实例
        // 已经流经的顺序流，需要高亮显示
        List<String> highFlows = new ArrayList<>();

        // 全部活动节点(包括正在执行的和未执行的)
        List<FlowNode> allHistoricActivityNodeList = new ArrayList<>();

        /*
         * 循环的目的：
         *           获取所有的历史节点FlowNode并放入allHistoricActivityNodeList
         *           获取所有确定结束了的历史节点finishedActivityInstancesList
         */
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
            // 获取流程节点
            // bpmnModel.getMainProcess()获取一个Process对象
            FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstance.getActivityId(), true);
            allHistoricActivityNodeList.add(flowNode);
        }
        // 循环活动节点
        for (FlowNode flowNode : allHistoricActivityNodeList) {
            // 获取每个活动节点的输入线
            List<SequenceFlow> incomingFlows = flowNode.getIncomingFlows();

            // 循环输入线，如果输入线的源头处于全部活动节点中，则将其包含在内
            for (SequenceFlow sequenceFlow : incomingFlows) {
                if (allHistoricActivityNodeList.stream().map(BaseElement::getId).collect(Collectors.toList()).contains(sequenceFlow.getSourceFlowElement().getId())) {
                    highFlows.add(sequenceFlow.getId());
                }
            }
        }
        return highFlows;
    }


    private List<String> getRunningActivityFlowsIds(BpmnModel bpmnModel, List<String> runningActivityIdList, List<HistoricActivityInstance> historicActivityInstanceList) {
        List<String> runningActivityFlowsIds = new ArrayList<>();
        List<String> runningActivityIds = new ArrayList<>(runningActivityIdList);
        // 逆序寻找，因为historicActivityInstanceList有序
        if (CollectionUtils.isEmpty(runningActivityIds)) {
            return runningActivityFlowsIds;
        }
        for (int i = historicActivityInstanceList.size() - 1; i >= 0; i--) {
            HistoricActivityInstance historicActivityInstance = historicActivityInstanceList.get(i);
            FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstance.getActivityId(), true);
            // 如果当前节点是未完成的节点
            if (runningActivityIds.contains(flowNode.getId())) {
                continue;
            }
            // 当前节点的所有流出线
            List<SequenceFlow> outgoingFlowList = flowNode.getOutgoingFlows();
            // 遍历所有的流出线
            for (SequenceFlow outgoingFlow : outgoingFlowList) {
                // 获取当前节点流程线对应的下一级节点
                FlowNode targetFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(outgoingFlow.getTargetRef(), true);
                // 如果找到流出线的目标是runningActivityIdList中的，那么添加后将其移除，避免找到重复的都指向runningActivityIdList的流出线
                if (runningActivityIds.contains(targetFlowNode.getId())) {
                    runningActivityFlowsIds.add(outgoingFlow.getId());
                    runningActivityIds.remove(targetFlowNode.getId());
                }
            }

        }
        return runningActivityFlowsIds;
    }
}
