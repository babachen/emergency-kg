<template>
  <div class="layout-shell">
    <aside class="layout-sidebar">
      <div class="brand-box">
        <div class="brand-icon">KG</div>
        <div>
          <div class="brand-title">应急预案知识图谱</div>
          <div class="brand-sub">毕业设计演示系统</div>
        </div>
      </div>
      <el-scrollbar>
        <SidebarMenu :menus="authStore.menus" />
      </el-scrollbar>
    </aside>
    <div class="layout-main">
      <HeaderBar />
      <main class="layout-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { useAuthStore } from '../store/auth'
import SidebarMenu from './SidebarMenu.vue'
import HeaderBar from './HeaderBar.vue'
const authStore = useAuthStore()
</script>

<style scoped>
.layout-shell { display: flex; min-height: 100vh; }
.layout-sidebar { width: 260px; background: linear-gradient(180deg, #0f172a 0%, #1e293b 100%); color: #fff; padding: 18px 16px; }
.brand-box { display: flex; gap: 12px; align-items: center; padding: 4px 8px 20px; }
.brand-icon { width: 44px; height: 44px; border-radius: 14px; background: linear-gradient(135deg, #38bdf8, #2563eb); display: flex; align-items: center; justify-content: center; font-weight: 800; }
.brand-title { font-size: 16px; font-weight: 700; }
.brand-sub { font-size: 12px; color: rgba(255,255,255,.7); }
.layout-main { flex: 1; display: flex; flex-direction: column; }
.layout-content { padding: 20px; }
</style>
