<template>
  <BMLayout>
    <BMBasicPage
      :title="t('配液产出')"
      :confirm-text="t('下一步')"
      :default-padding="false"
      :disabled-confirm="disabledConfirm"
      @left-click="leftClick"
      @confirm="handleNextStep"
      @cancel="handleCancel"
    >
      <template #titleRight>
        <wd-button type="text" @click="toResult">
          {{ t("产出结果") }}
        </wd-button>
      </template>
      <view style="height: 100%">
        <view class="steps-box">
          <wd-steps :active="0" align-center>
            <wd-step :title="t('物料信息')" />
            <wd-step :title="t('设备&模式')" />
            <wd-step :title="t('产出')" />
          </wd-steps>
        </view>
        <view class="execution-content">
          <view class="info-box">
            <Info
              :basic-items="infoItems"
              :info-data="planVO || instance.planVO"
            />
          </view>
          <BMForm ref="formRef" v-bind="formProps" />
        </view>
      </view>
    </BMBasicPage>
    <!-- 切换配液单 -->
    <BMRadioModal
      v-model="liquidPreparationListValue"
      v-model:open="showSwitchingLiquidPreparationList"
      :title="t('配液单选择')"
      :options="liquidPreparationListOptions"
      :field-names="{
        label: 'name',
        value: 'id',
      }"
      @cancel="handleLiquidPreparationListCancel"
      @confirm="handleLiquidPreparationListConfirm"
    />
    <BMSignModal
      v-model:show="showSign"
      v-model="signValue"
      show-remark
      :title="t('产出人员确认')"
      :label-list="labelList"
      :field-names="{
        value: 'loginName',
        label: 'userName',
        id: 'userId',
      }"
      :signature-data="signConfirmParams"
      @confirm="signConfirm"
    />
  </BMLayout>
</template>

<script setup>
import { BMBasicPage, BMForm, BMLayout, BMRadioModal, BMSignModal } from '@/BMComponents';
import Info from '@/pages/weighingComponents/info';
import { t } from '@/utils/useBmosI18n.js';
import { useExecution } from './hooks/useExecution.jsx';

const props = defineProps({
  componentId: {
    type: String,
    default: '',
  },
});
const {
  instance,
  planVO,
  infoItems,
  formRef,
  formProps,
  showSign,
  signValue,
  showSwitchingLiquidPreparationList,
  liquidPreparationListValue,
  liquidPreparationListOptions,
  labelList,
  signConfirmParams,
  disabledConfirm,
  leftClick,
  toResult,
  signConfirm,
  handleNextStep,
  handleCancel,
  handleLiquidPreparationListCancel,
  handleLiquidPreparationListConfirm,
} = useExecution({ props });
</script>

<style lang="scss" scoped>
.steps-box {
  background-color: var(--bmos-bg-color);
}
.execution-content {
  height: calc(100% - 37.5rpx - 9.38rpx);
  margin-top: 9.38rpx;
  .info-box {
    margin-bottom: 2.34rpx;
  }
}
</style>
