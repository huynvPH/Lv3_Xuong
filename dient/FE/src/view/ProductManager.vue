<template>
  <div class="p-4 product-page">
    <div class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-3">
      <h4 class="mb-0">Product</h4>
      <div v-if="currentUser" class="d-flex align-items-center gap-3">
        <div class="d-flex align-items-center gap-2 user-summary">
          <img
            v-if="currentUser.picture"
            :src="currentUser.picture"
            alt="Ảnh đại diện"
            class="rounded-circle user-avatar"
          >
          <div class="text-end">
            <div class="fw-semibold">{{ currentUser.name }}</div>
            <small class="text-muted">{{ currentUser.email }}</small>
          </div>
        </div>
        <button class="btn btn-outline-danger btn-sm" @click="handleLogout">
          <i class="fas fa-right-from-bracket me-1"></i>
          Đăng xuất
        </button>
      </div>
    </div>

    <!-- Filter Section -->
    <div class="row g-3 align-items-center mb-3">
      <div class="col-md-2">
        <input v-model="filters.name" type="text" class="form-control" placeholder="Name">
      </div>
      <div class="col-md-2">
        <input v-model.number="filters.price" type="number" class="form-control" placeholder="Price">
      </div>
      <div class="col-md-2">
        <select v-model="filters.brandId" class="form-select">
          <option value="">All Brands</option>
          <option v-for="b in brands" :key="b.id" :value="b.id">{{ b.brandName }}</option>
        </select>
      </div>
      <div class="col-md-2">
        <select v-model="filters.categoryId" class="form-select">
          <option value="">All Categories</option>
          <option v-for="c in categories" :key="c.id" :value="c.id">{{ c.cateName }}</option>
        </select>
      </div>
      <div class="col-md-2">
        <select v-model="filters.statusId" class="form-select">
          <option value="">All Status</option>
          <option v-for="s in statuses" :key="s.id" :value="s.id">{{ s.statusName }}</option>
        </select>
      </div>
      <div class="col-md-auto d-flex align-items-center justify-content-center">
        <button class="btn btn-outline-secondary rounded-circle d-flex align-items-center justify-content-center" style="width:36px;height:36px;padding:0" @click="search" title="Search">
          <i class="fa fa-search"></i>
        </button>
      </div>
      <div class="col-md-auto d-flex align-items-center">
        <button class="btn btn-outline-danger d-flex align-items-center gap-2" @click="loginWithGoogle">
          <i class="fab fa-google"></i>
          <span>Login with Google</span>
        </button>
      </div>
    </div>

    <!-- Add Product Button -->
    <button class="btn btn-success mb-3" @click="showAddModal = true">
      <i class="fas fa-plus me-1"></i> Add Product
    </button>

    <!-- Table -->
    <table class="table table-bordered">
      <thead class="table-light">
        <tr>
          <th>STT</th>
          <th>Product Name</th>
          <th>Brand Name</th>
          <th>Subcategory</th>
          <th>Price</th>
          <th>Status</th>
          <th>Function</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(p, idx) in products" :key="p.id">
          <td>{{ page * size + idx + 1 }}</td>
          <td>{{ p.productName }}</td>
          <td>{{ p.brandNames }}</td>
          <td>{{ p.subcateName }}</td>
          <td>{{ p.sellPrice }}</td>
          <td>{{ p.statusName }}</td>
          <td>
            <button class="btn-action view" @click="viewProduct(p.id)" title="Xem">
  <i class="fas fa-eye"></i>
</button>
<button class="btn-action edit" @click="editProduct(p.id)" title="Sửa">
  <i class="fas fa-pencil-alt"></i>
</button>
<button class="btn-action delete" @click="deleteProduct(p.id)" title="Xóa">
  <i class="fas fa-trash"></i>
</button>

          </td>
        </tr>
        <tr v-if="products.length === 0">
          <td colspan="7" class="text-center">No data available</td>
        </tr>
      </tbody>
    </table>

    <!-- Pagination -->
    <div class="d-flex justify-content-between align-items-center">
      <div>
        Showing {{ page * size + 1 }} to {{ Math.min((page + 1) * size, totalElements) }} of {{ totalElements }} products
      </div>
      <nav>
        <ul class="pagination mb-0">
          <li class="page-item" :class="{disabled: page === 0}">
            <a class="page-link" href="#" @click.prevent="changePage(page-1)">Previous</a>
          </li>
          <li v-for="i in totalPages" :key="i" class="page-item" :class="{active: i-1 === page}">
            <a class="page-link" href="#" @click.prevent="changePage(i-1)">{{ i }}</a>
          </li>
          <li class="page-item" :class="{disabled: page === totalPages-1}">
            <a class="page-link" href="#" @click.prevent="changePage(page+1)">Next</a>
          </li>
        </ul>
      </nav>
    </div>

    <!-- Add Product Modal -->
    <AddProductModal
      v-if="showAddModal"
      :brands="brands"
      :subcategories="subcategories"
      @close="showAddModal = false"
      @saved="onProductSaved"
    />

    <!-- Detail Modal -->
    <ProductDetailModal
      v-if="showDetailModal"
      :product="selectedProduct"
      @close="showDetailModal = false"
    />

    <!-- Update Product Modal -->
    <UpdateProductModal
      v-if="showUpdateModal"
      :product="selectedProduct"
      :brands="brands"
      :subcategories="subcategories"
      :statuses="statuses"
      @close="showUpdateModal = false"
      @updated="onProductSaved"
    />
    
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import AddProductModal from '../components/AddProductModal.vue'
import ProductDetailModal from '../components/ProductDetailModal.vue'
import UpdateProductModal from '../components/UpdateProductModal.vue'
import axios from 'axios'
import { getCurrentUser, clearCurrentUser } from '../service/auth'

