<template>
  <BMLayout>
    <BMBasicPage
      :title="t('房间详情')"
      background-color="#F2F3F5"
      @left-click="toBack"
    >
      <view style="padding-top: 9.38rpx;">
        <BMInfoDisplay
          icon="fangjian"
          :title="specifics?.name || '-'"
          :basic-items="[
            {
              label: t('房间编码'),
              field: 'code',
            },
            {
              label: t('房间状态'),
              field: 'status',
              tag: stateCorol[specifics?.status?.value],
            },
            {
              label: t('有效期至'),
              field: 'expireTime',
              valueStyle: isDate ? {
                color: '#ee3e12',
              } : {},
            },
          ]"
          :info-data="{
            code: specifics?.code,
            status: currentState[specifics?.status?.value],
            expireTime: specifics?.expireTime,
          }"
        />
      </view>
      <template #buttons>
        <wd-row :gutter="16">
          <wd-col :span="tabData.componentType === segmentedList.CLEAN_CHECK ? 6 : 12">
            <wd-button type="info" block @click="toBack">
              {{ t('取消') }}
            </wd-button>
          </wd-col>
          <wd-col v-if="tabData.componentType === segmentedList.CLEAN_CHECK" :span="6">
            <wd-button block :loading="loading" type="success" @click="submitConfirm">
              {{ t('确认') }}
            </wd-button>
          </wd-col>
          <wd-col :span="12">
            <wd-button block :loading="loading" @click="tabData.componentType === segmentedList.CLEAN_INFO ? inUseSubmit() : ToBeClearedSubmit()">
              {{ tabData.componentType === segmentedList.CLEAN_INFO ? t('确定') : t('清场') }}
            </wd-button>
          </wd-col>
        </wd-row>
      </template>
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
import {
  BMBasicPage,
  BMInfoDisplay,
  BMLayout,
} from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad, onShow } from '@dcloudio/uni-app';
import { useNotify } from 'wot-design-uni';
import { currentState, segmentedList, stateCorol } from './enum';

import { useColumns, useParams } from './hooks';

const { showNotify } = useNotify();
const UseParams = useParams();
const { isDate, isEin, tabData, specifics } = UseParams;
const { getRoomInfo, inUseSubmit, ToBeClearedSubmit, submitConfirm } = useColumns({ UseParams, showNotify });
// 返回
const toBack = () => {
  uni.navigateBack();
};
onLoad((e) => {
  // #ifdef APP-PLUS
  const query = Object.fromEntries(Object.keys(e)
    .map(key => [decodeURIComponent(key), decodeURIComponent(e[key])]));
  tabData.value = query;
  getRoomInfo();
  // #endif
  // #ifdef H5
  tabData.value = e;
  getRoomInfo();
  // #endif
});
onShow(() => {
  if (isEin.value) {
    getRoomInfo();
  }
});
</script>

<style lang="scss" scoped>

</style>
