<template>
  <div class="page-card">
    <div class="page-header"><div class="page-title">操作日志</div></div>
    <div class="search-bar"><el-form :inline="true" :model="query"><el-form-item label="模块"><el-input v-model="query.moduleName" /></el-form-item><el-form-item label="类型"><el-input v-model="query.operationType" /></el-form-item><el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item></el-form></div>
    <el-table :data="tableData.records" border><el-table-column prop="moduleName" label="模块" width="140" /><el-table-column prop="operationType" label="操作类型" width="120" /><el-table-column prop="operatorName" label="操作人" width="120" /><el-table-column prop="content" label="内容" /><el-table-column prop="createTime" label="时间" width="180" /></el-table>
    <div style="margin-top:16px; display:flex; justify-content:flex-end;"><el-pagination background layout="total, prev, pager, next" :current-page="query.current" :page-size="query.pageSize" :total="tableData.total" @current-change="(page) => { query.current = page; loadData() }" /></div>
  </div>
</template>

<script setup>
import { onMounted, reactive } from 'vue'
import { getLogs } from '../../api/system'
const query = reactive({ current: 1, pageSize: 10, moduleName: '', operationType: '' })
const tableData = reactive({ total: 0, records: [] })
const loadData = async () => { Object.assign(tableData, await getLogs(query)) }
onMounted(loadData)
</script>
