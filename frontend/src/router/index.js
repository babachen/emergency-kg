import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../layout/Layout.vue'
import { useAuthStore } from '../store/auth'

const routes = [
  { path: '/login', name: 'login', component: () => import('../views/Login.vue') },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', component: () => import('../views/dashboard/Dashboard.vue') },
      { path: 'system/users', component: () => import('../views/system/UserManage.vue') },
      { path: 'system/roles', component: () => import('../views/system/RoleManage.vue') },
      { path: 'system/menus', component: () => import('../views/system/MenuManage.vue') },
      { path: 'system/logs', component: () => import('../views/system/LogManage.vue') },
      { path: 'document/manage', component: () => import('../views/document/DocumentManage.vue') },
      { path: 'document/sections', component: () => import('../views/document/SectionManage.vue') },
      { path: 'extraction/tasks', component: () => import('../views/extraction/TaskManage.vue') },
      { path: 'kg/ontology', component: () => import('../views/kg/OntologyManage.vue') },
      { path: 'kg/entities', component: () => import('../views/kg/EntityManage.vue') },
      { path: 'kg/triples', component: () => import('../views/kg/TripleManage.vue') },
      { path: 'kg/graph', component: () => import('../views/kg/GraphView.vue') },
      { path: 'kg/qa', component: () => import('../views/kg/QaReasoning.vue') },
      { path: 'kg/quality', component: () => import('../views/kg/QualityManage.vue') },
      { path: 'kg/versions', component: () => import('../views/kg/VersionManage.vue') },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore()
  if (to.path === '/login') {
    if (authStore.token) return next('/dashboard')
    return next()
  }
  if (!authStore.token) return next('/login')
  if (!authStore.userInfo) {
    try {
      await authStore.fetchContext()
    } catch (error) {
      authStore.logout()
      return next('/login')
    }
  }
  next()
})

export default router
