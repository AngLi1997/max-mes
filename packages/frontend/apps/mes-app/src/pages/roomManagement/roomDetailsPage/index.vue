<template>
  <BMLayout>
    <BMBasicPage
      :title="t('房间详情')"
      :default-padding="false"
      :show-buttons="false"
      @left-click="toBack"
    >
      <view class="container">
        <BMInfoDisplay
          v-if="specifics"
          :title="specifics?.name ?? '-'"
          icon="fangjian"
          :basic-items="basicItems"
          :info-data="specifics"
          background
        />
        <view v-if="nowStatus === 1" class="msg_box">
          <view class="msg_title">
            {{ t('使用信息') }}
          </view>
          <BMInfoDisplay
            v-if="specifics"
            :is-show-title="false"
            :basic-items="[
              {
                label: t('产品名称'),
                field: 'productName',
              },
              {
                label: t('产品批号'),
                field: 'batchNo',
              },
              {
                label: t('工序'),
                field: 'procedureName',
              },
            ]"
            :info-data="specifics"
            background
          />
        </view>
        <view class="btn_box">
          <wd-button v-if="nowStatus !== 2" :style="{ width: nowStatus === 3 ? '32%' : '49%' }" type="info" size="medium" @click="toBeClear">
            {{ t('待清场') }}
          </wd-button>
          <wd-button v-if="nowStatus !== 1" :style="{ width: nowStatus === 3 ? '32%' : '49%' }" type="info" size="medium" @click="toBeUsed(1)">
            {{ t('使用') }}
          </wd-button>
          <wd-button :style="{ width: nowStatus === 3 ? '32%' : '49%' }" size="medium" @click="toBeUsed(3)">
            {{ t('清场') }}
          </wd-button>
        </view>
      </view>
    </BMBasicPage>
    <BMModal
      v-model="open"
      :title="t('生产信息填报')"
      size="large"
      @cancel="open = false"
      @confirm="submitForm"
    >
      <BMForm
        ref="formRef"
        v-bind="formProps"
      />
    </BMModal>
    <!-- 签名组件 -->
    <BMSignModal
      v-model:show="signOpen"
      v-model="signValue"
      :title="t('房间状态修改签名')"
      :signature-data="signatureParams"
      :label-list="labelList"
      show-remark
      remark-required
      @confirm="submitSign"
    />
  </BMLayout>
</template>

<script setup>
import { BMBasicPage, BMForm, BMInfoDisplay, BMLayout, BMModal, BMSignModal } from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad, onShow } from '@dcloudio/uni-app';
import { useData } from './hooks/useData';

const {
  specifics,
  basicItems,
  tabId,
  nowStatus,
  formProps,
  open,
  formRef,
  isEst,
  signOpen,
  signValue,
  signatureParams,
  labelList,
  toBeClear,
  submitForm,
  submitSign,
  toBeUsed,
  getRoomInfo,
} = useData();
// 返回
const toBack = () => {
  uni.navigateBack();
};
onLoad((e) => {
  // #ifdef APP-PLUS
  const query = Object.fromEntries(
    Object.keys(e).map(key => [
      decodeURIComponent(key),
      decodeURIComponent(e[key]),
    ]),
  );
  tabId.value = query?.id;
  // #endif
  // #ifdef H5
  tabId.value = e?.id;
  // #endif
});
onShow(() => {
  if (isEst.value) {
    getRoomInfo();
  }
});
</script>

<style lang="scss" scoped>
  .container {
    width: 100%;
    height: 100%;
    overflow: hidden;
    box-sizing: border-box;
    padding: 9.38rpx;
    background: linear-gradient(180deg, #ECF3FE 0%, #FFFFFF 20%);
    border-top-left-radius: 11.72rpx;
    border-top-right-radius: 11.72rpx;
    .btn_box {
      display: flex;
      align-items: center;
      justify-content: space-between;
    }
    .msg_box {
      border-top: 1px solid #E1E3E5;
      padding-top: 9.38rpx;
      .msg_title{
        padding-left: 9.38rpx;
        font-family: Source Han Sans CN;
        font-size: 14.06rpx;
        font-weight: 400;
        line-height: 16.41rpx;
      }
      :deep(.info-display-container) {
        padding-top: 0;
      }
    }
  }
</style>
