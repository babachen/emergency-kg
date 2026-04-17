import http from './http'
export const getDashboardSummary = () => http.get('/api/dashboard/summary')
