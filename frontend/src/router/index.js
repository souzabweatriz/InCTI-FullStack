import { createRouter, createWebHistory } from 'vue-router'

import Home from '../views/HomeView.vue'
import Login from '../views/LoginView.vue'
import Dashboard from '../views/DashboardView.vue'
import Upload from '../views/UploadView.vue'
import Relatorio from '../views/RelatorioView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: Home
  },

  {
    path: '/login',
    name: 'login',
    alias: '/Login',
    component: Login
  },

  {
    path: '/upload',
    redirect: { name: 'upload' }
  },

  {
    path: '/dashboard',
    component: Dashboard,
    redirect: { name: 'upload' },

    children: [
      {
        path: 'upload',
        name: 'upload',
        component: Upload
      },

      {
        path: 'relatorios',
        name: 'relatorios',
        component: Relatorio
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router