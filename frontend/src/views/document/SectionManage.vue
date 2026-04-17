<template>
  <div class="page-card">
    <div class="page-header"><div class="page-title">预案文本分段</div></div>
    <div class="search-bar"><el-form :inline="true"><el-form-item label="选择预案"><el-select v-model="documentId" filterable style="width: 320px" @change="loadSections"><el-option v-for="item in documents" :key="item.id" :label="item.title" :value="item.id" /></el-select></el-form-item><el-form-item><el-button type="primary" @click="loadSections">加载分段</el-button></el-form-item></el-form></div>
    <el-table :data="sections" border><el-table-column prop="sectionNo" label="段号" width="100" /><el-table-column prop="sectionTitle" label="章节标题" width="220" /><el-table-column prop="wordCount" label="字数" width="100" /><el-table-column prop="sectionContent" label="分段内容" min-width="460" show-overflow-tooltip /></el-table>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { getDocuments, getSections } from '../../api/document'
const route = useRoute()
const documents = ref([])
const sections = ref([])
const documentId = ref(route.query.documentId ? Number(route.query.documentId) : undefined)
const loadDocuments = async () => { const res = await getDocuments({ current: 1, pageSize: 100 }); documents.value = res.records }
const loadSections = async () => { if (!documentId.value) return; sections.value = await getSections(documentId.value) }
onMounted(async () => { await loadDocuments(); if (documentId.value) loadSections() })
</script>
