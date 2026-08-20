<template>
  <view class="display">
    <view class="left">
      <view class="weighing-name">
        {{ weighingName }}
      </view>
      <view class="scale-info">
        {{ t("称量范围") }}：{{ `${selectedBalance.minRange ? `${selectedBalance.minRange}-${selectedBalance.maxRange}` : '-'}${selectedBalance.unit}` }}
      </view>
      <view class="scale-info">
        {{ t("称量精度") }}：{{ selectedBalance.precision }}{{ selectedBalance.unit }}
      </view>
    </view>
    <view class="right">
      <text class="number">
        {{ weight }}
      </text>
      <text class="unit">
        {{ selectedBalance.unit }}
      </text>
    </view>
  </view>
</template>

<script setup>
import { useWeighingMachineStore } from '@/stores/weighingMachine/index.js';
import { t } from '@/utils/useBmosI18n.js';
import { storeToRefs } from 'pinia';

defineProps({
  // 称量名称
  weighingName: {
    type: String,
    default: () => t('配料称量'),
  },
  // 秤具读数
  weight: {
    type: String,
    default: '0.00',
  },
});
const weighingMachineStore = useWeighingMachineStore();
const { selectedBalance } = storeToRefs(weighingMachineStore);
</script>

<style lang="scss" scoped>
.display {
  color: var(--bmos-color-white);
  background-color: #333333;
  padding: 10.55rpx 9.38rpx;
  border-radius: 5.86rpx;
  display: flex;
  justify-content: space-between;
  .left {
    font-size: 9.38rpx;
    color: var(--bmos-color-text-placeholder);
    .weighing-name {
      color: var(--bmos-color-white);
      background-color: #474f59;
      padding: 0 5.86rpx;
      border-radius: 2.34rpx;
      display: inline-flex;
      align-items: center;
      box-sizing: border-box;
      height: 16.41rpx;
    }
    .scale-info {
      margin-top: 9.38rpx;
      height: 11.72rpx;
    }
  }
  .right {
    .number {
      font-family: Digital Numbers;
      font-size: 48.05rpx;
    }
    .unit {
      font-size: 23.44rpx;
      vertical-align: bottom;
    }
  }
}
</style>
