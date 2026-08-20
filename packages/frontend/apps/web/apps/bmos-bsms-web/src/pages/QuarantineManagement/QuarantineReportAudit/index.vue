<!-- 检疫期报告审核 -->
<template>
  <component :is="currentComponent" @enterView="enterView" :id="rowId" @back="backPage"></component>
</template>

<script setup lang="ts">
  import { ref } from 'vue';
  import { Page, ViewCom } from './components';

  defineOptions({
    name: 'QuarantineReportAudit',
  });

  const currentComponent = shallowRef<any>(Page);

  const rowId = ref<any>('');

  // 进入详情页
  const enterView = (id: any) => {
    rowId.value = id;
    currentComponent.value = ViewCom;
  };

  // 回到page页
  const backPage = () => {
    rowId.value = '';
    currentComponent.value = Page;
  };
</script>

<style lang="less" scoped></style>
