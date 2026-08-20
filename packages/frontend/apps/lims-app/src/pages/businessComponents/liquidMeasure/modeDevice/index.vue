<template>
  <BMBasicPage
    :title="t('配液量取')"
    :confirm-text="t('下一步')"
    :cancel-text="t('上一步')"
    :default-padding="false"
    @left-click="handleCancel"
    @confirm="handleNextStep"
    @cancel="handleCancel"
  >
    <template #titleRight>
      <wd-button type="text" @click="toResult">{{ t("量取结果") }}</wd-button>
    </template>
    <view style="height: 100%">
      <view class="steps-box">
        <wd-steps :active="1" align-center>
          <wd-step :title="t('物料信息')" />
          <wd-step :title="t('设备&模式')" />
          <wd-step :title="t('量取')" />
        </wd-steps>
      </view>
      <view class="mode-device-content">
        <view class="label" style="margin-bottom: 11.72rpx;">{{ t("量取模式") }}</view>
        <wd-radio-group v-model="mode" shape="button">
          <wd-row :gutter="40">
            <wd-col v-if="hasAutoPermission" :span="12">
              <wd-radio :value="true">{{ t("配液量取") }}</wd-radio>
            </wd-col>
            <wd-col v-if="hasManualPermission" :span="12">
              <wd-radio :value="false">{{ t("手动量取") }}</wd-radio>
            </wd-col>
          </wd-row>
        </wd-radio-group>
        <view class="label" style="margin-top: 14.06rpx;">
          {{ t("设备确认") }}
        </view>
        <view class="device-list-box">
          <scroll-view
            v-if="deviceList.length && mode"
            style="height: 100%;"
            scroll-y="true"
          >
            <wd-row :gutter="46">
              <wd-col v-for="device in deviceList" :key="device.balanceId" :span="12" style="margin-bottom: 5.86rpx;">
                <DeviceItem :item="device" :checked-id="selectedBalance.balanceId" @click="setSelectedBalance(device)" />
              </wd-col>
            </wd-row>
          </scroll-view>
          <BMNoData v-else type="emptyData" :text="t('无设备详情')" />
        </view>
      </view>
    </view>
  </BMBasicPage>
  <wd-notify :safe-height="90" />
</template>

<script setup>
  import { t } from '@/utils/useBmosI18n.js';
  import { BMBasicPage, BMNoData } from '@/BMComponents';
  import { useModeDevice } from './hooks/useModeDevice.js';
  import DeviceItem from '@/pages/weighingComponents/deviceItem/index.vue';

  const props = defineProps({
    id: {
      type: String,
      default: ''
    },
    measureBatchId: {
      type: String,
      default: ''
    },
    componentId: {
      type: String,
      default: ''
    }
  });
  const {
    mode,
    deviceList,
    selectedBalance,
    hasAutoPermission,
    hasManualPermission,
    handleCancel,
    handleNextStep,
    toResult,
    setSelectedBalance
  } = useModeDevice({ props });
</script>

<style lang="scss" scoped>
.steps-box {
  background-color: var(--bmos-bg-color);
}
.mode-device-content {
  padding: 8.2rpx 9.38rpx;
  box-sizing: border-box;
  height: calc(100% - 37.5rpx);
  .label {
    font-size: 14.06rpx;
    color: var(--bmos-color-text-title);
    line-height: 16.41rpx;
  }
  :deep(.wd-radio) {
    width: 100%;
    .wd-radio__label {
      width: 100%;
      max-width: none;
      text-align: center;
    }
  }
  .device-list-box {
    height: calc(100% - 86.72rpx);
    position: relative;
    padding-top: 13.48rpx;
    box-sizing: border-box;
  }
}
</style>
