import { createRouter, createWebHistory } from 'vue-router'
import ProductManager from '../view/ProductManager.vue'

const routes = [
  { path: '/', redirect: '/products' },
  {
    path: '/products',
    name: 'ProductManager',
    component: ProductManager
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router 