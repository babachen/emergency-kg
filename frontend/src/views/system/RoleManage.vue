<template>
  <div class="page-card">
    <div class="page-header"><div class="page-title">角色管理</div><el-button type="primary" @click="openDialog()">新增角色</el-button></div>
    <el-table :data="roles" border><el-table-column prop="roleCode" label="角色编码" /><el-table-column prop="roleName" label="角色名称" /><el-table-column prop="description" label="角色说明" /><el-table-column prop="statusText" label="状态"><template #default="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.statusText }}</el-tag></template></el-table-column><el-table-column label="菜单数"><template #default="{ row }">{{ row.menuIds?.length || 0 }}</template></el-table-column><el-table-column label="操作" width="220"><template #default="{ row }"><el-button link type="primary" @click="openDialog(row)">编辑</el-button><el-button link type="danger" @click="remove(row.id)">删除</el-button></template></el-table-column></el-table>
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑角色' : '新增角色'" width="620px">
      <el-form :model="form" label-width="90px"><el-form-item label="角色编码"><el-input v-model="form.roleCode" /></el-form-item><el-form-item label="角色名称"><el-input v-model="form.roleName" /></el-form-item><el-form-item label="状态"><el-switch v-model="form.status" :active-value="1" :inactive-value="0" /></el-form-item><el-form-item label="菜单权限"><el-tree-select v-model="form.menuIds" :data="menuOptions" multiple show-checkbox node-key="id" check-strictly :props="{ label: 'menuName', children: 'children' }" style="width:100%" /></el-form-item><el-form-item label="角色说明"><el-input v-model="form.description" type="textarea" /></el-form-item></el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="submit">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import { createRole, deleteRole, getMenus, getRoles, updateRole } from '../../api/system'
const roles = ref([])
const menuOptions = ref([])
const dialogVisible = ref(false)
const form = reactive({ id: null, roleCode: '', roleName: '', description: '', status: 1, menuIds: [] })
const resetForm = () => Object.assign(form, { id: null, roleCode: '', roleName: '', description: '', status: 1, menuIds: [] })
const loadData = async () => { roles.value = await getRoles(); menuOptions.value = await getMenus() }
const openDialog = (row) => { resetForm(); if (row) Object.assign(form, JSON.parse(JSON.stringify(row))); dialogVisible.value = true }
const submit = async () => { form.id ? await updateRole(form.id, form) : await createRole(form); ElMessage.success('保存成功'); dialogVisible.value = false; loadData() }
const remove = async (id) => { await ElMessageBox.confirm('确认删除该角色吗？', '提示'); await deleteRole(id); ElMessage.success('删除成功'); loadData() }
onMounted(loadData)
</script>
