import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'
import './styles/index.css'
import { useAuthStore } from './store/auth'

const app = createApp(App)
const pinia = createPinia()
app.use(pinia)
app.use(router)
app.use(ElementPlus)
Object.entries(ElementPlusIconsVue).forEach(([key, component]) => app.component(key, component))
app.directive('permission', {
  mounted(el, binding) {
    const authStore = useAuthStore()
    if (binding.value && !authStore.hasPermission(binding.value)) {
      el.parentNode && el.parentNode.removeChild(el)
    }
  },
})
app.mount('#app')
