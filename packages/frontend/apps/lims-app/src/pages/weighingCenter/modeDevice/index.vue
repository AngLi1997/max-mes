<template>
  <BMBasicPage
    :title="t('物料称量')"
    :confirm-text="t('下一步')"
    :cancel-text="t('上一步')"
    :default-padding="false"
    @left-click="handleCancel"
    @confirm="handleNextStep"
    @cancel="handleCancel"
  >
    <template #titleRight>
      <wd-button type="text" @click="toResult">
        {{ t("称量结果") }}
      </wd-button>
    </template>
    <view style="height: 100%">
      <view class="steps-box">
        <wd-steps :active="1" align-center>
          <wd-step :title="t('物料信息')" />
          <wd-step :title="t('设备&模式')" />
          <wd-step :title="t('清零去皮')" />
          <wd-step :title="t('称量')" />
        </wd-steps>
      </view>
      <view class="mode-device-content">
        <view class="label" style="margin-bottom: 11.72rpx;">
          {{ t("称量模式") }}
        </view>
        <wd-radio-group v-model="mode" shape="button">
          <wd-row :gutter="40">
            <wd-col v-if="hasAutoPermission" :span="12">
              <wd-radio :value="true">
                {{ t("物料称量") }}
              </wd-radio>
            </wd-col>
            <wd-col v-if="hasManualPermission" :span="12">
              <wd-radio :value="false">
                {{ t("手动称量") }}
              </wd-radio>
            </wd-col>
          </wd-row>
        </wd-radio-group>
        <view class="label" style="margin-top: 14.06rpx;">
          {{ t("秤具确认") }}
        </view>
        <view class="device-list-box">
          <template v-if="deviceList.length && mode">
            <wd-col v-for="device in deviceList" :key="device.balanceId" :span="12" style="margin-bottom: 5.86rpx;width: calc(50% - 4.69rpx);">
              <DeviceItem :item="device" :checked-id="selectedBalance.balanceId" @click="setSelectedBalance(device)" />
            </wd-col>
          </template>
          <BMNoData v-else type="emptyData" :text="t('无秤具详情')" />
        </view>
      </view>
    </view>
  </BMBasicPage>
  <wd-notify :safe-height="90" />
</template>

<script setup>
import { BMBasicPage, BMNoData } from '@/BMComponents';
import DeviceItem from '@/pages/weighingComponents/deviceItem/index.vue';
import { t } from '@/utils/useBmosI18n.js';
import { useModeDevice } from './hooks/useModeDevice.js';

const props = defineProps({
  id: {
    type: String,
    default: '',
  },
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
  setSelectedBalance,
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
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
  }
}
</style>
