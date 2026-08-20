<!-- 献浆者管理 -->
<template>
  <component :is="Page"></component>
</template>

<script setup lang="ts">
  import { Page } from './components';

  defineOptions({
    name: 'blood-donor-management',
  });
</script>

<style lang="less" scoped></style>
