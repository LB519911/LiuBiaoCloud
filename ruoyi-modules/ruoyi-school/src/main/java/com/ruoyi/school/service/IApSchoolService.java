package com.ruoyi.school.service;

import com.ruoyi.school.domain.School;

import java.util.List;

/**
 * 分校成立Service接口
 *
 * @author 刘彪
 * @date 2023-08-03
 */
public interface IApSchoolService {
    /**
     * 查询分校成立审批列表
     *
     * @return 分校成立集合
     */
    public List<School> selectSchoolList(List<String> ids);
}
