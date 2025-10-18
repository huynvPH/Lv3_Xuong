<template>
  <div class="modal fade show d-block" tabindex="-1" style="background:rgba(0,0,0,0.3)">
    <div class="modal-dialog modal-lg">
      <div class="modal-content p-4">
        <h5>Update Product</h5>
        <form @submit.prevent="submit">
          <input type="hidden" v-model="form.id">
          <div class="mb-2"><input v-model="form.productName" type="text" class="form-control" placeholder="Name" required></div>
          <div class="mb-2"><input v-model="form.color" type="text" class="form-control" placeholder="Color" required></div>
          <div class="mb-2"><input v-model.number="form.quantity" type="number" class="form-control" placeholder="Quantity" required></div>
          <div class="mb-2"><input v-model.number="form.sellPrice" type="number" class="form-control" placeholder="Sell price" required></div>
          <div class="mb-2"><input v-model.number="form.originPrice" type="number" class="form-control" placeholder="Origin price" required></div>
          <div class="mb-2">
            <select v-model="form.brandId" class="form-select filter-select" required>
              <option value="">Select Brand</option>
              <option v-for="b in brands" :key="b.id" :value="b.id">{{ b.brandName }}</option>
            </select>
          </div>
          <div class="mb-2">
            <select v-model="form.subcateId" class="form-select filter-select" required>
              <option value="">Select Subcategory</option>
              <option v-for="s in subcategories" :key="s.id" :value="s.id">{{ s.subCateName }}</option>
            </select>
          </div>
          <div class="mb-2">
            <select v-model="form.statusId" class="form-select filter-select" required>
              <option value="">Select Status</option>
              <option v-for="s in statuses" :key="s.id" :value="s.id">{{ s.statusName }}</option>
            </select>
          </div>
          <div class="text-end mt-3">
            <button type="submit" class="btn btn-success">Save changes</button>
            <button type="button" class="btn btn-secondary" @click="$emit('close')">Close</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, watch, onMounted } from 'vue'
import axios from 'axios'
const props = defineProps(['product', 'brands', 'subcategories', 'statuses'])
const emit = defineEmits(['close', 'updated'])
const form = ref({
  id: '', productName: '', color: '', quantity: '', sellPrice: '', originPrice: '', brandId: '', subcateId: '', statusId: ''
})
watch(() => props.product, (val) => {
  if (val) {
    form.value = {
      id: val.id,
      productName: val.productName,
      color: val.color,
      quantity: val.quantity,
      sellPrice: val.sellPrice,
      originPrice: val.originPrice,
      brandId: val.brandId ? val.brandId[0] : '',
      subcateId: val.subcateId || '',
      statusId: val.statusId || ''
    }
  }
}, { immediate: true })
function submit() {
  axios.put(`/api/products/${form.value.id}`, {
    id: form.value.id,
    productName: form.value.productName,
    color: form.value.color,
    quantity: form.value.quantity,
    sellPrice: form.value.sellPrice,
    originPrice: form.value.originPrice,
    brandId: [parseInt(form.value.brandId)],
    subcateId: parseInt(form.value.subcateId),
    statusId: parseInt(form.value.statusId)
  }).then(() => {
    emit('updated')
    alert('Cập nhật sản phẩm thành công')
  }).catch(err => {
    alert('Lỗi khi cập nhật sản phẩm: ' + (err.response?.data?.message || ''))
  })
}
</script>
<style scoped>
.filter-select:focus {
  outline: none;
  box-shadow: 0 0 0 0.2rem rgba(25,135,84,.25);
}
select.filter-select option {
  max-height: 40px;
}
</style> 