<template>
  <BMLayout>
    <BMBasicPage
      :title="t('称量工单执行')"
      :cancel-text="t('上一步')"
      :default-padding="false"
      :loading="nextLoading"
      :confirm-text="confirmButtonText"
      @confirm="handleNextStep"
      @left-click="toBack"
      @cancel="handlePreviousStep"
    >
      <template #titleRight>
        <wd-button type="text" @click="toResult">
          {{ t("称量结果") }}
        </wd-button>
      </template>
      <view style="height: 100%">
        <view class="steps-box">
          <wd-steps :active="actionNumber === 2 ? 3 : 2" align-center>
            <wd-step :title="t('物料信息')" />
            <wd-step :title="t('设备&模式')" />
            <wd-step :title="t('清零去皮')" />
            <wd-step :title="t('称量')" />
          </wd-steps>
        </view>
        <view class="material-weighing-content">
          <view class="info-box">
            <view class="info">
              <Info :basic-items="infoItems" :info-data="requirementDetail" />
              <DataInfo
                :basic-items="dataInfoItems"
                :info-data="requirementDetail"
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
              <BMScanInput
                v-model="scanContainerValue"
                :placeholder="t('容器编号')"
                @confirm="handleContainerScan(scanContainerValue)"
                @clear="handleClearContainer"
              />
            </wd-col>
            <wd-col :span="12">
              <BMScanInput
                v-model="scanPositionValue"
                :placeholder="t('选择货位')"
                type="select"
                @confirm="confirmPosition(scanPositionValue)"
                @select="onPositionScanSelect"
                @clear="onClearPosition"
              />
            </wd-col>
          </wd-row>
        </view>
      </view>
    </BMBasicPage>
    <!-- 直接完成称量 -->
    <BMSignModal
      ref="completeWeighingSignModalRef"
      v-model:show="showCompleteWeighing"
      v-model="completeWeighingSignValue"
      :title="t('是否直接完成称量')"
      :label-list="completeWeighingLabelList"
      :signature-data="completeWeighingParams"
    >
      <template #buttons>
        <wd-row :gutter="16">
          <wd-col :span="12">
            <wd-button type="info" block @click="showCompleteWeighing = false">
              {{ t("取消") }}
            </wd-button>
          </wd-col>
          <wd-col :span="12">
            <wd-button type="warning" block @click="completeWeighingSignConfirm">
              {{ t("完成称量") }}
            </wd-button>
          </wd-col>
        </wd-row>
      </template>
    </BMSignModal>
    <!-- 余料称量超出范围签名 -->
    <BMSignModal
      v-model:show="showResidualMaterialSign"
      v-model="residualMaterialSignValue"
      :title="t('超范围确认')"
      :label-list="residualMaterialLabelList"
      :signature-data="weighingCenterExecuteParams"
      @confirm="residualMaterialSignConfirm"
      @cancel="showResidualMaterialSign = false"
    />
    <!-- 工单详情 -->
    <BMModal
      v-model="showRequirementDetail"
      :title="t('需求详情')"
      size="large"
      :closable="false"
      :close-on-click-modal="false"
      :show-cancel-button="false"
    >
      <BMInfoDisplay
        is-show-one
        :title="t('需求详情')"
        icon="chengpinwuliao"
        :basic-items="[
          {
            label: t('产品信息'),
            field: ['productMaterialMergeCode', 'productMaterialName'],
            hyphen: '-',
          },
          {
            label: t('生产批号'),
            field: 'batchNo',
          },
          {
            label: t('物料信息'),
            field: ['materialMergeCode', 'materialName'],
            hyphen: '-',
          },
          {
            label: t('物料批号'),
            field: 'storageMaterialBatchNo',
          },
          {
            label: t('配料量'),
            field: 'targetTotalQuantityUnit',
          },
          {
            label: t('需求用途'),
            field: 'requirementUsage',
          },
        ]"
        :info-data="requirementDetail"
      />
      <template #buttons>
        <wd-row :gutter="16">
          <wd-col :span="24">
            <wd-button block @click="showRequirementDetail = false">
              {{ t("返回") }}
            </wd-button>
          </wd-col>
        </wd-row>
      </template>
    </BMModal>
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
      <BMScanNew v-if="showMaterialAdd" @success="scanMaterialSuccess" />
      <BMScanInput
        v-model="scanMaterialAddValue"
        :placeholder="t('物料件号或容器编号')"
        suffix-icon="search"
        @confirm="handleMaterialScan(scanMaterialAddValue)"
      />
      <view class="table-box">
        <BMTable ref="addMaterialTableRef" v-bind="addMaterialTableProps" />
      </view>
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
            :basic-items="resultDetailDataInfoItems"
            :info-data="{
              ...requirementDetail,
              ...weighingResultDetail,
            }"
          />
        </view>
        <view class="result-table-title">
          {{ t("称量信息") }}
        </view>
        <view class="result-table-box">
          <BMTable ref="resultTableRef" v-bind="resultTableProps" />
        </view>
      </view>
      <template v-if="weighingResultDetail.ticketCompleteCondition" #buttons>
        <wd-row :gutter="16">
          <wd-col :span="6">
            <wd-button type="success" block @click="exitWeighing">
              {{ t("完成称量") }}
            </wd-button>
          </wd-col>
          <wd-col :span="18">
            <wd-button block @click="handleResultCancel">
              {{ t("签名") }}
            </wd-button>
          </wd-col>
        </wd-row>
      </template>
    </BMModal>
    <BMScanNew v-if="!showMaterialAdd" @success="scanSuccess" />
  </BMLayout>
