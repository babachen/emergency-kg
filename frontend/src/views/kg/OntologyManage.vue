<template>
  <div class="page-card">
    <div class="page-header"><div class="page-title">本体 Schema 管理</div></div>
    <el-tabs>
      <el-tab-pane label="实体类型">
        <div style="margin-bottom:12px"><el-button type="primary" @click="openEntityDialog()">新增实体类型</el-button></div>
        <el-table :data="entityTypes" border><el-table-column prop="code" label="编码" /><el-table-column prop="name" label="名称" /><el-table-column prop="color" label="颜色/展示" /><el-table-column prop="description" label="说明" /><el-table-column label="操作" width="200"><template #default="{ row }"><el-button link type="primary" @click="openEntityDialog(row)">编辑</el-button><el-button link type="danger" @click="removeEntity(row.id)">删除</el-button></template></el-table-column></el-table>
      </el-tab-pane>
      <el-tab-pane label="关系类型">
        <div style="margin-bottom:12px"><el-button type="primary" @click="openRelationDialog()">新增关系类型</el-button></div>
        <el-table :data="relationTypes" border><el-table-column prop="code" label="编码" /><el-table-column prop="name" label="名称" /><el-table-column prop="color" label="方向/附注" /><el-table-column prop="description" label="说明" /><el-table-column label="操作" width="200"><template #default="{ row }"><el-button link type="primary" @click="openRelationDialog(row)">编辑</el-button><el-button link type="danger" @click="removeRelation(row.id)">删除</el-button></template></el-table-column></el-table>
      </el-tab-pane>
    </el-tabs>
    <el-dialog v-model="entityDialog" :title="entityForm.id ? '编辑实体类型' : '新增实体类型'" width="520px"><el-form :model="entityForm" label-width="80px"><el-form-item label="编码"><el-input v-model="entityForm.code" /></el-form-item><el-form-item label="名称"><el-input v-model="entityForm.name" /></el-form-item><el-form-item label="颜色"><el-input v-model="entityForm.color" /></el-form-item><el-form-item label="说明"><el-input v-model="entityForm.description" type="textarea" /></el-form-item></el-form><template #footer><el-button @click="entityDialog=false">取消</el-button><el-button type="primary" @click="submitEntity">保存</el-button></template></el-dialog>
    <el-dialog v-model="relationDialog" :title="relationForm.id ? '编辑关系类型' : '新增关系类型'" width="520px"><el-form :model="relationForm" label-width="80px"><el-form-item label="编码"><el-input v-model="relationForm.code" /></el-form-item><el-form-item label="名称"><el-input v-model="relationForm.name" /></el-form-item><el-form-item label="附注"><el-input v-model="relationForm.color" /></el-form-item><el-form-item label="说明"><el-input v-model="relationForm.description" type="textarea" /></el-form-item></el-form><template #footer><el-button @click="relationDialog=false">取消</el-button><el-button type="primary" @click="submitRelation">保存</el-button></template></el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { createEntityType, createRelationType, deleteEntityType, deleteRelationType, getEntityTypes, getRelationTypes, updateEntityType, updateRelationType } from '../../api/kg'
const entityTypes = ref([])
const relationTypes = ref([])
const entityDialog = ref(false)
const relationDialog = ref(false)
const entityForm = reactive({ id: null, code: '', name: '', color: '', description: '' })
const relationForm = reactive({ id: null, code: '', name: '', color: '', description: '' })
const loadData = async () => { entityTypes.value = await getEntityTypes(); relationTypes.value = await getRelationTypes() }
const openEntityDialog = (row) => { Object.assign(entityForm, { id: null, code: '', name: '', color: '', description: '' }, row || {}); entityDialog.value = true }
const openRelationDialog = (row) => { Object.assign(relationForm, { id: null, code: '', name: '', color: '', description: '' }, row || {}); relationDialog.value = true }
const submitEntity = async () => { entityForm.id ? await updateEntityType(entityForm.id, entityForm) : await createEntityType(entityForm); ElMessage.success('保存成功'); entityDialog.value = false; loadData() }
const submitRelation = async () => { relationForm.id ? await updateRelationType(relationForm.id, relationForm) : await createRelationType(relationForm); ElMessage.success('保存成功'); relationDialog.value = false; loadData() }
const removeEntity = async (id) => { await ElMessageBox.confirm('确认删除该实体类型吗？', '提示'); await deleteEntityType(id); ElMessage.success('删除成功'); loadData() }
const removeRelation = async (id) => { await ElMessageBox.confirm('确认删除该关系类型吗？', '提示'); await deleteRelationType(id); ElMessage.success('删除成功'); loadData() }
onMounted(loadData)
</script>
