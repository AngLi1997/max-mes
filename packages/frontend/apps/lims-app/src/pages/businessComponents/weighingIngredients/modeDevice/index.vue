<template>
  <BMLayout>
    <BMBasicPage
      :title="t('配料称量')"
      :cancel-text="t('上一步')"
      :confirm-text="t('下一步')"
      :default-padding="false"
      @left-click="toBack"
      @confirm="submit"
      @cancel="toBack"
    >
      <template #titleRight>
        <wd-button type="text" @click="toResult">
          {{ t("称量结果") }}
        </wd-button>
      </template>
      <view class="mode-device-box">
        <ModeDevice v-model="mode" :active="1" :step-list="stepList" :mode-list="modeList" :device-list="deviceList" />
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
import { useConst } from '../hooks/useConst.js';
import { useModeDevice } from './hooks/useModeDevice.js';

const { stepList } = useConst();
const { query, mode, modeList, deviceList, weightMode, toBack, submit, toResult,
} = useModeDevice();

onLoad((e) => {
  query.value = parseUrlQuery(e);
  try {
    if (JSON.parse(query.value.configInfo).weightMode && JSON.parse(query.value.configInfo).weightMode.length !== 0) {
      weightMode.value = JSON.parse(query.value.configInfo).weightMode;
    }
    else {
      weightMode.value = [0, 1];
    }
  }
  catch (error) {
    weightMode.value = [0, 1];
  }
});
</script>

<style lang="scss" scoped>
.mode-device-box {
  height: 100%;
  overflow: hidden;
}
</style>
