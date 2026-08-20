<!-- 不合格核查记录审核 -->
<template>
  <component :is="currentComponent" @enterView="enterView" :row="rowData" @back="backPage"></component>
</template>

<script setup lang="ts">
  import { ref } from 'vue';
  import { Page, ViewDetail } from './components';

  defineOptions({
    name: 'CheckRecordReview',
  });

  const currentComponent = shallowRef<any>(Page);

  const rowData = ref<any>({});

  // 进入详情页
  const enterView = (row: any) => {
    rowData.value = row;
    currentComponent.value = ViewDetail;
  };

  // 回到page页
  const backPage = () => {
    rowData.value = {};
    currentComponent.value = Page;
  };
</script>

<style lang="less" scoped></style>
