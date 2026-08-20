<template>
  <div class="empty-container">
    <Empty>
      <template #image>
        <BMIcons icon="No_data" style="width: 100px; height: 100px"></BMIcons>
      </template>
      <template #description>
        <slot name="description">{{ props.emptyName || t('暂无数据') }}</slot>
      </template>
    </Empty>
  </div>
</template>

<script setup lang="ts">
  import { ref } from 'vue';
  import { Empty } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { BMIcons } from '@bmos/icons';
  const props = defineProps({
    emptyName: {
      type: String,
      default: '',
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
