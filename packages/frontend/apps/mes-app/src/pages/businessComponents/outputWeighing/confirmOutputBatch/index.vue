<template>
  <BMLayout>
    <BMBasicPage
      :title="t('中间品产出')"
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
      <view class="container">
        <BMSteps :active="0" :step-list="stepList" />
        <view class="form-box">
          <BMForm ref="formsRef" v-bind="formProps" />
          <BMForm ref="relatedFormsRef" v-bind="relatedFormProps" />
        </view>
      </view>
    </BMBasicPage>

    <!-- 签名组件 -->
    <BMSignModal
      v-model:show="signOpen"
      v-model="signValue"
      :title="t('产出人员确认')"
      :label-list="labelList"
      :signature-data="signatureData"
      :field-names="{
        value: 'loginName',
        label: 'userName',
        id: 'userId',
      }"
      show-remark
      @confirm="signConfirm"
      @cancel="toBack"
    />
  </BMLayout>
</template>

<script setup>
import {
  BMBasicPage,
  BMForm,
  BMLayout,
  BMSignModal,
  BMSteps,
} from '@/BMComponents/index.js';
import { parseUrlQuery } from '@/utils/url';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad } from '@dcloudio/uni-app';
import { useConst } from '../hooks/useConst';
import { usePageData } from './hooks';

const { stepList } = useConst();
const {
  query,
  signValue,
  signOpen,
  signatureData,
  labelList,
  formsRef,
  relatedFormsRef,
  formProps,
  relatedFormProps,
  signConfirm,
  submit,
  toBack,
  toResult,
} = usePageData();

onLoad((e) => {
  query.value = parseUrlQuery(e);
});
</script>

<style lang="scss" scoped>
.container {
  width: 100%;
  height: 100%;
  overflow: hidden;
  .form-box {
    height: calc(100% - 33.98rpx);
    overflow-y: auto;
  }
}
</style>
