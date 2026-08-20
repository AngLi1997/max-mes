<template>
  <view class="total_box">
    <view class="total_title">{{ t('合计') }}</view>
    <view class="total_msg_box">
      <view class="total_num">
        <view class="total_show_title">{{ t('件数') }}:</view>
        <view class="total_show_value">{{ allNum }}{{ t('件') }}</view>
      </view>
      <view class="total_weight">
        <view class="total_show_title">{{ t('总量') }}:</view>
        <view class="total_show_value">{{ allWeight }}</view>
      </view>
    </view>
  </view>
</template>

<script setup>
  import { t } from '@/utils/useBmosI18n.js';

  const props = defineProps({
    allNum: {
      type: [Number, String],
      default: 0
    },
    allWeight: {
      type: [Number, String],
      default: 0
    }
  });
</script>

<style lang="scss" scoped>
    .total_box {
      height: 28.13rpx;
      background-color: #F4F8FF;
      display: flex;
      align-items: center;
      border-radius: 4.69rpx;
      font-size: 11.72rpx;
      .total_title{
        width: 51.56rpx;
        color: #2871FF;
        text-align: center;
        box-sizing: border-box;
        border-right: 1px solid #E1E3E5;
      }
      .total_msg_box{
        width: calc(100% - 51.56rpx);
        display: flex;
        &>view{
          width: 50%;
          display: flex;
          align-items: center;
          padding-left: 13.38rpx;
        }
        .total_show_title{
          color: #6C6E73;
          margin-right: 5.86rpx;
        }
        .total_show_value{
          color: #FF9933;
        }
      }
    }
</style>
