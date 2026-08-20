<template>
  <div class="info-container">
    <div class="info-title">
      <span class="info-type">{{ data.operationTypeName }}</span>
      <span class="info-name">{{ t('操作人') }}: {{ data.createUsername }}</span>
    </div>
    <div class="info-content">
      {{ data.remark }}
    </div>
  </div>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  defineProps({
    data:{
      type: Object,
      default:()=>({}),
    },
  });
</script>

<style scoped lang="less">
  .info-container {
    padding: 12px 10px;
    background-color: var(--bmos-background-color);
    color: var(--bmos-second-level-text-color);
    display: flex;
    flex-direction: column;
    row-gap: 12px;
  }
  .info-title {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .info-content {
    color: #808080;
  }
</style>
