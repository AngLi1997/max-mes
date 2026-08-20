<template>
  <Tabs v-bind="$props" class="tab-page-header">
    <TabPane
      v-for="tab in tabList"
      :key="tab.key"
      :tab="tab.title"
      class="tab-pane-item"></TabPane>
  </Tabs>
</template>

<script setup lang="ts">
  import { tabProps } from '../../props';
  import { Tabs, TabPane } from 'ant-design-vue';
  defineProps({ ...tabProps });
</script>