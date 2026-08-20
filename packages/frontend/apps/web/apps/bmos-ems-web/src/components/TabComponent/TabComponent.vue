<template>
  <div class="tabs-page">
    <TabsHeader v-bind="$props"></TabsHeader>
    <div class="tab-content">
      <slot></slot>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { tabProps } from './props';
  import TabsHeader from './components/Tabs/index.vue';
  defineProps({ ...tabProps });
</script>

<style scoped lang="less">
  .tabs-page {
    background-color: #ffffff;
    height: 100%;
    display: flex;
    flex-direction: column;
  }
  .tab-content {
    flex: 1;
    overflow-y: auto;
    overflow-x: auto;
  }
</style>
