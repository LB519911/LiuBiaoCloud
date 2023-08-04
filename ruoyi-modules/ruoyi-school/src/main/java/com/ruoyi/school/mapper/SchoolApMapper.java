package com.ruoyi.school.mapper;

import com.ruoyi.school.domain.School;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分校成立Mapper接口
 *
 * @author 刘彪
 * @date 2023-08-03
 */
public interface SchoolApMapper {

    /**
     * 查询分校成立审批列表
     */
    public List<School> selectSchoolList(@Param("ids") List<String> ids);
}
