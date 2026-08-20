<!-- 中间品产出组件称量器 -->
<template>
  <view class="weighing-machine-content">
    <DeviceIndication v-bind="attrs" />
    <view class="data-progress-box">
      <view class="data-box">
        <wd-input
          v-if="mode === '0'"
          v-model="value.tareWeight"
          label-width="46.88rpx"
          type="text"
          :label="t('皮重')"
          :readonly="true"
          placeholder=" "
        />
        <wd-input
          v-if="mode === '3'"
          v-model="tare"
          label-width="46.88rpx"
          type="number"
          :label="t('皮重')"
          :readonly="actionNumber !== 2"
          placeholder=" "
          use-suffix-slot
        >
          <template #suffix>
            <!-- #ifdef APP-PLUS -->
            <view class="scan-icon-box" @click.stop="iconClick">
              <BMIcon name="saomiao" size="14.06rpx" color="#2871FF" />
            </view>
            <!-- #endif -->
            <!-- #ifdef H5 -->
            <view class="scan-icon-box">
              <wd-button
                type="text"
                :disabled="actionNumber !== 2"
                @click="iconClick"
              >
                {{ t("确定") }}
              </wd-button>
            </view>
            <!-- #endif -->
          </template>
        </wd-input>
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
</template>

<script setup>
import { BMIcon } from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import { computed, useAttrs, watch } from 'vue';
import { DeviceIndication } from '../index.js';
import { useMaterialWeighing } from './hooks/useWeighingMachine.js';

const props = defineProps({
  modelValue: {
    type: Object,
    default: () => ({}),
  },
  // 0:清零 1:去皮 2:称量打码
  actionNumber: {
    type: Number,
    default: 0,
  },
  // 称量模式
  mode: {
    type: String,
    default: '0',
  },
  unitId: {
    type: String,
    default: '',
  },
});
const emit = defineEmits(['update:modelValue']);
const attrs = useAttrs();
const value = computed({
  get: () => props.modelValue,
  set: (val) => {
    emit('update:modelValue', val);
  },
});
const { tare, iconClick } = useMaterialWeighing({ value, props });
watch(
  () => props.modelValue,
  (val) => {
    tare.value = val.tareWeight;
  },
  { deep: true },
);
watch(
  () => tare.value,
  (val) => {
    if (tare.value.length < 20) {
      value.value.tareWeight = val;
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
  .data-progress-box {
    padding: 17.58rpx 14.06rpx;
    .data-box {
      display: flex;
      justify-content: space-between;
      column-gap: 14.06rpx;
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
      .scan-icon-box {
        // #ifdef H5
        width: 46.88rpx;
        // #endif
        // #ifdef APP-PLUS
        width: 31.64rpx;
        // #endif
        height: 21.09rpx;
        display: flex;
        align-items: center;
        border-left: 1px solid #e1e3e5;
        padding-left: 9.38rpx;
        box-sizing: border-box;
      }
    }
  }
}
</style>
