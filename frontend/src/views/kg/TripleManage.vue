<template>
  <div class="page-card">
    <div class="page-header"><div class="page-title">知识三元组管理</div></div>
    <div class="search-bar"><el-form :inline="true" :model="query"><el-form-item label="校验状态"><el-select v-model="query.validationStatus" clearable style="width: 160px"><el-option label="待校验" :value="0" /><el-option label="已确认" :value="1" /><el-option label="存在冲突" :value="2" /></el-select></el-form-item><el-form-item label="关键词"><el-input v-model="query.keyword" /></el-form-item><el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item></el-form></div>
    <el-table :data="tableData.records" border><el-table-column prop="subjectName" label="主体" min-width="180" /><el-table-column prop="predicateName" label="关系" width="120" /><el-table-column prop="objectName" label="客体" min-width="180" /><el-table-column prop="sourceDocumentTitle" label="来源预案" min-width="200" /><el-table-column prop="validationStatusText" label="校验状态" width="120" /><el-table-column prop="confidence" label="置信度" width="100" /></el-table>
  </div>
</template>

<script setup>
import { onMounted, reactive } from 'vue'
import { getTriples } from '../../api/kg'
const query = reactive({ current: 1, pageSize: 20, validationStatus: undefined, keyword: '' })
const tableData = reactive({ total: 0, records: [] })
const loadData = async () => Object.assign(tableData, await getTriples(query))
onMounted(loadData)
</script>
