<template>
  <section class="panel">
    <div class="panel-header">
      <h3>入库管理</h3>
      <div class="toolbar">
        <select class="table-input" v-model="selectedLeibie" @change="applyFilter">
          <option value="all">全部类别</option>
          <option v-for="item in rukuCats" :key="item" :value="item">{{ item }}</option>
        </select>
        <input
          ref="fileInput"
          type="file"
          accept=".xlsx"
          style="display:none"
          @change="onFileChange"
        />
        <button class="btn" @click="reload">刷新</button>
        <button class="btn" @click="chooseFile">导入Excel</button>
        <button class="btn primary" @click="openAddModal">添加</button>
      </div>
    </div>

    <div class="toolbar" style="margin-bottom: 12px;">
      <input class="table-input" v-model="queryName" placeholder="按物料名称查询" />
      <button class="btn" @click="searchByName">查询</button>
      <input class="table-input" v-model="querySupplier" placeholder="按供应商查询" />
      <button class="btn" @click="searchBySupplier">查询</button>
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
            <th>供应商</th>
            <th>单价</th>
            <th>数量</th>
            <th>金额</th>
            <th>时间</th>
            <th>提交人</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in rukuList" :key="row.id">
            <td>{{ row.id }}</td>
            <td>{{ row.name }}</td>
            <td>{{ row.supplier }}</td>
            <td>{{ row.price }}</td>
            <td>{{ row.quantity }}</td>
            <td>{{ row.money }}</td>
            <td>{{ formatTime(row.rktime) }}</td>
            <td>{{ row.user }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="showAddModal" class="modal-mask">
      <div class="modal-card" style="width: min(560px, 92vw);">
        <div class="modal-title">新增入库单</div>
        <div class="modal-body">
          <div class="form-grid" style="margin-top: 6px;">
            <label class="input">
              物料名称
              <input v-model="form.name" placeholder="输入物料" />
            </label>
            <label class="input">
              供应商
              <select v-model="form.supplier" @change="onSupplierSelect">
                <option value="">请选择供应商</option>
                <option v-for="item in supplierOptions" :key="item" :value="item">{{ item }}</option>
                <option value="__new__">+ 新增供应商</option>
              </select>
            </label>
            <label class="input">
              类别
              <input v-model="form.leibie" list="ruku-cat-options-manage" placeholder="请选择或输入新类别" />
              <datalist id="ruku-cat-options-manage">
                <option v-for="item in rukuCats" :key="item" :value="item"></option>
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
          <button class="btn primary" @click="submit" :disabled="!isValid">提交入库</button>
        </div>
      </div>
    </div>

    <div v-if="showSupplierModal" class="modal-mask">
      <div class="modal-card">
        <div class="modal-title">新增供应商</div>
        <div class="modal-body">
          <div class="form-grid" style="margin-top: 6px;">
            <label class="input">
              名称
              <input v-model="supplierForm.name" placeholder="供应商名称" />
            </label>
            <label class="input">
              联系人
              <input v-model="supplierForm.contact" placeholder="联系人" />
            </label>
            <label class="input">
              电话
              <input v-model="supplierForm.phone" placeholder="电话" />
            </label>
            <label class="input">
              地址
              <input v-model="supplierForm.address" placeholder="地址" />
            </label>
            <label class="input">
              邮箱
              <input v-model="supplierForm.email" placeholder="邮箱" />
            </label>
          </div>
        </div>
        <div class="modal-actions">
          <button class="btn ghost" @click="closeSupplierModal">取消</button>
          <button class="btn primary" @click="confirmAddSupplier" :disabled="!supplierForm.name.trim()">保存供应商</button>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed, reactive, ref } from "vue";

const props = defineProps({
  rukuList: { type: Array, default: () => [] },
  rukuCats: { type: Array, default: () => [] },
  suppliers: { type: Array, default: () => [] }
});

const emit = defineEmits([
  "submit-inbound",
  "refresh-all",
  "filter-ruku",
  "add-supplier",
  "query-ruku-name",
  "query-ruku-supplier",
  "query-ruku-user",
  "reset-ruku-query",
  "import-inbound-excel"
]);

const form = reactive({
  name: "",
  supplier: "",
  leibie: "",
  quantity: 1,
  price: 0
});

const supplierForm = reactive({
  name: "",
  contact: "",
  phone: "",
  address: "",
  email: ""
});

const queryName = ref("");
const querySupplier = ref("");
const queryUser = ref("");
const extraSupplierNames = ref([]);
const showAddModal = ref(false);
const showSupplierModal = ref(false);
const selectedLeibie = ref("all");
const fileInput = ref(null);

const supplierOptions = computed(() => {
  const fromProps = (props.suppliers || [])
    .map((item) => (item?.name || "").trim())
    .filter((name) => name.length > 0);
  return Array.from(new Set([...fromProps, ...extraSupplierNames.value]));
});

const isValid = computed(() => {
  return (
    form.name.trim().length > 0 &&
    form.supplier.trim().length > 0 &&
    form.leibie.trim().length > 0 &&
    Number.isFinite(form.quantity) &&
    form.quantity > 0 &&
    Number.isFinite(form.price) &&
    form.price >= 0
  );
});

function applyFilter() {
  const value = selectedLeibie.value === "all" ? "" : selectedLeibie.value;
  emit("filter-ruku", value);
}

function searchByName() {
  emit("query-ruku-name", queryName.value.trim());
}

function searchBySupplier() {
  emit("query-ruku-supplier", querySupplier.value.trim());
}

function searchByUser() {
  emit("query-ruku-user", queryUser.value.trim());
}

function resetQuery() {
  queryName.value = "";
  querySupplier.value = "";
  queryUser.value = "";
  emit("reset-ruku-query");
}

function resetForm() {
  form.name = "";
  form.supplier = "";
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

function onSupplierSelect() {
  if (form.supplier === "__new__") {
    form.supplier = "";
    openSupplierModal();
  }
}

function openSupplierModal() {
  showSupplierModal.value = true;
}

function closeSupplierModal() {
  showSupplierModal.value = false;
  supplierForm.name = "";
  supplierForm.contact = "";
  supplierForm.phone = "";
  supplierForm.address = "";
  supplierForm.email = "";
}

function confirmAddSupplier() {
  const name = supplierForm.name.trim();
  if (!name) return;
  const payload = {
    name,
    contact: supplierForm.contact.trim(),
    phone: supplierForm.phone.trim(),
    address: supplierForm.address.trim(),
    email: supplierForm.email.trim()
  };
  emit("add-supplier", payload);
  if (!extraSupplierNames.value.includes(name)) {
    extraSupplierNames.value.push(name);
  }
  form.supplier = name;
  closeSupplierModal();
}

function submit() {
  if (!isValid.value) return;
  emit("submit-inbound", { ...form });
  closeAddModal();
}

function formatTime(value) {
  if (!value) return "";
  return String(value).replace("T", " ").slice(0, 19);
}

function reload() {
  window.location.reload();
}

function chooseFile() {
  if (fileInput.value) fileInput.value.click();
}

function onFileChange(e) {
  const file = e?.target?.files?.[0];
  if (!file) return;
  emit("import-inbound-excel", file);
  // allow selecting same file again
  e.target.value = "";
}
</script>
