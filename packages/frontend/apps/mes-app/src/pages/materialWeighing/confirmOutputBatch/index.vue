<template>
  <BMLayout>
    <BMBasicPage
      :title="t('物料称量')"
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
            </view>{{ signValue?.userName1 ? `${signValue.userName1} - ${signValue.loginName1}` : '-' }}
          </view>
          <wd-divider vertical />
          <view class="person">
            <view class="title">
              {{ t('复核人') }}:
            </view>{{ signValue?.userName2 ? `${signValue.userName2} - ${signValue.loginName2}` : '-' }}
          </view>
        </view>
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
      :title="t('称量人员确认')"
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
  BMIcon,
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
  signValue,
  signOpen,
  signatureData,
  labelList,
  formsRef,
  relatedFormsRef,
  formProps,
  relatedFormProps,
  query,
  signConfirm,
  submit,
  toBack,
} = usePageData();

onLoad((e) => {
  query.value = parseUrlQuery(e);
  if (!query.value.continue) {
    formsRef.value?.resetForm();
    relatedFormsRef.value?.resetForm();
  }
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
    color: #242526;
    font-size: 10.55rpx;
    gap: 4.69rpx;
    .title {
      color: #6c6e73;
    }
  }
}
</style>
