<template>
  <div v-if="!isLoggedIn" class="login-shell">
    <div class="login-card login-animate">
      <div class="login-brand">
        <div class="brand-logo">IM</div>
        <div>
          <h2>库存管理系统</h2>
          <p>欢迎回来，请登录或注册</p>
        </div>
      </div>

      <div class="login-subline">
        <span class="pill">安全登录</span>
        <span class="pill">审批流</span>
        <span class="pill">日志追踪</span>
      </div>

      <div class="login-main">
        <div class="login-tabs">
          <button class="tab-btn" :class="{ active: !isRegister }" @click="isRegister = false">登录</button>
          <button class="tab-btn" :class="{ active: isRegister }" @click="isRegister = true">注册</button>
        </div>

        <Transition name="fade-slide" mode="out-in">
          <div class="login-form" v-if="!isRegister" :key="loginKey">
            <label class="input">
              用户名
              <input v-model="loginForm.username" placeholder="请输入用户名" name="login-username" autocomplete="username" />
            </label>
            <label class="input">
              密码
              <div class="password-row">
                <input v-model="loginForm.password" :type="showPassword ? 'text' : 'password'" placeholder="请输入密码" name="login-password" autocomplete="current-password" />
                <button class="ghost-link" type="button" @click="showPassword = !showPassword">
                  {{ showPassword ? "隐藏" : "显示" }}
                </button>
              </div>
            </label>
            <div class="login-row">
              <label class="check">
                <input type="checkbox" v-model="rememberMe" />
                记住账号
              </label>
              <span class="ghost-link">忘记密码</span>
            </div>

            <div v-if="loginError" class="login-error">{{ loginError }}</div>
            <button class="btn primary" @click="handleLogin" :disabled="loginLoading">
              {{ loginLoading ? "登录中..." : "登录" }}
            </button>
            <div class="login-tip">默认登录接口：/bs/login，token 写入 Header 的 token 字段</div>
          </div>

          <div class="login-form" v-else key="register">
            <label class="input">
              用户名
              <input v-model="registerForm.username" placeholder="请输入用户名" name="register-username" autocomplete="off" />
            </label>
            <label class="input">
              密码
              <input v-model="registerForm.password" :type="showPassword ? 'text' : 'password'" placeholder="请输入密码" name="register-password" autocomplete="new-password" />
            </label>
            <label class="input">
              确认密码
              <input v-model="registerForm.confirm" :type="showPassword ? 'text' : 'password'" placeholder="请再次输入密码" name="register-confirm" autocomplete="new-password" />
            </label>

            <div v-if="registerError" class="login-error">{{ registerError }}</div>
            <button class="btn primary" @click="handleRegister" :disabled="registerLoading">
              {{ registerLoading ? "注册中..." : "注册" }}
            </button>
            <div class="login-tip">注册接口：/bs/res</div>
          </div>
        </Transition>
      </div>
    </div>
  </div>

  <div v-else class="app-shell">
    <aside class="sidebar">
      <div class="brand">
        <div class="brand-logo">IM</div>
        <div>
          <h1>库存管理</h1>
          <div style="color: var(--muted); font-size: 12px;">Apple / Microsoft 风格 UI</div>
        </div>
      </div>

      <div class="nav-group">
        <RouterLink
          v-for="item in nav"
          :key="item.key"
          class="nav-item"
          :class="{ active: route.path === item.path }"
          :to="item.path"
        >
          <span>{{ item.label }}</span>
          <span style="color: var(--muted); font-size: 12px;">{{ item.badge }}</span>
        </RouterLink>
      </div>

      <div class="panel" style="padding: 14px;">
        <div style="font-size: 12px; color: var(--muted);">今日概览</div>
        <div style="margin-top: 8px; font-weight: 600;">待审单据 {{ auditPending.length }} 份</div>
        <div style="margin-top: 4px; color: var(--ink-2); font-size: 13px;">审核处理率 92%</div>
      </div>

      <button class="btn ghost" style="margin-top: auto;" @click="logout">退出登录</button>
    </aside>

    <main class="content">
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

      <RouterView
        :inventory="inventory"
        :ruku-list="rukuList"
        :chuku-list="chukuList"
        :audit-view="auditView"
        :audit-filter="auditFilter"
        :log-list="logList"
        @refresh-all="loadAll"
        @set-audit-filter="setAuditFilter"
        @submit-inbound="submitInbound"
        @submit-outbound="submitOutbound"
        @approve-audit="approve"
        @reject-audit="reject"
        @load-audit="loadAudit"
        @load-log="loadLog"
      />
    </main>
  </div>

  <div v-if="notice" class="toast" :class="notice.type">
    {{ notice.text }}
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from "vue";
import { useRoute, useRouter, RouterLink, RouterView } from "vue-router";
import { apiGet, apiPost, clearToken, getToken, setToken } from "./services/api";

const router = useRouter();
const route = useRoute();

