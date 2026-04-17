<template>
  <div class="page-card">
    <div class="page-header"><div class="page-title">图谱实体管理</div></div>
    <div class="search-bar"><el-form :inline="true" :model="query"><el-form-item label="实体类型"><el-select v-model="query.entityTypeId" clearable style="width: 180px"><el-option v-for="item in entityTypes" :key="item.id" :label="item.name" :value="item.id" /></el-select></el-form-item><el-form-item label="关键词"><el-input v-model="query.keyword" /></el-form-item><el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item></el-form></div>
    <el-table :data="tableData.records" border><el-table-column prop="entityName" label="实体名称" /><el-table-column prop="entityTypeName" label="实体类型" width="140" /><el-table-column prop="regionName" label="区域" width="120" /><el-table-column prop="sourceDocumentTitle" label="来源预案" min-width="200" /><el-table-column prop="confidence" label="置信度" width="100" /><el-table-column prop="statusText" label="状态" width="100" /></el-table>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { getEntities, getEntityTypes } from '../../api/kg'
const query = reactive({ current: 1, pageSize: 20, entityTypeId: undefined, keyword: '' })
const tableData = reactive({ total: 0, records: [] })
const entityTypes = ref([])
const loadData = async () => Object.assign(tableData, await getEntities(query))
onMounted(async () => { entityTypes.value = await getEntityTypes(); loadData() })
</script>
