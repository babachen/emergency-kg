import http from './http'
export const loginApi = (data) => http.post('/api/auth/login', data)
export const profileApi = () => http.get('/api/auth/profile')
export const menusApi = () => http.get('/api/auth/menus')
export const permissionsApi = () => http.get('/api/auth/permissions')
