<template>
  <view class="mode-device-content">
    <BMSteps :active="active" :step-list="stepList" />
    <view class="scroll-box">
      <view class="label">
        {{ t("称量模式") }}
      </view>
      <wd-radio-group v-model="mode" shape="button" class="radio-group-box">
        <template v-for="item in showModeList" :key="item.value">
          <wd-radio :value="item.value">
            {{ item.label }}
          </wd-radio>
        </template>
      </wd-radio-group>
      <view class="label">
        {{ t("秤具确认") }}
      </view>
      <view class="device-list-box">
        <template v-if="showDeviceList">
          <wd-col
            v-for="device in deviceList"
            :key="device.balanceId"
            :span="12"
            style="margin-bottom: 5.86rpx;width: calc(50% - 4.69rpx);"
          >
            <DeviceItem
              :item="device"
              :checked-id="selectedBalance.balanceId"
              @click="setSelectedBalance(device)"
            />
          </wd-col>
        </template>
        <BMNoData v-else type="emptyData" :text="t('无秤具详情')" />
      </view>
    </view>
  </view>
</template>

<script setup>
import { BMNoData, BMSteps } from '@/BMComponents';
import DeviceItem from '@/pages/weighingComponents/deviceItem/index.vue';
import { useWeighingMachineStore } from '@/stores/weighingMachine/index.js';
import { t } from '@/utils/useBmosI18n.js';
import { storeToRefs } from 'pinia';
import { computed, watch } from 'vue';

const weighingMachineStore = useWeighingMachineStore();
const { selectedBalance } = storeToRefs(weighingMachineStore);
const { setSelectedBalance } = weighingMachineStore;

const props = defineProps({
  modelValue: {
    type: Number,
    default: 0,
  },
  modeList: {
    type: Array,
    default: () => [],
  },
  deviceList: {
    type: Array,
    default: () => [],
  },
  active: {
    type: Number,
    default: 0,
  },
  stepList: {
    type: Array,
    default: () => [],
  },
});
const emit = defineEmits(['update:modelValue']);

const mode = computed({
  get: () => props.modelValue,
  set: value => emit('update:modelValue', value),
});

const showModeList = computed(() => {
  return props.modeList.filter(item => item.show);
});

const showDeviceList = computed(() => {
  try {
    return (
      props.deviceList.length && showModeList.value[mode.value]?.showDeviceList
    );
  }
  catch (error) {
    return false;
  }
});
watch(
  () => showModeList.value,
  (value) => {
    if (value.length === 0) {
      mode.value = null;
    }
    else {
      mode.value = value[0].value;
    }
  },
  { immediate: true },
  );

watch(
  () => props.deviceList,
  (value) => {
    if (value.length > 0) {
      setSelectedBalance(value[0]);
    }
    else {
      setSelectedBalance({});
    }
  },
  { immediate: true },
  );
</script>

<style lang="scss" scoped>
.mode-device-content {
  height: 100%;
  .scroll-box {
    padding: 0 9.38rpx 8.2rpx;
    box-sizing: border-box;
    height: calc(100% - 43.36rpx);
    overflow: auto;
    .label {
      font-size: 14.06rpx;
      color: var(--bmos-color-text-title);
      line-height: 16.41rpx;
      margin: 8.2rpx 0 11.72rpx;
    }
    .radio-group-box {
      display: flex;
      flex-wrap: wrap;
      justify-content: space-between;
      row-gap: 11.72rpx;
      margin-bottom: 14.06rpx;
    }
    :deep(.wd-radio) {
      width: calc(50% - 11.72rpx);
      margin-right: 0;
      .wd-radio__label {
        width: 100%;
        max-width: none;
        text-align: center;
      }
    }
    .device-list-box {
      height: calc(100% - 151.17rpx);
      position: relative;
      box-sizing: border-box;
      display: flex;
      flex-wrap: wrap;
      justify-content: space-between;
    }
  }
}
</style>
