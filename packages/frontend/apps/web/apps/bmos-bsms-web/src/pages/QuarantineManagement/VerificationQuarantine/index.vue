<!-- 检疫期核查数据 -->
<template>
  <component :is="Page"></component>
</template>

<script setup lang="ts">
  import { Page } from './components';

  defineOptions({
    name: 'VerificationQuarantine',
  });
</script>

<style lang="less" scoped></style>
