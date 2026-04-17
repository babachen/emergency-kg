import { defineStore } from 'pinia'
import { loginApi, menusApi, permissionsApi, profileApi } from '../api/auth'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('kg_token') || '',
    userInfo: JSON.parse(localStorage.getItem('kg_user') || 'null'),
    menus: JSON.parse(localStorage.getItem('kg_menus') || '[]'),
    permissions: JSON.parse(localStorage.getItem('kg_permissions') || '[]'),
  }),
  actions: {
    async login(form) {
      const res = await loginApi(form)
      this.token = res.token
      this.userInfo = res.userInfo
      this.menus = res.menus || []
      this.permissions = res.permissions || []
      localStorage.setItem('kg_token', res.token)
      localStorage.setItem('kg_user', JSON.stringify(this.userInfo))
      localStorage.setItem('kg_menus', JSON.stringify(this.menus))
      localStorage.setItem('kg_permissions', JSON.stringify(this.permissions))
    },
    async fetchContext() {
      const [userInfo, menus, permissions] = await Promise.all([profileApi(), menusApi(), permissionsApi()])
      this.userInfo = userInfo
      this.menus = menus || []
      this.permissions = permissions || []
      localStorage.setItem('kg_user', JSON.stringify(this.userInfo))
      localStorage.setItem('kg_menus', JSON.stringify(this.menus))
      localStorage.setItem('kg_permissions', JSON.stringify(this.permissions))
    },
    logout() {
      this.token = ''
      this.userInfo = null
      this.menus = []
      this.permissions = []
      localStorage.removeItem('kg_token')
      localStorage.removeItem('kg_user')
      localStorage.removeItem('kg_menus')
      localStorage.removeItem('kg_permissions')
    },
    hasPermission(code) {
      if (this.userInfo?.admin) return true
      return !code || this.permissions.includes(code)
    },
  },
})
