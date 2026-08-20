<template>
  <view class="device-detail">
    <view class="info">
      <view class="info-title">
        <view class="left">
          <wd-tag
            v-if="item.isCalibrated"
            type="success"
            plain
          >
            {{ t("已校准") }}
          </wd-tag>
          <wd-tag v-else custom-class="smallTag" type="warning" plain>
            {{ t("未校准") }}
          </wd-tag>
          <text>{{ item.balanceName }}-{{ item.balanceCode }}</text>
        </view>
        <view class="right" :class="{ check: isChecked }">
          <wd-icon
            v-if="isChecked"
            name="xuanze"
            size="14.06rpx"
            class-prefix="bmos-app-icon"
            color="#fff"
          />
        </view>
      </view>
      <view class="info-item">
        {{ `${t("称量协议类型")}: ${item.protocolType || '-'}` }}
      </view>
      <view class="info-item">
        {{ `${t("校准有效期")}: ${item.calibrateExpiredDate || '-'}` }}
      </view>
    </view>
    <BMDataInfoDisplay
      :basic-items="dataInfoItems"
      :info-data="dataInfoData"
      background="var(--bmos-bg-form)"
    />
  </view>
</template>

<script setup>
import { BMDataInfoDisplay } from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import { computed, ref } from 'vue';

const props = defineProps({
  item: {
    type: Object,
    default: () => {},
  },
  checkedId: {
    type: String,
    default: '',
  },
});
const dataInfoItems = ref([
  {
    label: t('称量范围'),
    field: 'range',
  },
  {
    label: t('称量精度'),
    field: 'precision',
  },
  {
    label: t('单位'),
    field: 'unit',
  },
]);
const dataInfoData = computed(() => {
  const detailData = props.item;
  return {
    range: {
      value: detailData.minRange && detailData.maxRange
        ? `${detailData.minRange}-${detailData.maxRange}`
        : '-',
    },
    precision: {
      value: detailData.precision,
    },
    unit: {
      value: detailData.unit,
    },
  };
});
const isChecked = computed(() => {
  return props.checkedId === props.item.balanceId;
});
</script>

<style lang="scss" scoped>
.device-detail {
  width: 100%;
  background-color: #fff;
  border: 0.59rpx solid var(--bmos-color-border);
  border-radius: 4.69rpx;
  padding: 7.03rpx 9.38rpx;
  box-sizing: border-box;
  .info {
    margin-bottom: 5.86rpx;
    .info-title {
      display: flex;
      justify-content: space-between;
      height: 18.75rpx;
      align-items: center;
      .left {
        display: flex;
        align-items: center;
        column-gap: 7.03rpx;
        color: var(--bmos-color-text-main);
        font-size: 14.06rpx;
      }
      .right {
        width: 18.75rpx;
        height: 18.75rpx;
        border-radius: 50%;
        border: 1px solid var(--bmos-color-icon-light);
        display: flex;
        justify-content: center;
        align-items: center;
      }
      .check {
        background-color: var(--bmos-color-primary);
        border: none;
      }
    }
    .info-item {
      margin-top: 5.86rpx;
      font-size: 10.55rpx;
      line-height: 12.89rpx;
      color: var(--bmos-color-text-sub);
    }
  }
}
</style>
