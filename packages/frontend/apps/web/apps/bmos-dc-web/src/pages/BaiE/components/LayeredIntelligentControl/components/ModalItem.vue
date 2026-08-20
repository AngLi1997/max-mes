<template>
  <template v-for="(field, index) in fields" :key="field.key">
    <div class="process-item-row">
      <div class="process-item-row-title">
        <img src="../../../../../assets/baiePng/process-detail-point.png" alt="" width="12px" height="12px" />
        <span>{{ t(field.label) }}</span>
      </div>
      <template v-if="Array.isArray(item[field.key])">
        <div
          v-for="(value, index) in item[field.key]"
          :key="index"
          :class="{ 'process-item-row-value': true, 'font-12': currentLng === 'ru_RU' }">
          {{ t(value) }}
        </div>
      </template>
      <div v-else class="process-item-row-value">{{ t(item[field.key] || '-') }}</div>
    </div>
    <div v-if="index !== fields.length - 1" class="process-item-line"></div>
  </template>
</template>

<script setup lang="ts">
  import { currentLng } from '@bmos/i18n';
  import { t } from '@bmos/i18n';

  const props = defineProps({
    item: {
      type: Object,
      default: () => {},
    },
    fields: {
      type: Array as PropType<Array<{ label: string; key: string }>>,
      default: () => [],
    },
  });
</script>

<style lang="less" scoped>
  .font-12 {
    font-size: 12px !important;
  }
  .process-item-row {
    width: 100%;
    min-height: 44px;
    box-sizing: border-box;
    .process-item-row-title {
      display: flex;
      align-items: center;
      color: rgba(152, 190, 217, 1);
      font-size: 14px;
      margin-bottom: 8px;
      > span {
        margin-left: 4px;
      }
    }
    .process-item-row-value {
      width: 100%;
      padding-left: 20px;
      box-sizing: border-box;
      color: #fff;
      font-size: 14px;
    }
  }
  .process-item-line {
    width: 100%;
    height: 1px;
    background-image: url('@/assets/baiePng/process-detail-line.png');
    background-size: 100% 100%;
    margin: 10px 0;
  }
</style>
