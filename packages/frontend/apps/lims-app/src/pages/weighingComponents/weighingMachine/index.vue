<template>
  <view v-if="auto" class="weighing-machine-content">
    <view class="display">
      <view class="left">
        <view class="weighing-name">
          {{ weighingName }}
        </view>
        <view class="scale-info">
          {{ t("称量范围") }}：{{ range }}
        </view>
        <view class="scale-info">
          {{ t("称量精度") }}：{{ precision }}
        </view>
      </view>
      <view class="right">
        <text class="number">
          {{ weight }}
        </text>
        <text class="unit">
          {{ unit }}
        </text>
      </view>
    </view>
    <view class="data-progress-box">
      <view class="info-box">
        <view>
          <text>
            {{ t("目标量") }}：<text class="value">
              {{ targetAmount }}{{ unit }}
            </text>
          </text>
          <text class="label">
            {{ t("允差范围") }}：<text class="value">
              {{ toleranceRange }}
            </text>
          </text>
        </view>
        <view>
          <text>
            {{ t("剩余量") }}：<text class="value">
              {{ remainingAmount }}
            </text>
          </text>
        </view>
      </view>
      <view v-if="actionNumber === 2" class="line-progress-box">
        <view class="normal-progress">
          <wd-progress
            :percentage="smallProgressValue"
            hide-text
            :duration="5"
            :color="progressColor"
          />
          <view
            class="zoom" :style="{
              width: smallProgressWidth,
            }"
          >
            <wd-icon
              custom-class="custom-arrow"
              name="arrow-down1"
              size="8.2rpx"
              color="var(--bmos-color-text-desc)"
            />
          </view>
        </view>
        <view class="big-progress">
          <wd-progress
            :key="progressColor"
            :percentage="bigProgressValue"
            hide-text
            :duration="5"
            :color="progressColor"
          />
          <view class="scale-1">
            <view class="tick-mark" />
            <text>{{ diff.toleranceDiff[0] || diff.toleranceDiff[1] }}</text>
          </view>
          <view class="scale-2">
            <view class="tick-mark" />
            <text>{{ diff.toleranceDiff[1] }}</text>
          </view>
          <view class="scale-3">
            <view class="tick-mark" />
            <text>{{ diff.toleranceDiff[2] || diff.toleranceDiff[1] }}</text>
          </view>
        </view>
      </view>
      <view v-else class="line-progress-box" />
      <view class="data-box">
        <wd-input
          v-model="value.tareWeight"
          label-width="46.88rpx"
          type="text"
          :label="t('皮重')"
          :readonly="true"
          placeholder=" "
        />
        <wd-input
          v-model="value.netWeight"
          label-width="46.88rpx"
          type="text"
          :label="t('净重')"
          :readonly="true"
          placeholder=" "
        />
        <wd-input
          v-model="value.grossWeight"
          label-width="46.88rpx"
          type="text"
          :label="t('毛重')"
          :readonly="true"
          placeholder=" "
        />
      </view>
    </view>
  </view>
  <view v-else class="weighing-machine-handle">
    <Info
      :basic-items="[
        {
          label: t('允差范围'),
          field: 'toleranceRange',
          type: 'text',
        },
        {
          label: t('目标范围'),
          field: 'targetRange',
          type: 'text',
        },
      ]"
      :info-data="{
        toleranceRange,
        targetRange,
      }"
      background="var(--bmos-bg-form)"
    >
      <template #icon>
        <view class="weighing-name">
          {{ weighingName }}
        </view>
      </template>
    </Info>
    <view class="input-box">
      <BMForm v-if="isLiquidMeasure" ref="formRef" v-bind="formProps2" />
      <BMForm v-else ref="formRef" v-bind="formProps" />
    </view>
  </view>
</template>

<script setup>
import { BMForm } from '@/BMComponents';
import Info from '@/pages/weighingComponents/info';
import { t } from '@/utils/useBmosI18n.js';
import { computed, watch } from 'vue';
import { useMaterialWeighing } from './hooks/useWeighingMachine.js';

