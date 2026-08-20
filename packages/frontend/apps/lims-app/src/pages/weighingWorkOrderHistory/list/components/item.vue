<template>
  <view class="item-container">
    <view class="title-box">
      <text class="title-box-left">
        {{ `${item.materialName}-${item.materialMergeCode}` }}
      </text>
    </view>
    <view class="weighing-center-title">
      {{ `${item.centreCode}-${item.centreName}` }}
    </view>
    <view
      class="text-box"
      :style="{ height: showMore ? '79.72rpx' : '39.86rpx' }"
    >
      <text>{{ t("工单编号") }}:&nbsp;&nbsp;{{ item.ticketNo }}</text>
      <text> {{ t("完成时间") }}:&nbsp;&nbsp;{{ item.completeTime }} </text>
      <text> {{ t("下发时间") }}:&nbsp;&nbsp;{{ item.sendTime }} </text>
      <text>
        {{ t("物料规格") }}:&nbsp;&nbsp;{{ item.materialSpecification }}
      </text>
      <view class="click-arrow" @click.stop="arrowClick">
        <wd-icon
          class="icon-arrow"
          :class="{ 'icon-rotate': showMore }"
          name="jiantou-xia"
          class-prefix="bmos-app-icon"
          size="14.07rpx"
          color="#B0B5BF"
        />
      </view>
    </view>
  </view>
</template>

<script setup>
import { t } from '@/utils/useBmosI18n.js';
import { ref } from 'vue';

defineProps({
  item: {
    type: Object,
    default: () => {
      return {};
    },
  },
});

const showMore = ref(false);

function arrowClick() {
  showMore.value = !showMore.value;
}
</script>

<style scoped lang="scss">
.item-container {
  max-width: 361.08rpx;
  min-height: 96.09rpx;
  padding: 9.38rpx 11.72rpx;
  border-radius: 7.03rpx;
  background-color: var(--bmos-color-white);
  box-sizing: border-box;
  position: relative;

  .title-box {
    display: flex;
    justify-content: space-between;

    &-left {
      font-size: 14.07rpx;
      line-height: 16.41rpx;
      font-weight: 600;
      color: var(--bmos-color-text-main);
    }
  }
  .expired {
    color: var(--bmos-color-error);
    font-size: 9.38rpx;
    line-height: 11.72rpx;
    background-color: var(--bmos-bg-color-error);
    padding: 2.34rpx 5.86rpx;
    position: absolute;
    right: 0;
    top: 0;
    border-top-right-radius: 7.03rpx;
    border-bottom-left-radius: 2.34rpx;
  }

  .weighing-center-title {
    width: fit-content;
    margin: 4.69rpx 0;
    height: 18.76rpx;
    align-items: center;
    display: flex;
    border-radius: 2.34rpx;
    background-color: var(--bmos-color-tag);
    font-size: 11.72rpx;
    color: var(--bmos-color-primary);
    box-sizing: border-box;
    padding: 2.34rpx;
  }

  .text-box {
    display: flex;
    flex-direction: column;
    color: var(--bmos-color-text-sub);
    font-size: 11.72rpx;
    font-weight: 400;
    line-height: 19.93rpx;
    position: relative;
    overflow: hidden;
    transition: 0.3s all;

    .click-arrow {
      position: absolute;
      bottom: 0;
      right: 0;
      width: 60rpx;
      height: 45rpx;
      display: flex;
      align-items: flex-end;
      justify-content: flex-end;

      .icon-arrow {
        width: 14.07rpx;
        height: 14.07rpx;
        transition: 0.3s all;
      }

      .icon-rotate {
        transform: rotate(180deg);
      }
    }
  }
}
</style>
