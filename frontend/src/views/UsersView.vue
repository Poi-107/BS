<template>
  <section class="panel">
    <div class="panel-header">
      <h3>用户管理</h3>
      <div class="toolbar">
        <button class="btn" @click="openAdd">添加用户</button>
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
                <option :value="1">主管</option>
                <option :value="2">经理</option>
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

    <div v-if="showAdd" class="modal-mask" @click="closeAdd">
      <div class="modal-card" style="width: min(520px, 92vw);" @click.stop>
        <div class="modal-title">添加用户</div>
        <div class="modal-body">
          <label class="input">
            用户名
            <input v-model.trim="addForm.username" class="table-input" placeholder="请输入用户名" autocomplete="off" />
          </label>
          <label class="input" style="margin-top: 12px;">
            密码
            <input v-model="addForm.password" class="table-input" type="password" placeholder="请输入密码" autocomplete="new-password" />
          </label>
          <label class="input" style="margin-top: 12px;">
            确认密码
            <input v-model="addForm.confirm" class="table-input" type="password" placeholder="请再次输入密码" autocomplete="new-password" />
          </label>

          <div v-if="addError" class="login-error" style="margin-top: 10px;">{{ addError }}</div>
          <div class="form-hint" style="margin-top: 10px;">新增用户默认权限为普通用户</div>
        </div>
        <div class="modal-actions">
          <button class="btn ghost" @click="closeAdd">取消</button>
          <button class="btn primary" @click="confirmAdd">添加</button>
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

const emit = defineEmits(["load-users", "update-user", "delete-user", "add-user"]);

const localUsers = ref([]);
const showDelete = ref(false);
const deleteTarget = ref(null);
const showAdd = ref(false);
const addForm = ref({ username: "", password: "", confirm: "" });
const addError = ref("");

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

function openAdd() {
  addError.value = "";
  addForm.value = { username: "", password: "", confirm: "" };
  showAdd.value = true;
}

function closeAdd() {
  showAdd.value = false;
}

function confirmAdd() {
  addError.value = "";
  const username = String(addForm.value.username || "").trim();
  const password = String(addForm.value.password || "");
  const confirm = String(addForm.value.confirm || "");
  if (!username) {
    addError.value = "请输入用户名";
    return;
  }
  if (!password) {
    addError.value = "请输入密码";
    return;
  }
  if (password !== confirm) {
    addError.value = "两次输入的密码不一致";
    return;
  }
  emit("add-user", { username, password });
  closeAdd();
}

function reload() {
  window.location.reload();
}
</script>


