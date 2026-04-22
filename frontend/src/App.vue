<template>
  <div v-if="!isLoggedIn" class="login-shell">
    <div class="login-grid">
      <div class="login-side login-animate">
        <div class="login-orbit">
          <span class="orbit-ring ring-1"></span>
          <span class="orbit-ring ring-2"></span>
          <span class="orbit-ring ring-3"></span>
          <span class="scan-line"></span>
          <span class="spark spark-a"></span>
          <span class="spark spark-b"></span>
          <span class="spark spark-c"></span>
          <span class="orb orb-a"></span>
          <span class="orb orb-b"></span>
          <span class="orb orb-c"></span>
          <span class="orbit-core"></span>
        </div>
        <div class="login-wave"></div>
        <div class="login-hero">
          <h3 class="brand-headline">库存智能管理平台</h3>
          <p class="brand-sub">一体化覆盖入库、出库、审核与全链路追溯。</p>
        </div>
        <div class="brand-strip">
          <span>企业级稳定</span>
          <span>流程化审批</span>
          <span>分级权限</span>
        </div>
        <div class="brand-vision">
          <div class="vision-line">实时库存可视化</div>
          <div class="vision-line">操作链路透明可追踪</div>
          <div class="vision-line">数据驱动高效决策</div>
        </div>
        <div class="login-micro">
          <div>
            <div class="micro-title">数据状态</div>
            <div class="micro-value">实时同步</div>
          </div>
          <div>
            <div class="micro-title">响应体验</div>
            <div class="micro-value">低延迟</div>
          </div>
        </div>
      </div>
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
  </div>

  <div v-else class="app-shell">
    <aside class="sidebar">
      <div class="brand">
        <div class="brand-logo">IM</div>
        <div>
          <h1>库存管理</h1>
        </div>
      </div>

      <div class="nav-group">
        <template v-for="item in nav" :key="item.key">
          <div v-if="item.children" class="nav-parent" @click="toggleNav(item.key)">
            <span>{{ item.label }}</span>
            <span class="nav-caret">{{ navOpenState[item.key] ? "▾" : "▸" }}</span>
            <span v-if="item.badge" style="color: var(--muted); font-size: 12px;">{{ item.badge }}</span>
          </div>
          <div v-if="item.children && navOpenState[item.key]" class="nav-sub">
            <RouterLink
              v-for="child in item.children"
              :key="child.key"
              class="nav-item nav-child"
              :class="{ active: route.path === child.path }"
              :to="child.path"
            >
              <span>{{ child.label }}</span>
              <span v-if="child.badge" style="color: var(--muted); font-size: 12px;">{{ child.badge }}</span>
            </RouterLink>
          </div>
          <RouterLink
            v-else-if="!item.children"
            class="nav-item"
            :class="{ active: route.path === item.path }"
            :to="item.path"
          >
            <span>{{ item.label }}</span>
            <span v-if="item.badge" style="color: var(--muted); font-size: 12px;">{{ item.badge }}</span>
          </RouterLink>
        </template>
      </div>

      <div class="panel" style="padding: 14px;">
        <div style="font-size: 12px; color: var(--muted);">今日概览</div>
        <div style="margin-top: 8px; font-weight: 600;">待审单据 {{ auditPending.length }} 份</div>
        <div style="margin-top: 4px; color: var(--ink-2); font-size: 13px;">审核处理率 92%</div>
      </div>

      <button class="btn ghost" style="margin-top: auto;" @click="logout">退出登录</button>
    </aside>

    <main class="content">
      <RouterView
        :inventory="inventory"
        :ruku-list="rukuList"
        :ruku-mine="rukuMine"
        :chuku-list="chukuList"
        :chuku-mine="chukuMine"
        :audit-view="auditView"
        :audit-filter="auditFilter"
        :log-list="logList"
        :users="users"
        :current-user="currentUser"
        :suppliers="suppliers"
        :clients="clients"
        :current-per="currentPer"
        :ruku-cats="rukuCats"
        :chuku-cats="chukuCats"
        :kucun-cats="kucunCats"
        :audit-cats="auditCats"
        :xinxi-list="xinxiList"
        :stats="stats"
        :total-quantity="totalQuantity"
        :low-stock-count="lowStockCount"
        @refresh-all="loadAll"
        @set-audit-filter="setAuditFilter"
        @submit-inbound="submitInbound"
        @submit-outbound="submitOutbound"
        @approve-audit="approve"
        @reject-audit="reject"
        @load-audit="loadAudit"
        @load-log="loadLog"
        @load-users="loadUsers"
        @update-user="updateUser"
        @delete-user="deleteUser"
        @add-user="addUser"
        @search-supplier="loadSuppliers"
        @search-client="loadClients"
        @add-supplier="addSupplier"
        @add-client="addClient"
        @update-supplier="updateSupplier"
        @delete-supplier="deleteSupplier"
        @update-client="updateClient"
        @delete-client="deleteClient"
        @filter-ruku="filterRuku"
        @query-ruku-name="queryRukuByName"
        @query-ruku-supplier="queryRukuBySupplier"
        @query-ruku-user="queryRukuByUser"
        @reset-ruku-query="resetRukuQuery"
        @filter-chuku="filterChuku"
        @query-chuku-name="queryChukuByName"
        @query-chuku-client="queryChukuByClient"
        @query-chuku-user="queryChukuByUser"
        @reset-chuku-query="resetChukuQuery"
        @filter-kucun="filterKucun"
        @search-kucun-by-name="searchKucunByName"
        @search-kucun-by-code="searchKucunByCode"
        @update-inventory="updateInventory"
        @filter-audit="filterAudit"
        @import-inbound-excel="importInboundExcel"
        @add-xinxi="addXinxi"
        @update-xinxi="updateXinxi"
        @delete-xinxi="deleteXinxi"
      />
    </main>
  </div>

  <div v-if="importModalOpen" class="modal-mask" @click="closeImportModal">
    <div class="modal-card" style="width: min(820px, 92vw);" @click.stop>
      <div class="modal-title">Excel 导入失败明细</div>
      <div class="modal-body">
        <div style="display:flex; gap: 12px; flex-wrap: wrap; color: var(--muted); font-size: 12px;">
          <div>成功 {{ importSummary.success }} 条</div>
          <div>失败 {{ importSummary.failed }} 条</div>
        </div>

        <div v-if="importErrors.length === 0" class="form-hint" style="margin-top: 10px;">
          暂无错误明细
        </div>

        <div v-else class="table-wrap" style="margin-top: 12px; max-height: min(52vh, 520px); overflow:auto;">
          <table class="table">
            <thead>
              <tr>
                <th style="width: 90px;">行号</th>
                <th>原因</th>
                <th style="width: 140px;">填写金额</th>
                <th style="width: 140px;">应为金额</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(e, idx) in importErrors" :key="`imp-err-${idx}`">
                <td>{{ e.row }}</td>
                <td style="white-space: normal; word-break: break-all;">{{ e.message }}</td>
                <td>{{ e.givenMoney ?? "-" }}</td>
                <td>{{ e.expectedMoney ?? "-" }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="modal-actions">
        <button class="btn ghost" @click="closeImportModal">关闭</button>
      </div>
    </div>
  </div>

  <div v-if="notice" class="toast" :class="notice.type">
    {{ notice.text }}
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from "vue";
import { useRoute, useRouter, RouterLink, RouterView } from "vue-router";
import { apiGet, apiPost, clearToken, getToken, setToken } from "./services/api";
import { apiUpload } from "./services/api";

const router = useRouter();
const route = useRoute();

function readNavOpen(key, fallback = false) {
  const raw = localStorage.getItem(key);
  if (raw === null) return fallback;
  return raw === "1";
}

function writeNavOpen(key, value) {
  localStorage.setItem(key, value ? "1" : "0");
}

const currentUser = ref({});
const currentUsername = computed(() => currentUser.value.username || "");
const currentPer = computed(() => Number(currentUser.value.per ?? 0));
const nav = ref([]);
const navOpenState = ref({
  inbound: readNavOpen("bs_nav_inbound_open", false),
  outbound: readNavOpen("bs_nav_outbound_open", false),
  sucli: readNavOpen("bs_nav_sucli_open", false)
});

function toggleNav(key) {
  navOpenState.value[key] = !navOpenState.value[key];
  writeNavOpen(`bs_nav_${key}_open`, navOpenState.value[key]);
}


const inventory = ref([]);
const rukuList = ref([]);
const chukuList = ref([]);
const rukuMine = computed(() => rukuList.value.filter((item) => item.user === currentUsername.value));
const chukuMine = computed(() => chukuList.value.filter((item) => item.user === currentUsername.value));
const auditAll = ref([]);
const auditPending = ref([]);
const auditFilter = ref("all");
const logList = ref([]);
const users = ref([]);
const suppliers = ref([]);
const clients = ref([]);
const rukuCats = ref([]);
const chukuCats = ref([]);
const kucunCats = ref([]);
const auditCats = ref([]);
const xinxiList = ref([]);
const sucliBadge = computed(() => suppliers.value.length + clients.value.length);
const xinxiBadge = computed(() => String(xinxiList.value.length));

const stats = ref({
  jinruku: 0,
  jinchuku: 0,
  jinsale: 0,
  jinpur: 0,
  yueruku: 0,
  yuechuku: 0,
  yuesale: 0,
  yuecpur: 0,
  yearsale: 0,
  yearpur: 0
});

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

const importModalOpen = ref(false);
const importErrors = ref([]);
const importSummary = ref({ success: 0, failed: 0 });

function notify(type, text) {
  notice.value = { type, text };
  if (noticeTimer) {
    clearTimeout(noticeTimer);
  }
  noticeTimer = setTimeout(() => {
    notice.value = null;
  }, 2600);
}

function closeImportModal() {
  importModalOpen.value = false;
}

function isForbiddenError(err) {
  return !!(err && (err.status === 403 || err.isForbidden || err.message === "Forbidden" || err.message === "HTTP_403"));
}

function notifyQueryError(err, fallbackText) {
  if (isForbiddenError(err)) return;
  notify("error", fallbackText);
}

const totalQuantity = computed(() =>
  inventory.value.reduce((sum, item) => sum + (item.quantity || 0), 0)
);

const lowStockCount = computed(() =>
  inventory.value.filter((item) => (item.quantity || 0) <= (item.safe || SAFE_STOCK)).length
);

function buildNav(per) {
  const name = currentUsername.value || "";
  const rukuCount = name ? rukuMine.value.length : rukuList.value.length;
  const chukuCount = name ? chukuMine.value.length : chukuList.value.length;

  const allNavs = [
    { key: "home", label: "首页", path: "/home", per: [0, 1, 2] },
    { key: "inventory", label: "库存", path: "/inventory", badge: () => String(inventory.value.length), per: [0, 1, 2] },
    {
      key: "inbound",
      label: "入库",
      per: [0, 1, 2],
      children: [
        { key: "inbound-my", label: "入库", path: "/inbound-my", badge: () => `单据 ${rukuCount}`, per: [0, 1, 2] },
        { key: "inbound-manage", label: "入库管理", path: "/inbound", badge: () => `单据 ${rukuList.value.length}`, per: [1, 2] }
      ]
    },
    {
      key: "outbound",
      label: "出库",
      per: [0, 1, 2],
      children: [
        { key: "outbound-my", label: "出库", path: "/outbound-my", badge: () => `单据 ${chukuCount}`, per: [0, 1, 2] },
        { key: "outbound-manage", label: "出库管理", path: "/outbound", badge: () => `单据 ${chukuList.value.length}`, per: [1, 2] }
      ]
    },
    { key: "audit", label: "审核", path: "/audit", badge: () => String(auditPending.value.length), per: [1, 2] },
    {
      key: "sucli",
      label: "供应商 / 客户",
      per: [0, 1, 2],
      children: [
        { key: "suppliers", label: "供应商管理", path: "/suppliers", per: [0, 1, 2] },
        { key: "clients", label: "客户管理", path: "/clients", per: [0, 1, 2] }
      ]
    },
    { key: "users", label: "用户管理", path: "/users", badge: () => String(users.value.length), per: [1, 2] },
    { key: "log", label: "操作日志", path: "/log", badge: () => String(logList.value.length), per: [2] },
    { key: "xinxi", label: "消息通知", path: "/xinxi", badge: () => xinxiBadge.value, per: [0, 1, 2] },
    { key: "profile", label: "个人中心", path: "/profile", per: [0, 1, 2] }
  ];

  return allNavs
    .filter(item => item.per.includes(per))
    .map(item => {
      const badge = typeof item.badge === 'function' ? item.badge() : item.badge;
      if (item.children) {
        const visibleChildren = item.children.filter(child => child.per.includes(per));
        if (visibleChildren.length === 0) return null;
        return {
          ...item,
          badge,
          children: visibleChildren.map(child => ({...child, badge: typeof child.badge === 'function' ? child.badge() : child.badge}))
        };
      }
      return { ...item, badge };
    })
    .filter(Boolean);
}

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
    code: item.code,
    leibie: item.leibie,
    id: item.id,
    name: item.name,
    quantity: item.quantity ?? 0,
    safe: item.safe ?? SAFE_STOCK,
    warning: !!item.warning
  }));
}

