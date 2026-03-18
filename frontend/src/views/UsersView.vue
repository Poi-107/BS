<template>
  <section class="panel">
    <div class="panel-header">
      <h3>用户管理</h3>
      <div class="toolbar">
        <button class="btn" @click="reload">刷新</button>
      </div>
    </div>
    <div class="table-wrap">
      <table class="table">
        <thead>
          <tr>
            <th>编号</th>
            <th>头像</th>
            <th>用户名</th>
            <th>号码</th>
            <th>邮箱</th>
            <th>地址</th>
            <th>权限</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in localUsers" :key="row.id">
            <td>{{ row.id }}</td>
            <td>
              <img v-if="row.avatar" :src="formatAvatar(row.avatar)" alt="avatar" class="avatar" />
              <span v-else>-</span>
            </td>
            <td>{{ row.username }}</td>
            <td>{{ row.phone || '-' }}</td>
            <td>{{ row.email || '-' }}</td>
            <td>{{ row.address || '-' }}</td>
            <td>
              <select v-model.number="row.per" class="table-input">
                <option :value="0">普通用户</option>
                <option :value="1">管理员</option>
              </select>
            </td>
            <td>
              <button class="btn primary" style="margin-right: 8px;" @click="save(row)" :disabled="row.per === row._originPer">保存</button>
              <button class="btn danger" @click="openDelete(row)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="showDelete" class="modal-mask">
      <div class="modal-card">
        <div class="modal-title">确认删除</div>
        <div class="modal-body">
          确定删除用户 <strong>{{ deleteTarget?.username }}</strong> 吗？
        </div>
        <div class="modal-actions">
          <button class="btn ghost" @click="closeDelete">取消</button>
          <button class="btn primary" @click="confirmDelete">确定</button>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, watch } from "vue";

const props = defineProps({
  users: { type: Array, default: () => [] }
});

const emit = defineEmits(["load-users", "update-user", "delete-user"]);

const localUsers = ref([]);
const showDelete = ref(false);
const deleteTarget = ref(null);

const API_BASE = import.meta.env.VITE_API_BASE || "http://localhost:8080";

function formatAvatar(url) {
  if (!url) return "";
  if (url.startsWith("http")) return url;
  if (url.startsWith("/uploads")) return `${API_BASE}${url}`;
  return url;
}

watch(
  () => props.users,
  (list) => {
    localUsers.value = (list || []).map((item) => ({
      ...item,
      _originPer: item.per
    }));
  },
  { immediate: true }
);

function save(row) {
  emit("update-user", {
    id: row.id,
    per: row.per
  });
}

function openDelete(row) {
  deleteTarget.value = row;
  showDelete.value = true;
}

function closeDelete() {
  showDelete.value = false;
  deleteTarget.value = null;
}

function confirmDelete() {
  if (!deleteTarget.value) return;
  emit("delete-user", { id: deleteTarget.value.id });
  closeDelete();
}

function reload() {
  window.location.reload();
}
</script>

