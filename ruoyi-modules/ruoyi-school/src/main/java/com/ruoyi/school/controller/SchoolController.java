package com.ruoyi.school.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.school.domain.School;
import com.ruoyi.school.service.ISchoolService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 分校成立Controller
 * 
 * @author 刘彪
 * @date 2023-08-03
 */
@RestController
@RequestMapping("/school")
public class SchoolController extends BaseController
{
    @Autowired
    private ISchoolService schoolService;

    /**
     * 查询分校成立列表
     */
    @RequiresPermissions("school:school:list")
    @GetMapping("/list")
    public TableDataInfo list(School school)
    {
        startPage();
        List<School> list = schoolService.selectSchoolList(school);
        return getDataTable(list);
    }

    /**
     * 导出分校成立列表
     */
    @RequiresPermissions("school:school:export")
    @Log(title = "分校成立", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, School school)
    {
        List<School> list = schoolService.selectSchoolList(school);
        ExcelUtil<School> util = new ExcelUtil<School>(School.class);
        util.exportExcel(response, list, "分校成立数据");
    }

    /**
     * 获取分校成立详细信息
     */
    @RequiresPermissions("school:school:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(schoolService.selectSchoolById(id));
    }

    /**
     * 新增分校成立
     */
    @RequiresPermissions("school:school:add")
    @Log(title = "分校成立", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody School school)
    {
        return toAjax(schoolService.insertSchool(school));
    }

    /**
     * 修改分校成立
     */
    @RequiresPermissions("school:school:edit")
    @Log(title = "分校成立", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody School school)
    {
        return toAjax(schoolService.updateSchool(school));
    }

    /**
     * 删除分校成立
     */
    @RequiresPermissions("school:school:remove")
    @Log(title = "分校成立", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(schoolService.deleteSchoolByIds(ids));
    }
}
