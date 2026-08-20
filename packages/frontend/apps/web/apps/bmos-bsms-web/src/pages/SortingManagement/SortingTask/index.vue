<!-- 分拣任务 -->
<template>
  <component :is="currentComponent" @enterView="enterView" :rowData="rowData" @back="backPage"></component>
</template>

<script setup lang="ts">
  import { ref } from 'vue';
  import { Page, ViewCom } from './components';

  defineOptions({
    name: 'SortingTask',
    inheritAttrs: false,
  });

  const currentComponent = shallowRef<any>(Page);

  const rowData = ref<any>({});

  // 进入详情页
  const enterView = (row: any) => {
    rowData.value = row;
    currentComponent.value = ViewCom;
  };

  // 回到page页
  const backPage = () => {
    rowData.value = {};
    currentComponent.value = Page;
  };
</script>

<style lang="less" scoped></style>
