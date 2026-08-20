<!-- 称量数据-设备模式 -->
<template>
  <BMLayout>
    <BMBasicPage
      :title="t('称量')"
      :cancel-text="t('取消')"
      :confirm-text="t('下一步')"
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
      <view class="mode-device-box">
        <ModeDevice v-model="mode" :active="0" :step-list="stepList" :mode-list="modeList" :device-list="deviceList" />
      </view>
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
  import { BMBasicPage, BMLayout } from '@/BMComponents';
  import ModeDevice from '@/pages/weighingComponents/modeDevice/index.vue';
  import { parseUrlQuery } from '@/utils/url';
  import { t } from '@/utils/useBmosI18n.js';
  import { onLoad } from '@dcloudio/uni-app';
  import { useModeDevice } from './hooks/useModeDevice.js';

  const {
    query,
    mode,
    modeList,
    stepList,
    deviceList,
    handleCancel,
    handleNextStep,
    toResult
  } = useModeDevice();

  onLoad((e) => {
    query.value = parseUrlQuery(e);
  });
</script>

<style lang="scss" scoped>
.mode-device-box {
  height: 100%;
}
</style>
