package com.ruoyi.school.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.school.mapper.SchoolMapper;
import com.ruoyi.school.domain.School;
import com.ruoyi.school.service.ISchoolService;

/**
 * 分校管理Service业务层处理
 * 
 * @author 刘彪
 * @date 2023-08-03
 */
@Service
public class SchoolServiceImpl implements ISchoolService 
{
    @Autowired
    private SchoolMapper schoolMapper;

    /**
     * 查询分校管理
     * 
     * @param id 分校管理主键
     * @return 分校管理
     */
    @Override
    public School selectSchoolById(String id)
    {
        return schoolMapper.selectSchoolById(id);
    }

    /**
     * 查询分校管理列表
     * 
     * @param school 分校管理
     * @return 分校管理
     */
    @Override
    public List<School> selectSchoolList(School school)
    {
        return schoolMapper.selectSchoolList(school);
    }

    /**
     * 新增分校管理
     * 
     * @param school 分校管理
     * @return 结果
     */
    @Override
    public int insertSchool(School school)
    {
        return schoolMapper.insertSchool(school);
    }

    /**
     * 修改分校管理
     * 
     * @param school 分校管理
     * @return 结果
     */
    @Override
    public int updateSchool(School school)
    {
        return schoolMapper.updateSchool(school);
    }

    /**
     * 批量删除分校管理
     * 
     * @param ids 需要删除的分校管理主键
     * @return 结果
     */
    @Override
    public int deleteSchoolByIds(String[] ids)
    {
        return schoolMapper.deleteSchoolByIds(ids);
    }

    /**
     * 删除分校管理信息
     * 
     * @param id 分校管理主键
     * @return 结果
     */
    @Override
    public int deleteSchoolById(String id)
    {
        return schoolMapper.deleteSchoolById(id);
    }
}
