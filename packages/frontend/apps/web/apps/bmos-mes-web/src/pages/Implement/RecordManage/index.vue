<template>
  <div class="container">
    <!-- 实施玩 -->
    <RecordConfig :implement="implement"></RecordConfig>
  </div>
</template>

<script setup lang="ts">
  import { ref } from 'vue';
  import RecordConfig from '@/pages/RecordConfig/index.vue';
  const implement = ref<string>('1'); //'1'为记录管理,'2'为记录编辑
</script>

<style lang="less" scoped>
  .container {
    width: 100%;
    height: 100%;
  }
</style>
