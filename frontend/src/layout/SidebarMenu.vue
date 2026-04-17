<template>
  <el-menu :default-active="$route.path" background-color="transparent" text-color="#cbd5e1" active-text-color="#ffffff" router unique-opened>
    <template v-for="menu in menus" :key="menu.id">
      <el-sub-menu v-if="menu.children?.length" :index="menu.routePath || `${menu.id}`">
        <template #title>
          <el-icon><component :is="resolveIcon(menu.icon)" /></el-icon>
          <span>{{ menu.menuName }}</span>
        </template>
        <el-menu-item v-for="child in menu.children" :key="child.id" :index="child.routePath">
          <el-icon><component :is="resolveIcon(child.icon)" /></el-icon>
          <span>{{ child.menuName }}</span>
        </el-menu-item>
      </el-sub-menu>
      <el-menu-item v-else :index="menu.routePath">
        <el-icon><component :is="resolveIcon(menu.icon)" /></el-icon>
        <span>{{ menu.menuName }}</span>
      </el-menu-item>
    </template>
  </el-menu>
</template>

<script setup>
import { resolveIcon } from '../utils/icons'
defineProps({ menus: { type: Array, default: () => [] } })
</script>

<style scoped>
:deep(.el-menu) { border-right: none; }
:deep(.el-menu-item), :deep(.el-sub-menu__title) { border-radius: 12px; margin-bottom: 6px; }
:deep(.el-menu-item.is-active) { background: linear-gradient(90deg, rgba(59,130,246,.65), rgba(14,165,233,.65)); }
</style>
