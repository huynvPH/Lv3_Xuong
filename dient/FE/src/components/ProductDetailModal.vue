<template>
  <div class="modal fade show d-block" tabindex="-1" style="background:rgba(0,0,0,0.3)">
    <div class="modal-dialog modal-lg">
      <div class="modal-content p-4">
        <h5>Product Details</h5>
        <div class="mb-2"><label>Name:</label><input type="text" class="form-control" :value="product?.productName" disabled></div>
        <div class="mb-2"><label>Color:</label><input type="text" class="form-control" :value="product?.color" disabled></div>
        <div class="mb-2"><label>Quantity:</label><input type="text" class="form-control" :value="product?.quantity" disabled></div>
        <div class="mb-2"><label>Sell Price:</label><input type="text" class="form-control" :value="product?.sellPrice" disabled></div>
        <div class="mb-2"><label>Origin Price:</label><input type="text" class="form-control" :value="product?.originPrice" disabled></div>
        <div class="mb-2"><label>Brand:</label><input type="text" class="form-control" :value="product?.brandNames" disabled></div>
        <div class="mb-2"><label>Subcategory:</label><input type="text" class="form-control" :value="product?.subcateName" disabled></div>
        <div class="mb-2"><label>Status:</label><input type="text" class="form-control" :value="product?.statusName" disabled></div>
        <div class="text-end mt-3">
          <button class="btn btn-secondary" @click="$emit('close')">Close</button>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
const props = defineProps(['product'])
</script> 