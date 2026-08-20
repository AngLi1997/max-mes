<template>
  <view class="item-container">
    <view class="name bmos-ellipsis-1">
      <text>{{ item.name }}</text>
    </view>
    <view class="code">
      <text>{{ t('设备编码') }}: {{ item.code }}</text>
    </view>
  </view>
</template>

<script setup>
  import { t } from '@/utils/useBmosI18n.js';

  defineProps({
    item: {
      type: Object,
      default: () => {}
    }
  });
</script>

<style lang="scss" scoped>
.item-container {
  width: 237.3rpx;
  height: 62.11rpx;
  border-radius: 7.03rpx;
  padding: 9.38rpx;
  box-sizing: border-box;
  background-color: #fff;
  .name {
    font-size: 14.07rpx;
    color: #242526;
    font-weight: 600;
    line-height: 18.41rpx;
    margin-bottom: 7.03rpx;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .code {
    font-size: 11.72rpx;
    color: #797C80;
    font-weight: 400;
    line-height: 19.92rpx;
  }
}
</style>
