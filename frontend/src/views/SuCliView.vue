<template>
  <section class="panel">
    <div class="panel-header">
      <h3>供应商 / 客户</h3>
      <div class="toolbar">
        <button class="btn" :class="{ primary: activeTab === 'supplier' }" @click="setTab('supplier')">供应商</button>
        <button class="btn" :class="{ primary: activeTab === 'client' }" @click="setTab('client')">客户</button>
        <button class="btn" @click="openAdd">新增</button>
        <button class="btn ghost" @click="reload">刷新</button>
      </div>
    </div>

    <div class="toolbar" style="margin-bottom: 12px;">
      <input class="table-input" v-model="keyword" placeholder="输入名称查询" />
      <button class="btn" @click="search">查询</button>
      <button class="btn ghost" @click="reset">重置</button>
    </div>

    <div class="table-wrap">
      <table class="table sucli-table" v-if="activeTab === 'supplier'">
        <thead>
          <tr>
            <th>编号</th>
            <th>名称</th>
            <th>联系人</th>
            <th>电话</th>
            <th>地址</th>
            <th>邮箱</th>
            <th>创建时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in localSuppliers" :key="row.id">
            <td>{{ row.id }}</td>
            <td><template v-if="row._editing"><input class="table-input" v-model="row.name" /></template><template v-else>{{ row.name }}</template></td>
            <td><template v-if="row._editing"><input class="table-input" v-model="row.contact" /></template><template v-else>{{ row.contact }}</template></td>
            <td><template v-if="row._editing"><input class="table-input" v-model="row.phone" /></template><template v-else>{{ row.phone }}</template></td>
            <td><template v-if="row._editing"><input class="table-input" v-model="row.address" /></template><template v-else>{{ row.address }}</template></td>
            <td><template v-if="row._editing"><input class="table-input" v-model="row.email" /></template><template v-else>{{ row.email }}</template></td>
            <td>{{ formatTime(row.crtime) }}</td>
            <td>
              <button class="btn" v-if="!row._editing" @click="editRow(row)">修改</button>
              <button class="btn primary" style="margin-right: 8px;" v-if="row._editing" @click="saveSupplier(row)" :disabled="!isSupplierChanged(row)">保存</button>
              <button class="btn ghost" v-if="row._editing" @click="cancelEdit(row, 'supplier')">取消</button>
              <button class="btn danger" v-else @click="openDelete('supplier', row)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>

      <table class="table sucli-table" v-else>
        <thead>
          <tr>
            <th>编号</th>
            <th>名称</th>
            <th>联系人</th>
            <th>电话</th>
            <th>地址</th>
            <th>邮箱</th>
            <th>创建时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in localClients" :key="row.id">
            <td>{{ row.id }}</td>
            <td><template v-if="row._editing"><input class="table-input" v-model="row.name" /></template><template v-else>{{ row.name }}</template></td>
            <td><template v-if="row._editing"><input class="table-input" v-model="row.contact" /></template><template v-else>{{ row.contact }}</template></td>
            <td><template v-if="row._editing"><input class="table-input" v-model="row.phone" /></template><template v-else>{{ row.phone }}</template></td>
            <td><template v-if="row._editing"><input class="table-input" v-model="row.address" /></template><template v-else>{{ row.address }}</template></td>
            <td><template v-if="row._editing"><input class="table-input" v-model="row.email" /></template><template v-else>{{ row.email }}</template></td>
            <td>{{ formatTime(row.crtime) }}</td>
            <td>
              <button class="btn" v-if="!row._editing" @click="editRow(row)">修改</button>
              <button class="btn primary" style="margin-right: 8px;" v-if="row._editing" @click="saveClient(row)" :disabled="!isClientChanged(row)">保存</button>
              <button class="btn ghost" v-if="row._editing" @click="cancelEdit(row, 'client')">取消</button>
              <button class="btn danger" v-else @click="openDelete('client', row)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="showAdd" class="modal-mask">
      <div class="modal-card">
        <div class="modal-title">新增{{ activeTab === "supplier" ? "供应商" : "客户" }}</div>
        <div class="modal-body">
          <div class="form-grid" style="margin-top: 6px;">
            <label class="input">
              名称
              <input v-model="addForm.name" placeholder="名称" />
            </label>
            <label class="input">
              联系人
              <input v-model="addForm.contact" placeholder="联系人" />
            </label>
            <label class="input">
              电话
              <input v-model="addForm.phone" placeholder="电话" />
            </label>
            <label class="input">
              地址
              <input v-model="addForm.address" placeholder="地址" />
            </label>
            <label class="input">
              邮箱
              <input v-model="addForm.email" placeholder="邮箱" />
            </label>
          </div>
        </div>
        <div class="modal-actions">
          <button class="btn ghost" @click="closeAdd">取消</button>
          <button class="btn primary" @click="confirmAdd" :disabled="!addValid">确定</button>
        </div>
      </div>
    </div>

    <div v-if="showDelete" class="modal-mask">
      <div class="modal-card">
        <div class="modal-title">确认删除</div>
        <div class="modal-body">
          确定删除{{ deleteType === 'supplier' ? '供应商' : '客户' }} <strong>{{ deleteTarget?.name }}</strong> 吗？
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
import { computed, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";

const props = defineProps({
  suppliers: { type: Array, default: () => [] },
  clients: { type: Array, default: () => [] }
});

const route = useRoute();
const router = useRouter();

const emit = defineEmits([
  "search-supplier",
  "search-client",
  "add-supplier",
  "add-client",
  "update-supplier",
  "delete-supplier",
  "update-client",
  "delete-client"
]);

const activeTab = ref("supplier");
const keyword = ref("");
const showDelete = ref(false);
const deleteTarget = ref(null);
const deleteType = ref("supplier");
const showAdd = ref(false);
const addForm = ref({
  name: "",
  contact: "",
  phone: "",
  address: "",
  email: ""
});

const localSuppliers = ref([]);
const localClients = ref([]);

watch(
  () => props.suppliers,
  (list) => {
    localSuppliers.value = (list || []).map((item) => ({
      ...item,
      _editing: false,
      _origin: {
        name: item.name,
        contact: item.contact,
        phone: item.phone,
        address: item.address,
        email: item.email
      }
    }));
  },
  { immediate: true }
);

watch(
  () => props.clients,
  (list) => {
    localClients.value = (list || []).map((item) => ({
      ...item,
      _editing: false,
      _origin: {
        name: item.name,
        contact: item.contact,
        phone: item.phone,
        address: item.address,
        email: item.email
      }
    }));
  },
  { immediate: true }
);

const addValid = computed(() => {
  return addForm.value.name.trim().length > 0;
});

function setTab(tab) {
  activeTab.value = tab;
  const target = tab === "supplier" ? "/suppliers" : "/clients";
  if (route.path !== target) {
    router.replace(target);
  }
}

watch(
  () => route.path,
  (path) => {
    if (path === "/clients") {
      activeTab.value = "client";
    } else if (path === "/suppliers") {
      activeTab.value = "supplier";
    }
  },
  { immediate: true }
);

function isSupplierChanged(row) {
  return (
    row.name !== row._origin.name ||
    row.contact !== row._origin.contact ||
    row.phone !== row._origin.phone ||
    row.address !== row._origin.address ||
    row.email !== row._origin.email
  );
}

function isClientChanged(row) {
  return (
    row.name !== row._origin.name ||
    row.contact !== row._origin.contact ||
    row.phone !== row._origin.phone ||
    row.address !== row._origin.address ||
    row.email !== row._origin.email
  );
}

function editRow(row) {
  row._editing = true;
}

function cancelEdit(row, type) {
  row._editing = false;
  row.name = row._origin.name;
  row.contact = row._origin.contact;
  row.phone = row._origin.phone;
  row.address = row._origin.address;
  row.email = row._origin.email;
}

function saveSupplier(row) {
  emit("update-supplier", { ...row });
  row._editing = false;
}

function saveClient(row) {
  emit("update-client", { ...row });
  row._editing = false;
}

function search() {
  if (activeTab.value === "supplier") {
    emit("search-supplier", keyword.value.trim());
  } else {
    emit("search-client", keyword.value.trim());
  }
}

function reset() {
  keyword.value = "";
  if (activeTab.value === "supplier") {
    emit("search-supplier", "");
  } else {
    emit("search-client", "");
  }
}

function openAdd() {
  showAdd.value = true;
}

function closeAdd() {
  showAdd.value = false;
  addForm.value = {
    name: "",
    contact: "",
    phone: "",
    address: "",
    email: ""
  };
}

function confirmAdd() {
  if (!addValid.value) return;
  const payload = {
    name: addForm.value.name.trim(),
    contact: addForm.value.contact.trim(),
    phone: addForm.value.phone.trim(),
    address: addForm.value.address.trim(),
    email: addForm.value.email.trim()
  };
  if (activeTab.value === "supplier") {
    emit("add-supplier", payload);
  } else {
    emit("add-client", payload);
  }
  closeAdd();
}

function reload() {
  window.location.reload();
}

function openDelete(type, row) {
  deleteType.value = type;
  deleteTarget.value = row;
  showDelete.value = true;
}

function closeDelete() {
  showDelete.value = false;
  deleteTarget.value = null;
}

function confirmDelete() {
  if (!deleteTarget.value) return;
  if (deleteType.value === "supplier") {
    emit("delete-supplier", { id: deleteTarget.value.id });
  } else {
    emit("delete-client", { id: deleteTarget.value.id });
  }
  closeDelete();
}

function formatTime(value) {
  if (!value) return "";
  return String(value).replace("T", " ").slice(0, 19);
}
</script>


