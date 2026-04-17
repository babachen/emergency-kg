<template>
  <div class="login-shell">
    <div class="login-panel">
      <div class="hero-block">
        <div class="hero-tag">LLM + KG + 应急管理</div>
        <h1>基于大语言模型的应急预案知识图谱构建</h1>
        <p>支持全国省市级防汛应急预案的上传管理、文本预处理、知识抽取、图谱问答、推理与质量评估。</p>
        <ul>
          <li>31 省市预案知识统一表达</li>
          <li>图谱检索 + 可视化 + Cypher 封装</li>
          <li>知识冲突检测 + 补全 + 版本管理</li>
        </ul>
      </div>
      <div class="form-block">
        <div class="form-title">系统登录</div>
        <el-form ref="formRef" :model="form" label-position="top" @keyup.enter="handleLogin">
          <el-form-item label="用户名">
            <el-input v-model="form.username" placeholder="请输入用户名" />
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" />
          </el-form-item>
          <el-button type="primary" class="login-btn" :loading="loading" @click="handleLogin">登录系统</el-button>
          <div class="default-tip">默认账号：admin / 123456</div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../store/auth'
const router = useRouter()
const authStore = useAuthStore()
const loading = ref(false)
const form = reactive({ username: 'admin', password: '123456' })
const handleLogin = async () => {
  loading.value = true
  try {
    await authStore.login(form)
    ElMessage.success('登录成功')
    router.push('/dashboard')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-shell { min-height: 100vh; display: flex; align-items: center; justify-content: center; padding: 24px; background: radial-gradient(circle at top left, rgba(59,130,246,.25), transparent 30%), radial-gradient(circle at bottom right, rgba(14,165,233,.25), transparent 35%), #0f172a; }
.login-panel { width: min(1180px, 100%); display: grid; grid-template-columns: 1.25fr .85fr; gap: 24px; }
.hero-block, .form-block { background: rgba(255,255,255,.95); border-radius: 24px; padding: 36px; box-shadow: 0 30px 80px rgba(15,23,42,.28); }
.hero-tag { display: inline-flex; padding: 8px 14px; border-radius: 999px; background: #dbeafe; color: #1d4ed8; font-weight: 700; }
.hero-block h1 { font-size: 40px; line-height: 1.2; margin: 18px 0 14px; }
.hero-block p { color: #475569; font-size: 16px; line-height: 1.8; }
.hero-block ul { margin-top: 18px; padding-left: 20px; color: #334155; line-height: 2; }
.form-block { align-self: center; }
.form-title { font-size: 26px; font-weight: 700; margin-bottom: 24px; }
.login-btn { width: 100%; height: 44px; margin-top: 8px; }
.default-tip { margin-top: 14px; color: #64748b; text-align: center; }
@media (max-width: 960px) { .login-panel { grid-template-columns: 1fr; } .hero-block h1 { font-size: 28px; } }
</style>
