package com.ruoyi.school.service.impl;

import com.ruoyi.school.domain.School;
import com.ruoyi.school.mapper.SchoolApMapper;
import com.ruoyi.school.service.IApSchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分校成立Service业务层处理
 *
 * @author 刘彪
 * @date 2023-08-03
 */
@Service
public class SchoolApServiceImpl implements IApSchoolService {
    @Autowired
    private SchoolApMapper schoolApMapper;


    /**
     * 查询分校成立审批列表
     *
     * @return 分校成立
     */
    @Override
    public List<School> selectSchoolList(List<String> ids) {
        return schoolApMapper.selectSchoolList(ids);
    }
}
