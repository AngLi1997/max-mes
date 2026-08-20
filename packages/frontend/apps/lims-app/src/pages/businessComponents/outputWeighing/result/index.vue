<template>
  <BMLayout>
    <BMBasicPage :title="t('产出结果')" @left-click="toBack">
      <template #titleRight>
        <wd-button type="text" @click="close">
          {{ t("退出称量") }}
        </wd-button>
      </template>
      <view class="weighing-result-content">
        <Info
          :title="t('当前操作人')"
          icon="caozuoren"
          :basic-items="infoItems"
          :info-data="detailData"
        />
        <view class="table-box">
          <view class="label">
            {{ t("称量信息") }}
          </view>
          <BMTable :key="tableKey" ref="tableRef" v-bind="tableProps" @selection-change="tableSelectChange" />
        </view>
      </view>
      <template #buttons>
        <wd-row :gutter="16">
          <template v-if="tableProps.type === 'selection'">
            <wd-col :span="12">
              <wd-button type="warning" block @click="handleConfirmVoid">
                {{ t("确认作废") }}
              </wd-button>
            </wd-col>
            <wd-col :span="12">
              <wd-button type="info" block @click="handleCancelSelect">
                {{ t("取消") }}
              </wd-button>
            </wd-col>
          </template>
          <template v-else>
            <wd-col :span="6">
              <wd-button type="warning" block @click="handleVoid">
                {{ t("产出作废") }}
              </wd-button>
            </wd-col>
            <wd-col :span="6">
              <wd-button
                class-prefix="bmos-app-icon"
                icon="caozuoren"
                type="info"
                block
                @click="handleChangeSign"
              >
                {{ t("更换操作人") }}
              </wd-button>
            </wd-col>
            <wd-col :span="12">
              <wd-button block @click="sign">
                {{ t("签名") }}
              </wd-button>
            </wd-col>
          </template>
        </wd-row>
      </template>
    </BMBasicPage>
    <!-- 退出产出组件 -->
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
            <wd-button type="warning" block @click="goBackToTargetPath">
              {{ t("退出") }}
            </wd-button>
          </wd-col>
        </wd-row>
      </template>
    </BMMessageBox>
    <!-- 更换操作人签名 -->
    <BMDoubleSignModal
      v-model:show="signOpen1"
      v-model:value1="signValue1"
      v-model:value2="signValue2"
      :title="t('更换操作人')"
      :label-list1="labelList1"
      :label-list2="labelList2"
      :label-title1="t('当前产出人签名')"
      :label-title2="t('更换操作人签名')"
      :field-names="{
        value: 'loginName',
        label: 'userName',
        id: 'userId',
      }"
      :signature-data="signatureData1"
      @confirm="signConfirm1"
    />
    <!-- 签名 -->
    <BMSignModal
      v-model:show="signOpen3"
      v-model="signValue3"
      :title="t('签名')"
      :label-list="labelList3"
      :signature-data="signatureData3"
      show-remark
      @confirm="signConfirm3"
    />
    <!-- 作废签名 -->
    <BMSignModal
      v-model:show="signOpen4"
      v-model="signValue4"
      :title="t('签名')"
      :label-list="labelList4"
      :signature-data="signatureData4"
      show-remark
      @confirm="signConfirm4"
    />
    <BmosPrinter ref="bmosPrinterInstance" />
  </BMLayout>
</template>

<script setup>
import {
  BMBasicPage,
  BMDoubleSignModal,
  BMLayout,
  BMMessageBox,
  BMSignModal,
  BMTable,
} from '@/BMComponents/index.js';
import BmosPrinter from '@/components/BmosPrinter/index.vue';
import Info from '@/pages/weighingComponents/info';
import { t } from '@/utils/useBmosI18n.js';
import { useTable } from './hooks/useTable.jsx';

const props = defineProps({
  componentId: {
    type: String,
    default: '',
  },
});

const {
  bmosPrinterInstance,
  showQuitModal,
  infoItems,
  detailData,
  tableRef,
  tableProps,
  tableKey,
  signOpen1,
  signOpen3,
  signOpen4,
  signValue1,
  signValue2,
  signValue3,
  signValue4,
  labelList1,
  labelList2,
  labelList3,
  labelList4,
  signatureData1,
  signatureData3,
  signatureData4,
  goBackToTargetPath,
  toBack,
  close,
  sign,
  handleChangeSign,
  handleVoid,
  handleCancelSelect,
  handleConfirmVoid,
  signConfirm1,
  signConfirm3,
  signConfirm4,
  tableSelectChange,
} = useTable({ ...props });
</script>

<style lang="scss" scoped>
.weighing-result-content {
  height: 100%;
  overflow: hidden;
  .label {
    margin: 9.38rpx 0;
    font-size: 14.06rpx;
    color: var(--bmos-color-text-title);
    line-height: 16.41rpx;
  }
  .table-box {
    height: calc(100% - 32.81rpx - 18.75rpx - 16.41rpx);
  }
}
</style>
