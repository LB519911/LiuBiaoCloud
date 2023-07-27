package com.ruoyi.workflow.config.activiti;

import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liubiao
 */
@Component
public class ActivitiConfig implements ProcessEngineConfigurationConfigurer {

    @Autowired
    private ComActivitiEventListener comActivitiEventListener;

    @Override
    public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {
        List<ActivitiEventListener> activitiEventListener = new ArrayList<>();
        activitiEventListener.add(comActivitiEventListener);
        processEngineConfiguration.setEventListeners(activitiEventListener);
    }
}
