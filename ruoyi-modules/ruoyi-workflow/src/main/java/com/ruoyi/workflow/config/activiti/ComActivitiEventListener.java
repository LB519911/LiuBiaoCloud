package com.ruoyi.workflow.config.activiti;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.springframework.stereotype.Component;

import java.io.Serializable;


/**
 * @author liubiao
 */

@Component
public class ComActivitiEventListener implements ActivitiEventListener, Serializable {

    @Override
    public void onEvent(ActivitiEvent event) {
        ActivitiEventType type = event.getType();
        System.out.println();
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
