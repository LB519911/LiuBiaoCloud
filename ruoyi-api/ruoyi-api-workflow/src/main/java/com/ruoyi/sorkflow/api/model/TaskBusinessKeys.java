package com.ruoyi.sorkflow.api.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author liubiao
 */
@Data
public class TaskBusinessKeys implements Serializable {
    private static final long serialVersionUID = 1L;
    List<String> businessKeys;
    Long currentPage;
    Long pageSize;
    Long pageCount;
    boolean hasNextPage;
    boolean hasPreviousPage;
}
