import { createRouter, createWebHistory } from 'vue-router'
import ProductManager from '../view/ProductManager.vue'
import Login from '../view/Login.vue'
import { isAuthenticated } from '../service/auth'

const routes = [
  { path: '/', redirect: '/products' },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/products',
    name: 'ProductManager',
    component: ProductManager,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const authed = isAuthenticated()

  if (to.meta.requiresAuth && !authed) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
    return
  }

  if (to.name === 'Login' && authed) {
    next({ name: 'ProductManager' })
    return
  }

  next()
})

export default router
