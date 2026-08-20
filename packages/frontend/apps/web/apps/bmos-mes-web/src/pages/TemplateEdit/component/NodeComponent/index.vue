<template>
  <div @click="$emit('node-click', data)" :class="{'component-node': true,'is-click':clickType==data?.componentType}">
    <BMIcons
      :icon="data?.icon"
      style="font-size: 24px; width: 24px; height: 24px; color: rgba(36, 37, 38, 1); padding: 2px" />
    <span>{{ data?.componentName }}</span>
  </div>
</template>

<script setup lang="ts">
  import { PropType } from 'vue';
  import { BMIcons } from '@bmos/icons';
  import { ComponentNode } from '@/components/Record';
  defineProps({
    data: Object as PropType<ComponentNode & { icon: string }>,
    clickType: String
  });
</script>

<style scoped lang="less">
  .component-node {
    display: flex;
    flex-direction: column;
    row-gap: 4px;
    font-size: 14px;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    color: rgba(96, 98, 102, 1);
    width: 20%;
    padding: 10px 0;
    &:hover {
      // background-color: rgb(226, 227, 230);
    }
  }
  .is-click{
    background-color:  #EBF1FF;
    border-radius: 8px;
  }
</style>
