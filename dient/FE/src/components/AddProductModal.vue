<template>
  <div class="modal fade show d-block" tabindex="-1" style="background:rgba(0,0,0,0.3)">
    <div class="modal-dialog modal-lg">
      <div class="modal-content p-4">
        <h5>Add Product</h5>
        <form @submit.prevent="submit">
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
import { ref } from 'vue'
import { addProduct } from '../service'

defineProps({
  brands: { type: Array, default: () => [] },
  subcategories: { type: Array, default: () => [] }
})

const emit = defineEmits(['close', 'saved'])

const initialState = () => ({
  productName: '',
  color: '',
  quantity: '',
  sellPrice: '',
  originPrice: '',
  brandId: '',
  subcateId: ''
})

const form = ref(initialState())

async function submit() {
  const brandId = Number(form.value.brandId)
  const subcateId = Number(form.value.subcateId)

  if (Number.isNaN(brandId) || Number.isNaN(subcateId)) {
    alert('Vui lòng chọn đầy đủ thương hiệu và danh mục con')
    return
  }

  const payload = {
    productName: form.value.productName.trim(),
    color: form.value.color.trim(),
    quantity: Number(form.value.quantity),
    sellPrice: Number(form.value.sellPrice),
    originPrice: Number(form.value.originPrice),
    brandId: [brandId],
    subcateId
  }

  try {
    await addProduct(payload)
    emit('saved')
    form.value = initialState()
    alert('Thêm sản phẩm thành công')
  } catch (err) {
    alert('Lỗi khi thêm sản phẩm: ' + (err.response?.data?.message || ''))
  }
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
