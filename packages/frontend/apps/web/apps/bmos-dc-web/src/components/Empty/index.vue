<template>
  <div class="empty-container">
    <Empty>
      <template #image>
        <BMIcons :icon="icon" style="width: 100px; height: 100px"></BMIcons>
      </template>
      <template #description>
        <slot name="description">{{ props.emptyName || t('暂无数据') }}</slot>
      </template>
    </Empty>
  </div>
</template>

<script setup lang="ts">
  import { Empty } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { BMIcons } from '@bmos/icons';
  const props = defineProps({
    emptyName: {
      type: String,
      default: '',
    },
    icon: {
      type: String,
      default: 'No_data',
    },
  });
</script>

<style scoped lang="less">
  .empty-container {
    height: 100%;
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
  }
</style>
