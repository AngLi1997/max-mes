<template>
  <view class="node_item_box">
    <view class="node_item_icon">
      <BMIcon name="shengchanqianqueren" size="15.06rpx" />
    </view>
    <view class="node_item_msg_box">
      <view class="node_time">
        <view class="node_time_name">
          {{ nodeData.productMergeCode }}-{{ nodeData.productName }}
        </view>
      </view>
      <view class="node_msg">
        {{ t("批号") }}：{{ nodeData.batchNo }}
      </view>
      <view class="node_msg">
        {{ t("工艺") }}：{{ nodeData.processName }}-{{ nodeData.processVersion }}
      </view>
      <view class="node_msg">
        {{ t("产线") }}：{{ nodeData.code }}-{{ nodeData.name }}
      </view>
      <view class="node_msg">
        {{ t("计划生产时间") }}：{{ nodeData.productDate }}
      </view>
      <view class="node_msg">
        {{ t("计划编号") }}：{{ nodeData.planNo }}
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import {
  BMIcon,
} from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';

defineProps({
  nodeData: {
    type: Object,
    default: () => {},
  },
});
</script>

<style lang="scss" scoped>
  .node_item_box {
  flex-wrap: wrap;
  width: calc(50% - 4.69rpx);
  box-sizing: border-box;
  padding: 9.38rpx;
  border-radius: 4.69rpx;
  display: flex;
  align-items: center;
  background-color: #f5f9ff;
  border: 1px solid #ccdfff;
  margin-top: 9.38rpx;
  position: relative;
  overflow: hidden;
  .node_item_icon {
    width: 29.3rpx;
    height: 29.3rpx;
    border-radius: 50%;
    background-color: #e0ebff;
    line-height: 29.3rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
    .node_item_icon_img {
      width: 14.06rpx;
      height: 14.06rpx;
      position: absolute;
      top: 0;
      bottom: 0;
      left: 0;
      right: 0;
      margin: auto;
    }
  }
  .node_item_msg_box {
    width: calc(100% - 39rpx);
    margin-left: 9.38rpx;
    font-size: 11.72rpx;
    .node_time {
      color: #1a8dff;
      margin-bottom: 5.86rpx;
      display: flex;
      .node_time_name {
        max-width: calc(100% - 64.45rpx);
      }
      text {
        max-width: 64.45rpx;
        margin-left: 2.93rpx;
      }
    }
    .node_msg {
      color: #6c6e73;
      margin-bottom: 5.86rpx;
      display: flex;
      .node_msg_content {
        width: calc(100% - 58.59rpx);
      }
    }
    .node_suspend {
      position: absolute;
      top: 0;
      right: 0;
      font-size: 9.38rpx;
      padding: 2.34rpx 5.86rpx;
      background-color: #ffd5cc;
      color: #ff4c26;
      border-bottom-left-radius: 2.34rpx;
    }
  }
}
</style>
