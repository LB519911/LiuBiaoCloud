<template>
  <div class="app-container">
    <el-table v-loading="loading" :data="schoolList" @selection-change="handleSelectionChange">
      <el-table-column label="校区名称" align="center" prop="name"/>
      <el-table-column label="地址" align="center" prop="address"/>
      <el-table-column label="建校时间" align="center" prop="date" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.date, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="简介" align="center" prop="brief"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
              size="small"
              type="success"
              @click="startWorkFlow(scope.row)"
              v-hasPermi="['school:school:apList']">通过
          </el-button>
          <el-button
              size="small"
              type="danger"
              @click="hiFlow(scope.row)"
              v-hasPermi="['school:school:apList']">不通过
          </el-button>
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
        <el-form-item label="校区名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入"/>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入地址"/>
        </el-form-item>
        <el-form-item label="建校时间" prop="date">
          <el-date-picker clearable
                          v-model="form.date"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择建校时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="简介" prop="brief">
          <el-input v-model="form.brief" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog
        title="您的审批进度"
        :visible.sync="hiFlowImgDialogVisible"
        width="60%"
        :before-close="handleClose">
      <div v-html="hiFlowImg"></div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="hiFlowImgDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="hiFlowImgDialogVisible = false">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
import {apListSchool, getSchool, delSchool, addSchool, updateSchool, startFlow, hiFlow} from "@/api/school/school";

export default {
  name: "School",
  data() {
    return {
      hiFlowImgDialogVisible: false,
      hiFlowImg: '',
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
        address: null,
        date: null,
        yyMode: null,
        yyType: null,
        status: null,
        state: null,
        category: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
    };
  },
  created() {
    this.getList();
  },
  methods: {
    handleClose(done) {
      this.hiFlowImgDialogVisible = false
      this.hiFlowImg = ''
    },
    /** 查询分校成立列表 */
    getList() {
      this.loading = true;
      apListSchool(this.queryParams).then(response => {
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
      this.single = selection.length !== 1
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
    /** 发起审批按钮操作 */
    startWorkFlow(row) {
      this.reset();
      const id = row.id || this.ids
      startFlow(id).then(response => {
        this.form = response.data;
        row.workflowStatus = 1
        this.$modal.msgSuccess("流程发起成功");
      });
    },
    /** 查看审批进度 */
    hiFlow(row) {
      this.reset();
      const id = row.id || this.ids
      hiFlow(id).then(response => {
        this.hiFlowImgDialogVisible = true
        this.hiFlowImg = response.data
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
      this.$modal.confirm('是否确认删除分校成立编号为"' + ids + '"的数据项？').then(function () {
        return delSchool(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
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
