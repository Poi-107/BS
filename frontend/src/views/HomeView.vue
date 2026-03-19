<template>
  <section class="panel home-panel">
    <section class="hero">
      <h2>库存管理系统</h2>
      <p>已对接后端接口，支持库存台账、出入库单据、审核与日志实时加载。</p>
    </section>

    <div class="panel-header">
      <h3>数据统计</h3>
      <div class="toolbar">
        <button class="btn" @click="reload">刷新</button>
      </div>
    </div>

    <div class="stats-block">
      <div class="home-grid">
        <div class="stat-card">
          <h3>今日入库</h3>
          <strong>{{ statsSafe.jinruku }}</strong>
          <div class="stat-unit">单</div>
        </div>
        <div class="stat-card">
          <h3>今日出库</h3>
          <strong>{{ statsSafe.jinchuku }}</strong>
          <div class="stat-unit">单</div>
        </div>
        <div class="stat-card">
          <h3>今日销售</h3>
          <strong>{{ statsSafe.jinsale }}</strong>
          <div class="stat-unit">元</div>
        </div>
        <div class="stat-card">
          <h3>今日采购</h3>
          <strong>{{ statsSafe.jinpur }}</strong>
          <div class="stat-unit">元</div>
        </div>
        <div class="stat-card">
          <h3>本月入库</h3>
          <strong>{{ statsSafe.yueruku }}</strong>
          <div class="stat-unit">单</div>
        </div>
        <div class="stat-card">
          <h3>本月出库</h3>
          <strong>{{ statsSafe.yuechuku }}</strong>
          <div class="stat-unit">单</div>
        </div>
        <div class="stat-card">
          <h3>本月销售</h3>
          <strong>{{ statsSafe.yuesale }}</strong>
          <div class="stat-unit">元</div>
        </div>
        <div class="stat-card">
          <h3>本月采购</h3>
          <strong>{{ statsSafe.yuecpur }}</strong>
          <div class="stat-unit">元</div>
        </div>
        <div class="stat-card">
          <h3>年度销售</h3>
          <strong>{{ statsSafe.yearsale }}</strong>
          <div class="stat-unit">元</div>
        </div>
        <div class="stat-card">
          <h3>年度采购</h3>
          <strong>{{ statsSafe.yearpur }}</strong>
          <div class="stat-unit">元</div>
        </div>
      </div>

      <div class="chart-grid">
        <section class="chart-card" :class="{ 'is-open': activeChart === 'io' }" @click="openChart('io')">
          <div class="chart-head">
            <div class="chart-title">七日入库出库折线图</div>
            <div class="legend">
              <span class="dot" style="background:#2a9d8f"></span><span>入库</span>
              <span class="dot" style="background:#e76f51"></span><span>出库</span>
            </div>
          </div>
          <div class="line-wrap">
            <div class="y-axis">
              <span v-for="(v, i) in inOutYAxis" :key="`io-${i}-${v}`">{{ v }}</span>
            </div>
            <svg class="line-svg" viewBox="0 0 420 190" preserveAspectRatio="none">
              <line v-for="y in yGrid" :key="`io-grid-${y}`" class="grid-line" x1="24" :y1="y" x2="408" :y2="y" />
              <polyline class="line in" :points="inOutChart.inLine" />
              <polyline class="line out" :points="inOutChart.outLine" />
              <circle v-for="(p, i) in inOutChart.inPoints" :key="'in'+i" :cx="p.x" :cy="p.y" r="3" class="point in" />
              <circle v-for="(p, i) in inOutChart.outPoints" :key="'out'+i" :cx="p.x" :cy="p.y" r="3" class="point out" />
            </svg>
          </div>
          <div class="x-axis">
            <span v-for="d in dayLabels" :key="d">{{ d }}</span>
          </div>
        </section>

        <section class="chart-card" :class="{ 'is-open': activeChart === 'money' }" @click="openChart('money')">
          <div class="chart-head">
            <div class="chart-title">七日采购销售折线图</div>
            <div class="legend">
              <span class="dot" style="background:#3a86ff"></span><span>采购</span>
              <span class="dot" style="background:#9b5de5"></span><span>销售</span>
            </div>
          </div>
          <div class="line-wrap">
            <div class="y-axis">
              <span v-for="(v, i) in moneyYAxis" :key="`money-${i}-${v}`">{{ v }}</span>
            </div>
            <svg class="line-svg" viewBox="0 0 420 190" preserveAspectRatio="none">
              <line v-for="y in yGrid" :key="`money-grid-${y}`" class="grid-line" x1="24" :y1="y" x2="408" :y2="y" />
              <polyline class="line buy" :points="moneyChart.buyLine" />
              <polyline class="line sale" :points="moneyChart.saleLine" />
              <circle v-for="(p, i) in moneyChart.buyPoints" :key="'buy'+i" :cx="p.x" :cy="p.y" r="3" class="point buy" />
              <circle v-for="(p, i) in moneyChart.salePoints" :key="'sale'+i" :cx="p.x" :cy="p.y" r="3" class="point sale" />
            </svg>
          </div>
          <div class="x-axis">
            <span v-for="d in dayLabels" :key="d">{{ d }}</span>
          </div>
        </section>
      </div>

      <div class="category-grid">
        <section class="category-card" :class="{ 'is-open': activeChart === 'category' }" @click="openChart('category')">
          <div class="chart-head">
            <div class="chart-title">物品类别占比图</div>
            <div class="legend">总量 {{ categoryTotal }}</div>
          </div>
          <div class="category-wrap">
            <div class="donut" :style="{ background: categoryGradient }">
              <div class="donut-center">类别占比</div>
            </div>
            <div class="category-list">
              <div class="category-item" v-for="row in categoryRows" :key="row.name">
                <span class="dot" :style="{ background: row.color }"></span>
                <span class="category-name">{{ row.name }}</span>
                <span class="category-percent">{{ row.percent }}%</span>
                <span class="category-value">{{ row.value }}</span>
              </div>
            </div>
          </div>
        </section>

        <section class="category-card" :class="{ 'is-open': activeChart === 'amount' }" @click="openChart('amount')">
          <div class="chart-head">
            <div class="chart-title">物品金额占比图</div>
            <div class="legend">总额 {{ amountTotal }}</div>
          </div>
          <div class="category-wrap">
            <div class="donut" :style="{ background: amountGradient }">
              <div class="donut-center">金额占比</div>
            </div>
            <div class="category-list">
              <div class="category-item" v-for="row in amountRows" :key="'amount-' + row.name">
                <span class="dot" :style="{ background: row.color }"></span>
                <span class="category-name">{{ row.name }}</span>
                <span class="category-percent">{{ row.percent }}%</span>
                <span class="category-value">{{ row.value }}</span>
              </div>
            </div>
          </div>
        </section>
      </div>

    <div v-if="activeChart" class="chart-overlay" @click="closeChart">
      <div class="chart-modal" @click.stop>
        <div class="chart-head">
          <div class="chart-title">
            {{ activeChart === 'io' ? '七日入库出库折线图' : activeChart === 'money' ? '七日采购销售折线图' : activeChart === 'amount' ? '物品金额占比图' : '物品类别占比图' }}
          </div>
          <button class="btn ghost" @click="closeChart">关闭</button>
        </div>

        <div v-if="activeChart === 'category'" class="category-wrap">
          <div class="donut" :style="{ background: categoryGradient }">
            <div class="donut-center">类别占比</div>
          </div>
          <div class="category-list">
            <div class="category-item" v-for="row in categoryRows" :key="`pop-${row.name}`">
              <span class="dot" :style="{ background: row.color }"></span>
              <span class="category-name">{{ row.name }}</span>
              <span class="category-percent">{{ row.percent }}%</span>
              <span class="category-value">{{ row.value }}</span>
            </div>
          </div>
        </div>

        <div v-else-if="activeChart === 'amount'" class="category-wrap">
          <div class="donut" :style="{ background: amountGradient }">
            <div class="donut-center">金额占比</div>
          </div>
          <div class="category-list">
            <div class="category-item" v-for="row in amountRows" :key="'pop-amount-' + row.name">
              <span class="dot" :style="{ background: row.color }"></span>
              <span class="category-name">{{ row.name }}</span>
              <span class="category-percent">{{ row.percent }}%</span>
              <span class="category-value">{{ row.value }}</span>
            </div>
          </div>
        </div>

        <div v-else>
          <div class="line-wrap">
            <div class="y-axis">
              <span v-if="activeChart === 'io'" v-for="(v, i) in inOutYAxis" :key="`io-pop-${i}-${v}`">{{ v }}</span>
              <span v-else v-for="(v, i) in moneyYAxis" :key="`money-pop-${i}-${v}`">{{ v }}</span>
            </div>
            <svg class="line-svg large" viewBox="0 0 420 190" preserveAspectRatio="none">
              <line v-for="y in yGrid" :key="`pop-grid-${y}`" class="grid-line" x1="24" :y1="y" x2="408" :y2="y" />
              <template v-if="activeChart === 'io'">
                <polyline class="line in" :points="inOutChart.inLine" />
                <polyline class="line out" :points="inOutChart.outLine" />
                <circle v-for="(p, i) in inOutChart.inPoints" :key="`in-pop-${i}`" :cx="p.x" :cy="p.y" r="3.5" class="point in" />
                <circle v-for="(p, i) in inOutChart.outPoints" :key="`out-pop-${i}`" :cx="p.x" :cy="p.y" r="3.5" class="point out" />
              </template>
              <template v-else>
                <polyline class="line buy" :points="moneyChart.buyLine" />
                <polyline class="line sale" :points="moneyChart.saleLine" />
                <circle v-for="(p, i) in moneyChart.buyPoints" :key="`buy-pop-${i}`" :cx="p.x" :cy="p.y" r="3.5" class="point buy" />
                <circle v-for="(p, i) in moneyChart.salePoints" :key="`sale-pop-${i}`" :cx="p.x" :cy="p.y" r="3.5" class="point sale" />
              </template>
            </svg>
          </div>
          <div class="x-axis">
            <span v-for="d in dayLabels" :key="`pop-${d}`">{{ d }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</section>
</template>

<script setup>
import { computed, ref } from "vue";

const props = defineProps({
  stats: { type: Object, default: () => ({}) },
  rukuList: { type: Array, default: () => [] },
  chukuList: { type: Array, default: () => [] },
  inventory: { type: Array, default: () => [] }
});

const statsSafe = computed(() => ({
  jinruku: Number(props.stats.jinruku || 0),
  jinchuku: Number(props.stats.jinchuku || 0),
  jinsale: Number(props.stats.jinsale || 0),
  jinpur: Number(props.stats.jinpur || 0),
  yueruku: Number(props.stats.yueruku || 0),
  yuechuku: Number(props.stats.yuechuku || 0),
  yuesale: Number(props.stats.yuesale || 0),
  yuecpur: Number(props.stats.yuecpur || 0),
  yearsale: Number(props.stats.yearsale || 0),
  yearpur: Number(props.stats.yearpur || 0)
}));

function toDateKey(value) {
  if (!value) return "";
  const raw = String(value).replace(" ", "T");
  const d = new Date(raw);
  if (Number.isNaN(d.getTime())) return "";
  const y = d.getFullYear();
  const m = String(d.getMonth() + 1).padStart(2, "0");
  const day = String(d.getDate()).padStart(2, "0");
  return `${y}-${m}-${day}`;
}

function dateRange7() {
  const arr = [];
  const now = new Date();
  now.setHours(0, 0, 0, 0);
  for (let i = 6; i >= 0; i -= 1) {
    const d = new Date(now);
    d.setDate(now.getDate() - i);
    const y = d.getFullYear();
    const m = String(d.getMonth() + 1).padStart(2, "0");
    const day = String(d.getDate()).padStart(2, "0");
    arr.push(`${y}-${m}-${day}`);
  }
  return arr;
}

const dayKeys = computed(() => dateRange7());
const dayLabels = computed(() => dayKeys.value.map((k) => k.slice(5)));

const inDaily = computed(() => {
  const map = Object.fromEntries(dayKeys.value.map((k) => [k, 0]));
  (props.rukuList || []).forEach((row) => {
    const key = toDateKey(row.rktime || row.time);
    if (key in map) map[key] += 1;
  });
  return dayKeys.value.map((k) => map[k]);
});

const outDaily = computed(() => {
  const map = Object.fromEntries(dayKeys.value.map((k) => [k, 0]));
  (props.chukuList || []).forEach((row) => {
    const key = toDateKey(row.cktime || row.time);
    if (key in map) map[key] += 1;
  });
  return dayKeys.value.map((k) => map[k]);
});

const buyDaily = computed(() => {
  const map = Object.fromEntries(dayKeys.value.map((k) => [k, 0]));
  (props.rukuList || []).forEach((row) => {
    const key = toDateKey(row.rktime || row.time);
    const money = Number(row.money ?? (Number(row.price || 0) * Number(row.quantity || 0)));
    if (key in map) map[key] += Number.isFinite(money) ? money : 0;
  });
  return dayKeys.value.map((k) => map[k]);
});

const saleDaily = computed(() => {
  const map = Object.fromEntries(dayKeys.value.map((k) => [k, 0]));
  (props.chukuList || []).forEach((row) => {
    const key = toDateKey(row.cktime || row.time);
    const money = Number(row.money ?? (Number(row.price || 0) * Number(row.quantity || 0)));
    if (key in map) map[key] += Number.isFinite(money) ? money : 0;
  });
  return dayKeys.value.map((k) => map[k]);
});

function buildLine(seriesA, seriesB) {
  const w = 420;
  const h = 190;
  const pl = 24;
  const pr = 12;
  const pt = 14;
  const pb = 20;
  const maxVal = Math.max(...seriesA, ...seriesB, 1);
  const stepX = (w - pl - pr) / 6;

  const mapPoint = (val, idx) => {
    const x = pl + stepX * idx;
    const y = h - pb - ((val / maxVal) * (h - pt - pb));
    return { x: Number(x.toFixed(2)), y: Number(y.toFixed(2)) };
  };

  const aPts = seriesA.map((v, i) => mapPoint(v, i));
  const bPts = seriesB.map((v, i) => mapPoint(v, i));

  return {
    inPoints: aPts,
    outPoints: bPts,
    buyPoints: aPts,
    salePoints: bPts,
    inLine: aPts.map((p) => `${p.x},${p.y}`).join(" "),
    outLine: bPts.map((p) => `${p.x},${p.y}`).join(" "),
    buyLine: aPts.map((p) => `${p.x},${p.y}`).join(" "),
    saleLine: bPts.map((p) => `${p.x},${p.y}`).join(" ")
  };
}

const inOutChart = computed(() => buildLine(inDaily.value, outDaily.value));
const moneyChart = computed(() => buildLine(buyDaily.value, saleDaily.value));
const yGrid = [14, 53, 92, 131, 170];

function buildYAxis(maxVal) {
  const top = Math.max(1, Number(maxVal || 0));
  return [
    Math.round(top),
    Math.round(top * 0.75),
    Math.round(top * 0.5),
    Math.round(top * 0.25),
    0
  ];
}

const inOutYAxis = computed(() => buildYAxis(Math.max(...inDaily.value, ...outDaily.value, 1)));
const moneyYAxis = computed(() => buildYAxis(Math.max(...buyDaily.value, ...saleDaily.value, 1)));

const palette = ["#2a9d8f", "#3a86ff", "#e76f51", "#9b5de5", "#f4a261", "#264653", "#43aa8b", "#f28482"];

const categorySource = computed(() => {
  const map = {};
  (props.inventory || []).forEach((row) => {
    const raw = String(row.leibie ?? row.category ?? "").trim();
    const key = raw && raw !== "null" && raw !== "undefined" ? raw : "未分类";
    const qty = Number(row.quantity ?? row.num ?? 0);
    map[key] = (map[key] || 0) + (Number.isFinite(qty) ? qty : 0);
  });
  const onlyUnclassified = Object.keys(map).length === 1 && Object.prototype.hasOwnProperty.call(map, "未分类");
  if (Object.keys(map).length === 0 || onlyUnclassified) {
    const next = {};
    (props.rukuList || []).forEach((row) => {
      const raw = String(row.leibie ?? "").trim();
      const key = raw && raw !== "null" && raw !== "undefined" ? raw : "未分类";
      const qty = Number(row.quantity ?? 0);
      next[key] = (next[key] || 0) + (Number.isFinite(qty) ? qty : 0);
    });
    return next;
  }
  return map;
});

const categoryRows = computed(() => {
  const entries = Object.entries(categorySource.value)
    .map(([name, value]) => ({ name, value: Number(value || 0) }))
    .filter((row) => row.value > 0)
    .sort((a, b) => b.value - a.value)
    .slice(0, 8);
  const total = entries.reduce((sum, row) => sum + row.value, 0) || 1;
  return entries.map((row, idx) => ({
    ...row,
    color: palette[idx % palette.length],
    percent: ((row.value / total) * 100).toFixed(1)
  }));
});

const categoryTotal = computed(() => categoryRows.value.reduce((sum, row) => sum + row.value, 0));

const categoryGradient = computed(() => {
  if (categoryRows.value.length === 0) {
    return "conic-gradient(#d9e2ec 0 100%)";
  }
  let start = 0;
  const parts = categoryRows.value.map((row) => {
    const size = (row.value / categoryTotal.value) * 100;
    const end = start + size;
    const seg = `${row.color} ${start.toFixed(2)}% ${end.toFixed(2)}%`;
    start = end;
    return seg;
  });
  return `conic-gradient(${parts.join(", ")})`;
});

const amountSource = computed(() => {
  const map = {};
  (props.inventory || []).forEach((row) => {
    const key = String(row.leibie || row.category || "未分类").trim() || "未分类";
    const quantity = Number(row.quantity ?? row.num ?? 0);
    const money = Number(row.money ?? (Number(row.price || 0) * quantity));
    map[key] = (map[key] || 0) + (Number.isFinite(money) ? money : 0);
  });
  if (Object.keys(map).length === 0) {
    (props.rukuList || []).forEach((row) => {
      const key = String(row.leibie || "未分类").trim() || "未分类";
      const quantity = Number(row.quantity ?? 0);
      const money = Number(row.money ?? (Number(row.price || 0) * quantity));
      map[key] = (map[key] || 0) + (Number.isFinite(money) ? money : 0);
    });
  }
  return map;
});

const amountRows = computed(() => {
  const entries = Object.entries(amountSource.value)
    .map(([name, value]) => ({ name, value: Number(value || 0) }))
    .filter((row) => row.value > 0)
    .sort((a, b) => b.value - a.value)
    .slice(0, 8);
  const total = entries.reduce((sum, row) => sum + row.value, 0) || 1;
  return entries.map((row, idx) => ({
    ...row,
    color: palette[idx % palette.length],
    percent: ((row.value / total) * 100).toFixed(1)
  }));
});

const amountTotal = computed(() => amountRows.value.reduce((sum, row) => sum + row.value, 0));

const amountGradient = computed(() => {
  if (amountRows.value.length === 0) {
    return "conic-gradient(#d9e2ec 0 100%)";
  }
  let start = 0;
  const parts = amountRows.value.map((row) => {
    const size = (row.value / amountTotal.value) * 100;
    const end = start + size;
    const seg = row.color + " " + start.toFixed(2) + "% " + end.toFixed(2) + "%";
    start = end;
    return seg;
  });
  return "conic-gradient(" + parts.join(", ") + ")";
});

const activeChart = ref("");

function openChart(key) {
  activeChart.value = key;
}

function closeChart() {
  activeChart.value = "";
}

function reload() {
  window.location.reload();
}
</script>

<style scoped>
.stats-block {
  display: grid;
  gap: 18px;
}

.chart-grid {
  margin-top: 18px;
  display: grid;
  grid-template-columns: repeat(2, minmax(320px, 1fr));
  gap: 14px;
}

.chart-card {
  border: 1px solid var(--line);
  border-radius: 14px;
  background: var(--panel);
  padding: 14px;
  transition: transform 0.18s ease, box-shadow 0.18s ease;
  cursor: zoom-in;
}

.chart-card:hover {
  transform: scale(1.02);
  box-shadow: var(--soft-shadow);
}

.chart-card.is-open {
  transform: scale(1.02);
  box-shadow: var(--soft-shadow);
}

.category-card {
  margin-top: 14px;
  border: 1px solid var(--line);
  border-radius: 14px;
  background: var(--panel);
  padding: 14px;
  transition: transform 0.18s ease, box-shadow 0.18s ease;
  cursor: zoom-in;
  overflow: hidden;
  max-width: 100%;
}

.category-card:hover {
  transform: scale(1.02);
  box-shadow: var(--soft-shadow);
}

.category-card.is-open {
  transform: scale(1.02);
  box-shadow: var(--soft-shadow);
}

.category-grid {
  margin-top: 14px;
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14px;
}

.category-wrap {
  display: grid;
  grid-template-columns: minmax(140px, 180px) minmax(0, 1fr);
  gap: 14px;
  align-items: center;
  overflow: hidden;
  max-width: 100%;
}

.donut {
  width: min(140px, 46vw);
  height: min(140px, 46vw);
  max-width: 100%;
  max-height: 100%;
  border-radius: 50%;
  display: grid;
  place-items: center;
  margin: 0 auto;
}

.category-list {
  min-width: 0;
}

.category-item {
  display: grid;
  grid-template-columns: 12px 1fr auto auto;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  min-width: 0;
}

.category-name {
  color: var(--ink);
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.category-percent {
  color: var(--ink-2);
  min-width: 52px;
  text-align: right;
}

.category-value {
  color: var(--muted);
  min-width: 48px;
  text-align: right;
}

.chart-overlay {
  position: fixed;
  inset: 0;
  background: rgba(12, 14, 18, 0.35);
  backdrop-filter: blur(6px);
  display: grid;
  place-items: center;
  z-index: 50;
  overflow-y: auto;
  padding: 16px;
}

.chart-modal {
  width: min(900px, 92vw);
  background: var(--panel);
  border-radius: 16px;
  border: 1px solid var(--line);
  padding: 18px;
  box-shadow: var(--shadow);
  max-height: calc(100vh - 32px);
  overflow-y: auto;
}

.line-svg.large {
  height: 260px;
}

.chart-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.chart-title {
  font-weight: 700;
}

.legend {
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--muted);
  font-size: 12px;
}

.dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  display: inline-block;
}

