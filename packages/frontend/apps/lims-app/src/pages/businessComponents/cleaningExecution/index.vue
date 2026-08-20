<template>
  <BMLayout>
    <BMBasicPage
      :title="t('房间清场执行')"
      :confirm-text="t('完成')"
      background-color="#F2F3F5"
      @left-click="toBack"
      @cancel="toBack"
      @confirm="submit"
    >
      <view class="container">
        <BMInfoDisplay
          icon="fangjian"
          :title="t('清场房间信息')"
          :basic-items="[
            {
              label: t('房间名称'),
              field: 'name',
            },
            {
              label: t('房间编码'),
              field: 'code',
            },
          ]"
          :info-data="specifics"
        />
        <BMForm
          ref="roomFormsRef"
          v-bind="formProps"
        />
      </view>
      <BMSignModal
        v-model:show="signOpen"
        v-model="signValue"
        :title="t('房间状态修改签名')"
        :label-list="labelList"
        :signature-data="signatureParams"
        :show-remark="remark"
        :remark-required="remark"
        :field-names="{
          value: 'loginName',
          label: 'userName',
          id: 'userId',
        }"
        @confirm="submitSign"
      />
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
import { BMBasicPage, BMForm, BMInfoDisplay, BMLayout, BMSignModal } from '@/BMComponents/index.js';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad } from '@dcloudio/uni-app';
import { useNotify } from 'wot-design-uni';
import { useForm, useParams } from './hooks';

const { showNotify } = useNotify();
const UseParams = useParams();
const { specifics, signOpen, labelList, paramsData, remark } = UseParams;
const { roomFormsRef, signValue, signatureParams, formProps, submit, submitSign, getRoomInfo, getRoomAuthUser } = useForm({ UseParams, showNotify });
const toBack = () => {
  uni.navigateBack();
};
onLoad((e) => {
  // #ifdef APP-PLUS
  const query = Object.fromEntries(Object.keys(e)
    .map(key => [decodeURIComponent(key), decodeURIComponent(e[key])]));
  paramsData.value = query;
  getRoomInfo();
  // #endif
  // #ifdef H5
  paramsData.value = e;
  getRoomInfo();
  // #endif
  getRoomAuthUser();
});
</script>

<style lang="scss" scoped>
.container {
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 9.38rpx;
  margin: 9.38rpx 0;
}
</style>
