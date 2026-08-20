<template>
  <view class="item-box">
    <image :src="codeEnum[`${item.id}`]?.icon" class="icon" />
    <text class="name">
      {{ item.name }}
    </text>
  </view>
</template>

<script setup>
import { codeEnum } from '../../menuCode.js';

defineProps({
  item: {
    type: Object,
    default: () => {
      return {};
    },
  },
});
</script>

<style lang="scss" scoped>
.item-box {
  width: 46.89rpx;
  height: 70.93rpx;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  .icon {
    width: 46.89rpx;
    height: 46.89rpx;
    border-radius: 5.86rpx;
    margin-bottom: 7.03rpx;
  }

  .name {
    font-family: Source Han Sans CN;
    font-size: 11.72rpx;
    font-weight: 700;
    line-height: 17rpx;
    letter-spacing: 0em;
    text-align: center;
    color: #545659;
    white-space: nowrap;
    max-width: 87.89rpx;
    text-overflow: ellipsis;
    overflow: hidden;
  }
}
</style>
