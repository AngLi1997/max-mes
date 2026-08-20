<template>
  <BMLayout>
    <BMBasicPage :title="t('产出结果')" @left-click="leftClick">
      <template #titleRight>
        <wd-button type="text" @click="exitWeighing">
          {{ t("退出") }}
        </wd-button>
      </template>
      <view class="weighing-result-content">
        <Info
          :title="t('当前操作人')"
          icon="caozuoren"
          :basic-items="infoItems"
          :info-data="resultDetail"
        />
        <view class="title-box">
          {{ t("产出信息") }}
        </view>
        <view class="table-box">
          <BMTable
            :key="tableKey"
            ref="tableRef"
            v-bind="tableProps"
            @selection-change="tableSelection"
          />
        </view>
      </view>
      <template #buttons>
        <wd-row v-if="tableProps.type" :gutter="16">
          <wd-col :span="12">
            <wd-button type="warning" block @click="confirmInvalid">
              {{ t("确认作废") }}
            </wd-button>
          </wd-col>
          <wd-col :span="12">
            <wd-button block @click="cancelInvalid">
              {{ t("取消") }}
            </wd-button>
          </wd-col>
        </wd-row>
        <wd-row v-else :gutter="16">
          <wd-col :span="6">
            <wd-button type="warning" block @click="handleInvalid">
              {{ t("产出作废") }}
            </wd-button>
          </wd-col>
          <wd-col :span="6">
            <wd-button type="info" block @click="handleReplace">
              {{ t("更换操作人") }}
            </wd-button>
          </wd-col>
          <wd-col :span="12">
            <wd-button block @click="handleSign">
              {{ t("签名") }}
            </wd-button>
          </wd-col>
        </wd-row>
      </template>
    </BMBasicPage>
    <BMMessageBox
      v-model="showQuitModal"
      :title="t('存在未签名物料件，是否退出?')"
      :close-on-click-modal="false"
    >
      <template #buttons>
        <wd-row :gutter="16">
          <wd-col :span="12">
            <wd-button type="info" block @click="showQuitModal = false">
              {{ t("取消") }}
            </wd-button>
          </wd-col>
          <wd-col :span="12">
            <wd-button type="warning" block @click="quitConfirm">
              {{ t("退出") }}
            </wd-button>
          </wd-col>
        </wd-row>
      </template>
    </BMMessageBox>
    <!-- 签名弹窗 -->
    <BMSignModal
      v-model:show="showSignModal"
      v-model="signValue"
      :title="t('量取结果确认')"
      show-remark
      :signature-data="{
        progressId,
        producerId: signValue.userId1,
        reCheckerId: signValue.userId2,
        remark: signValue.remark,
      }"
      :label-list="labelList"
      @confirm="signConfirm"
    />
    <!-- 更换操作人弹窗 -->
    <ReplaceOperator
      v-model:show="showReplaceModal"
      v-model="replaceValue"
      :label-list1="labelList1"
      :label-list2="replaceLabelList"
      :signature-data="{
        progressId,
        producerId: replaceValue.userId1,
        reCheckerId: replaceValue.userId2,
      }"
      @confirm="replaceConfirm"
    />
    <!-- 产出作废签名弹窗 -->
    <BMSignModal
      v-model:show="showInvalidModal"
      v-model="invalidSignValue"
      :title="t('作废确认')"
      show-remark
      :signature-data="{
        progressId,
        scrapStorageMaterialIdList: selectedTableRows.map(item => item.id),
        remark: invalidSignValue.remark,
        producerId: invalidSignValue.userId1,
        reCheckerId: invalidSignValue.userId2,
      }"
      :label-list="invalidLabelList"
      @confirm="invalidConfirm"
    />
    <!-- 打印 -->
    <BmosPrinter ref="bmosPrinterInstance" />
  </BMLayout>
</template>

<script setup>
import {
  BMBasicPage,
  BMLayout,
  BMMessageBox,
  BMSignModal,
  BMTable,
} from '@/BMComponents';
import BmosPrinter from '@/components/BmosPrinter';
import Info from '@/pages/weighingComponents/info';
import ReplaceOperator from '@/pages/weighingComponents/replaceOperator';
import { t } from '@/utils/useBmosI18n.js';
import { useResult } from './hooks/useResult.jsx';

const props = defineProps({
  progressId: {
    type: String,
    default: '',
  },
});
const {
  bmosPrinterInstance,
  resultDetail,
  infoItems,
  tableKey,
  tableRef,
  tableProps,
  selectedTableRows,
  showQuitModal,
  showSignModal,
  showInvalidModal,
  signValue,
  labelList,
  labelList1,
  replaceLabelList,
  showReplaceModal,
  replaceValue,
  invalidSignValue,
  invalidLabelList,
  leftClick,
  exitWeighing,
  handleInvalid,
  confirmInvalid,
  cancelInvalid,
  invalidConfirm,
  handleReplace,
  handleSign,
  signConfirm,
  quitConfirm,
  replaceConfirm,
  tableSelection,
} = useResult({ props });
</script>

<style lang="scss" scoped>
.weighing-result-content {
  height: 100%;

  .title-box {
    color: var(--bmos-color-text-title);
    font-size: 14.06rpx;
    margin: 9.38rpx auto;
  }
  .table-box {
    height: calc(100% - 32.81rpx - 35.16rpx - 5.86rpx);
  }
}
</style>
