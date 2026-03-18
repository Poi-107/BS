<template>
  <section class="panel">
    <div class="panel-header">
      <h3>入库</h3>
      <div class="toolbar">
        <button class="btn ghost" @click="resetForm">清空</button>
        <select class="table-input" v-model="selectedLeibie" @change="applyFilter">
          <option value="all">全部类别</option>
          <option v-for="item in rukuCats" :key="item" :value="item">{{ item }}</option>
        </select>
        <button class="btn" @click="reload">刷新</button>
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
        类别
        <select v-model="form.leibie">
          <option value="">请选择类别</option>
          <option v-for="item in rukuCats" :key="item" :value="item">{{ item }}</option>
        </select>
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
            <th>编号</th>
            <th>物料</th>
            <th>供应商</th>
            <th>单价</th>
            <th>数量</th>
            <th>金额</th>
            <th>时间</th>
            <th>提交人</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in rukuMine" :key="row.id">
            <td>{{ row.id }}</td>
            <td>{{ row.name }}</td>
            <td>{{ row.supplier }}</td>
            <td>{{ row.price }}</td>
            <td>{{ row.quantity }}</td>
            <td>{{ row.money }}</td>
            <td>{{ formatTime(row.rktime) }}</td>
            <td>{{ row.user }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import { computed, reactive, ref } from "vue";

defineProps({
  rukuMine: { type: Array, default: () => [] },
  rukuCats: { type: Array, default: () => [] }
});

const emit = defineEmits(["submit-inbound", "refresh-all", "filter-ruku"]);

const form = reactive({
  name: "",
  supplier: "",
  leibie: "",
  quantity: 1,
  price: 0
});

const isValid = computed(() => {
  return (
    form.name.trim().length > 0 &&
    form.supplier.trim().length > 0 &&
    form.leibie.trim().length > 0 &&
    Number.isFinite(form.quantity) &&
    form.quantity > 0 &&
    Number.isFinite(form.price) &&
    form.price >= 0
  );
});

const selectedLeibie = ref("all");

function applyFilter() {
  const value = selectedLeibie.value === "all" ? "" : selectedLeibie.value;
  emit("filter-ruku", value);
}

function resetForm() {
  form.name = "";
  form.supplier = "";
  form.leibie = "";
  form.quantity = 1;
  form.price = 0;
}

function submit() {
  if (!isValid.value) return;
  emit("submit-inbound", { ...form });
  resetForm();
}

function formatTime(value) {
  if (!value) return "";
  return String(value).replace("T", " ").slice(0, 19);
}

function reload() {
  window.location.reload();
}
</script>
