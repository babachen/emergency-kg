<template>
  <div>
    <div class="page-header">
      <div class="page-title">答辩演示看板</div>
      <el-button type="primary" @click="loadData">刷新数据</el-button>
    </div>
    <div class="stat-grid">
      <div class="stat-card" style="background: linear-gradient(135deg,#2563eb,#3b82f6)"><h3>预案数量</h3><div class="value">{{ summary.documentCount }}</div></div>
      <div class="stat-card" style="background: linear-gradient(135deg,#0891b2,#06b6d4)"><h3>分段数量</h3><div class="value">{{ summary.sectionCount }}</div></div>
      <div class="stat-card" style="background: linear-gradient(135deg,#16a34a,#22c55e)"><h3>抽取任务</h3><div class="value">{{ summary.taskCount }}</div></div>
      <div class="stat-card" style="background: linear-gradient(135deg,#7c3aed,#8b5cf6)"><h3>图谱实体</h3><div class="value">{{ summary.entityCount }}</div></div>
      <div class="stat-card" style="background: linear-gradient(135deg,#ea580c,#f97316)"><h3>三元组数量</h3><div class="value">{{ summary.tripleCount }}</div></div>
    </div>
    <div class="chart-grid">
      <div class="chart-card">
        <div class="chart-title">任务状态分布</div>
        <div ref="taskChartRef" style="height: 320px"></div>
      </div>
      <div class="chart-card">
        <div class="chart-title">省市预案覆盖</div>
        <div ref="regionChartRef" style="height: 320px"></div>
      </div>
    </div>
    <div class="chart-card" style="margin-top: 16px">
      <div class="chart-title">图谱版本质量趋势</div>
      <div ref="qualityChartRef" style="height: 320px"></div>
    </div>
  </div>
</template>

<script setup>
import * as echarts from 'echarts'
import { onMounted, reactive, ref } from 'vue'
import { getDashboardSummary } from '../../api/dashboard'

const summary = reactive({ taskStatusList: [], regionDistribution: [], qualityTrend: [] })
const taskChartRef = ref()
const regionChartRef = ref()
const qualityChartRef = ref()

const renderCharts = () => {
  echarts.init(taskChartRef.value).setOption({ tooltip: { trigger: 'item' }, series: [{ type: 'pie', radius: ['45%', '68%'], data: summary.taskStatusList }] })
  echarts.init(regionChartRef.value).setOption({ tooltip: {}, xAxis: { type: 'category', data: summary.regionDistribution.map((item) => item.name), axisLabel: { rotate: 45 } }, yAxis: { type: 'value' }, series: [{ type: 'bar', data: summary.regionDistribution.map((item) => item.value), itemStyle: { color: '#3b82f6' } }] })
  echarts.init(qualityChartRef.value).setOption({ tooltip: {}, xAxis: { type: 'category', data: summary.qualityTrend.map((item) => item.name) }, yAxis: { type: 'value', min: 0, max: 100 }, series: [{ type: 'line', smooth: true, data: summary.qualityTrend.map((item) => item.value), areaStyle: {}, itemStyle: { color: '#8b5cf6' } }] })
}

const loadData = async () => {
  const data = await getDashboardSummary()
  Object.assign(summary, data)
  renderCharts()
}

onMounted(loadData)
</script>
