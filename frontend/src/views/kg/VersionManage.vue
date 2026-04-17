<template>
  <div class="page-card">
    <div class="page-header"><div class="page-title">图谱版本管理</div><el-button type="primary" @click="dialogVisible = true">创建版本快照</el-button></div>
    <el-table :data="tableData.records" border><el-table-column prop="versionName" label="版本名称" /><el-table-column prop="versionNo" label="版本号" width="120" /><el-table-column prop="nodeCount" label="节点数" width="90" /><el-table-column prop="relationCount" label="关系数" width="90" /><el-table-column prop="tripleCount" label="三元组" width="90" /><el-table-column prop="qualityScore" label="质量分" width="100" /><el-table-column prop="publishedStatusText" label="版本状态" width="120" /><el-table-column prop="createTime" label="创建时间" width="180" /></el-table>
    <el-dialog v-model="dialogVisible" title="创建图谱版本" width="520px"><el-form :model="form" label-width="90px"><el-form-item label="版本名称"><el-input v-model="form.versionName" /></el-form-item><el-form-item label="版本号"><el-input v-model="form.versionNo" /></el-form-item><el-form-item label="来源说明"><el-input v-model="form.sourceDesc" type="textarea" /></el-form-item></el-form><template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="submit">保存</el-button></template></el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { createVersion, getVersions } from '../../api/kg'
const tableData = reactive({ total: 0, records: [] })
const dialogVisible = ref(false)
const form = reactive({ versionName: '2026答辩演示版', versionNo: 'v2026.04', sourceDesc: '基于当前抽取与补全过程生成' })
const loadData = async () => Object.assign(tableData, await getVersions({ current: 1, pageSize: 20 }))
const submit = async () => { await createVersion(form); ElMessage.success('版本创建成功'); dialogVisible.value = false; loadData() }
onMounted(loadData)
</script>
