<template>
  <section class="panel">
    <div class="panel-header">
      <h3>消息通知</h3>
      <div class="toolbar">
        <button class="btn" @click="reload">刷新</button>
        <button v-if="canManage" class="btn" @click="openCreate">发布通知</button>
      </div>
    </div>

    <div class="sub-nav">
      <button class="btn" :class="{ primary: activeTab === 'system' }" @click="activeTab = 'system'">系统通知</button>
      <button class="btn" :class="{ primary: activeTab === 'personal' }" @click="activeTab = 'personal'">个人通知</button>
    </div>

    <div class="table-wrap">
      <div class="notice-list">
        <button
          v-for="row in sortedList"
          :key="row.id"
          class="notice-item"
          :class="priorityClass(row.priority)"
          @click="openDetail(row)"
        >
          <div class="notice-title">{{ row.title }}</div>
          <div class="notice-meta">
            <span class="notice-priority">{{ priorityText(row.priority) }}</span>
            <span>{{ formatTime(row.crtime) }}</span>
            <span v-if="row.crname">发布人：{{ row.crname }}</span>
            <span v-if="row.jieshou && activeTab === 'personal'">接收对象：{{ formatJieshou(row.jieshou) }}</span>
          </div>
          <div v-if="canManage" class="notice-actions" @click.stop>
            <button class="btn" @click="openEdit(row)">修改</button>
            <button class="btn danger" @click="openDelete(row)">删除</button>
          </div>
        </button>

        <div v-if="sortedList.length === 0" class="empty-tip">暂无通知</div>
      </div>
    </div>

    <div v-if="detailOpen" class="modal-mask" @click="closeDetail">
      <div class="modal-card" style="width:min(760px,92vw);" @click.stop>
        <div class="modal-title detail-title" :class="priorityClass(activeRow.priority)">
          {{ activeRow.title }}
        </div>
        <div class="modal-body detail-body">
          <p>{{ activeRow.text }}</p>
        </div>
        <div class="detail-foot">
          <span>发布人：{{ publisherText(activeRow) }}</span>
          <span>发布时间：{{ formatTime(activeRow.crtime) }}</span>
          <span v-if="activeRow.jieshou">接收对象：{{ formatJieshou(activeRow.jieshou) }}</span>
        </div>
        <div class="modal-actions">
          <button class="btn ghost" @click="closeDetail">关闭</button>
        </div>
      </div>
    </div>

    <div v-if="formOpen" class="modal-mask" @click="closeForm">
      <div class="modal-card" style="width:min(680px,92vw);" @click.stop>
        <div class="modal-title">{{ editingId ? "修改通知" : "发布通知" }}</div>
        <div class="modal-body" style="display:grid; gap:12px;">
          <label class="input">
            标题
            <input v-model.trim="form.title" placeholder="请输入标题" />
          </label>
          <label class="input">
            内容
            <textarea v-model.trim="form.text" rows="6" placeholder="请输入通知内容"></textarea>
          </label>
          <label class="input">
            优先级
            <select v-model.number="form.priority">
              <option :value="0">普通</option>
              <option :value="1">紧急</option>
            </select>
          </label>
          <label class="input">
            接收对象
            <select v-model="form.jieshou">
              <option value="0">系统全体</option>
              <option v-for="u in users" :key="u.id" :value="String(u.id)">
                {{ u.username }}
              </option>
            </select>
          </label>
        </div>
        <div class="modal-actions">
          <button class="btn ghost" @click="closeForm">取消</button>
          <button class="btn" @click="saveForm">保存</button>
        </div>
      </div>
    </div>

    <div v-if="showDelete" class="modal-mask">
      <div class="modal-card">
        <div class="modal-title">确认删除</div>
        <div class="modal-body">
          确定删除通知 <strong>{{ deleteTarget?.title }}</strong> 吗？
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
import { computed, ref } from "vue";

const props = defineProps({
  xinxiList: { type: Array, default: () => [] },
  users: { type: Array, default: () => [] },
  currentUser: { type: Object, default: () => ({}) },
  currentPer: { type: Number, default: 0 }
});

const emit = defineEmits(["refresh-all", "add-xinxi", "update-xinxi", "delete-xinxi"]);

const activeTab = ref('system');
const canManage = computed(() => Number(props.currentPer || 0) >= 1);

