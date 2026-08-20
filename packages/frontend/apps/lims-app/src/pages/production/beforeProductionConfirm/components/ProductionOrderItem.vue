<template>
  <view class="item-container">
    <view class="title-box">
      <text class="title-box-left">
        {{ item.productMergeCode }}-{{ item.productName }}
      </text>
      <view class="title-box-right">
        <text>{{ item.batchNo }}</text>
      </view>
    </view>
    <view
      class="text-box"
      :style="{ 'min-height': showMore ? '112.5rpx' : '56.25rpx' }"
    >
      <BMInfoDisplay
        :is-show-title="false"
        is-show-one
        :basic-items="
          showMore
            ? [
              {
                label: t('计划编号'),
                field: 'planNo',
              },
              {
                label: t('工艺'),
                field: 'processName',
              },
              {
                label: t('版本'),
                field: 'processVersion',
              },
              {
                label: t('规格'),
                field: 'productSpecification',
              },
              {
                label: t('计划生产时间'),
                field: 'productDate',
              },
              {
                label: t('指令单类型'),
                field: 'label',
              },
            ]
            : [
              {
                label: t('计划编号'),
                field: 'planNo',
              },
              {
                label: t('工艺'),
                field: 'processName',
              },
              {
                label: t('版本'),
                field: 'processVersion',
              },
            ]
        "
        :info-data="item"
      />
      <view class="click-arrow" @click.stop="arrowClick">
        <wd-icon
          :class="{ 'icon-rotate': showMore }"
          class-prefix="bmos-app-icon"
          name="jiantou-xia"
          size="14.06rpx"
          color="#B0B5BF"
          style="margin-right: 4.69rpx"
        />
      </view>
    </view>
  </view>
</template>

<script setup>
  import { t } from '@/utils/useBmosI18n.js';
  import { ref, toRefs } from 'vue';
  import { BMInfoDisplay } from '@/BMComponents';
  const props = defineProps({
    item: {
      type: Object,
      default: () => {
        return {
          productMergeCode: '',
          productName: '',
          batchNo: '',
          planNo: '',
          processName: '',
          processVersion: '',
          productSpecification: '',
          productDate: '',
          type: { value: '', label: '' }
        };
      }
    }
  });

  const { item } = toRefs(props);
  const showMore = ref(false);

  function arrowClick() {
    showMore.value = !showMore.value;
  }
</script>

<style scoped lang="scss">
.item-container {
  max-width: 361.08rpx;
  padding: 9.38rpx;
  border-radius: 7.03rpx;
  background-color: #ffffff;
  box-sizing: border-box;

  .title-box {
    display: flex;
    flex-direction: column;
    font-weight: 500;

    &-left {
      font-size: 14.07rpx;
      line-height: 16.41rpx;
      color: var(--bmos-color-text-main);
    }

    &-right {
      width: fit-content;
      margin-top: 4.69rpx;
      display: flex;
      font-size: 11.72rpx;
      line-height: 14.06rpx;
      border-radius: 2.34rpx;
      padding: 1.76rpx 4.69rpx;
      color: var(--bmos-color-white);
      background: linear-gradient(90deg, #599eff -2.49%, #3274f9 101.99%);
    }
  }

  .text-box {
    display: flex;
    flex-direction: column;
    color: #545659;
    font-size: 11.72rpx;
    font-weight: 400;
    line-height: 19.93rpx;
    position: relative;
    overflow: hidden;
    transition: 0.3s all;
    :deep(.info-display-container) {
      padding: 0;
      .info-content-show-one {
        margin-top: 8px;
        font-size: 11.72rpx;
        line-height: 14.06rpx;
        > span {
          color: #6c6e73;
        }
      }
    }

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
