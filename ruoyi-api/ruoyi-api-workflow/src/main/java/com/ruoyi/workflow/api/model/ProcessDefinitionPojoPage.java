package com.ruoyi.workflow.api.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author liubiao
 */
@Data
public class ProcessDefinitionPojoPage implements Serializable {
    private static final long serialVersionUID = 1L;
    List<ProcessDefinitionPojo> processDefinitionPojoList;
    Long currentPage;
    Long pageSize;
    Long pageCount;
    boolean hasNextPage;
    boolean hasPreviousPage;
}