</template>

<script setup>
import {
  BMBasicPage,
  BMInfoDisplay,
  BMLayout,
  BMMessageBox,
  BMModal,
  BMScanInput,
  BMScanNew,
  BMSignModal,
  BMTable,
  BMTreeModal,
} from '@/BMComponents';
import BmosPrinter from '@/components/BmosPrinter';
import DataInfo from '@/pages/weighingComponents/dataInfo';
import Info from '@/pages/weighingComponents/info';
import WeighingMachine from '@/pages/weighingComponents/weighingMachine';
import { t } from '@/utils/useBmosI18n.js';
import { useMaterialWeighing } from './hooks/useMaterialWeighing.jsx';

const props = defineProps({
  id: {
    type: String,
    default: '',
  },
  mode: {
    type: String,
    default: 'true',
  },
  requirementId: {
    type: String,
    default: '',
  },
});

const {
  nextLoading,
  weighingCenterExecuteParams,
  completeWeighingSignModalRef,
  weighingMachineValue,
  weighingMachineProps,
  actionNumber,
  confirmButtonText,
  infoItems,
  dataInfoItems,
  requirementDetail,
  showCompleteWeighing,
  completeWeighingSignValue,
  completeWeighingLabelList,
  completeWeighingParams,
  showResidualMaterialSign,
  residualMaterialSignValue,
  residualMaterialLabelList,
  showRequirementDetail,
  showMaterialAdd,
  scanMaterialAddValue,
  addMaterialTableRef,
  addMaterialTableProps,
  showWeighingResult,
  showDeleteModal,
  scanContainerValue,
  scanPositionValue,
  showPositionModal,
  positionId,
  treePositionData,
  bmosPrinterInstance,
  weighingResultDetail,
  resultDetailDataInfoItems,
  resultTableRef,
  resultTableProps,
  resultConfirmText,
  completeWeighingSignConfirm,
  residualMaterialSignConfirm,
  toBack,
  handlePreviousStep,
  handleNextStep,
  toResult,
  deleteMaterialConfirm,
  handleContainerScan,
  handleClearContainer,
  materialAddConfirm,
  scanMaterialSuccess,
  scanSuccess,
  handleMaterialScan,
  onPositionScanSelect,
  onClearPosition,
  confirmPosition,
  handleWeigh,
  handleResultCancel,
  handleResultConfirm,
  exitWeighing,
} = useMaterialWeighing({ props });
</script>

<style lang="scss" scoped>
.steps-box {
  background-color: var(--bmos-bg-color);
}
.material-weighing-content {
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
  width: 100%;
  overflow-x: auto;
  margin-top: 9.38rpx;
  :deep(.uni-table) {
    width: 897.66rpx;
  }
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
    width: 100%;
    overflow-x: auto;
    height: 150rpx;
    :deep(.uni-table) {
      width: 684.38rpx;
    }
  }
}
</style>
