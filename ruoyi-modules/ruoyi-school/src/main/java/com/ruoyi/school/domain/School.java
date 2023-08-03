package com.ruoyi.school.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 分校成立对象 school
 * 
 * @author 刘彪
 * @date 2023-08-03
 */
public class School extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private String id;

    /**  */
    @Excel(name = "")
    private String name;

    /**  */
    private String alias;

    /**  */
    private String province;

    /**  */
    private String provinceCode;

    /**  */
    private String city;

    /**  */
    private String cityCode;

    /**  */
    private String area;

    /**  */
    private String areaCode;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 建校时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "建校时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date date;

    /** 类型：1省会级，2市级，3县级 */
    private Long type;

    /** 运营方式：1第三种方式 2省级加盟总校 3省级直营市级 4市级加盟总校 5市级直营县级 6县级加盟总校 7总校直营 8县级加盟市级 9试运营 16外机构 */
    @Excel(name = "运营方式：1第三种方式 2省级加盟总校 3省级直营市级 4市级加盟总校 5市级直营县级 6县级加盟总校 7总校直营 8县级加盟市级 9试运营 16外机构")
    private Long yyMode;

    /** 运营类型 1直营 2加盟 3第三种 4外机构 */
    @Excel(name = "运营类型 1直营 2加盟 3第三种 4外机构")
    private Long yyType;

    /** 简介 */
    @Excel(name = "简介")
    private String brief;

    /** 运营状态：1正常，2删除，3未完善，4停业 */
    @Excel(name = "运营状态：1正常，2删除，3未完善，4停业")
    private Long status;

    /** 停课状态（暂定） */
    @Excel(name = "停课状态", readConverterExp = "暂=定")
    private Long state;

    /** 类型，school校区，depart部门 */
    @Excel(name = "类型，school校区，depart部门")
    private String category;

    /**  */
    private Long addressId;

    /** 当前负责人ID */
    private Long adminId;

    /** 上级ID */
    private Long pid;

    /** 负责的省级校区ID */
    private Long directlyId;

    /** 排课校区ID */
    private Long classId;

    /** CRM系统ID */
    private Long crmId;

    /** 原始ID */
    private Long originId;

    /**  */
    private Date createdAt;

    /**  */
    private Date updatedAt;

    /**  */
    private Date deletedAt;

    /** 门头照片 */
    private String doorHeadPhoto;

    /** 营业执照 */
    private String businessLicense;

    /** 流程实例ID（审批进度、历史都要用到这个字段） */
    private String workflowId;

    /** 当前审批到哪里（BPMN中的TaskName） */
    private Long workflowTaskNode;

    /** 审批状态（0:待审批、1审批中、2审批不通过、3审批通过、4审批中途不通过) */
    private Long workflowStatus;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setAlias(String alias) 
    {
        this.alias = alias;
    }

    public String getAlias() 
    {
        return alias;
    }
    public void setProvince(String province) 
    {
        this.province = province;
    }

    public String getProvince() 
    {
        return province;
    }
    public void setProvinceCode(String provinceCode) 
    {
        this.provinceCode = provinceCode;
    }

    public String getProvinceCode() 
    {
        return provinceCode;
    }
    public void setCity(String city) 
    {
        this.city = city;
    }

    public String getCity() 
    {
        return city;
    }
    public void setCityCode(String cityCode) 
    {
        this.cityCode = cityCode;
    }

    public String getCityCode() 
    {
        return cityCode;
    }
    public void setArea(String area) 
    {
        this.area = area;
    }

    public String getArea() 
    {
        return area;
    }
    public void setAreaCode(String areaCode) 
    {
        this.areaCode = areaCode;
    }

    public String getAreaCode() 
    {
        return areaCode;
    }
    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }
    public void setDate(Date date) 
    {
        this.date = date;
    }

    public Date getDate() 
    {
        return date;
    }
    public void setType(Long type) 
    {
        this.type = type;
    }

    public Long getType() 
    {
        return type;
    }
    public void setYyMode(Long yyMode) 
    {
        this.yyMode = yyMode;
    }

    public Long getYyMode() 
    {
        return yyMode;
    }
    public void setYyType(Long yyType) 
    {
        this.yyType = yyType;
    }

    public Long getYyType() 
    {
        return yyType;
    }
    public void setBrief(String brief) 
    {
        this.brief = brief;
    }

    public String getBrief() 
    {
        return brief;
    }
    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }
    public void setState(Long state) 
    {
        this.state = state;
    }

    public Long getState() 
    {
        return state;
    }
    public void setCategory(String category) 
    {
        this.category = category;
    }

    public String getCategory() 
    {
        return category;
    }
    public void setAddressId(Long addressId) 
    {
        this.addressId = addressId;
    }

    public Long getAddressId() 
    {
        return addressId;
    }
    public void setAdminId(Long adminId) 
    {
        this.adminId = adminId;
    }

    public Long getAdminId() 
    {
        return adminId;
    }
    public void setPid(Long pid) 
    {
        this.pid = pid;
    }

    public Long getPid() 
    {
        return pid;
    }
    public void setDirectlyId(Long directlyId) 
    {
        this.directlyId = directlyId;
    }

    public Long getDirectlyId() 
    {
        return directlyId;
    }
    public void setClassId(Long classId) 
    {
        this.classId = classId;
    }

    public Long getClassId() 
    {
        return classId;
    }
    public void setCrmId(Long crmId) 
    {
        this.crmId = crmId;
    }

    public Long getCrmId() 
    {
        return crmId;
    }
    public void setOriginId(Long originId) 
    {
        this.originId = originId;
    }

    public Long getOriginId() 
    {
        return originId;
    }
    public void setCreatedAt(Date createdAt) 
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() 
    {
        return createdAt;
    }
    public void setUpdatedAt(Date updatedAt) 
    {
        this.updatedAt = updatedAt;
    }

    public Date getUpdatedAt() 
    {
        return updatedAt;
    }
    public void setDeletedAt(Date deletedAt) 
    {
        this.deletedAt = deletedAt;
    }

    public Date getDeletedAt() 
    {
        return deletedAt;
    }
    public void setDoorHeadPhoto(String doorHeadPhoto) 
    {
        this.doorHeadPhoto = doorHeadPhoto;
    }

    public String getDoorHeadPhoto() 
    {
        return doorHeadPhoto;
    }
    public void setBusinessLicense(String businessLicense) 
    {
        this.businessLicense = businessLicense;
    }

    public String getBusinessLicense() 
    {
        return businessLicense;
    }
    public void setWorkflowId(String workflowId) 
    {
        this.workflowId = workflowId;
    }

    public String getWorkflowId() 
    {
        return workflowId;
    }
    public void setWorkflowTaskNode(Long workflowTaskNode) 
    {
        this.workflowTaskNode = workflowTaskNode;
    }

    public Long getWorkflowTaskNode() 
    {
        return workflowTaskNode;
    }
    public void setWorkflowStatus(Long workflowStatus) 
    {
        this.workflowStatus = workflowStatus;
    }

    public Long getWorkflowStatus() 
    {
        return workflowStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("alias", getAlias())
            .append("province", getProvince())
            .append("provinceCode", getProvinceCode())
            .append("city", getCity())
            .append("cityCode", getCityCode())
            .append("area", getArea())
            .append("areaCode", getAreaCode())
            .append("address", getAddress())
            .append("date", getDate())
            .append("type", getType())
            .append("yyMode", getYyMode())
            .append("yyType", getYyType())
            .append("brief", getBrief())
            .append("status", getStatus())
            .append("state", getState())
            .append("category", getCategory())
            .append("addressId", getAddressId())
            .append("adminId", getAdminId())
            .append("pid", getPid())
            .append("directlyId", getDirectlyId())
            .append("classId", getClassId())
            .append("crmId", getCrmId())
            .append("originId", getOriginId())
            .append("createdAt", getCreatedAt())
            .append("updatedAt", getUpdatedAt())
            .append("deletedAt", getDeletedAt())
            .append("doorHeadPhoto", getDoorHeadPhoto())
            .append("businessLicense", getBusinessLicense())
            .append("workflowId", getWorkflowId())
            .append("workflowTaskNode", getWorkflowTaskNode())
            .append("workflowStatus", getWorkflowStatus())
            .toString();
    }
}
