<!-- 检疫期结果查询 -->
<template>
  <component :is="Page"></component>
</template>

<script setup lang="ts">
  import { Page } from './components';

  defineOptions({
    name: 'ResultsQuery',
  });
</script>

<style lang="less" scoped></style>
