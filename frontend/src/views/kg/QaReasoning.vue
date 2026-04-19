<template>
  <div class="page-card">
    <div class="page-header"><div class="page-title">知识问答与推理分析</div></div>
    <el-form :model="form" label-width="90px">
      <el-form-item label="区域">
        <el-select v-model="form.regionId" clearable filterable style="width: 320px" @change="handleRegionChange">
          <el-option v-for="item in regions" :key="item.id" :label="item.regionName" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="预案">
        <el-select v-model="form.documentId" clearable filterable style="width: 420px">
          <el-option v-for="item in documents" :key="item.id" :label="item.title" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="问题描述"><el-input v-model="form.question" type="textarea" :rows="4" placeholder="例如：某省防汛预案中哪些主体负责预警发布与人员转移？" /></el-form-item>
    </el-form>
    <div style="display:flex; gap:12px; margin-bottom:16px"><el-button type="primary" @click="ask">知识问答</el-button><el-button type="success" @click="doReasoning">知识推理</el-button></div>
    <el-card shadow="never"><template #header>回答结果</template><div style="line-height: 1.8">{{ result.answer || '请先发起问答或推理。' }}</div></el-card>
    <el-card shadow="never" style="margin-top:16px"><template #header>推理链 / 引用</template><el-timeline><el-timeline-item v-for="(item, index) in (result.reasoningChain?.length ? result.reasoningChain : result.references)" :key="index">{{ item }}</el-timeline-item></el-timeline></el-card>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { getDocuments, getRegions } from '../../api/document'
import { qa, reasoning } from '../../api/kg'
const regions = ref([])
const documents = ref([])
const form = reactive({ regionId: undefined, documentId: undefined, question: '某省防汛预案中哪些主体负责预警发布与人员转移？' })
const result = reactive({ answer: '', reasoningChain: [], references: [] })
const loadDocuments = async () => {
  const { records } = await getDocuments({ current: 1, pageSize: 200, regionId: form.regionId })
  documents.value = records
}
const handleRegionChange = async () => {
  form.documentId = undefined
  await loadDocuments()
}
const ask = async () => Object.assign(result, await qa(form))
const doReasoning = async () => Object.assign(result, await reasoning(form))
onMounted(async () => { regions.value = await getRegions(); await loadDocuments() })
</script>
