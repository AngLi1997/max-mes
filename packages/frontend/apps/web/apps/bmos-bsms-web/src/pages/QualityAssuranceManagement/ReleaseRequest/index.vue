<!-- 放行单查询 -->
<template>
  <component :is="Page"></component>
</template>

<script setup lang="ts">
  import { Page } from './components';

  defineOptions({
    name: 'ReleaseRequest',
  });
</script>

<style lang="less" scoped></style>