const router = useRouter()
const products = ref([])
const brands = ref([])
const categories = ref([])
const subcategories = ref([])
const statuses = ref([])
const currentUser = ref(getCurrentUser())

const googleLoginUrl = import.meta.env.VITE_GOOGLE_LOGIN_URL || '/oauth2/authorization/google'

const filters = reactive({
  name: '',
  price: '',
  brandId: '',
  categoryId: '',
  statusId: ''
})

const page = ref(0)
const size = 10
const totalPages = ref(1)
const totalElements = ref(0)

const showAddModal = ref(false)
const showDetailModal = ref(false)
const showUpdateModal = ref(false)
const selectedProduct = ref(null)

function handleLogout() {
  clearCurrentUser()
  router.replace('/login')
}

function fetchBrands() {
  axios.get('/api/brands').then(res => brands.value = res.data)
}
function fetchCategories() {
  axios.get('/api/categories').then(res => categories.value = res.data)
}
function fetchSubcategories() {
  axios.get('/api/subcategories').then(res => subcategories.value = res.data)
}
function fetchStatuses() {
  axios.get('/api/statuses').then(res => statuses.value = res.data)
}

function fetchProducts() {
  axios.get('/api/products', {
    params: {
      productName: filters.name,
      price: filters.price || null,
      brandId: filters.brandId || null,
      categoryId: filters.categoryId || null,
      statusId: filters.statusId || null,
      page: page.value,
      size
    }
  }).then(res => {
    console.log('API trả về toàn bộ:', res.data)
    console.log('Mảng products:', res.data.products)
    products.value = res.data.products || []
    totalPages.value = res.data.totalPages || 1
    totalElements.value = res.data.totalElements || 0
  }).catch(() => {
    products.value = []
    totalPages.value = 1
    totalElements.value = 0
  })
}

function loginWithGoogle() {
  try {
    if (!googleLoginUrl) {
      throw new Error('Google login URL is not configured')
    }

    const isAbsoluteUrl = /^https?:\/\//i.test(googleLoginUrl)
    const targetUrl = isAbsoluteUrl
      ? googleLoginUrl
      : `${window.location.origin}${googleLoginUrl.startsWith('/') ? googleLoginUrl : `/${googleLoginUrl}`}`

    window.location.assign(targetUrl)
  } catch (error) {
    console.error('Failed to start Google login', error)
    alert('Không thể chuyển đến trang đăng nhập Google. Vui lòng thử lại sau.')
  }
}

function search() {
  page.value = 0
  fetchProducts()
}

function changePage(p) {
  if (p >= 0 && p < totalPages.value) {
    page.value = p
    fetchProducts()
  }
}

function viewProduct(id) {
  axios.get(`/api/products/${id}`).then(res => {
    selectedProduct.value = res.data
    showDetailModal.value = true
  })
}

function editProduct(id) {
  axios.get(`/api/products/${id}`).then(res => {
    selectedProduct.value = res.data
    showUpdateModal.value = true
  })
}

function deleteProduct(id) {
  if (confirm('Are you sure you want to delete this product?')) {
    axios.delete(`/api/products/${id}`).then(() => {
      fetchProducts()
      alert('Product deleted successfully')
    }).catch(err => {
      alert('Error deleting product: ' + (err.response?.data?.message || ''))
    })
  }
}

function onProductSaved() {
  showAddModal.value = false
  showUpdateModal.value = false
  fetchProducts()
}

onMounted(() => {
  fetchBrands()
  fetchCategories()
  fetchSubcategories()
  fetchStatuses()
  fetchProducts()
})
</script>

<style scoped>
.btn-search {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
}
.btn-search i {
  font-size: 18px;
}
.no-data {
  text-align: center;
  font-style: italic;
  color: #6c757d;
}
/* chung cho cả 3 nút */
.btn-action {
  width: 40px;
  height: 40px;
  padding: 0;
  margin-right: 4px;
  border: none;
  border-radius: 8px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  cursor: pointer;
  transition: transform .2s, box-shadow .2s, background-color .3s;
}
.btn-action i {
  pointer-events: none;
}

/* View (xanh biển) */
.btn-action.view {
  background-color: #17a2b8;
  color: #fff;
}
.btn-action.view:hover {
  background-color: #138496;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

/* Edit (vàng) */
.btn-action.edit {
  background-color: #ffc107;
  color: #212529;
}
.btn-action.edit:hover {
  background-color: #e0a800;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

/* Delete (đỏ) */
.btn-action.delete {
  background-color: #dc3545;
  color: #fff;
}
.btn-action.delete:hover {
  background-color: #c82333;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

/* trạng thái nhấn/active */
.btn-action:active {
  transform: scale(0.95);
}

.product-page {
  background-color: #f8f9fa;
  min-height: 100vh;
}

.user-avatar {
  width: 40px;
  height: 40px;
  object-fit: cover;
}

.user-summary {
  min-width: 180px;
}

</style>