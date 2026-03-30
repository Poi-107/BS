<template>
  <section class="panel">
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
    <div class="panel-header">
      <h3>库存台账</h3>
      <div class="toolbar">
        <select class="table-input" v-model="selectedLeibie" @change="applyFilter">
          <option value="all">全部类别</option>
          <option v-for="item in kucunCats" :key="item" :value="item">{{ item }}</option>
        </select>
        <button class="btn" @click="reload">刷新</button>
      </div>
    </div>
    <div class="table-wrap">
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>条形码</th>
            <th>类别</th>
            <th>物料</th>
            <th>库存</th>
            <th>安全库存</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in inventory" :key="row.id">
            <td>{{ row.id }}</td>
            <td>
              <input v-if="editingId === row.id" class="table-input" v-model.trim="editForm.code" />
              <span v-else>{{ row.code }}</span>
            </td>
            <td>
              <input v-if="editingId === row.id" class="table-input" v-model.trim="editForm.leibie" />
              <span v-else>{{ row.leibie }}</span>
            </td>
            <td>
              <input v-if="editingId === row.id" class="table-input" v-model.trim="editForm.name" />
              <span v-else>{{ row.name }}</span>
            </td>
            <td>
              <input v-if="editingId === row.id" class="table-input" type="number" min="0" step="1" v-model.number="editForm.quantity" />
              <span v-else>{{ row.quantity }}</span>
            </td>
            <td>{{ row.safe }}</td>
            <td>
              <span class="badge" :class="row.quantity < row.safe ? 'warn' : 'ok'">
                {{ row.quantity < row.safe ? '低库存' : '正常' }}
              </span>
            </td>
            <td>
              <template v-if="editingId === row.id">
                <button class="btn" @click="saveRow(row)">保存</button>
                <button class="btn ghost" @click="cancelEdit">取消</button>
              </template>
              <button v-else class="btn" @click="startEdit(row)">修改</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import { ref } from "vue";


const emit = defineEmits(["refresh-all", "filter-kucun", "update-inventory"]);

const props = defineProps({
  inventory: { type: Array, default: () => [] },
  kucunCats: { type: Array, default: () => [] },
  totalQuantity: { type: Number, default: 0 },
  lowStockCount: { type: Number, default: 0 },
  rukuList: { type: Array, default: () => [] },
  chukuList: { type: Array, default: () => [] }
});


const selectedLeibie = ref("all");
const editingId = ref(null);
const editForm = ref({ id: null, code: "", leibie: "", name: "", quantity: 0 });

function startEdit(row) {
  editingId.value = row.id;
  editForm.value = {
    id: row.id,
    code: String(row.code || "").trim(),
    leibie: String(row.leibie || "").trim(),
    name: String(row.name || "").trim(),
    quantity: Number(row.quantity || 0)
  };
}

function cancelEdit() {
  editingId.value = null;
  editForm.value = { id: null, code: "", leibie: "", name: "", quantity: 0 };
}

function saveRow(row) {
  if (!editForm.value.code || !editForm.value.leibie || !editForm.value.name) {
    alert("条形码、类别、物料不能为空");
    return;
  }
  if (!Number.isFinite(Number(editForm.value.quantity)) || Number(editForm.value.quantity) < 0) {
    alert("库存数量必须是大于等于 0 的数字");
    return;
  }
  emit("update-inventory", {
    id: row.id,
    code: editForm.value.code,
    leibie: editForm.value.leibie,
    name: editForm.value.name,
    quantity: Number(editForm.value.quantity)
  });
  cancelEdit();
}

function applyFilter() {
  const value = selectedLeibie.value === "all" ? "" : selectedLeibie.value;
  emit("filter-kucun", value);
}

function reload() {
  window.location.reload();
}
</script>