.line-wrap {
  display: grid;
  grid-template-columns: 42px 1fr;
  gap: 8px;
  align-items: stretch;
}

.y-axis {
  height: 190px;
  display: grid;
  grid-template-rows: repeat(5, 1fr);
  align-items: center;
  justify-items: end;
  color: var(--muted);
  font-size: 11px;
}

.line-svg {
  width: 100%;
  height: 190px;
  background: linear-gradient(180deg, rgba(0, 0, 0, 0.02), rgba(0, 0, 0, 0));
  border-radius: 10px;
}

.line {
  fill: none;
  stroke-width: 2.5;
}

.grid-line {
  stroke: var(--line);
  stroke-width: 1;
  stroke-dasharray: 4 4;
  opacity: 0.9;
}

.line.in {
  stroke: #2a9d8f;
}

.line.out {
  stroke: #e76f51;
}

.line.buy {
  stroke: #3a86ff;
}

.line.sale {
  stroke: #9b5de5;
}

.point.in {
  fill: #2a9d8f;
}

.point.out {
  fill: #e76f51;
}

.point.buy {
  fill: #3a86ff;
}

.point.sale {
  fill: #9b5de5;
}

.x-axis {
  margin-top: 6px;
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  font-size: 12px;
  color: var(--muted);
  text-align: center;
}

@media (max-width: 1100px) {
  .chart-grid {
    grid-template-columns: 1fr;
  }

  .category-wrap {
    grid-template-columns: 1fr;
  }
}
</style>


