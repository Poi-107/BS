<template>
  <section class="panel">
    <div class="panel-header">
      <h3>审核中心</h3>
      <div class="toolbar">
        <button class="btn" :class="{ primary: auditFilter === 'all' }" @click="$emit('set-audit-filter', 'all')">全部</button>
        <button class="btn" :class="{ primary: auditFilter === 'pending' }" @click="$emit('set-audit-filter', 'pending')">未审核</button>
        <button class="btn" :class="{ primary: auditFilter === 'done' }" @click="$emit('set-audit-filter', 'done')">已审核</button>
        <button class="btn ghost" @click="$emit('load-audit')">刷新</button>
      </div>
    </div>
    <div class="table-wrap">
      <table class="table">
        <thead>
          <tr>
            <th>类型</th>
            <th>单号</th>
            <th>物料</th>
            <th>数量</th>
            <th>提交人</th>
            <th>状态</th>
            <th>审核人</th>
            <th>审核时间</th>
            <th>审核意见</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in auditView" :key="row.id">
            <td>{{ row.type }}</td>
            <td>{{ row.id }}</td>
            <td>{{ row.name }}</td>
            <td>{{ row.quantity }}</td>
            <td>{{ row.user }}</td>
            <td>
              <span class="badge" :class="statusBadgeClass(row.status)">
                {{ statusLabel(row.status) }}
              </span>
            </td>
            <td>{{ isDone(row.status) ? row.reviewer : '-' }}</td>
            <td>{{ isDone(row.status) ? formatTime(row.reviewertime) : '-' }}</td>
            <td>
              <template v-if="isDone(row.status)">
                {{ row.remark || '-' }}
              </template>
              <template v-else>
                <input class="input" v-model="row.remark" placeholder="请输入审核意见" />
              </template>
            </td>
            <td>
              <button class="btn primary" style="margin-right: 8px;" @click="$emit('approve-audit', row)" :disabled="row.status !== 0 && row.status !== '0'">通过</button>
              <button class="btn ghost" @click="$emit('reject-audit', row)" :disabled="row.status !== 0 && row.status !== '0'">驳回</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
defineProps({
  auditView: { type: Array, default: () => [] },
  auditFilter: { type: String, default: "all" }
});

function statusLabel(status) {
  if (status === 0 || status === "0") return "未审核";
  if (status === 1 || status === "1") return "已审核";
  if (status === 2 || status === "2") return "已驳回";
  return "未知";
}

function statusBadgeClass(status) {
  if (status === 0 || status === "0") return "warn";
  if (status === 1 || status === "1") return "ok";
  if (status === 2 || status === "2") return "warn";
  return "warn";
}

function isDone(status) {
  return status === 1 || status === "1" || status === 2 || status === "2";
}

function formatTime(value) {
  if (!value) return "";
  return String(value).replace("T", " ").slice(0, 19);
}
</script>

