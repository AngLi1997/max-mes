<template>
  <view class="statistical-info-box">
    <view class="title">
      {{ title }}
    </view>
    <view v-for="item in basicItems" :key="item.field" class="item">
      <text>{{ item.label }}: </text>
      <text class="value">
        {{ infoData[item.field] || "-"
        }}{{ infoData[item.field] ? item.unit : "" }}
      </text>
    </view>
  </view>
</template>

<script setup>
import { t } from '@/utils/useBmosI18n.js';

defineProps({
  title: {
    type: String,
    default: () => t('统计'),
  },
  basicItems: {
    type: Array,
    default: () => [],
  },
  infoData: {
    type: Object,
    default: () => {},
  },
});
</script>

<style lang="scss" scoped>
.statistical-info-box {
  display: flex;
  padding: 9.38rpx;
  border-radius: 4.69rpx;
  background-color: var(--bmos-bg-form);
  font-size: 11.72rpx;
  color: var(--bmos-text-main);
  .title {
    padding-right: 9.38rpx;
    border-right: 0.59rpx solid var(--bmos-color-border);
  }
  .item {
    flex: 1;
    margin-left: 18.75rpx;
    line-height: 14.06rpx;
    .value {
      color: var(--bmos-color-warning);
    }
  }
}
</style>
