<template>
  <BMBasicPage
    :title="t('称量结果')"
    @left-click="leftClick"
  >
    <template #titleRight>
      <wd-button type="text" @click="exitWeighing">
        {{ t("退出称量") }}
      </wd-button>
    </template>
    <view class="weighing-result-content">
      <Info
        :title="t('当前操作人')"
        icon="caozuoren"
        :basic-items="infoItems"
        :info-data="resultDetail"
      />
      <view class="segmented-box">
        <wd-segmented
          v-model:value="currentSegmented"
          :options="segmentedOptions"
          @change="segmentedChange"
        >
          <template #label="{ option }">
            {{ option.label }}
          </template>
        </wd-segmented>
      </view>
      <view class="table-box">
        <BMTable ref="tableRef" v-bind="tableProps" />
      </view>
    </view>
    <template #buttons>
      <wd-row :gutter="16">
        <wd-col :span="6">
          <wd-button type="info" block @click="handleReplace">
            {{ t('更换操作人') }}
          </wd-button>
        </wd-col>
        <wd-col :span="18">
          <wd-button block @click="handleSign">
            {{ t('签名') }}
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
            {{ t('取消') }}
          </wd-button>
        </wd-col>
        <wd-col :span="12">
          <wd-button type="warning" block @click="quitConfirm">
            {{ t('退出') }}
          </wd-button>
        </wd-col>
      </wd-row>
    </template>
  </BMMessageBox>
  <!-- 签名弹窗 -->
  <BMSignModal
    v-model:show="showSignModal"
    v-model="signValue"
    :title="t('称量人员确认')"
    show-remark
    :label-list="labelList"
    :signature-data="{ ticketId: id, remark: signValue.remark }"
    @confirm="signConfirm"
  />
  <!-- 更换操作人弹窗 -->
  <ReplaceOperator
    v-model:show="showReplaceModal"
    v-model="replaceValue"
    :label-list1="labelList1"
    :label-list2="replaceLabelList"
    :signature-data="{ taskId: id, weigherId: replaceValue.userId1, reCheckerId: replaceValue.userId2 }"
    @confirm="replaceConfirm"
  />
  <!-- 打印 -->
  <BmosPrinter ref="bmosPrinterInstance" />
  <wd-notify :safe-height="90" />
</template>

<script setup>
import {
  BMBasicPage,
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
  id: {
    type: String,
    default: '',
  },
});
const {
  bmosPrinterInstance,
  resultDetail,
  infoItems,
  segmentedOptions,
  currentSegmented,
  tableRef,
  tableProps,
  showQuitModal,
  showSignModal,
  signValue,
  labelList,
  labelList1,
  replaceLabelList,
  showReplaceModal,
  replaceValue,
  leftClick,
  exitWeighing,
  handleReplace,
  handleSign,
  signConfirm,
  quitConfirm,
  replaceConfirm,
  segmentedChange,
} = useResult({ props });
</script>

  <style lang="scss" scoped>
  .weighing-result-content {
  height: 100%;

  .segmented-box {
    margin: 9.38rpx auto;
  }
  .table-box {
    height: calc(100% - 32.81rpx - 31.64rpx - 18.75rpx - 5.86rpx);
  }
  :deep(.uni-table) {
    width: 1928.32rpx;
  }
}
</style>
