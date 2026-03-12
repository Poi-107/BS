<template>
  <section class="panel">
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
  inventory: { type: Array, default: () => [] }
});

defineEmits(["refresh-all"]);
</script>

