<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="" prop="alias">
        <el-input
          v-model="queryParams.alias"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="" prop="province">
        <el-input
          v-model="queryParams.province"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="" prop="provinceCode">
        <el-input
          v-model="queryParams.provinceCode"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="" prop="city">
        <el-input
          v-model="queryParams.city"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="" prop="cityCode">
        <el-input
          v-model="queryParams.cityCode"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="" prop="area">
        <el-input
          v-model="queryParams.area"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="" prop="areaCode">
        <el-input
          v-model="queryParams.areaCode"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="地址" prop="address">
        <el-input
          v-model="queryParams.address"
          placeholder="请输入地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="建校时间" prop="date">
        <el-date-picker clearable
          v-model="queryParams.date"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择建校时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="运营方式：1第三种方式 2省级加盟总校 3省级直营市级 4市级加盟总校 5市级直营县级 6县级加盟总校 7总校直营 8县级加盟市级 9试运营 16外机构" prop="yyMode">
        <el-input
          v-model="queryParams.yyMode"
          placeholder="请输入运营方式：1第三种方式 2省级加盟总校 3省级直营市级 4市级加盟总校 5市级直营县级 6县级加盟总校 7总校直营 8县级加盟市级 9试运营 16外机构"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="停课状态" prop="state">
        <el-input
          v-model="queryParams.state"
          placeholder="请输入停课状态"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类型，school校区，depart部门" prop="category">
        <el-input
          v-model="queryParams.category"
          placeholder="请输入类型，school校区，depart部门"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="" prop="addressId">
        <el-input
          v-model="queryParams.addressId"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="当前负责人ID" prop="adminId">
        <el-input
          v-model="queryParams.adminId"
          placeholder="请输入当前负责人ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上级ID" prop="pid">
        <el-input
          v-model="queryParams.pid"
          placeholder="请输入上级ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="负责的省级校区ID" prop="directlyId">
        <el-input
          v-model="queryParams.directlyId"
          placeholder="请输入负责的省级校区ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="排课校区ID" prop="classId">
        <el-input
          v-model="queryParams.classId"
          placeholder="请输入排课校区ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="CRM系统ID" prop="crmId">
        <el-input
          v-model="queryParams.crmId"
          placeholder="请输入CRM系统ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="原始ID" prop="originId">
        <el-input
          v-model="queryParams.originId"
          placeholder="请输入原始ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="" prop="createdAt">
        <el-date-picker clearable
          v-model="queryParams.createdAt"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="" prop="updatedAt">
        <el-date-picker clearable
          v-model="queryParams.updatedAt"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="" prop="deletedAt">
        <el-date-picker clearable
          v-model="queryParams.deletedAt"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="门头照片" prop="doorHeadPhoto">
        <el-input
          v-model="queryParams.doorHeadPhoto"
          placeholder="请输入门头照片"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="营业执照" prop="businessLicense">
        <el-input
          v-model="queryParams.businessLicense"
          placeholder="请输入营业执照"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="当前审批到哪里" prop="workflowTaskNode">
        <el-input
          v-model="queryParams.workflowTaskNode"
          placeholder="请输入当前审批到哪里"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['school:school:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['school:school:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['school:school:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['school:school:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="schoolList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="" align="center" prop="id" />
      <el-table-column label="" align="center" prop="name" />
      <el-table-column label="" align="center" prop="alias" />
      <el-table-column label="" align="center" prop="province" />
      <el-table-column label="" align="center" prop="provinceCode" />
      <el-table-column label="" align="center" prop="city" />
      <el-table-column label="" align="center" prop="cityCode" />
      <el-table-column label="" align="center" prop="area" />
      <el-table-column label="" align="center" prop="areaCode" />
      <el-table-column label="地址" align="center" prop="address" />
      <el-table-column label="建校时间" align="center" prop="date" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.date, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="类型：1省会级，2市级，3县级" align="center" prop="type" />
      <el-table-column label="运营方式：1第三种方式 2省级加盟总校 3省级直营市级 4市级加盟总校 5市级直营县级 6县级加盟总校 7总校直营 8县级加盟市级 9试运营 16外机构" align="center" prop="yyMode" />
      <el-table-column label="运营类型 1直营 2加盟 3第三种 4外机构" align="center" prop="yyType" />
      <el-table-column label="简介" align="center" prop="brief" />
      <el-table-column label="运营状态：1正常，2删除，3未完善，4停业" align="center" prop="status" />
      <el-table-column label="停课状态" align="center" prop="state" />
      <el-table-column label="类型，school校区，depart部门" align="center" prop="category" />
      <el-table-column label="" align="center" prop="addressId" />
      <el-table-column label="当前负责人ID" align="center" prop="adminId" />
      <el-table-column label="上级ID" align="center" prop="pid" />
      <el-table-column label="负责的省级校区ID" align="center" prop="directlyId" />
      <el-table-column label="排课校区ID" align="center" prop="classId" />
      <el-table-column label="CRM系统ID" align="center" prop="crmId" />
      <el-table-column label="原始ID" align="center" prop="originId" />
      <el-table-column label="" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="" align="center" prop="updatedAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updatedAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="" align="center" prop="deletedAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.deletedAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="门头照片" align="center" prop="doorHeadPhoto" />
      <el-table-column label="营业执照" align="center" prop="businessLicense" />
      <el-table-column label="流程实例ID" align="center" prop="workflowId" />
      <el-table-column label="当前审批到哪里" align="center" prop="workflowTaskNode" />
      <el-table-column label="审批状态" align="center" prop="workflowStatus" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['school:school:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['school:school:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改分校成立对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="" prop="name">
          <el-input v-model="form.name" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="" prop="alias">
          <el-input v-model="form.alias" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="" prop="province">
          <el-input v-model="form.province" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="" prop="provinceCode">
          <el-input v-model="form.provinceCode" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="" prop="city">
          <el-input v-model="form.city" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="" prop="cityCode">
          <el-input v-model="form.cityCode" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="" prop="area">
          <el-input v-model="form.area" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="" prop="areaCode">
          <el-input v-model="form.areaCode" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="建校时间" prop="date">
          <el-date-picker clearable
            v-model="form.date"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择建校时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="运营方式：1第三种方式 2省级加盟总校 3省级直营市级 4市级加盟总校 5市级直营县级 6县级加盟总校 7总校直营 8县级加盟市级 9试运营 16外机构" prop="yyMode">
          <el-input v-model="form.yyMode" placeholder="请输入运营方式：1第三种方式 2省级加盟总校 3省级直营市级 4市级加盟总校 5市级直营县级 6县级加盟总校 7总校直营 8县级加盟市级 9试运营 16外机构" />
        </el-form-item>
        <el-form-item label="简介" prop="brief">
          <el-input v-model="form.brief" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="停课状态" prop="state">
          <el-input v-model="form.state" placeholder="请输入停课状态" />
        </el-form-item>
        <el-form-item label="类型，school校区，depart部门" prop="category">
          <el-input v-model="form.category" placeholder="请输入类型，school校区，depart部门" />
        </el-form-item>
        <el-form-item label="" prop="addressId">
          <el-input v-model="form.addressId" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="当前负责人ID" prop="adminId">
          <el-input v-model="form.adminId" placeholder="请输入当前负责人ID" />
        </el-form-item>
        <el-form-item label="上级ID" prop="pid">
          <el-input v-model="form.pid" placeholder="请输入上级ID" />
        </el-form-item>
        <el-form-item label="负责的省级校区ID" prop="directlyId">
          <el-input v-model="form.directlyId" placeholder="请输入负责的省级校区ID" />
        </el-form-item>
        <el-form-item label="排课校区ID" prop="classId">
          <el-input v-model="form.classId" placeholder="请输入排课校区ID" />
        </el-form-item>
        <el-form-item label="CRM系统ID" prop="crmId">
          <el-input v-model="form.crmId" placeholder="请输入CRM系统ID" />
        </el-form-item>
        <el-form-item label="原始ID" prop="originId">
          <el-input v-model="form.originId" placeholder="请输入原始ID" />
        </el-form-item>
        <el-form-item label="" prop="createdAt">
          <el-date-picker clearable
            v-model="form.createdAt"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="" prop="updatedAt">
          <el-date-picker clearable
            v-model="form.updatedAt"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="" prop="deletedAt">
          <el-date-picker clearable
            v-model="form.deletedAt"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="门头照片" prop="doorHeadPhoto">
          <el-input v-model="form.doorHeadPhoto" placeholder="请输入门头照片" />
        </el-form-item>
        <el-form-item label="营业执照" prop="businessLicense">
          <el-input v-model="form.businessLicense" placeholder="请输入营业执照" />
        </el-form-item>
        <el-form-item label="当前审批到哪里" prop="workflowTaskNode">
          <el-input v-model="form.workflowTaskNode" placeholder="请输入当前审批到哪里" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listSchool, getSchool, delSchool, addSchool, updateSchool } from "@/api/school/school";

export default {
  name: "School",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 分校成立表格数据
      schoolList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        alias: null,
        province: null,
        provinceCode: null,
        city: null,
        cityCode: null,
        area: null,
        areaCode: null,
        address: null,
        date: null,
        type: null,
        yyMode: null,
        yyType: null,
        brief: null,
        status: null,
        state: null,
        category: null,
        addressId: null,
        adminId: null,
        pid: null,
        directlyId: null,
        classId: null,
        crmId: null,
        originId: null,
        createdAt: null,
        updatedAt: null,
        deletedAt: null,
        doorHeadPhoto: null,
        businessLicense: null,
        workflowId: null,
        workflowTaskNode: null,
        workflowStatus: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        adminId: [
          { required: true, message: "当前负责人ID不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询分校成立列表 */
    getList() {
      this.loading = true;
      listSchool(this.queryParams).then(response => {
        this.schoolList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        alias: null,
        province: null,
        provinceCode: null,
        city: null,
        cityCode: null,
        area: null,
        areaCode: null,
        address: null,
        date: null,
        type: null,
        yyMode: null,
        yyType: null,
        brief: null,
        status: null,
        state: null,
        category: null,
        addressId: null,
        adminId: null,
        pid: null,
        directlyId: null,
        classId: null,
        crmId: null,
        originId: null,
        createdAt: null,
        updatedAt: null,
        deletedAt: null,
        doorHeadPhoto: null,
        businessLicense: null,
        workflowId: null,
        workflowTaskNode: null,
        workflowStatus: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加分校成立";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getSchool(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改分校成立";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateSchool(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSchool(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除分校成立编号为"' + ids + '"的数据项？').then(function() {
        return delSchool(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('school/school/export', {
        ...this.queryParams
      }, `school_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
