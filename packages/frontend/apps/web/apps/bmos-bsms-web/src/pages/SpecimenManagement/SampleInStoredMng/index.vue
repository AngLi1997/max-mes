<!-- 待入库标本管理 -->
<template>
  <component :is="currentComponent" :row="rowData" @enterView="enterView" @back="backPage"></component>
</template>

<script setup lang="ts">
  import { ref } from 'vue';
  import { Page, ViewCom } from './components';

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

<style scoped></style>
