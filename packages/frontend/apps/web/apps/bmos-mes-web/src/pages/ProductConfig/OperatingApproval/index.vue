<template>
  <SwitchComponent :list="componentList" hasKeepAlive></SwitchComponent>
</template>

<script lang="ts" setup>
  import SwitchComponent from '@/components/SwitchComponent/index.vue';
  import ListPage from './components/ListPage/index.vue';
  import SchedulePage from './components/SchedulePage/index.vue';
  import DealPage from './components/DealPage/index.vue';
  const componentList = [ListPage, SchedulePage, DealPage] as DefineComponent[];
</script>

<style lang="less" scoped></style>
