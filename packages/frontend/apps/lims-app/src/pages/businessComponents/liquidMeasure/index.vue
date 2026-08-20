<template>
  <BMLayout>
    <BMBasicPage
      :title="t('配液量取')"
      :confirm-text="t('下一步')"
      :default-padding="false"
      @left-click="leftClick"
      @confirm="handleNextStep"
      @cancel="handleCancel"
    >
      <template #titleRight>
        <wd-button type="text" @click="toResult">
          {{ t("量取结果") }}
        </wd-button>
      </template>
      <view style="height: 100%">
        <view class="steps-box">
          <wd-steps :active="0" align-center>
            <wd-step :title="t('物料信息')" />
            <wd-step :title="t('设备&模式')" />
            <wd-step :title="t('量取')" />
          </wd-steps>
        </view>
        <view class="execution-content">
          <view class="info-box">
            <view class="info">
              <Info
                :basic-items="infoItems"
                :info-data="selectedLiquidMeasureSheet || instance"
              />
              <DataInfo
                :basic-items="dataInfoItems"
                :info-data="selectedMaterialBatch || instance"
              />
            </view>
          </view>
          <wd-row :gutter="16">
            <wd-col :span="12">
              <StatisticalInfo
                :basic-items="statisticalInfoItems"
                :info-data="statisticalInfoData"
              />
            </wd-col>
            <wd-col :span="12">
              <BMScan
                v-model="scanValue"
                type="input"
                :placeholder="t('物料件/容器')"
                :allow-types="['01', '02', '04']"
                :error-type-placeholder="t('请扫描物料件或容器标签')"
                @success="onScanSuccess"
                @fail="onScanFail"
                @complete="onScanComplete"
                @confirm="onScanConfirm"
              />
            </wd-col>
          </wd-row>
          <view class="table-box">
            <BMTable ref="tableRef" v-bind="tableProps" />
          </view>
        </view>
      </view>
    </BMBasicPage>
    <!-- 配液单选择 -->
    <BMRadioModal
      v-model="liquidMeasureSheetValue"
      v-model:open="showLiquidMeasureSheet"
      :title="t('配液单选择')"
      :options="liquidMeasureSheetOptions"
      :field-names="{
        label: 'name',
        value: 'id',
      }"
      required
      @cancel="cancelLiquidMeasureSheet"
      @confirm="confirmLiquidMeasureSheet"
    />
    <!-- 选择物料批次 -->
    <BMRadioModal
      v-model="materialBatchValue"
      v-model:open="showMaterialBatch"
      :title="t('选中物料批次')"
      :options="materialBatchOptions"
      :field-names="{
        label: 'materialFullName',
        value: 'id',
      }"
      :sub-labels="materialBatchSubLabels"
      @confirm="confirmMaterialBatch"
    />
    <BMSignModal
      v-model:show="showSign"
      v-model="signValue"
      :title="t('操作人员确认')"
      show-remark
      :label-list="labelList"
      :signature-data="signParams"
      @confirm="signConfirm"
    />
    <!-- 取消物料件确认 -->
    <BMMessageBox
      v-model="showDeleteModal"
      :title="t('是否取消该物料件')"
      :cancel-text="t('否')"
      :confirm-text="t('是')"
      @cancel="showDeleteModal = false"
      @confirm="deleteMaterialConfirm"
    />
    <!-- 查看物料件详情 -->
    <BMModal
      v-model="openBatchDetail"
      :title="t('物料件详情')"
      size="medium"
      :default-padding="false"
    >
      <BMInfoDisplay
        :is-show-title="false"
        :basic-items="materialDetailsBasicItems"
        :info-data="currentRow"
        is-show-one
      />
      <template #buttons>
        <wd-row :gutter="16">
          <wd-col :span="24">
            <wd-button block @click="openBatchDetail = false">
              {{ t('确定') }}
            </wd-button>
          </wd-col>
        </wd-row>
      </template>
    </BMModal>
  </BMLayout>
</template>

<script setup>
import {
  BMBasicPage,
  BMInfoDisplay,
  BMLayout,
  BMMessageBox,
  BMModal,
  BMRadioModal,
  BMScan,
  BMSignModal,
  BMTable,
} from '@/BMComponents';
import DataInfo from '@/pages/weighingComponents/dataInfo';
import Info from '@/pages/weighingComponents/info';
import StatisticalInfo from '@/pages/weighingComponents/statisticalInfo';
import { t } from '@/utils/useBmosI18n.js';
import { defineProps, onMounted } from 'vue';
import { useExecution } from './hooks/useExecution.jsx';

const props = defineProps({
  componentId: {
    type: String,
    default: '',
  },
});
const {
  infoItems,
  dataInfoItems,
  statisticalInfoItems,
  statisticalInfoData,
  instance,
  scanValue,
  tableRef,
  tableProps,
  openBatchDetail,
  materialDetailsBasicItems,
  currentRow,
  showSign,
  signValue,
  signParams,
  showLiquidMeasureSheet,
  liquidMeasureSheetValue,
  liquidMeasureSheetOptions,
  selectedLiquidMeasureSheet,
  showMaterialBatch,
  materialBatchValue,
  materialBatchOptions,
  materialBatchSubLabels,
  selectedMaterialBatch,
  labelList,
  showDeleteModal,
  leftClick,
  toResult,
  onScanSuccess,
  onScanFail,
  onScanComplete,
  onScanConfirm,
  signConfirm,
  handleNextStep,
  handleCancel,
  getLiquidMeasureInstance,
  getLiquidMeasurePlanList,
  cancelLiquidMeasureSheet,
  confirmLiquidMeasureSheet,
  confirmMaterialBatch,
  deleteMaterialConfirm,
  getReCheckerList,
} = useExecution({ props });

onMounted(() => {
  getLiquidMeasureInstance();
  getLiquidMeasurePlanList();
  getReCheckerList();
});
</script>

<style lang="scss" scoped>
.steps-box {
  background-color: var(--bmos-bg-color);
}
.execution-content {
  height: calc(100% - 37.5rpx - 4.69rpx);
  margin-top: 4.69rpx;
  padding: 0 9.38rpx;
  box-sizing: border-box;
  .info-box {
    margin-bottom: 7.03rpx;
    .info {
      border: 1px solid var(--bmos-color-border);
      border-radius: 4.69rpx;
      box-sizing: border-box;
    }
  }
  .table-box {
    width: 100%;
    overflow-x: auto;
    margin-top: 9.38rpx;
    height: calc(100% - 80.86rpx - 8.2rpx - 32.81rpx - 9.38rpx - 9.38rpx);
    :deep(.bm-table) {
      width: 1654px;
    }
  }
}
</style>
