package com.ruoyi.workflow.service.activiti.functionAndUtils;

import lombok.Data;
import org.activiti.engine.*;

/**
 * 用于获取流程引擎
 *
 * @author liubiao
 */
@Data
public class ActivitiUtil {

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    RepositoryService repositoryService = processEngine.getRepositoryService();
    TaskService taskService = processEngine.getTaskService();
    RuntimeService runtimeService = processEngine.getRuntimeService();
    HistoryService historyService = processEngine.getHistoryService();

    private static ActivitiUtil activitiUtil;


    public static ActivitiUtil instance() {
        if (null == activitiUtil) {
            activitiUtil = new ActivitiUtil();
        }
        return activitiUtil;
    }

}
