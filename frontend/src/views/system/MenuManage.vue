<template>
  <div class="page-card">
    <div class="page-header"><div class="page-title">菜单管理</div><el-button type="primary" @click="openDialog()">新增菜单</el-button></div>
    <el-table :data="menus" row-key="id" border default-expand-all><el-table-column prop="menuName" label="菜单名称" /><el-table-column prop="menuType" label="类型" /><el-table-column prop="routePath" label="路由" /><el-table-column prop="permissionCode" label="权限标识" /><el-table-column prop="icon" label="图标" /><el-table-column label="操作" width="220"><template #default="{ row }"><el-button link type="primary" @click="openDialog(row)">编辑</el-button><el-button link type="danger" @click="remove(row.id)">删除</el-button></template></el-table-column></el-table>
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑菜单' : '新增菜单'" width="620px">
      <el-form :model="form" label-width="90px"><el-form-item label="父级菜单"><el-tree-select v-model="form.parentId" :data="[{ id: 0, menuName: '顶级菜单', children: menus }]" node-key="id" :props="{ label: 'menuName', children: 'children' }" style="width:100%" /></el-form-item><el-form-item label="菜单名称"><el-input v-model="form.menuName" /></el-form-item><el-form-item label="菜单类型"><el-select v-model="form.menuType" style="width:100%"><el-option label="目录" value="CATALOG" /><el-option label="菜单" value="MENU" /><el-option label="按钮" value="BUTTON" /></el-select></el-form-item><el-form-item label="路由路径"><el-input v-model="form.routePath" /></el-form-item><el-form-item label="组件路径"><el-input v-model="form.componentPath" /></el-form-item><el-form-item label="图标"><el-input v-model="form.icon" /></el-form-item><el-form-item label="权限标识"><el-input v-model="form.permissionCode" /></el-form-item></el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="submit">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import { createMenu, deleteMenu, getMenus, updateMenu } from '../../api/system'
const menus = ref([])
const dialogVisible = ref(false)
const form = reactive({ id: null, parentId: 0, menuName: '', menuType: 'MENU', routePath: '', componentPath: '', icon: 'Menu', permissionCode: '', sortOrder: 0, visible: 1, status: 1 })
const resetForm = () => Object.assign(form, { id: null, parentId: 0, menuName: '', menuType: 'MENU', routePath: '', componentPath: '', icon: 'Menu', permissionCode: '', sortOrder: 0, visible: 1, status: 1 })
const loadData = async () => { menus.value = await getMenus() }
const openDialog = (row) => { resetForm(); if (row) Object.assign(form, JSON.parse(JSON.stringify(row))); dialogVisible.value = true }
const submit = async () => { form.id ? await updateMenu(form.id, form) : await createMenu(form); ElMessage.success('保存成功'); dialogVisible.value = false; loadData() }
const remove = async (id) => { await ElMessageBox.confirm('确认删除该菜单吗？', '提示'); await deleteMenu(id); ElMessage.success('删除成功'); loadData() }
onMounted(loadData)
</script>
