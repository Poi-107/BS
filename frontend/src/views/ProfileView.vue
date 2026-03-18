<template>
  <section class="panel profile-panel">
    <div class="panel-header">
      <h3>个人中心</h3>
      <div class="toolbar">
        <button class="btn" @click="loadProfile">刷新</button>
      </div>
    </div>

    <div class="profile-top">
      <section class="profile-section profile-avatar-section">
        <div class="profile-avatar-row">
          <div class="avatar-box sm">
            <img v-if="avatarUrl" :src="avatarUrl" alt="avatar" class="avatar-lg" @error="onAvatarError" />
            <div v-else class="avatar-placeholder">无头像</div>
          </div>
          <div class="profile-avatar-actions">
            <label class="btn ghost" for="avatar-file">选择头像</label>
            <input id="avatar-file" class="file-input" type="file" accept="image/*" @change="onFileChange" />
            <button class="btn" @click="uploadAvatar" :disabled="!avatarFile || uploading">
              {{ uploading ? "保存中..." : "保存头像" }}
            </button>
            <div class="profile-hint">支持 jpg/png，建议 300x300</div>
          </div>
        </div>
      </section>

      <section class="profile-section">
        <div class="profile-meta">
          <div>用户ID：{{ profile.id || '-' }}</div>
          <div>权限：{{ profile.per === 1 ? '管理员' : '普通用户' }}</div>
        </div>
      </section>
    </div>

    <section class="profile-section">
      <div class="form-stack">
        <label class="input">
          用户名
          <input v-model="form.username" :disabled="!editingInfo" placeholder="用户名" />
        </label>
        <label class="input">
          手机号码
          <input v-model="form.phone" :disabled="!editingInfo" placeholder="手机号" />
        </label>
        <label class="input">
          邮箱
          <input v-model="form.email" :disabled="!editingInfo" placeholder="邮箱" />
        </label>
        <label class="input">
          住址
          <input v-model="form.address" :disabled="!editingInfo" placeholder="住址" />
        </label>
      </div>
      <div class="toolbar" style="margin-top: 12px;">
        <button class="btn" v-if="!editingInfo" @click="startEditInfo">修改信息</button>
        <button class="btn ghost" v-if="editingInfo" @click="cancelEditInfo">取消</button>
        <button class="btn primary" v-if="editingInfo" @click="saveInfo" :disabled="saving">
          {{ saving ? "保存中..." : "保存信息" }}
        </button>
      </div>

      

      <div class="form-stack" v-if="editingPwd">
        <label class="input">
          新密码
          <input v-model="form.password" type="password" placeholder="新密码" />
        </label>
        <label class="input">
          确认密码
          <input v-model="form.confirm" type="password" placeholder="再次输入密码" />
        </label>
      </div>
      <div class="toolbar" style="margin-top: 12px;">
        <button class="btn" v-if="!editingPwd" @click="startEditPwd">修改密码</button>
        <button class="btn ghost" v-if="editingPwd" @click="cancelEditPwd">取消</button>
        <button class="btn primary" v-if="editingPwd" @click="savePwd" :disabled="saving">
          {{ saving ? "保存中..." : "保存密码" }}
        </button>
      </div>
    </section>

    <div v-if="error" class="login-error" style="margin-top: 12px;">{{ error }}</div>
  </section>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { apiGet, apiPost, apiUpload } from "../services/api";

const profile = ref({});
const avatarUrl = ref("");
const avatarFile = ref(null);
const avatarVersion = ref(Date.now());
const uploading = ref(false);
const saving = ref(false);
const error = ref("");
const editingInfo = ref(false);
const editingPwd = ref(false);

const form = ref({
  username: "",
  phone: "",
  email: "",
  address: "",
  password: "",
  confirm: ""
});

const API_BASE = import.meta.env.VITE_API_BASE || "http://localhost:8080";

function normalizeAvatar(url) {
  if (!url) return "";
  if (url.startsWith("http")) return url;
  if (url.startsWith("/uploads")) return `${API_BASE}${url}`;
  return url;
}

async function loadProfile() {
  error.value = "";
  try {
    const res = await apiGet("/bs/seluser2");
    profile.value = res.data || {};
    form.value.username = profile.value.username || "";
    form.value.phone = profile.value.phone || "";
    form.value.email = profile.value.email || "";
    form.value.address = profile.value.address || "";
    avatarVersion.value = Date.now();
    if (profile.value.avatar) {
      avatarUrl.value = normalizeAvatar(profile.value.avatar);
    } else {
      const av = await apiGet("/bs/selav");
      avatarUrl.value = normalizeAvatar(av.data || "");
    }
  } catch (e) {
    error.value = "加载个人信息失败";
  }
}

function onFileChange(e) {
  const file = e.target.files && e.target.files[0];
  avatarFile.value = file || null;
}

async function uploadAvatar() {
  if (!avatarFile.value) return;
  uploading.value = true;
  error.value = "";
  try {
    const formData = new FormData();
    formData.append("file", avatarFile.value);
    const res = await apiUpload("/bs/uploadAvatar", formData);
    if (res.code === "1" || res.code === 1) {
      avatarVersion.value = Date.now();
      avatarUrl.value = normalizeAvatar(res.data || avatarUrl.value);
      avatarFile.value = null;
    } else {
      error.value = res.message || "上传失败";
    }
  } catch (e) {
    error.value = "上传失败";
  } finally {
    uploading.value = false;
  }
}

function onAvatarError() {
  error.value = "头像加载失败，请确认后端静态资源映射";
}

function startEditInfo() {
  editingInfo.value = true;
}

function cancelEditInfo() {
  editingInfo.value = false;
  form.value.username = profile.value.username || "";
  form.value.phone = profile.value.phone || "";
  form.value.email = profile.value.email || "";
  form.value.address = profile.value.address || "";
}

async function saveInfo() {
  error.value = "";
  if (!form.value.username.trim()) {
    error.value = "请输入用户名";
    return;
  }
  saving.value = true;
  try {
    const payload = {
      id: profile.value.id,
      username: form.value.username.trim(),
      phone: form.value.phone.trim(),
      email: form.value.email.trim(),
      address: form.value.address.trim()
    };
    const res = await apiPost("/bs/upuser2", payload);
    if (res.code === "1" || res.code === 1) {
      await loadProfile();
      editingInfo.value = false;
    } else {
      error.value = res.message || "保存失败";
    }
  } catch (e) {
    error.value = "保存失败";
  } finally {
    saving.value = false;
  }
}

function startEditPwd() {
  editingPwd.value = true;
}

function cancelEditPwd() {
  editingPwd.value = false;
  form.value.password = "";
  form.value.confirm = "";
}

async function savePwd() {
  error.value = "";
  if (!form.value.password) {
    error.value = "请输入新密码";
    return;
  }
  if (form.value.password !== form.value.confirm) {
    error.value = "两次密码不一致";
    return;
  }
  saving.value = true;
  try {
    const payload = {
      id: profile.value.id,
      password: form.value.password
    };
    const res = await apiPost("/bs/upuser2", payload);
    if (res.code === "1" || res.code === 1) {
      editingPwd.value = false;
      form.value.password = "";
      form.value.confirm = "";
    } else {
      error.value = res.message || "保存失败";
    }
  } catch (e) {
    error.value = "保存失败";
  } finally {
    saving.value = false;
  }
}

onMounted(loadProfile);
</script>

