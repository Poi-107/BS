<template>
  <section class="panel">
    <section class="hero">
      <h2>库存管理系统</h2>
      <p>已对接后端接口，支持库存台账、出入库单据、审核与日志实时加载。</p>
    </section>

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
        <button class="btn" @click="$emit('refresh-all')">刷新</button>
      </div>
    </div>
    <div class="table-wrap">
      <table class="table">
        <thead>
          <tr>
            <th>物料</th>
            <th>库存</th>
            <th>安全库存</th>
            <th>状态</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in inventory" :key="row.id">
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
defineProps({
  inventory: { type: Array, default: () => [] },
  totalQuantity: { type: Number, default: 0 },
  lowStockCount: { type: Number, default: 0 },
  rukuList: { type: Array, default: () => [] },
  chukuList: { type: Array, default: () => [] }
});

defineEmits(["refresh-all"]);
</script>



