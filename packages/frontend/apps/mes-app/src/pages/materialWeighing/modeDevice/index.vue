<template>
  <BMLayout>
    <BMBasicPage
      :title="t('物料称量')"
      :cancel-text="t('上一步')"
      :confirm-text="t('下一步')"
      :default-padding="false"
      @left-click="toBack"
      @confirm="submit"
      @cancel="toBack"
    >
      <template #titleRight>
        <view class="weigh-user">
          <BMIcon name="caozuoren" size="10.55rpx" color="#2871FF" />
          <view class="person">
            <view class="title">
              {{ t('称量人') }}:
            </view>{{ storeSignValue.userName1 ? `${storeSignValue.userName1} - ${storeSignValue.loginName1}` : '-' }}
          </view>
          <wd-divider vertical />
          <view class="person">
            <view class="title">
              {{ t('复核人') }}:
            </view>{{ storeSignValue.userName2 ? `${storeSignValue.userName2} - ${storeSignValue.loginName2}` : '-' }}
          </view>
        </view>
      </template>
      <view class="mode-device-box">
        <ModeDevice v-model="mode" :active="1" :step-list="stepList" :mode-list="modeList" :device-list="deviceList" />
      </view>
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
import { BMBasicPage, BMIcon, BMLayout } from '@/BMComponents';
import ModeDevice from '@/pages/weighingComponents/modeDevice/index.vue';
import { useMaterialWeighingStore } from '@/stores/workbench/materialWeighing/index.js';
import { parseUrlQuery } from '@/utils/url';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad } from '@dcloudio/uni-app';
import { storeToRefs } from 'pinia';
import { useConst } from '../hooks/useConst.js';
import { useModeDevice } from './hooks/useModeDevice.js';

const { stepList } = useConst();
const { query, mode, modeList, deviceList, toBack, submit,
} = useModeDevice();

const materialWeighingStore = useMaterialWeighingStore();
const { storeSignValue } = storeToRefs(materialWeighingStore);
onLoad((e) => {
  query.value = parseUrlQuery(e);
});
</script>

<style lang="scss" scoped>
.mode-device-box {
  height: 100%;
  overflow: hidden;
}
.weigh-user {
  display: inline-flex;
  padding: 2.34rpx 4.69rpx;
  align-items: center;
  gap: 4.69rpx;
  border-radius: 2.34rpx;
  background: linear-gradient(90deg, #ebf2ff 0%, #fff 100%);
  line-height: 12.89rpx;
  .person {
    display: inline-flex;
    align-items: center;
    font-size: 10.55rpx;
    color: #242526;
    gap: 4.69rpx;
    .title {
      color: #6c6e73;
    }
  }
}
</style>
