<template>
  <header class="header-bar page-card">
    <div>
      <div class="header-title">基于大语言模型的应急预案知识图谱构建</div>
      <div class="text-muted">支持预案上传、文本分段、知识抽取、图谱检索、问答推理与质量评估</div>
    </div>
    <div class="header-actions">
      <el-tag type="success">{{ authStore.userInfo?.realName || '未登录' }}</el-tag>
      <el-button plain @click="refresh">刷新上下文</el-button>
      <el-button type="danger" plain @click="logout">退出登录</el-button>
    </div>
  </header>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../store/auth'
const router = useRouter()
const authStore = useAuthStore()
const refresh = async () => {
  await authStore.fetchContext()
  ElMessage.success('上下文已刷新')
}
const logout = () => {
  authStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.header-bar { display: flex; align-items: center; justify-content: space-between; padding: 16px 20px; margin: 20px 20px 0; }
.header-title { font-size: 20px; font-weight: 700; margin-bottom: 4px; }
.header-actions { display: flex; align-items: center; gap: 12px; }
</style>
