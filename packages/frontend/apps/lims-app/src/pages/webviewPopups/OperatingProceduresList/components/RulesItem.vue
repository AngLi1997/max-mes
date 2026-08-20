<template>
  <view class="rule_box">
    <view class="title">
      <BMIcon name="xinxi" size="18.75rpx" color="#2871FF"/>
      <view>{{ ruleData.name }}</view>
    </view>
    <view class="msg_box">
      <view class="msg_label">
        {{ t('文件编号') }}：
      </view>
      <view class="msg_content">
        {{ ruleData.code }}
      </view>
    </view>
    <view class="msg_box">
      <view class="msg_label">
        {{ t('版本号') }}：
      </view>
      <view class="msg_content">
        {{ ruleData.version }}
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
  ruleData: {
    type: Object,
    default: () => {},
  },
});
</script>

<style lang="scss">
  .rule_box {
    width: calc(50% - 4.69rpx);
    background-color: #FFF;
    border-radius: 4.69rpx;
    padding: 9.38rpx;
    box-sizing: border-box;
    .title {
      margin-bottom: 9.38rpx;
      font-size: 14.06rpx;
      display: flex;
      align-items: center;
      color: #2871FF;
      gap: 7.03rpx;
    }
    .msg_box{
      margin-top: 9.38rpx;
      font-size: 11.72rpx;
      display: flex;
      align-items: center;
      .msg_label{
        color: #6C6E73;
      }
      .msg_content{
        color: #242526;
      }
    }
  }
</style>
