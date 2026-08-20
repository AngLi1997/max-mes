<template>
  <SwitchComponent :list="componentList" hasKeepAlive>contents</SwitchComponent>
</template>

<script setup lang="ts">
  import SwitchComponent from '@/components/SwitchComponent/index.vue';
  import List from './components/list/index.vue';
  import Detail from './components/detail/index.vue';

  const componentList = [List, Detail] as DefineComponent[];
</script>

<style scoped lang="less"></style>
