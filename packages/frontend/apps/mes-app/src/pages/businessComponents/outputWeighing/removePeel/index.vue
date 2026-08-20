<template>
  <BMLayout>
    <BMBasicPage
      :title="t('中间品产出')"
      :cancel-text="t('上一步')"
      :confirm-text="confirmText"
      :default-padding="false"
      :loading="loading"
      @left-click="toBack"
      @cancel="previousStep"
      @confirm="submit"
    >
      <template #titleRight>
        <wd-button type="text" @click="toResult">
          {{ t("产出结果") }}
        </wd-button>
      </template>
      <view class="container">
        <BMSteps :active="stepActive" :step-list="stepList" />
        <view class="content">
          <BMInfoDisplay
            :title="t('产出批次')"
            icon="xinxi"
            background="#F7F8FA"
            :basic-items="[
              {
                label: t('物料名称'),
                field: 'materialName',
              },
              {
                label: t('物料编码'),
                field: 'materialMergeCode',
              },
              {
                label: t('物料规格'),
                field: 'materialSpecification',
              },
              {
                label: t('物料批号'),
                field: 'storageMaterialBatchNo',
              },
            ]"
            :info-data="detailData"
          />
          <view v-if="isAuto" class="machine-box">
            <WeighingMachineOutput
              v-model="weighingMachineValue"
              v-bind="weighingMachineProps"
            />
          </view>
          <view v-else class="form-box">
            <BMForm ref="formRef" v-bind="formProps" />
          </view>
          <wd-row :gutter="16">
            <wd-col :span="12">
              <BMScan
                v-model="container"
                type="input"
                :placeholder="t('容器编号')"
                :allow-types="['04']"
                :error-type-placeholder="t('请扫描容器')"
                @success="onScanSuccess($event, 'container')"
                @fail="onScanFail($event, 'container')"
                @complete="onScanComplete($event, 'container')"
                @confirm="onScanConfirm($event, 'container')"
              />
            </wd-col>
            <wd-col :span="12">
              <BMScan
                v-model="storage"
                type="select"
                :placeholder="t('选择货位')"
                :allow-types="['03']"
                :error-type-placeholder="t('请扫描货位')"
                @success="onScanSuccess($event, 'storage')"
                @fail="onScanFail($event, 'storage')"
                @select="onPositionScanSelect"
                @complete="onScanComplete($event, 'storage')"
                @confirm="onScanConfirm($event, 'storage')"
              />
            </wd-col>
          </wd-row>
        </view>
      </view>
    </BMBasicPage>
  </BMLayout>
  <!-- 打印 -->
  <BmosPrinter ref="bmosPrinterInstance" @jump-over="handleWeighPrint" />
  <!-- 称量结果确认弹窗 -->
  <BMModal
    v-model="showWeighingResult"
    :title="t('结果确认')"
    size="xLarge"
    :closable="false"
    :close-on-click-modal="false"
    :cancel-text="t('签名')"
    :confirm-text="t('继续产出')"
    @cancel="toSign"
    @confirm="continueWeighing"
  >
    <view class="result-content">
      <view class="result-table-title">
        {{ t("产出信息") }}
      </view>
      <view class="result-table-box">
        <BMTable v-bind="resultTableProps" />
      </view>
    </view>
  </BMModal>
  <!-- 选择货位弹窗 -->
  <BMTreeModal
    v-model="storageId"
    v-model:open="showStorageModal"
    :title="t('暂存货位')"
    :tree-data="treeStorageData"
    :field-names="{
      name: 'name',
      key: 'id',
      checkKey: 'level.value',
      checkKeyValue: 4,
      parentId: 'parentId',
      children: 'children',
    }"
    @confirm="confirmStorage"
  />
</template>

<script setup>
import {
  BMBasicPage,
  BMForm,
  BMInfoDisplay,
  BMLayout,
  BMModal,
  BMScan,
  BMSteps,
  BMTable,
  BMTreeModal,
} from '@/BMComponents';
import BmosPrinter from '@/components/BmosPrinter/index.vue';
import { WeighingMachineOutput } from '@/pages/weighingComponents';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad } from '@dcloudio/uni-app';
import { useRemovePeel } from './hooks/index.js';

const {
  query,
  stepActive,
  loading,
  stepList,
  isAuto,
  formRef,
  formProps,
  confirmText,
  container,
  storage,
  storageId,
  detailData,
  weighingMachineValue,
  weighingMachineProps,
  bmosPrinterInstance,
  showWeighingResult,
  resultTableProps,
  showStorageModal,
  treeStorageData,
  toSign,
  continueWeighing,
  handleWeighPrint,
  onPositionScanSelect,
  confirmStorage,
  onScanSuccess,
  onScanFail,
  onScanComplete,
  onScanConfirm,
  toBack,
  toResult,
  previousStep,
  submit,
} = useRemovePeel();

onLoad((e) => {
  query.value = e;
});
</script>

<style lang="scss" scoped>
.container {
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  background: #ffffff;
  .content {
    padding: 11.72rpx 9.38rpx;
    .machine-box {
      margin: 11.72rpx 0;
    }
    .form-box {
      margin-top: 11.72rpx;
    }
  }
}
.result-content {
  height: 280.08rpx;
  .result-table-title {
    margin: 0 0 9.38rpx;
    font-size: 14.06rpx;
    color: var(--bmos-color-text-title);
  }
  .result-table-box {
    height: 253.13rpx;
  }
}
</style>
