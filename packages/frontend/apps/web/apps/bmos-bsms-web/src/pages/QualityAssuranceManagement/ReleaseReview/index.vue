<!-- 放行单审核 -->
<template>
  <component :is="Page"></component>
</template>

<script setup lang="ts">
  import { Page } from './components';

  defineOptions({
    name: 'ReleaseReview',
  });
</script>

<style lang="less" scoped></style>
