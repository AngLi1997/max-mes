<template>
  <BMLayout>
    <BMBasicPage
      :title="t('配液量取')"
      :confirm-text="confirmButtonText"
      :cancel-text="t('上一步')"
      :default-padding="false"
      @left-click="toBack"
      @cancel="handlePreviousStep"
      @confirm="handleNextStep"
    >
      <template #titleRight>
        <wd-button type="text" @click="toResult">
          {{ t("量取结果") }}
        </wd-button>
      </template>
      <view style="height: 100%">
        <view class="steps-box">
          <wd-steps :active="2" align-center>
            <wd-step :title="t('物料信息')" />
            <wd-step :title="t('设备&模式')" />
            <wd-step :title="t('量取')" />
          </wd-steps>
        </view>
        <view class="liquid-measure-content">
          <view class="info-box">
            <view class="info">
              <Info :basic-items="infoItems" :info-data="measureBatchDetail" />
              <DataInfo
                :basic-items="dataInfoItems"
                :info-data="measureBatchDetail"
              />
            </view>
          </view>
          <view class="machine-box">
            <WeighingMachine
              v-model="weighingMachineValue"
              v-bind="weighingMachineProps"
            />
          </view>
          <wd-row :gutter="16">
            <wd-col :span="12">
              <BMScan
                v-model="scanContainerValue"
                type="input"
                :placeholder="t('容器编号')"
                :allow-types="['04']"
                :error-type-placeholder="t('请扫描容器')"
                @success="onScanSuccess($event, 'container')"
                @fail="onScanFail($event, 'container')"
                @complete="onScanComplete($event, 'container')"
                @confirm="onScanConfirm($event, 'container')"
                @clear="onScanClear($event, 'container')"
              />
            </wd-col>
            <wd-col :span="12">
              <BMScan
                v-model="scanPositionValue"
                type="select"
                :placeholder="t('选择货位')"
                :allow-types="['03']"
                :error-type-placeholder="t('请扫描货位')"
                @success="onScanSuccess($event, 'position')"
                @fail="onScanFail($event, 'position')"
                @select="onPositionScanSelect"
                @complete="onScanComplete($event, 'position')"
                @confirm="onScanConfirm($event, 'position')"
                @clear="onScanClear($event, 'position')"
              />
            </wd-col>
          </wd-row>
        </view>
      </view>
    </BMBasicPage>
    <!-- 添加物料 -->
    <BMModal
      v-model="showMaterialAdd"
      :title="t('添加物料')"
      size="xLarge"
      :closable="false"
      :close-on-click-modal="false"
      @cancel="showMaterialAdd = false"
      @confirm="materialAddConfirm"
    >
      <BMScan
        v-model="scanMaterialAddValue"
        type="input"
        :placeholder="t('物料件/容器')"
        :allow-types="['01', '02', '04']"
        :error-type-placeholder="t('请扫描物料件或容器标签')"
        @success="onScanSuccess($event, 'addMaterial')"
        @fail="onScanFail($event, 'addMaterial')"
        @complete="onScanComplete($event, 'addMaterial')"
        @confirm="onScanConfirm($event, 'addMaterial')"
      />
      <view class="table-box">
        <BMTable ref="addMaterialTableRef" v-bind="addMaterialTableProps" />
      </view>
    </BMModal>
    <!-- 查看物料批次详情 -->
    <BMModal
      v-model="openBatchDetail"
      :title="t('物料批次详情')"
      size="medium"
      :default-padding="false"
    >
      <BMInfoDisplay
        :is-show-title="false"
        :basic-items="[
          {
            label: t('物料件号'),
            field: 'materialNo',
          },
          {
            label: t('物料量'),
            field: 'materialQuantity',
          },
          {
            label: t('单位'),
            field: 'unitName',
          },
          {
            label: t('有效期至'),
            field: 'expiredDate',
          },
          {
            label: t('原厂批号'),
            field: 'originalBatchNo',
          },
        ]"
        :info-data="currentRow"
        is-show-one
      />
      <template #buttons>
        <wd-row :gutter="16">
          <wd-col :span="24">
            <wd-button block @click="openBatchDetail = false">
              {{ t("确定") }}
            </wd-button>
          </wd-col>
        </wd-row>
      </template>
    </BMModal>
    <!-- 删除物料件确认 -->
    <BMMessageBox
      v-model="showDeleteModal"
      :title="t('是否取消该物料件')"
      :cancel-text="t('否')"
      :confirm-text="t('是')"
      @cancel="showDeleteModal = false"
      @confirm="deleteMaterialConfirm"
    />
    <!-- 余液量取超出范围签名 -->
    <BMSignModal
      v-model:show="showResidualMaterialSign"
      v-model="residualMaterialSignValue"
      :title="t('超范围确认')"
      :label-list="residualMaterialLabelList"
      :signature-data="measureParams"
      @confirm="residualMaterialSignConfirm"
      @cancel="showResidualMaterialSign = false"
    />
    <!-- 直接完成称量 -->
    <BMSignModal
      v-model:show="showCompleteWeighing"
      v-model="completeWeighingSignValue"
      :title="t('是否直接完成称量')"
      :label-list="completeWeighingLabelList"
      :signature-data="completeWeighingParams"
      :confirm-text="t('完成称量')"
      @confirm="completeWeighingSignConfirm"
      @cancel="showCompleteWeighing = false"
    />
    <!-- 选择货位弹窗 -->
    <BMTreeModal
      v-model="positionId"
      v-model:open="showPositionModal"
      :title="t('暂存货位')"
      :tree-data="treePositionData"
      :field-names="{
        name: 'name',
        key: 'id',
        checkKey: 'level.value',
        checkKeyValue: 4,
        parentId: 'parentId',
        children: 'children',
      }"
      @confirm="confirmPosition"
    />
    <!-- 打印 -->
    <BmosPrinter ref="bmosPrinterInstance" @jump-over="handleWeigh" />
    <!-- 称量结果确认 -->
    <BMModal
      v-model="showWeighingResult"
      :title="t('结果确认')"
      size="xLarge"
      :closable="false"
      :close-on-click-modal="false"
      :cancel-text="t('签名')"
      :confirm-text="resultConfirmText"
      @cancel="handleResultCancel"
      @confirm="handleResultConfirm"
    >
      <view class="result-content">
        <view class="data-info-box">
          <DataInfo
            :basic-items="resultDataInfoItems"
            :info-data="weighingResultDetail"
          />
        </view>
        <view class="result-table-title">
          {{ t("量取信息") }}
        </view>
        <view class="result-table-box">
          <BMTable ref="resultTableRef" v-bind="resultTableProps" />
        </view>
      </view>
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
  BMScan,
  BMSignModal,
  BMTable,
  BMTreeModal,
} from '@/BMComponents';
import BmosPrinter from '@/components/BmosPrinter';
import DataInfo from '@/pages/weighingComponents/dataInfo';
import Info from '@/pages/weighingComponents/info';
import WeighingMachine from '@/pages/weighingComponents/weighingMachine';
import { t } from '@/utils/useBmosI18n.js';
import { useLiquidMeasure } from './hooks/useLiquidMeasure.jsx';