const props = defineProps({
  modelValue: {
    type: Object,
    default: () => ({}),
  },
  // 是否自动称量
  auto: {
    type: Boolean,
    default: false,
  },
  // 称量名称
  weighingName: {
    type: String,
    default: () => t('配料称量'),
  },
  // 秤具读数
  weight: {
    type: String,
    default: '0.01',
  },
  // 单位
  unit: {
    type: String,
    default: 'kg',
  },
  // 目标量
  targetAmount: {
    type: String,
    default: '0.01',
  },
  // 允差信息
  diff: {
    type: Object,
    default: () => ({}),
  },
  // 剩余量
  remainingAmount: {
    type: String,
    default: '0.01',
  },
  // 0:清零 1:去皮 2:称量打码
  actionNumber: {
    type: Number,
    default: 0,
  },
  // 是否配液量取
  isLiquidMeasure: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(['update:modelValue']);

const value = computed({
  get: () => props.modelValue,
  set: (val) => {
    emit('update:modelValue', val);
  },
});
const {
  toleranceRange,
  range,
  precision,
  smallProgressValue,
  smallProgressWidth,
  bigProgressValue,
  progressColor,
  targetRange,
  formProps,
  formProps2,
  formRef,
} = useMaterialWeighing({ props, value });

watch(
  () => props.modelValue,
  (val) => {
    !props.auto && formRef.value && formRef.value.setFormModels(val);
  },
  {
    deep: true,
  },
);
watch(
  () => props.unit,
  (val) => {
    if (val && props.isLiquidMeasure) {
      formRef.value && formRef.value.setFormModel('unit', val);
    }
  },
);
</script>

<style lang="scss" scoped>
.weighing-machine-content {
  border-radius: 5.86rpx;
  box-shadow: 0px 0px 3px 0.5px #00000033;
  background-color: #edeff2;
  height: 100%;
  overflow-y: auto;
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
        display: flex;
        align-items: center;
        box-sizing: border-box;
        height: 16.41rpx;
        font-size: 9.38rpx;
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
  .data-progress-box {
    padding: 7.03rpx 14.06rpx 8.2rpx;
    .info-box {
      font-size: 10.55rpx;
      display: flex;
      height: 12.89rpx;
      justify-content: space-between;
      color: var(--bmos-color-text-sub);
      .label {
        margin-left: 17.58rpx;
      }
      .value {
        color: var(--bmos-color-text-main);
      }
    }
    .line-progress-box {
      height: 39.26rpx;
      margin-top: 5.27rpx;
      margin-bottom: 5.86rpx;
      :deep(.wd-progress) {
        border-radius: 5.86rpx;
        padding: 0;
        :root {
          --wd-progress-height: 3.52rpx;
        }
      }
      .normal-progress {
        --wot-progress-height: 3.52rpx;
        margin-bottom: 8.2rpx;
        position: relative;
        .zoom {
          width: 140.63rpx;
          height: 7.03rpx;
          border-radius: 1.17rpx;
          border: 0.59rpx solid var(--bmos-color-text-desc);
          box-sizing: border-box;
          position: absolute;
          top: -1.76rpx;
          right: 134.77rpx;
          .custom-arrow {
            position: absolute;
            left: calc(50% - 4.1rpx);
            top: 4.69rpx;
          }
        }
      }
      .big-progress {
        --wot-progress-height: 7.03rpx;
        position: relative;
        .scale-1,
        .scale-2,
        .scale-3 {
          color: var(--bmos-color-text-sub);
          font-size: 10.55rpx;
          .tick-mark {
            width: 0.59rpx;
            height: 5.86rpx;
            background-color: var(--bmos-color-text-sub);
          }
          position: absolute;
          display: flex;
          flex-direction: column;
        }

        .scale-2 {
          right: 50%;
          align-items: center;
        }
        .scale-3 {
          right: 0;
          align-items: end;
        }
      }
    }
    .data-box {
      display: flex;
      justify-content: space-between;
      :deep(.wd-input) {
        padding-left: 0;
        border: none;
        box-shadow:
          0px 1px 1px 0px #ffffff80,
          0px 0px 3px 0px #00000033 inset;

        .wd-input__label {
          color: var(--bmos-color-text-placeholder);
          border-right: 0.59rpx solid var(--bmos-color-text-placeholder);
          justify-content: center;
        }
        .uni-input-wrapper {
          padding-top: 0;
          padding-bottom: 0;
        }
      }
    }
  }
}
.weighing-machine-handle {
  margin-bottom: 15.23rpx;
  .weighing-name {
    color: var(--bmos-color-white);
    background-color: var(--bmos-color-text-desc);
    padding: 0 5.86rpx;
    border-radius: 2.34rpx;
    display: flex;
    align-items: center;
    box-sizing: border-box;
    height: 16.41rpx;
    font-size: 9.38rpx;
  }
  .input-box {
    margin-top: 14.06rpx;
  }
}
</style>
