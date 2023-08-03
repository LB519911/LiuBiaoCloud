package com.ruoyi.school.mapper;

import java.util.List;
import com.ruoyi.school.domain.School;

/**
 * 分校成立Mapper接口
 * 
 * @author 刘彪
 * @date 2023-08-03
 */
public interface SchoolMapper 
{
    /**
     * 查询分校成立
     * 
     * @param id 分校成立主键
     * @return 分校成立
     */
    public School selectSchoolById(String id);

    /**
     * 查询分校成立列表
     * 
     * @param school 分校成立
     * @return 分校成立集合
     */
    public List<School> selectSchoolList(School school);

    /**
     * 新增分校成立
     * 
     * @param school 分校成立
     * @return 结果
     */
    public int insertSchool(School school);

    /**
     * 修改分校成立
     * 
     * @param school 分校成立
     * @return 结果
     */
    public int updateSchool(School school);

    /**
     * 删除分校成立
     * 
     * @param id 分校成立主键
     * @return 结果
     */
    public int deleteSchoolById(String id);

    /**
     * 批量删除分校成立
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSchoolByIds(String[] ids);
}
