<template>
  <section class="panel">
    <div class="panel-header">
      <h3>操作日志</h3>
      <div class="toolbar">
        <button class="btn" @click="$emit('load-log')">刷新</button>
      </div>
    </div>
    <div class="table-wrap">
      <table class="table">
        <thead>
          <tr>
            <th>编号</th>
            <th>人员</th>
            <th>时间</th>
            <th>对象</th>
            <th>动作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in logList" :key="row.id">
            <td>{{ row.id }}</td>
            <td>{{ row.name }}</td>
            <td>{{ formatTime(row.time) }}</td>
            <td>{{ row.target }}</td>
            <td>{{ row.action }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
defineProps({
  logList: { type: Array, default: () => [] }
});

function formatTime(value) {
  if (!value) return "";
  return String(value).replace("T", " ").slice(0, 19);
}
</script>

