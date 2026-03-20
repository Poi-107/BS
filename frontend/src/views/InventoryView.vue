<template>
  <section class="panel">
    <section class="card-grid">
      <div class="stat-card">
        <h3>总库存</h3>
        <strong>{{ totalQuantity }}</strong>
      </div>
      <div class="stat-card">
        <h3>低库存预警</h3>
        <strong>{{ lowStockCount }}</strong>
      </div>
      <div class="stat-card">
        <h3>入库单</h3>
        <strong>{{ rukuList.length }}</strong>
      </div>
      <div class="stat-card">
        <h3>出库单</h3>
        <strong>{{ chukuList.length }}</strong>
      </div>
    </section>
    <div class="panel-header">
      <h3>库存台账</h3>
      <div class="toolbar">
        <select class="table-input" v-model="selectedLeibie" @change="applyFilter">
          <option value="all">全部类别</option>
          <option v-for="item in kucunCats" :key="item" :value="item">{{ item }}</option>
        </select>
        <button class="btn" @click="reload">刷新</button>
      </div>
    </div>
    <div class="table-wrap">
      <table class="table">
        <thead>
          <tr>
            <th>条形码</th>
            <th>类别</th>
            <th>物料</th>
            <th>库存</th>
            <th>安全库存</th>
            <th>状态</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in inventory" :key="row.id">
            <td>{{ row.code }}</td>
            <td>{{ row.leibie }}</td>
            <td>{{ row.name }}</td>
            <td>{{ row.quantity }}</td>
            <td>{{ row.safe }}</td>
            <td>
              <span class="badge" :class="row.quantity < row.safe ? 'warn' : 'ok'">
                {{ row.quantity < row.safe ? '低库存' : '正常' }}
              </span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import {ref} from "vue";


const emit = defineEmits(["refresh-all", "filter-kucun"]);

const props = defineProps({
  inventory: { type: Array, default: () => [] },
  kucunCats: { type: Array, default: () => [] },
  totalQuantity: { type: Number, default: 0 },
  lowStockCount: { type: Number, default: 0 },
  rukuList: { type: Array, default: () => [] },
  chukuList: { type: Array, default: () => [] }
});


const selectedLeibie = ref("all");

function applyFilter() {
  const value = selectedLeibie.value === "all" ? "" : selectedLeibie.value;
  emit("filter-kucun", value);
}

function reload() {
  window.location.reload();
}
</script>
