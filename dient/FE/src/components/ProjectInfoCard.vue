<template>
  <div class="card project-info-card shadow-sm">
    <div class="card-body">
      <div class="d-flex align-items-center mb-3">
        <div class="icon-wrapper me-3">
          <i class="fa-solid fa-hexagon"></i>
        </div>
        <div>
          <h5 class="card-title mb-0">Thông tin dự án</h5>
          <small class="text-muted">Chi tiết cấu hình Google Cloud</small>
        </div>
      </div>
      <dl class="row mb-0">
        <dt class="col-sm-4">Tên dự án</dt>
        <dd class="col-sm-8">{{ projectName }}</dd>

        <dt class="col-sm-4">Số dự án</dt>
        <dd class="col-sm-8">{{ projectNumber }}</dd>

        <dt class="col-sm-4">ID dự án</dt>
        <dd class="col-sm-8">{{ projectId }}</dd>
      </dl>
    </div>
  </div>
</template>

<script setup>
const projectName = (import.meta.env.VITE_PROJECT_NAME || '').trim() || 'Chưa cấu hình'
const projectNumber = (import.meta.env.VITE_PROJECT_NUMBER || '').trim() || 'Chưa cấu hình'
const projectId = (import.meta.env.VITE_PROJECT_ID || '').trim() || 'Chưa cấu hình'
</script>

<style scoped>
.project-info-card {
  border: none;
}

.icon-wrapper {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: linear-gradient(135deg, #4f46e5, #7c3aed);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
}

dl {
  margin-bottom: 0;
}

dt {
  font-weight: 600;
  color: #374151;
}

dd {
  margin-bottom: 0.5rem;
  color: #1f2937;
}

dd:last-child {
  margin-bottom: 0;
}
</style>
