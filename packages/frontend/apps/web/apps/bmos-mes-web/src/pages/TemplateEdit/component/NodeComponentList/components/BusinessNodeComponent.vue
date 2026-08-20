<template>
  <div
    class="component-node"
    v-if="hasPermission(data?.permission || '')"
    @click="emit('business-node-click', cloneDeep(data))">
    <span>{{ data?.componentName }}</span>
  </div>
</template>

<script setup lang="ts">
  import { PropType } from 'vue';
  import { cloneDeep } from '@bmos/utils';
  import { ComponentNode } from '@/components/Record';
  import { usePermissionStore } from '@/stores/permission';
  const { hasPermission } = usePermissionStore();
  const emit = defineEmits(['business-node-click']);
  defineProps({
    data: {
      type: Object as PropType<ComponentNode>,
      default: null,
    },
  });
</script>

<style scoped lang="less">
  .component-node {
    width: 109px;
    height: 38px;
    display: flex;
    font-size: 14px;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    background-color: #f5f6f7;
    color: var(--bmos-second-level-text-color);
    font-weight: 400;
    border-radius: 6px;
  }
</style>
