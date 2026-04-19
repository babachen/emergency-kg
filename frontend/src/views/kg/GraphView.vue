<template>
  <div>
    <div class="page-card">
      <div class="page-header"><div class="page-title">图谱检索与可视化</div></div>
      <el-form :inline="true" :model="query">
        <el-form-item label="区域">
          <el-select v-model="query.regionId" clearable style="width: 180px" @change="handleRegionChange">
            <el-option v-for="item in regions" :key="item.id" :label="item.regionName" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="预案">
          <el-select v-model="query.documentId" clearable filterable style="width: 280px" @change="handleFilterChange">
            <el-option v-for="item in documents" :key="item.id" :label="item.title" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="关键词">
          <el-input v-model="query.keyword" placeholder="实体、关系或资源关键词" @keyup.enter="handleSearch" />
        </el-form-item>
        <el-form-item><el-button type="primary" @click="handleSearch">检索图谱</el-button></el-form-item>
      </el-form>
      <div ref="graphRef" class="graph-panel"></div>
      <el-alert :title="graph.message || '图谱检索结果'" type="info" :closable="false" style="margin-top: 12px" />
      <div style="margin-top: 16px">
        <el-table :data="graph.links" border>
          <el-table-column prop="source" label="主体" min-width="220" />
          <el-table-column prop="value" label="关系" width="120" />
          <el-table-column prop="target" label="客体" min-width="220" />
        </el-table>
        <div style="margin-top: 16px; display:flex; justify-content:flex-end;">
          <el-pagination
            background
            layout="total, sizes, prev, pager, next"
            :current-page="query.current"
            :page-size="query.pageSize"
            :page-sizes="[10, 12, 20, 50]"
            :total="graph.total"
            @current-change="handleCurrentChange"
            @size-change="handleSizeChange"
          />
        </div>
      </div>
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
import { nextTick, onBeforeUnmount, onMounted, reactive, ref } from 'vue'
import { getDocuments, getRegions } from '../../api/document'
import { execCypher, getGraph } from '../../api/kg'

const graphRef = ref()
const regions = ref([])
const documents = ref([])
const graph = reactive({ nodes: [], links: [], message: '', total: 0, current: 1, pageSize: 12 })
const query = reactive({ regionId: undefined, documentId: undefined, keyword: '', current: 1, pageSize: 12 })
const cypher = ref('MATCH (n) RETURN n LIMIT 10')
const cypherResult = reactive({ message: '', rows: [] })
let chartInstance

const ensureChart = async () => {
  await nextTick()
  if (!graphRef.value) return null
  chartInstance = echarts.getInstanceByDom(graphRef.value) || echarts.init(graphRef.value)
  chartInstance.resize()
  return chartInstance
}

const renderGraph = async () => {
  const chart = await ensureChart()
  if (!chart) return
  if (!graph.nodes.length || !graph.links.length) {
    chart.clear()
    chart.setOption({
      graphic: {
        type: 'text',
        left: 'center',
        top: 'middle',
        style: { text: '当前筛选条件下暂无图谱关系', fill: '#94a3b8', fontSize: 18, fontWeight: 500 }
      }
    })
    return
  }
  const categories = [...new Set(graph.nodes.map((item) => item.category || '实体'))].map((name) => ({ name }))
  chart.setOption({
    animationDurationUpdate: 400,
    tooltip: { trigger: 'item' },
    legend: [{ data: categories.map((item) => item.name) }],
    series: [{
      type: 'graph',
      layout: 'force',
      roam: true,
      draggable: true,
      data: graph.nodes.map((item) => ({ ...item, category: categories.findIndex((category) => category.name === (item.category || '实体')) })),
      links: graph.links,
      categories,
      force: { repulsion: 320, edgeLength: [120, 180], gravity: 0.06 },
      label: { show: true, color: '#334155', fontSize: 13 },
      lineStyle: { color: '#94a3b8', width: 1.5, curveness: 0.12 },
      edgeLabel: { show: true, formatter: '{c}', color: '#64748b', fontSize: 12 },
      emphasis: { focus: 'adjacency', scale: 1.08 },
      itemStyle: { borderColor: '#fff', borderWidth: 2, shadowBlur: 14, shadowColor: 'rgba(15,23,42,.12)' }
    }]
  }, true)
}

const loadRegions = async () => { regions.value = await getRegions() }

const loadDocuments = async () => {
  const { records } = await getDocuments({ current: 1, pageSize: 200, regionId: query.regionId })
  documents.value = records
}

const loadGraph = async () => {
  const data = await getGraph(query)
  Object.assign(graph, data)
  await renderGraph()
}

const handleFilterChange = () => {
  query.current = 1
}

const handleRegionChange = async () => {
  query.documentId = undefined
  handleFilterChange()
  await loadDocuments()
}

const handleSearch = async () => {
  query.current = 1
  await loadGraph()
}

const handleCurrentChange = async (page) => {
  query.current = page
  await loadGraph()
}

const handleSizeChange = async (size) => {
  query.pageSize = size
  query.current = 1
  await loadGraph()
}

const runCypher = async () => { Object.assign(cypherResult, await execCypher({ cypher: cypher.value })) }

const handleResize = () => {
  if (chartInstance) chartInstance.resize()
}

onMounted(async () => {
  window.addEventListener('resize', handleResize)
  await loadRegions()
  await loadDocuments()
  await loadGraph()
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  if (chartInstance) {
    chartInstance.dispose()
    chartInstance = null
  }
})
</script>

<style scoped>
.graph-panel {
  height: 520px;
  margin-top: 16px;
  border-radius: 14px;
  background: linear-gradient(180deg, #f8fbff 0%, #ffffff 100%);
  border: 1px solid #e2e8f0;
}
</style>
