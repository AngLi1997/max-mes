<template>
  <div class="chart_box">
    <div class="content">
      <EchartBoxItem v-for="item in boardList" :key="item.id" :data="item" />
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
    boardList.value = data;
  });
</script>
<style lang="less">
  .dc-content {
    padding: 0px !important;
  }
</style>
<style lang="less" scoped>
  .chart_box {
    height: 100%;
    position: relative;
    background: radial-gradient(61.36% 50% at 50% 50%, #334466 0%, #292c33 100%);
    .content {
      display: flex;
      justify-content: space-between;
      flex-wrap: wrap;
      height: 100%;
      padding: 0 30px 20px;
      overflow-y: auto;
    }
    .footer {
      width: 100%;
      height: 20px;
      position: absolute;
      left: 0;
      bottom: 0;
      background-image: url('/src/assets/img/footer.png');
      background-size: 100% 100%;
      background-color: #292c33;
    }
  }
</style>