function toNumber(value) {
  const num = Number(value);
  return Number.isFinite(num) ? num : 0;
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

function sortByIdAsc(list) {
  return [...(list || [])].sort((a, b) => (Number(a.id) || 0) - (Number(b.id) || 0));
}

async function loadInventory(leibie) {
  const path = leibie ? `/bs/selkucun1?leibie=${encodeURIComponent(leibie)}` : "/bs/selkucun";
  const res = await apiGet(path);

  inventory.value = normalizeInventory(res.data || []);

}

async function loadRuku(leibie) {
  const path = leibie ? `/bs/selruku1?leibie=${encodeURIComponent(leibie)}` : "/bs/selruku";
  const res = await apiGet(path);
  rukuList.value = sortNewest(res.data || []);
}

async function loadChuku(leibie) {
  const path = leibie ? `/bs/selchuku1?leibie=${encodeURIComponent(leibie)}` : "/bs/selchuku";
  const res = await apiGet(path);
  chukuList.value = sortNewest(res.data || []);
}

async function loadAudit(leibie) {
  if (leibie) {
    const allRes = await apiGet(`/bs/selaudit1?leibie=${encodeURIComponent(leibie)}`);
    auditAll.value = sortAudit(allRes.data || []);
    auditPending.value = auditAll.value.filter((item) => item.status === 0 || item.status === "0");
    return;
  }
  const [allRes, pendingRes] = await Promise.all([
    apiGet("/bs/selaudit"),
    apiGet("/bs/sel0audit")
  ]);
  auditAll.value = sortAudit(allRes.data || []);
  auditPending.value = sortNewest(pendingRes.data || []);
}

async function loadLog() {
  try {
    const res = await apiGet("/bs/sellog");
    logList.value = sortNewest(res.data || []);
  } catch {
    // 权限不足，静默忽略
  }
}

async function loadUsers() {
  const res = await apiGet("/bs/seluser");
  users.value = sortByIdAsc(res.data || []);
}

async function loadCurrentUser() {
  const res = await apiGet("/bs/seluser2");
  currentUser.value = res.data || {};
}

async function loadRukuCats() {
  const res = await apiGet("/bs/selleibie3");
  rukuCats.value = res.data || [];
}

async function loadChukuCats() {
  const res = await apiGet("/bs/selleibie1");
  chukuCats.value = res.data || [];
}

async function loadKucunCats() {
  const res = await apiGet("/bs/selleibie2");
  kucunCats.value = res.data || [];
}

async function loadAuditCats() {
  const res = await apiGet("/bs/selleibie");
  auditCats.value = res.data || [];
}

async function loadXinxi() {
  const res = await apiGet("/bs/selxinxi");
  xinxiList.value = sortNewest(res.data || []);
}
function filterRuku(leibie) {
  loadRuku(leibie || "").catch((err) => {
    notifyQueryError(err, "入库分类查询失败");
  });
}

function filterChuku(leibie) {
  loadChuku(leibie || "").catch((err) => {
    notifyQueryError(err, "出库分类查询失败");
  });
}

function filterKucun(leibie) {
  loadInventory(leibie || "").catch((err) => {
    notifyQueryError(err, "库存分类查询失败");
  });
}

async function searchKucunByName(name) {
  const keyword = String(name || "").trim();
  if (!keyword) {
    await loadInventory();
    return;
  }
  const res = await apiGet(`/bs/selname?name=${encodeURIComponent(keyword)}`);
  inventory.value = res.data ? [res.data] : [];
}

async function searchKucunByCode(code) {
  const keyword = String(code || "").trim();
  if (!keyword) {
    await loadInventory();
    return;
  }
  const res = await apiGet(`/bs/selcode?code=${encodeURIComponent(keyword)}`);
  inventory.value = res.data ? [res.data] : [];
}

function filterAudit(leibie) {
  loadAudit(leibie || "").catch((err) => {
    notifyQueryError(err, "审核分类查询失败");
  });
}

async function queryRukuByName(name) {
  const keyword = String(name || "").trim();
  if (!keyword) {
    await loadRuku();
    return;
  }
  const res = await apiGet(`/bs/selruku3?name=${encodeURIComponent(keyword)}`);
  rukuList.value = sortNewest(res.data || []);
}

async function queryRukuBySupplier(supplier) {
  const keyword = String(supplier || "").trim();
  if (!keyword) {
    await loadRuku();
    return;
  }
  const res = await apiGet(`/bs/selruku4?supplier=${encodeURIComponent(keyword)}`);
  rukuList.value = sortNewest(res.data || []);
}

async function queryRukuByUser(user) {
  const keyword = String(user || "").trim();
  if (!keyword) {
    await loadRuku();
    return;
  }
  const res = await apiGet(`/bs/selruku5?user=${encodeURIComponent(keyword)}`);
  rukuList.value = sortNewest(res.data || []);
}

function resetRukuQuery() {
  loadRuku().catch((err) => {
    notifyQueryError(err, "重置入库查询失败");
  });
}

async function queryChukuByName(name) {
  const keyword = String(name || "").trim();
  if (!keyword) {
    await loadChuku();
    return;
  }
  const res = await apiGet(`/bs/selchuku3?name=${encodeURIComponent(keyword)}`);
  chukuList.value = sortNewest(res.data || []);
}

async function queryChukuByClient(client) {
  const keyword = String(client || "").trim();
  if (!keyword) {
    await loadChuku();
    return;
  }
  const res = await apiGet(`/bs/selchuku4?client=${encodeURIComponent(keyword)}`);
  chukuList.value = sortNewest(res.data || []);
}

async function queryChukuByUser(user) {
  const keyword = String(user || "").trim();
  if (!keyword) {
    await loadChuku();
    return;
  }
  const res = await apiGet(`/bs/selchuku5?user=${encodeURIComponent(keyword)}`);
  chukuList.value = sortNewest(res.data || []);
}

function resetChukuQuery() {
  loadChuku().catch((err) => {
    notifyQueryError(err, "重置出库查询失败");
  });
}

async function loadSuppliers(name) {
  const path = name ? `/bs/selsu2?name=${encodeURIComponent(name)}` : "/bs/selsu";
  const res = await apiGet(path);
  suppliers.value = sortByIdAsc(res.data || []);
}

async function loadClients(name) {
  const path = name ? `/bs/selcli2?name=${encodeURIComponent(name)}` : "/bs/selcli";
  const res = await apiGet(path);
  clients.value = sortByIdAsc(res.data || []);
}

async function loadStats() {
  const [
    jinruku,
    jinchuku,
    jinsale,
    jinpur,
    yueruku,
    yuechuku,
    yuesale,
    yuecpur,
    yearsale,
    yearpur
  ] = await Promise.all([
    apiGet("/bs/jinruku"),
    apiGet("/bs/jinchuku"),
    apiGet("/bs/jinsale"),
    apiGet("/bs/jinpur"),
    apiGet("/bs/yueruku"),
    apiGet("/bs/yuechuku"),
    apiGet("/bs/yuesale"),
    apiGet("/bs/yuecpur"),
    apiGet("/bs/yearsale"),
    apiGet("/bs/yearpur")
  ]);
  stats.value = {
    jinruku: toNumber(jinruku.data),
    jinchuku: toNumber(jinchuku.data),
    jinsale: toNumber(jinsale.data),
    jinpur: toNumber(jinpur.data),
    yueruku: toNumber(yueruku.data),
    yuechuku: toNumber(yuechuku.data),
    yuesale: toNumber(yuesale.data),
    yuecpur: toNumber(yuecpur.data),
    yearsale: toNumber(yearsale.data),
    yearpur: toNumber(yearpur.data)
  };
}

async function loadAll() {
  try {
    await Promise.all([loadInventory(), loadRuku(), loadChuku(), loadAudit(), loadUsers(), loadCurrentUser(), loadSuppliers(), loadClients(), loadRukuCats(), loadChukuCats(), loadKucunCats(), loadAuditCats(), loadXinxi(), loadStats()]);
    nav.value = buildNav(currentPer.value);
    // 只有权限足够才加载日志
    if (currentPer.value >= 2) {
      await loadLog();
    }
  } catch (err) {
    notifyQueryError(err, "数据加载失败，请检查后端服务");
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
        code: (form.code || "").trim(),
      name: form.name.trim(),
      supplier: form.supplier.trim(),
      leibie: form.leibie.trim(),
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

async function importInboundExcel(file) {
  if (!file) return;
  try {
    const formData = new FormData();
    formData.append("file", file);
    const res = await apiUpload("/bs/ruku/import", formData);
    const data = res?.data || {};
    const success = Number(data.success || 0);
    const failed = Number(data.failed || 0);
    if (failed > 0) {
      notify("error", `导入完成：成功 ${success}，失败 ${failed}`);
      importSummary.value = { success, failed };
      importErrors.value = Array.isArray(data.errors) ? data.errors : [];
      importModalOpen.value = true;
    } else {
      notify("success", `导入成功：共 ${success} 条`);
      importSummary.value = { success, failed: 0 };
      importErrors.value = [];
    }
    await loadAll();
  } catch (err) {
    notify("error", "Excel 导入失败");
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
      code: (form.code || "").trim(),
      name: form.name.trim(),
      client: form.client.trim(),
      leibie: form.leibie.trim(),
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
  if (!form.leibie || !form.leibie.trim()) return "请选择类别";
  if (!Number.isFinite(form.quantity) || form.quantity <= 0) return "数量必须大于 0";
  if (!Number.isFinite(form.price) || form.price < 0) return "单价不能小于 0";
  return "";
}

function validateOutbound(form) {
  if (!form || !form.name || !form.name.trim()) return "请输入物料名称";
  if (!form.client || !form.client.trim()) return "请输入客户";
  if (!form.leibie || !form.leibie.trim()) return "请选择类别";
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

async function updateInventory(row) {
  try {
    await apiPost("/bs/upkucun", {
      id: row.id,
      code: row.code,
      leibie: row.leibie,
      name: row.name,
      quantity: Number(row.quantity)
    });
    notify("success", "库存信息已更新");
    await Promise.all([loadInventory(), loadKucunCats()]);
  } catch (err) {
    notify("error", "库存更新失败");
  }
}

async function updateUser(user) {
  try {
    await apiPost("/bs/upuser", {
      id: user.id,
      per: user.per
    });
    notify("success", "用户权限已更新");
    await loadUsers();
  } catch (err) {
    notify("error", "用户更新失败");
  }
}

async function deleteUser(user) {
  try {
    await apiPost("/bs/deluser", { id: user.id });
    notify("success", "用户已删除");
    await loadUsers();
  } catch (err) {
    notify("error", "删除失败");
  }
}

async function addUser(payload) {
  try {
    const res = await apiPost("/bs/res", {
      username: payload.username,
      password: payload.password
    });
    const ok = res.code === "1" || res.code === 1;
    if (ok) {
      notify("success", "用户添加成功");
      await loadUsers();
    } else {
      notify("error", res.message || "添加用户失败");
    }
  } catch (err) {
    notify("error", "添加用户失败，请检查服务");
  }
}

async function updateSupplier(row) {
  try {
    await apiPost("/bs/upsu", {
      id: row.id,
      name: row.name,
      contact: row.contact,
      phone: row.phone,
      address: row.address,
      email: row.email
    });
    notify("success", "供应商已更新");
    await loadSuppliers();
  } catch (err) {
    notify("error", "供应商更新失败");
  }
}

async function deleteSupplier(row) {
  try {
    await apiPost("/bs/delsu", { id: row.id });
    notify("success", "供应商已删除");
    await loadSuppliers();
  } catch (err) {
    notify("error", "删除失败");
  }
}

async function addSupplier(payload) {
  try {
    await apiPost("/bs/addsu", payload);
    notify("success", "供应商已新增");
    await loadSuppliers();
  } catch (err) {
    notify("error", "新增失败");
  }
}

async function updateClient(row) {
  try {
    await apiPost("/bs/upcli", {
      id: row.id,
      name: row.name,
      contact: row.contact,
      phone: row.phone,
      address: row.address,
      email: row.email
    });
    notify("success", "客户已更新");
    await loadClients();
  } catch (err) {
    notify("error", "客户更新失败");
  }
}

async function deleteClient(row) {
  try {
    await apiPost("/bs/delcli", { id: row.id });
    notify("success", "客户已删除");
    await loadClients();
  } catch (err) {
    notify("error", "删除失败");
  }
}

async function addClient(payload) {
  try {
    await apiPost("/bs/addcli", payload);
    notify("success", "客户已新增");
    await loadClients();
  } catch (err) {
    notify("error", "新增失败");
  }
}

async function addXinxi(payload) {
  try {
    await apiPost("/bs/addxinxi", payload);
    notify("success", "通知发布成功");
    await loadXinxi();
  } catch (err) {
    notify("error", "通知发布失败");
  }
}

async function updateXinxi(payload) {
  try {
    await apiPost("/bs/upxinxi", payload);
    notify("success", "通知更新成功");
    await loadXinxi();
  } catch (err) {
    notify("error", "通知更新失败");
  }
}

async function deleteXinxi(id) {
  try {
    await apiPost("/bs/delxinxi", Number(id));
    notify("success", "通知删除成功");
    await loadXinxi();
  } catch (err) {
    notify("error", "通知删除失败");
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
        router.replace("/home");
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

watch(currentUsername, () => {
  nav.value = buildNav(currentPer.value);
});

watch(currentPer, () => {
  nav.value = buildNav(currentPer.value);
});

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


















































































