<template>
  <div class="page-card">
    <div class="page-header"><div class="page-title">用户管理</div><el-button type="primary" @click="openDialog()">新增用户</el-button></div>
    <div class="search-bar">
      <el-form :inline="true" :model="query"><el-form-item label="关键词"><el-input v-model="query.keyword" placeholder="用户名/姓名" clearable /></el-form-item><el-form-item label="状态"><el-select v-model="query.status" clearable style="width: 120px"><el-option label="启用" :value="1" /><el-option label="停用" :value="0" /></el-select></el-form-item><el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item></el-form>
    </div>
    <el-table :data="tableData.records" border><el-table-column prop="username" label="用户名" /><el-table-column prop="realName" label="姓名" /><el-table-column prop="roleNames" label="角色"><template #default="{ row }">{{ row.roleNames?.join('、') }}</template></el-table-column><el-table-column prop="phone" label="手机号" /><el-table-column prop="statusText" label="状态"><template #default="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.statusText }}</el-tag></template></el-table-column><el-table-column prop="createTime" label="创建时间" width="180" /><el-table-column label="操作" width="220"><template #default="{ row }"><el-button link type="primary" @click="openDialog(row)">编辑</el-button><el-button link type="danger" @click="remove(row.id)">删除</el-button></template></el-table-column></el-table>
    <div style="margin-top: 16px; display:flex; justify-content:flex-end;"><el-pagination background layout="total, prev, pager, next" :current-page="query.current" :page-size="query.pageSize" :total="tableData.total" @current-change="(page) => { query.current = page; loadData() }" /></div>
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑用户' : '新增用户'" width="620px">
      <el-form :model="form" label-width="90px"><el-row :gutter="14"><el-col :span="12"><el-form-item label="用户名"><el-input v-model="form.username" /></el-form-item></el-col><el-col :span="12"><el-form-item label="密码"><el-input v-model="form.password" placeholder="留空则保留/默认123456" show-password /></el-form-item></el-col></el-row><el-row :gutter="14"><el-col :span="12"><el-form-item label="姓名"><el-input v-model="form.realName" /></el-form-item></el-col><el-col :span="12"><el-form-item label="手机号"><el-input v-model="form.phone" /></el-form-item></el-col></el-row><el-row :gutter="14"><el-col :span="12"><el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item></el-col><el-col :span="12"><el-form-item label="状态"><el-switch v-model="form.status" :active-value="1" :inactive-value="0" /></el-form-item></el-col></el-row><el-form-item label="角色"><el-select v-model="form.roleIds" multiple style="width: 100%"><el-option v-for="item in roleOptions" :key="item.id" :label="item.roleName" :value="item.id" /></el-select></el-form-item><el-form-item label="备注"><el-input v-model="form.remark" type="textarea" :rows="3" /></el-form-item></el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="submit">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import { createUser, deleteUser, getRoles, getUsers, updateUser } from '../../api/system'
const query = reactive({ current: 1, pageSize: 10, keyword: '', status: undefined })
const tableData = reactive({ total: 0, records: [] })
const dialogVisible = ref(false)
const roleOptions = ref([])
const form = reactive({ id: null, username: '', password: '', realName: '', phone: '', email: '', status: 1, roleIds: [], remark: '' })
const resetForm = () => Object.assign(form, { id: null, username: '', password: '', realName: '', phone: '', email: '', status: 1, roleIds: [], remark: '' })
const loadData = async () => { Object.assign(tableData, await getUsers(query)) }
const loadRoles = async () => { roleOptions.value = await getRoles() }
const openDialog = (row) => { resetForm(); if (row) Object.assign(form, { ...row, password: '' }); dialogVisible.value = true }
const submit = async () => { form.id ? await updateUser(form.id, form) : await createUser(form); ElMessage.success('保存成功'); dialogVisible.value = false; loadData() }
const remove = async (id) => { await ElMessageBox.confirm('确认删除该用户吗？', '提示'); await deleteUser(id); ElMessage.success('删除成功'); loadData() }
onMounted(() => { loadRoles(); loadData() })
</script>
