<template>
  <div class="chart_box">
    <div class="content">
      <EchartBoxItem v-for="item in boardList" :key="item.id" isShow :data="item" />
    </div>
    <div class="footer"></div>
  </div>
</template>
<script setup lang="ts">
  import EchartBoxItem from './component/EchartBoxItem.vue';
  import { queryDashboardListAll } from '@/services';

  const boardList = ref<any>([]);

  onMounted(async () => {
    const { data } = await queryDashboardListAll();
    if (data.length < 2) {
      boardList.value = data;
    } else {
      boardList.value = [data[1], data[2]];
    }
  });
</script>
<style lang="less">
  .dc-layout {
    background: rgba(0, 0, 0, 0) !important;
  }
  .dc-content {
    background: rgba(0, 0, 0, 0);
    padding: 0 !important;
  }
  .chart_box {
    position: relative;
    .content {
      display: flex;
      flex-direction: column;
      overflow-y: auto;
    }
  }
</style>
