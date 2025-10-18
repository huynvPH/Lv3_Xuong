import axios from 'axios'

const api = axios.create({
  baseURL: '/api'
})

export const getBrands = () => api.get('/brands')
export const getCategories = () => api.get('/categories')
export const getSubcategories = () => api.get('/subcategories')
export const getStatuses = () => api.get('/statuses')

export const getProducts = (params) => api.get('/products', { params })
export const getProductById = (id) => api.get(`/products/${id}`)
export const addProduct = (data) => api.post('/products', data)
export const updateProduct = (id, data) => api.put(`/products/${id}`, data)
export const deleteProduct = (id) => api.delete(`/products/${id}`)