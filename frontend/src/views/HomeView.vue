<template>
  <section class="panel home-panel">
    <div class="panel-header">
      <h3>数据统计</h3>
      <div class="toolbar">
        <button class="btn" @click="$emit('refresh-all')">刷新</button>
      </div>
    </div>

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

  </section>
</template>

<script setup>
import { computed } from "vue";

const props = defineProps({
  stats: { type: Object, default: () => ({}) }
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


</script>

