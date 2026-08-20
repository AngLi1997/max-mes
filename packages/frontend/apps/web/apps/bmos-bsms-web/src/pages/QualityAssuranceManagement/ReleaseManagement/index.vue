<!-- 放行单管理 -->
<template>
  <component :is="currentComponent" :id="rowId" @back="backPage"></component>
</template>

<script setup lang="ts">
  import { ref } from 'vue';
  import { Page } from './components';

  defineOptions({
    name: 'ReleaseManagement',
  });

  const currentComponent = shallowRef<any>(Page);

  const rowId = ref<any>('');

  // 回到page页
  const backPage = () => {
    rowId.value = '';
    currentComponent.value = Page;
  };
</script>

<style lang="less" scoped></style>