const sortedList = computed(() => {
  const userId = String(props.currentUser.id || "");
  const username = props.currentUser.username || "";

  let filtered = [];
  if (activeTab.value === 'system') {
    filtered = (props.xinxiList || []).filter(row => row.jieshou === '0');
  } else {
    filtered = (props.xinxiList || []).filter(row => {
      return row.jieshou !== '0' && (row.crname === username || String(row.jieshou) === userId);
    });
  }

  return [...filtered].sort((a, b) => {
    const pa = Number(a.priority || 0);
    const pb = Number(b.priority || 0);
    if (pa !== pb) return pb - pa;
    return toTime(b.crtime) - toTime(a.crtime);
  });
});

const detailOpen = ref(false);
const activeRow = ref({});

const formOpen = ref(false);
const editingId = ref(0);
const form = ref({ title: "", text: "", priority: 0, jieshou: "0" });

const showDelete = ref(false);
const deleteTarget = ref(null);

function toTime(v) {
  if (!v) return 0;
  const t = Date.parse(String(v).replace("T", " "));
  return Number.isNaN(t) ? 0 : t;
}

function formatTime(value) {
  if (!value) return "";
  return String(value).replace("T", " ").slice(0, 19);
}

function priorityText(priority) {
  return Number(priority || 0) === 1 ? "紧急" : "普通";
}

function priorityClass(priority) {
  return Number(priority || 0) === 1 ? "priority-danger" : "priority-normal";
}

function publisherText(row) {
  if (row.crname) return row.crname;
  return row.publisher || row.user || row.username || "系统";
}

function formatJieshou(jieshou) {
  if (!jieshou || jieshou === "0") return "系统全体";
  const user = props.users.find(u => String(u.id) === String(jieshou));
  return user ? user.username : `用户ID:${jieshou}`;
}

function reload() {
  emit("refresh-all");
}

function openDetail(row) {
  activeRow.value = row || {};
  detailOpen.value = true;
}

function closeDetail() {
  detailOpen.value = false;
}

function openCreate() {
  editingId.value = 0;
  form.value = { title: "", text: "", priority: 0, jieshou: "0" };
  formOpen.value = true;
}

function openEdit(row) {
  editingId.value = Number(row.id || 0);
  form.value = {
    title: String(row.title || ""),
    text: String(row.text || ""),
    priority: Number(row.priority || 0),
    jieshou: String(row.jieshou || "0")
  };
  formOpen.value = true;
}

function closeForm() {
  formOpen.value = false;
}

function saveForm() {
  if (!form.value.title || !form.value.text) {
    alert("标题和内容不能为空");
    return;
  }
  const payload = {
    title: form.value.title,
    text: form.value.text,
    priority: Number(form.value.priority || 0),
    jieshou: form.value.jieshou
  };
  if (editingId.value) {
    emit("update-xinxi", { ...payload, id: editingId.value });
  } else {
    emit("add-xinxi", payload);
  }
  formOpen.value = false;
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
  emit("delete-xinxi", Number(deleteTarget.value.id));
  closeDelete();
}
</script>

<style scoped>
.sub-nav {
  display: flex;
  gap: 10px;
  margin-bottom: 12px;
}

.notice-list {
  display: grid;
  gap: 10px;
}

.table-wrap {
  max-height: calc(100vh - 260px);
  overflow-y: auto;
}

.notice-item {
  text-align: left;
  width: 100%;
  border: 1px solid var(--line);
  border-radius: 14px;
  background: var(--panel);
  padding: 14px 16px;
  display: grid;
  gap: 8px;
  cursor: pointer;
  transition: transform 0.16s ease, box-shadow 0.16s ease;
}

.notice-item:hover {
  transform: translateY(-1px);
  box-shadow: var(--soft-shadow);
}

.notice-title {
  font-size: 22px;
  font-weight: 800;
  line-height: 1.25;
  color: var(--ink);
}

.notice-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 12px;
  color: var(--muted);
}

.notice-priority {
  font-weight: 700;
}

.notice-actions {
  display: flex;
  gap: 8px;
}

.empty-tip {
  text-align: center;
  color: var(--muted);
  padding: 20px 12px;
}

.detail-title {
  font-size: 28px;
  line-height: 1.25;
}

.detail-body {
  font-size: 16px;
  line-height: 1.8;
  color: var(--ink);
  white-space: pre-wrap;
  word-break: break-word;
}

.detail-foot {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  flex-wrap: wrap;
  font-size: 12px;
  color: var(--muted);
  margin-top: 8px;
}

.priority-danger {
  border-color: rgba(239, 68, 68, 0.35);
}

.priority-danger .notice-title,
.priority-danger .notice-priority,
.priority-danger.detail-title {
  color: #b91c1c;
}

.priority-normal {
  border-color: var(--line);
}

textarea,
select,
input {
  width: 100%;
  padding: 10px 12px;
  border-radius: 12px;
  border: 1px solid var(--line);
  background: var(--panel);
}
</style>
