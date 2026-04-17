<template>
  <div>
    <div class="page-card">
      <div class="page-header"><div class="page-title">图谱检索与可视化</div></div>
      <el-form :inline="true" :model="query"><el-form-item label="关键词"><el-input v-model="query.keyword" /></el-form-item><el-form-item><el-button type="primary" @click="loadGraph">检索图谱</el-button></el-form-item></el-form>
      <div ref="graphRef" style="height: 520px; margin-top: 16px"></div>
      <el-alert :title="graph.message || '图谱检索结果'" type="info" :closable="false" style="margin-top: 12px" />
    </div>
    <div class="page-card" style="margin-top: 16px">
      <div class="page-header"><div class="page-title">Cypher 查询封装</div></div>
      <el-input v-model="cypher" type="textarea" :rows="5" placeholder="例如：MATCH (n) RETURN n LIMIT 10" />
      <div style="margin-top: 12px"><el-button type="primary" @click="runCypher">执行查询</el-button></div>
      <el-alert style="margin-top:12px" :title="cypherResult.message || '等待执行'" type="success" :closable="false" />
      <pre style="white-space: pre-wrap; background:#0f172a; color:#e2e8f0; padding:16px; border-radius:12px; margin-top:12px">{{ JSON.stringify(cypherResult.rows || [], null, 2) }}</pre>
    </div>
  </div>
</template>

<script setup>
import * as echarts from 'echarts'
import { onMounted, reactive, ref } from 'vue'
import { execCypher, getGraph } from '../../api/kg'
const graphRef = ref()
const graph = reactive({ nodes: [], links: [], message: '' })
const query = reactive({ keyword: '' })
const cypher = ref('MATCH (n) RETURN n LIMIT 10')
const cypherResult = reactive({ message: '', rows: [] })
const renderGraph = () => {
  echarts.init(graphRef.value).setOption({ tooltip: {}, legend: [{ data: ['实体'] }], series: [{ type: 'graph', layout: 'force', roam: true, draggable: true, data: graph.nodes, links: graph.links, force: { repulsion: 220, edgeLength: 130 }, label: { show: true }, edgeLabel: { show: true, formatter: '{c}' } }] })
}
const loadGraph = async () => { Object.assign(graph, await getGraph(query)); renderGraph() }
const runCypher = async () => { Object.assign(cypherResult, await execCypher({ cypher: cypher.value })) }
onMounted(loadGraph)
</script>
