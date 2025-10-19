<template>
  <div class="login-page d-flex align-items-center justify-content-center">
    <div class="card shadow-sm login-card">
      <div class="card-body">
        <h2 class="card-title text-center mb-3">Đăng nhập</h2>
        <p class="text-muted text-center mb-4">
          Vui lòng sử dụng tài khoản Google của bạn để tiếp tục quản lý sản phẩm.
        </p>
        <div ref="googleButton" class="d-flex justify-content-center"></div>
        <div v-if="errorMessage" class="alert alert-danger mt-3" role="alert">
          {{ errorMessage }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { setCurrentUser, isAuthenticated } from '../service/auth'

const router = useRouter()
const route = useRoute()
const googleButton = ref(null)
const errorMessage = ref('')

function parseJwt(token) {
  try {
    const base64 = token.split('.')[1]
    const normalized = base64.replace(/-/g, '+').replace(/_/g, '/')
    const padded = normalized.padEnd(normalized.length + (4 - (normalized.length % 4 || 4)) % 4, '=')
    const decoded = atob(padded)
    return JSON.parse(decoded)
  } catch (error) {
    console.error('Failed to parse Google credential.', error)
    return null
  }
}

function handleCredential(response) {
  if (!response?.credential) {
    errorMessage.value = 'Không nhận được mã xác thực từ Google. Vui lòng thử lại.'
    return
  }

  const payload = parseJwt(response.credential)
  if (!payload) {
    errorMessage.value = 'Không thể đọc thông tin người dùng từ Google.'
    return
  }

  setCurrentUser({
    credential: response.credential,
    name: payload.name,
    email: payload.email,
    picture: payload.picture
  })

  const redirect = typeof route.query.redirect === 'string' ? route.query.redirect : '/products'
  router.replace(redirect)
}

function initGoogleButton() {
  const clientId = import.meta.env.VITE_GOOGLE_CLIENT_ID

  if (!clientId) {
    errorMessage.value = 'Chưa cấu hình Google Client ID. Vui lòng thêm VITE_GOOGLE_CLIENT_ID vào biến môi trường.'
    return
  }

  /* global google */
  const { google } = window

  if (!google?.accounts?.id) {
    errorMessage.value = 'Không thể khởi tạo Google Identity Services.'
    return
  }

  google.accounts.id.initialize({
    client_id: clientId,
    callback: handleCredential
  })

  google.accounts.id.renderButton(googleButton.value, {
    theme: 'outline',
    size: 'large',
    width: '280'
  })

  google.accounts.id.prompt()
}

function loadGoogleScript() {
  if (document.getElementById('google-client-script')) {
    if (window.google) {
      initGoogleButton()
    } else {
      document.getElementById('google-client-script')?.addEventListener('load', initGoogleButton, { once: true })
    }
    return
  }

  const script = document.createElement('script')
  script.id = 'google-client-script'
  script.src = 'https://accounts.google.com/gsi/client'
  script.async = true
  script.defer = true
  script.onload = initGoogleButton
  script.onerror = () => {
    errorMessage.value = 'Không thể tải Google Identity Services. Vui lòng kiểm tra kết nối mạng và thử lại.'
  }

  document.head.appendChild(script)
}

onMounted(() => {
  if (isAuthenticated()) {
    router.replace('/products')
    return
  }

  loadGoogleScript()
})
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #f8fafc 0%, #eef2ff 100%);
  padding: 1.5rem;
}

.login-card {
  max-width: 420px;
  width: 100%;
  border: none;
  border-radius: 1rem;
}

.card-title {
  font-weight: 600;
}
</style>