const nav = ref([
  { key: "inventory", label: "库存台账", badge: "", path: "/inventory" },
  { key: "inbound", label: "入库管理", badge: "", path: "/inbound" },
  { key: "outbound", label: "出库管理", badge: "", path: "/outbound" },
  { key: "audit", label: "审核中心", badge: "", path: "/audit" },
  { key: "log", label: "操作日志", badge: "", path: "/log" }
]);

const inventory = ref([]);
const rukuList = ref([]);
const chukuList = ref([]);
const auditAll = ref([]);
const auditPending = ref([]);
const auditFilter = ref("all");
const logList = ref([]);

const loginForm = ref({ username: "", password: "" });
const loginError = ref("");
const loginLoading = ref(false);
const registerForm = ref({ username: "", password: "", confirm: "" });
const registerError = ref("");
const registerLoading = ref(false);
const isRegister = ref(false);
const showPassword = ref(false);
const rememberMe = ref(true);
const loginKey = ref(0);

const token = ref(getToken());
const isLoggedIn = computed(() => !!token.value);

const SAFE_STOCK = 50;

const notice = ref(null);
let noticeTimer = null;

function notify(type, text) {
  notice.value = { type, text };
  if (noticeTimer) {
    clearTimeout(noticeTimer);
  }
  noticeTimer = setTimeout(() => {
    notice.value = null;
  }, 2600);
}

const totalQuantity = computed(() =>
  inventory.value.reduce((sum, item) => sum + (item.quantity || 0), 0)
);

const lowStockCount = computed(() =>
  inventory.value.filter((item) => (item.quantity || 0) < (item.safe || SAFE_STOCK)).length
);

const auditView = computed(() => {
  if (auditFilter.value === "pending") {
    return auditAll.value.filter((item) => item.status === 0 || item.status === "0");
  }
  if (auditFilter.value === "done") {
    return auditAll.value.filter((item) => item.status !== 0 && item.status !== "0");
  }
  return auditAll.value;
});

function setAuditFilter(value) {
  auditFilter.value = value;
}

function normalizeInventory(list) {
  return (list || []).map((item) => ({
    id: item.id,
    name: item.name,
    quantity: item.quantity ?? 0,
    safe: SAFE_STOCK
  }));
}

function toTime(value) {
  if (!value) return 0;
  const text = String(value).replace("T", " ");
  const parsed = Date.parse(text);
  if (!Number.isNaN(parsed)) return parsed;
  if (typeof value === "number") return value;
  return 0;
}

function pickNewestTime(row) {
  return (
    toTime(row.time) ||
    toTime(row.rktime) ||
    toTime(row.cktime) ||
    toTime(row.reviewertime) ||
    toTime(row.createtime) ||
    toTime(row.addtime) ||
    toTime(row.applytime) ||
    toTime(row.updatetime) ||
    0
  );
}

function sortNewest(list) {
  return [...(list || [])].sort((a, b) => {
    const diff = pickNewestTime(b) - pickNewestTime(a);
    if (diff !== 0) return diff;
    return (Number(b.id) || 0) - (Number(a.id) || 0);
  });
}

function sortAudit(list) {
  return [...(list || [])].sort((a, b) => {
    const aPending = a.status === 0 || a.status === "0";
    const bPending = b.status === 0 || b.status === "0";
    if (aPending !== bPending) return aPending ? -1 : 1;
    const diff = pickNewestTime(b) - pickNewestTime(a);
    if (diff !== 0) return diff;
    return (Number(b.id) || 0) - (Number(a.id) || 0);
  });
}

async function loadInventory() {
  const res = await apiGet("/bs/selkucun");
  inventory.value = normalizeInventory(res.data || []);
}

async function loadRuku() {
  const res = await apiGet("/bs/selruku");
  rukuList.value = sortNewest(res.data || []);
}

async function loadChuku() {
  const res = await apiGet("/bs/selchuku");
  chukuList.value = sortNewest(res.data || []);
}

async function loadAudit() {
  const [allRes, pendingRes] = await Promise.all([
    apiGet("/bs/selaudit"),
    apiGet("/bs/sel0audit")
  ]);
  auditAll.value = sortAudit(allRes.data || []);
  auditPending.value = sortNewest(pendingRes.data || []);
}

async function loadLog() {
  const res = await apiGet("/bs/sellog");
  logList.value = sortNewest(res.data || []);
}

async function loadAll() {
  try {
    await Promise.all([loadInventory(), loadRuku(), loadChuku(), loadAudit(), loadLog()]);
    nav.value = [
      { key: "inventory", label: "库存台账", badge: String(inventory.value.length), path: "/inventory" },
      { key: "inbound", label: "入库管理", badge: `单据 ${rukuList.value.length}`, path: "/inbound" },
      { key: "outbound", label: "出库管理", badge: `单据 ${chukuList.value.length}`, path: "/outbound" },
      { key: "audit", label: "审核中心", badge: String(auditPending.value.length), path: "/audit" },
      { key: "log", label: "操作日志", badge: String(logList.value.length), path: "/log" }
    ];
  } catch (err) {
    notify("error", "数据加载失败，请检查后端服务");
  }
}

