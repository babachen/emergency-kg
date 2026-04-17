<template>
  <div class="page-card">
    <div class="page-header"><div class="page-title">应急预案文档管理</div><el-button type="primary" @click="openDialog()">新增预案</el-button></div>
    <div class="search-bar"><el-form :inline="true" :model="query"><el-form-item label="区域"><el-select v-model="query.regionId" clearable style="width: 180px"><el-option v-for="item in regions" :key="item.id" :label="item.regionName" :value="item.id" /></el-select></el-form-item><el-form-item label="关键词"><el-input v-model="query.keyword" placeholder="预案标题" /></el-form-item><el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item></el-form></div>
    <el-table :data="tableData.records" border><el-table-column prop="title" label="预案标题" min-width="220" /><el-table-column prop="regionName" label="区域" width="120" /><el-table-column prop="planYear" label="年份" width="90" /><el-table-column prop="preprocessStatusText" label="分段状态" width="100"><template #default="{ row }"><el-tag :type="row.preprocessStatus === 1 ? 'success' : 'info'">{{ row.preprocessStatusText }}</el-tag></template></el-table-column><el-table-column prop="extractionStatusText" label="抽取状态" width="100"><template #default="{ row }"><el-tag :type="row.extractionStatus === 3 ? 'success' : row.extractionStatus === 4 ? 'danger' : 'warning'">{{ row.extractionStatusText }}</el-tag></template></el-table-column><el-table-column prop="sectionCount" label="分段数" width="90" /><el-table-column prop="publishOrg" label="发布单位" width="160" /><el-table-column label="操作" width="260"><template #default="{ row }"><el-button link type="primary" @click="openDialog(row)">编辑</el-button><el-button link @click="handlePreprocess(row.id)">分段</el-button><el-button link @click="viewSections(row.id)">查看分段</el-button><el-button link type="danger" @click="remove(row.id)">删除</el-button></template></el-table-column></el-table>
    <div style="margin-top: 16px; display:flex; justify-content:flex-end;"><el-pagination background layout="total, prev, pager, next" :current-page="query.current" :page-size="query.pageSize" :total="tableData.total" @current-change="(page) => { query.current = page; loadData() }" /></div>
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑预案' : '新增预案'" width="860px">
      <el-form :model="form" label-width="90px"><el-row :gutter="14"><el-col :span="12"><el-form-item label="预案标题"><el-input v-model="form.title" /></el-form-item></el-col><el-col :span="12"><el-form-item label="所属区域"><el-select v-model="form.regionId" style="width:100%"><el-option v-for="item in regions" :key="item.id" :label="item.regionName" :value="item.id" /></el-select></el-form-item></el-col></el-row><el-row :gutter="14"><el-col :span="8"><el-form-item label="预案年份"><el-input-number v-model="form.planYear" :min="2020" :max="2035" style="width:100%" /></el-form-item></el-col><el-col :span="8"><el-form-item label="预案类型"><el-input v-model="form.planType" /></el-form-item></el-col><el-col :span="8"><el-form-item label="版本号"><el-input v-model="form.versionNo" /></el-form-item></el-col></el-row><el-row :gutter="14"><el-col :span="12"><el-form-item label="发布单位"><el-input v-model="form.publishOrg" /></el-form-item></el-col><el-col :span="12"><el-form-item label="审批日期"><el-input v-model="form.approvalDate" placeholder="如 2026-03-21" /></el-form-item></el-col></el-row><el-form-item label="源链接"><el-input v-model="form.sourceUrl" /></el-form-item><el-form-item label="文件上传"><el-upload :show-file-list="false" :http-request="handleUpload"><el-button>上传附件</el-button></el-upload><span class="text-muted" style="margin-left:12px">{{ form.fileName || '未上传文件' }}</span></el-form-item><el-form-item label="摘要"><el-input v-model="form.summary" type="textarea" :rows="3" /></el-form-item><el-form-item label="正文内容"><el-input v-model="form.content" type="textarea" :rows="10" placeholder="答辩演示建议粘贴若干段预案正文，支持后续自动分段" /></el-form-item></el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="submit">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import { createDocument, deleteDocument, getDocuments, getRegions, preprocessDocument, updateDocument, uploadFile } from '../../api/document'
const router = useRouter()
const query = reactive({ current: 1, pageSize: 10, regionId: undefined, keyword: '' })
const tableData = reactive({ total: 0, records: [] })
const regions = ref([])
const dialogVisible = ref(false)
const form = reactive({ id: null, title: '', regionId: undefined, planYear: 2026, planType: '防汛应急预案', sourceUrl: '', fileId: null, fileName: '', filePath: '', publishOrg: '', approvalDate: '', versionNo: 'V1.0', summary: '', content: '' })
const resetForm = () => Object.assign(form, { id: null, title: '', regionId: undefined, planYear: 2026, planType: '防汛应急预案', sourceUrl: '', fileId: null, fileName: '', filePath: '', publishOrg: '', approvalDate: '', versionNo: 'V1.0', summary: '', content: '' })
const loadData = async () => Object.assign(tableData, await getDocuments(query))
const loadRegions = async () => { regions.value = await getRegions() }
const openDialog = (row) => { resetForm(); if (row) Object.assign(form, JSON.parse(JSON.stringify(row))); dialogVisible.value = true }
const handleUpload = async ({ file }) => { const data = new FormData(); data.append('file', file); data.append('businessType', 'plan'); const res = await uploadFile(data); form.fileId = res.id; form.fileName = res.originalName; form.filePath = res.filePath; ElMessage.success('上传成功') }
const submit = async () => { form.id ? await updateDocument(form.id, form) : await createDocument(form); ElMessage.success('保存成功'); dialogVisible.value = false; loadData() }
const handlePreprocess = async (id) => { await preprocessDocument(id); ElMessage.success('预处理完成'); loadData() }
const viewSections = (id) => router.push(`/document/sections?documentId=${id}`)
const remove = async (id) => { await ElMessageBox.confirm('确认删除该预案吗？', '提示'); await deleteDocument(id); ElMessage.success('删除成功'); loadData() }
onMounted(() => { loadRegions(); loadData() })
</script>
