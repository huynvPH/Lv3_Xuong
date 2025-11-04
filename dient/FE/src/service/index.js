import axios from 'axios'

export const getBrands = () => axios.get('/api/brands')
export const getCategories = () => axios.get('/api/categories')
export const getSubcategories = () => axios.get('/api/subcategories')
export const getStatuses = () => axios.get('/api/statuses')

export const getProducts = (params) => axios.get('/api/products', { params })
export const getProductById = (id) => axios.get(`/api/products/${id}`)
export const addProduct = (data) => axios.post('/api/products', data)
export const updateProduct = (id, data) => axios.put(`/api/products/${id}`, data)
export const deleteProduct = (id) => axios.delete(`/api/products/${id}`)