async function submitInbound(form) {
  const msg = validateInbound(form);
  if (msg) {
    notify("error", msg);
    return;
  }
  try {
    const payload = {
      name: form.name.trim(),
      supplier: form.supplier.trim(),
      quantity: Number(form.quantity),
      price: Number(form.price),
      money: (form.quantity || 0) * (form.price || 0)
    };
    await apiPost("/bs/addruku", payload);
    notify("success", "入库提交成功");
    await loadAll();
  } catch (err) {
    notify("error", "入库提交失败");
  }
}

async function submitOutbound(form) {
  const msg = validateOutbound(form);
  if (msg) {
    notify("error", msg);
    return;
  }
  try {
    const payload = {
      name: form.name.trim(),
      client: form.client.trim(),
      quantity: Number(form.quantity),
      price: Number(form.price),
      money: (form.quantity || 0) * (form.price || 0)
    };
    await apiPost("/bs/addchuku", payload);
    notify("success", "出库提交成功");
    await loadAll();
  } catch (err) {
    notify("error", "出库提交失败");
  }
}

function validateInbound(form) {
  if (!form || !form.name || !form.name.trim()) return "请输入物料名称";
  if (!form.supplier || !form.supplier.trim()) return "请输入供应商";
  if (!Number.isFinite(form.quantity) || form.quantity <= 0) return "数量必须大于 0";
  if (!Number.isFinite(form.price) || form.price < 0) return "单价不能小于 0";
  return "";
}

function validateOutbound(form) {
  if (!form || !form.name || !form.name.trim()) return "请输入物料名称";
  if (!form.client || !form.client.trim()) return "请输入客户";
  if (!Number.isFinite(form.quantity) || form.quantity <= 0) return "数量必须大于 0";
  if (!Number.isFinite(form.price) || form.price < 0) return "单价不能小于 0";
  return "";
}

async function approve(row) {
  if (row.status !== 0 && row.status !== "0") return;
  try {
    const remark = row.remark && String(row.remark).trim() ? String(row.remark).trim() : "已通过";
    await apiPost("/bs/upaudit", {
      id: row.id,
      status: 1,
      remark
    });
    notify("success", "已通过审核");
    await loadAudit();
  } catch (err) {
    notify("error", "审核操作失败");
  }
}

async function reject(row) {
  if (row.status !== 0 && row.status !== "0") return;
  try {
    const remark = row.remark && String(row.remark).trim() ? String(row.remark).trim() : "已驳回";
    await apiPost("/bs/upaudit", {
      id: row.id,
      status: 2,
      remark
    });
    notify("success", "已驳回申请");
    await loadAudit();
  } catch (err) {
    notify("error", "审核操作失败");
  }
}

async function handleLogin() {
  loginError.value = "";
  loginLoading.value = true;
  try {
    const res = await apiPost("/bs/login", {
      username: loginForm.value.username,
      password: loginForm.value.password
    });
    const ok = res.code === "1" || res.code === 1;
    if (ok && res.data) {
      setToken(res.data);
      token.value = getToken();
      loginForm.value = { username: "", password: "" };
      notify("success", "登录成功");
      await loadAll();
      if (route.path === "/" || route.path === "/login") {
        router.replace("/inventory");
      }
    } else if (ok && !res.data) {
      loginError.value = "登录成功但未返回 token";
      notify("error", loginError.value);
    } else {
      loginError.value = res.message || "登录失败";
      notify("error", loginError.value);
    }
  } catch (err) {
    if (err.message === "Not_Login") {
      loginError.value = "未登录或 token 失效";
    } else {
      loginError.value = "登录失败，请检查服务";
    }
    notify("error", loginError.value);
  } finally {
    loginLoading.value = false;
  }
}

async function handleRegister() {
  registerError.value = "";
  registerLoading.value = true;
  try {
    if (!registerForm.value.username.trim()) {
      registerError.value = "请输入用户名";
      return;
    }
    if (!registerForm.value.password.trim()) {
      registerError.value = "请输入密码";
      return;
    }
    if (registerForm.value.password !== registerForm.value.confirm) {
      registerError.value = "两次密码不一致";
      return;
    }
    const res = await apiPost("/bs/res", {
      username: registerForm.value.username.trim(),
      password: registerForm.value.password
    });
    const ok = res.code === "1" || res.code === 1;
    if (ok) {
      notify("success", "注册成功，请登录");
      isRegister.value = false;
      registerForm.value = { username: "", password: "", confirm: "" };
      loginForm.value = { username: "", password: "" };
      loginKey.value += 1;
    } else {
      registerError.value = res.message || "注册失败";
    }
  } catch (err) {
    registerError.value = "注册失败，请检查服务";
  } finally {
    registerLoading.value = false;
  }
}

function logout() {
  clearToken();
  token.value = "";
  notify("success", "已退出登录");
  router.replace("/login");
}

watch(token, (value) => {
  if (!value) {
    router.replace("/login");
  }
});

onMounted(() => {
  if (token.value) {
    loadAll().catch(() => {
      clearToken();
      token.value = "";
    });
  } else {
    router.replace("/login");
  }
});
</script>







