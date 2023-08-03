package com.ruoyi.school.service;

import java.util.List;
import com.ruoyi.school.domain.School;

/**
 * 分校管理Service接口
 * 
 * @author 刘彪
 * @date 2023-08-03
 */
public interface ISchoolService 
{
    /**
     * 查询分校管理
     * 
     * @param id 分校管理主键
     * @return 分校管理
     */
    public School selectSchoolById(String id);

    /**
     * 查询分校管理列表
     * 
     * @param school 分校管理
     * @return 分校管理集合
     */
    public List<School> selectSchoolList(School school);

    /**
     * 新增分校管理
     * 
     * @param school 分校管理
     * @return 结果
     */
    public int insertSchool(School school);

    /**
     * 修改分校管理
     * 
     * @param school 分校管理
     * @return 结果
     */
    public int updateSchool(School school);

    /**
     * 批量删除分校管理
     * 
     * @param ids 需要删除的分校管理主键集合
     * @return 结果
     */
    public int deleteSchoolByIds(String[] ids);

    /**
     * 删除分校管理信息
     * 
     * @param id 分校管理主键
     * @return 结果
     */
    public int deleteSchoolById(String id);
}
