<template>
  <div class="page-card">
    <div class="page-header"><div class="page-title">冲突检测与知识补全</div></div>
    <el-tabs>
      <el-tab-pane label="冲突记录"><el-table :data="conflicts.records" border><el-table-column prop="conflictType" label="冲突类型" width="140" /><el-table-column prop="conflictDesc" label="冲突描述" min-width="240" /><el-table-column prop="statusText" label="状态" width="100" /><el-table-column prop="suggestedResolution" label="建议处理" min-width="260" /><el-table-column label="操作" width="120"><template #default="{ row }"><el-button link type="primary" @click="resolve(row.id)">标记已处理</el-button></template></el-table-column></el-table></el-tab-pane>
      <el-tab-pane label="补全建议"><el-table :data="completions.records" border><el-table-column prop="documentTitle" label="预案" min-width="180" /><el-table-column prop="suggestionContent" label="补全建议" min-width="280" /><el-table-column label="建议三元组" min-width="220"><template #default="{ row }">{{ row.missingSubject }} - {{ row.missingPredicate }} - {{ row.missingObject }}</template></el-table-column><el-table-column prop="statusText" label="状态" width="100" /><el-table-column label="操作" width="120"><template #default="{ row }"><el-button link type="success" @click="apply(row.id)">应用补全</el-button></template></el-table-column></el-table></el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { onMounted, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { applyCompletion, getCompletions, getConflicts, updateConflictStatus } from '../../api/kg'
const conflicts = reactive({ total: 0, records: [] })
const completions = reactive({ total: 0, records: [] })
const loadData = async () => { Object.assign(conflicts, await getConflicts({ current: 1, pageSize: 20 })); Object.assign(completions, await getCompletions({ current: 1, pageSize: 20 })) }
const resolve = async (id) => { await updateConflictStatus(id, 1); ElMessage.success('已更新状态'); loadData() }
const apply = async (id) => { await applyCompletion(id); ElMessage.success('补全已应用'); loadData() }
onMounted(loadData)
</script>
