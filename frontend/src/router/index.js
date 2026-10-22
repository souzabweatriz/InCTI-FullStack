import { createRouter, createWebHistory } from 'vue-router'

import Home from '../views/HomeView.vue'
import Login from '../views/LoginView.vue'
import Cadastro from '../views/CadastroView.vue'
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
    path: '/cadastro',
    name: 'cadastro',
    alias: '/Cadastro',
    component: Cadastro
  },

  {
    path: '/upload',
    redirect: { name: 'upload' }
  },

  {
    path: '/dashboard',
    component: Dashboard,
    redirect: { name: 'upload' },
    meta: { requiresAuth: true },

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

router.beforeEach((to) => {
  const autenticado = localStorage.getItem('cti_logado') === 'true'
    && Boolean(localStorage.getItem('cti_token'))

  if (to.meta.requiresAuth && !autenticado) {
    return { name: 'login', query: { redirect: to.fullPath } }
  }

  return true
})

export default router