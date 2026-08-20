<!-- 标本数据同步 -->
<template>
  <component :is="currentComponent" @enterImport="enterImport" @back="backPage"></component>
</template>

<script setup lang="ts">
  import { Page, ImportExcel } from './components';

  defineOptions({
    name: 'SampleDataSync',
    inheritAttrs: false,
  });

  const currentComponent = shallowRef<any>(Page);

  // 进入详情页
  const enterImport = () => {
    currentComponent.value = ImportExcel;
  };

  // 回到page页
  const backPage = () => {
    console.log('backPage');
    currentComponent.value = Page;
  };
</script>

<style lang="less" scoped></style>
