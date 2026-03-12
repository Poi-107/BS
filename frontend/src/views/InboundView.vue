<template>
  <section class="panel">
    <div class="panel-header">
      <h3>入库管理</h3>
      <div class="toolbar">
        <button class="btn ghost" @click="resetForm">清空</button>
        <button class="btn" @click="$emit('refresh-all')">刷新</button>
        <button class="btn primary" @click="submit" :disabled="!isValid">提交入库</button>
      </div>
    </div>
    <div class="form-grid">
      <label class="input">
        物料名称
        <input v-model="form.name" placeholder="输入物料" />
      </label>
      <label class="input">
        供应商
        <input v-model="form.supplier" placeholder="供应商" />
      </label>
      <label class="input">
        数量
        <input v-model.number="form.quantity" type="number" min="1" />
      </label>
      <label class="input">
        单价
        <input v-model.number="form.price" type="number" min="0" />
      </label>
    </div>
    <div class="form-hint" v-if="!isValid">请填写完整信息，数量 > 0</div>
    <div class="divider"></div>
    <div class="table-wrap">
      <table class="table">
        <thead>
          <tr>
            <th>单号</th>
            <th>物料</th>
            <th>数量</th>
            <th>供应商</th>
            <th>提交人</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in rukuList" :key="row.id">
            <td>{{ row.id }}</td>
            <td>{{ row.name }}</td>
            <td>{{ row.quantity }}</td>
            <td>{{ row.supplier }}</td>
            <td>{{ row.user }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import { computed, reactive } from "vue";

defineProps({
  rukuList: { type: Array, default: () => [] }
});

const emit = defineEmits(["submit-inbound", "refresh-all"]);

const form = reactive({
  name: "",
  supplier: "",
  quantity: 1,
  price: 0
});

const isValid = computed(() => {
  return (
    form.name.trim().length > 0 &&
    form.supplier.trim().length > 0 &&
    Number.isFinite(form.quantity) &&
    form.quantity > 0 &&
    Number.isFinite(form.price) &&
    form.price >= 0
  );
});

function resetForm() {
  form.name = "";
  form.supplier = "";
  form.quantity = 1;
  form.price = 0;
}

function submit() {
  if (!isValid.value) return;
  emit("submit-inbound", { ...form });
  resetForm();
}
</script>