const props = defineProps({
  id: {
    type: String,
    default: '',
  },
  measureBatchId: {
    type: String,
    default: '',
  },
  componentId: {
    type: String,
    default: '',
  },
});
const {
  bmosPrinterInstance,
  confirmButtonText,
  measureBatchDetail,
  weighingMachineValue,
  weighingMachineProps,
  scanMaterialAddValue,
  infoItems,
  dataInfoItems,
  showMaterialAdd,
  addMaterialTableRef,
  addMaterialTableProps,
  openBatchDetail,
  currentRow,
  showDeleteModal,
  showCompleteWeighing,
  completeWeighingSignValue,
  completeWeighingLabelList,
  scanContainerValue,
  scanPositionValue,
  positionId,
  showPositionModal,
  treePositionData,
  resultDataInfoItems,
  weighingResultDetail,
  resultTableRef,
  resultTableProps,
  showWeighingResult,
  resultConfirmText,
  showResidualMaterialSign,
  residualMaterialSignValue,
  residualMaterialLabelList,
  measureParams,
  completeWeighingParams,
  onScanSuccess,
  onScanFail,
  onScanComplete,
  onScanConfirm,
  onScanClear,
  deleteMaterialConfirm,
  materialAddConfirm,
  toBack,
  handlePreviousStep,
  handleNextStep,
  toResult,
  completeWeighingSignConfirm,
  onPositionScanSelect,
  confirmPosition,
  handleWeigh,
  handleResultCancel,
  handleResultConfirm,
  residualMaterialSignConfirm,
} = useLiquidMeasure({ props });
</script>

<style lang="scss" scoped>
.steps-box {
  background-color: var(--bmos-bg-color);
}
.liquid-measure-content {
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
  .machine-box {
    margin-bottom: 4.69rpx;
  }
}

.table-box {
  height: 222.66rpx;
  margin-top: 9.38rpx;
}

.result-content {
  height: 273.05rpx;

  .data-info-box {
    background-color: #f2f7ff;
    border-radius: 4.69rpx;
    margin-top: 7.03rpx;
  }
  .result-table-title {
    margin: 9.38rpx 0;
    font-size: 14.06rpx;
    color: var(--bmos-color-text-title);
  }
  .result-table-box {
    height: 187.5rpx;
  }
}
</style>
