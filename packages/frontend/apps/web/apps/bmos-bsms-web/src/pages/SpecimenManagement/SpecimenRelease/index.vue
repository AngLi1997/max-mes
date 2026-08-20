<!-- 标本出库 -->
<template>
  <component
    :is="currentComponent"
    @enterView="enterView"
    :outPlanBatchNo="outPlanBatchNo"
    @back="backPage"></component>
</template>

<script setup lang="ts">
  import { ref } from 'vue';
  import { Page, ViewCom } from './components';

  defineOptions({
    name: 'specimen-release',
    inheritAttrs: false,
  });

  const currentComponent = shallowRef<any>(Page);

  const outPlanBatchNo = ref<any>('');

  // 进入详情页
  const enterView = (no: any) => {
    outPlanBatchNo.value = no;
    currentComponent.value = ViewCom;
  };

  // 回到page页
  const backPage = () => {
    outPlanBatchNo.value = '';
    currentComponent.value = Page;
  };
</script>

<style lang="less" scoped></style>
