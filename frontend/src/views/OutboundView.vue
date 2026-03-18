<template>
  <section class="panel">
    <div class="panel-header">
      <h3>出库管理</h3>
      <div class="toolbar">
        <button class="btn ghost" @click="resetForm">清空</button>
        <select class="table-input" v-model="selectedLeibie" @change="applyFilter">
          <option value="all">全部类别</option>
          <option v-for="item in chukuCats" :key="item" :value="item">{{ item }}</option>
        </select>
        <button class="btn" @click="reload">刷新</button>
        <button class="btn primary" @click="submit" :disabled="!isValid">提交出库</button>
      </div>
    </div>
    <div class="form-grid">
      <label class="input">
        物料名称
        <input v-model="form.name" placeholder="输入物料" />
      </label>
      <label class="input">
        客户
        <input v-model="form.client" placeholder="客户" />
      </label>
      <label class="input">
        类别
        <select v-model="form.leibie">
          <option value="">请选择类别</option>
          <option v-for="item in chukuCats" :key="item" :value="item">{{ item }}</option>
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
            <th>客户</th>
            <th>单价</th>
            <th>数量</th>
            <th>金额</th>
            <th>时间</th>
            <th>提交人</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in chukuList" :key="row.id">
            <td>{{ row.id }}</td>
            <td>{{ row.name }}</td>
            <td>{{ row.client }}</td>
            <td>{{ row.price }}</td>
            <td>{{ row.quantity }}</td>
            <td>{{ row.money }}</td>
            <td>{{ formatTime(row.cktime) }}</td>
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
  chukuList: { type: Array, default: () => [] },
  chukuCats: { type: Array, default: () => [] }
});

const emit = defineEmits(["submit-outbound", "refresh-all", "filter-chuku"]);

const form = reactive({
  name: "",
  client: "",
  leibie: "",
  quantity: 1,
  price: 0
});

const isValid = computed(() => {
  return (
    form.name.trim().length > 0 &&
    form.client.trim().length > 0 &&
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
  emit("filter-chuku", value);
}

function resetForm() {
  form.name = "";
  form.client = "";
  form.leibie = "";
  form.quantity = 1;
  form.price = 0;
}

function submit() {
  if (!isValid.value) return;
  emit("submit-outbound", { ...form });
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
