<template>
  <section class="panel">
    <div class="panel-header">
      <h3>出库管理</h3>
      <div class="toolbar">
        <select class="table-input" v-model="selectedLeibie" @change="applyFilter">
          <option value="all">全部类别</option>
          <option v-for="item in chukuCats" :key="item" :value="item">{{ item }}</option>
        </select>
        <button class="btn" @click="reload">刷新</button>
        <button class="btn primary" @click="openAddModal">添加</button>
      </div>
    </div>

    <div class="toolbar" style="margin-bottom: 12px;">
      <input class="table-input" v-model="queryName" placeholder="按物料名称查询" />
      <button class="btn" @click="searchByName">查询</button>
      <input class="table-input" v-model="queryClient" placeholder="按客户查询" />
      <button class="btn" @click="searchByClient">查询</button>
      <input class="table-input" v-model="queryUser" placeholder="按操作人查询" />
      <button class="btn" @click="searchByUser">查询</button>
      <button class="btn ghost" @click="resetQuery">重置</button>
    </div>

    <div class="table-wrap">
      <table class="table">
        <thead>
          <tr>
            <th>编号</th>
            <th>物料</th>
            <th>客户</th>
            <th>单价</th>
            <th>数量</th>
            <th>金额</th>
            <th>时间</th>
            <th>提交人</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in chukuList" :key="row.id">
            <td>{{ row.id }}</td>
            <td>{{ row.name }}</td>
            <td>{{ row.client }}</td>
            <td>{{ row.price }}</td>
            <td>{{ row.quantity }}</td>
            <td>{{ row.money }}</td>
            <td>{{ formatTime(row.cktime) }}</td>
            <td>{{ row.user }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="showAddModal" class="modal-mask">
      <div class="modal-card" style="width: min(560px, 92vw);">
        <div class="modal-title">新增出库单</div>
        <div class="modal-body">
          <div class="form-grid" style="margin-top: 6px;">
            <label class="input">
              物料名称
              <input v-model="form.name" placeholder="输入物料" />
            </label>
            <label class="input">
              客户
              <select v-model="form.client" @change="onClientSelect">
                <option value="">请选择客户</option>
                <option v-for="item in clientOptions" :key="item" :value="item">{{ item }}</option>
                <option value="__new__">+ 新增客户</option>
              </select>
            </label>
            <label class="input">
              类别
              <input v-model="form.leibie" list="chuku-cat-options-manage" placeholder="请选择或输入新类别" />
              <datalist id="chuku-cat-options-manage">
                <option v-for="item in chukuCats" :key="item" :value="item"></option>
              </datalist>
            </label>
            <label class="input">
              数量
              <input v-model.number="form.quantity" type="number" min="1" />
            </label>
            <label class="input">
              单价
              <input v-model.number="form.price" type="number" min="0" />
            </label>
          </div>
          <div class="form-hint" v-if="!isValid">请填写完整信息，数量 > 0</div>
        </div>
        <div class="modal-actions">
          <button class="btn ghost" @click="closeAddModal">取消</button>
          <button class="btn primary" @click="submit" :disabled="!isValid">提交出库</button>
        </div>
      </div>
    </div>

    <div v-if="showClientModal" class="modal-mask">
      <div class="modal-card">
        <div class="modal-title">新增客户</div>
        <div class="modal-body">
          <div class="form-grid" style="margin-top: 6px;">
            <label class="input">
              名称
              <input v-model="clientForm.name" placeholder="客户名称" />
            </label>
            <label class="input">
              联系人
              <input v-model="clientForm.contact" placeholder="联系人" />
            </label>
            <label class="input">
              电话
              <input v-model="clientForm.phone" placeholder="电话" />
            </label>
            <label class="input">
              地址
              <input v-model="clientForm.address" placeholder="地址" />
            </label>
            <label class="input">
              邮箱
              <input v-model="clientForm.email" placeholder="邮箱" />
            </label>
          </div>
        </div>
        <div class="modal-actions">
          <button class="btn ghost" @click="closeClientModal">取消</button>
          <button class="btn primary" @click="confirmAddClient" :disabled="!clientForm.name.trim()">保存客户</button>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed, reactive, ref } from "vue";

const props = defineProps({
  chukuList: { type: Array, default: () => [] },
  chukuCats: { type: Array, default: () => [] },
  clients: { type: Array, default: () => [] }
});

const emit = defineEmits([
  "submit-outbound",
  "refresh-all",
  "filter-chuku",
  "add-client",
  "query-chuku-name",
  "query-chuku-client",
  "query-chuku-user",
  "reset-chuku-query"
]);

const form = reactive({
  name: "",
  client: "",
  leibie: "",
  quantity: 1,
  price: 0
});

const clientForm = reactive({
  name: "",
  contact: "",
  phone: "",
  address: "",
  email: ""
});

const queryName = ref("");
const queryClient = ref("");
const queryUser = ref("");
const extraClientNames = ref([]);
const showAddModal = ref(false);
const showClientModal = ref(false);
const selectedLeibie = ref("all");

const clientOptions = computed(() => {
  const fromProps = (props.clients || [])
    .map((item) => (item?.name || "").trim())
    .filter((name) => name.length > 0);
  return Array.from(new Set([...fromProps, ...extraClientNames.value]));
});

const isValid = computed(() => {
  return (
    form.name.trim().length > 0 &&
    form.client.trim().length > 0 &&
    form.leibie.trim().length > 0 &&
    Number.isFinite(form.quantity) &&
    form.quantity > 0 &&
    Number.isFinite(form.price) &&
    form.price >= 0
  );
});

function applyFilter() {
  const value = selectedLeibie.value === "all" ? "" : selectedLeibie.value;
  emit("filter-chuku", value);
}

function searchByName() {
  emit("query-chuku-name", queryName.value.trim());
}

function searchByClient() {
  emit("query-chuku-client", queryClient.value.trim());
}

function searchByUser() {
  emit("query-chuku-user", queryUser.value.trim());
}

function resetQuery() {
  queryName.value = "";
  queryClient.value = "";
  queryUser.value = "";
  emit("reset-chuku-query");
}

function resetForm() {
  form.name = "";
  form.client = "";
  form.leibie = "";
  form.quantity = 1;
  form.price = 0;
}

function openAddModal() {
  showAddModal.value = true;
}

function closeAddModal() {
  showAddModal.value = false;
  resetForm();
}

function onClientSelect() {
  if (form.client === "__new__") {
    form.client = "";
    openClientModal();
  }
}

function openClientModal() {
  showClientModal.value = true;
}

function closeClientModal() {
  showClientModal.value = false;
  clientForm.name = "";
  clientForm.contact = "";
  clientForm.phone = "";
  clientForm.address = "";
  clientForm.email = "";
}

function confirmAddClient() {
  const name = clientForm.name.trim();
  if (!name) return;
  const payload = {
    name,
    contact: clientForm.contact.trim(),
    phone: clientForm.phone.trim(),
    address: clientForm.address.trim(),
    email: clientForm.email.trim()
  };
  emit("add-client", payload);
  if (!extraClientNames.value.includes(name)) {
    extraClientNames.value.push(name);
  }
  form.client = name;
  closeClientModal();
}

function submit() {
  if (!isValid.value) return;
  emit("submit-outbound", { ...form });
  closeAddModal();
}

function formatTime(value) {
  if (!value) return "";
  return String(value).replace("T", " ").slice(0, 19);
}

function reload() {
  window.location.reload();
}
</script>
