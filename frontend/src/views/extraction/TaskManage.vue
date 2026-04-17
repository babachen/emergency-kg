<template>
  <div class="page-card">
    <div class="page-header"><div class="page-title">知识抽取任务管理</div><el-button type="primary" @click="openDialog()">新建抽取任务</el-button></div>
    <div class="search-bar"><el-form :inline="true" :model="query"><el-form-item label="任务状态"><el-select v-model="query.taskStatus" clearable style="width: 120px"><el-option label="待执行" :value="0" /><el-option label="执行中" :value="1" /><el-option label="已完成" :value="2" /><el-option label="失败" :value="3" /></el-select></el-form-item><el-form-item label="关键词"><el-input v-model="query.keyword" /></el-form-item><el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item></el-form></div>
    <el-table :data="tableData.records" border><el-table-column prop="taskName" label="任务名称" /><el-table-column prop="documentTitle" label="关联预案" min-width="220" /><el-table-column prop="modelName" label="模型/助手" width="180" /><el-table-column prop="taskStatusText" label="状态" width="100"><template #default="{ row }"><el-tag :type="row.taskStatus === 2 ? 'success' : row.taskStatus === 3 ? 'danger' : 'warning'">{{ row.taskStatusText }}</el-tag></template></el-table-column><el-table-column label="进度" width="160"><template #default="{ row }"><el-progress :percentage="row.progressPercent || 0" :status="row.taskStatus === 3 ? 'exception' : row.taskStatus === 2 ? 'success' : ''" /></template></el-table-column><el-table-column prop="extractedCount" label="抽取条数" width="100" /><el-table-column label="操作" width="160"><template #default="{ row }"><el-button link type="primary" @click="run(row.id)">执行</el-button></template></el-table-column></el-table>
    <div style="margin-top:16px; display:flex; justify-content:flex-end;"><el-pagination background layout="total, prev, pager, next" :current-page="query.current" :page-size="query.pageSize" :total="tableData.total" @current-change="(page) => { query.current = page; loadData() }" /></div>
    <el-dialog v-model="dialogVisible" title="新建抽取任务" width="620px"><el-form :model="form" label-width="100px"><el-form-item label="关联预案"><el-select v-model="form.documentId" style="width:100%"><el-option v-for="item in documents" :key="item.id" :label="item.title" :value="item.id" /></el-select></el-form-item><el-form-item label="任务名称"><el-input v-model="form.taskName" /></el-form-item><el-form-item label="模型名称"><el-input v-model="form.modelName" placeholder="默认：应急抽取助手-Mock" /></el-form-item><el-form-item label="提示模板"><el-input v-model="form.promptTemplate" type="textarea" :rows="5" /></el-form-item></el-form><template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="submit">保存</el-button></template></el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { createTask, executeTask, getDocuments, getTasks } from '../../api/document'
const query = reactive({ current: 1, pageSize: 10, taskStatus: undefined, keyword: '' })
const tableData = reactive({ total: 0, records: [] })
const documents = ref([])
const dialogVisible = ref(false)
const form = reactive({ documentId: undefined, taskName: '', modelName: '', promptTemplate: '请抽取主体、任务、资源与三元组' })
const loadData = async () => Object.assign(tableData, await getTasks(query))
const loadDocuments = async () => { const res = await getDocuments({ current: 1, pageSize: 100 }); documents.value = res.records }
const openDialog = () => { Object.assign(form, { documentId: undefined, taskName: '', modelName: '', promptTemplate: '请抽取主体、任务、资源与三元组' }); dialogVisible.value = true }
const submit = async () => { await createTask(form); ElMessage.success('任务创建成功'); dialogVisible.value = false; loadData() }
const run = async (id) => { await executeTask(id); ElMessage.success('任务已执行'); loadData() }
onMounted(() => { loadDocuments(); loadData() })
</script>
