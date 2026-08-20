<template>
  <BMLayout>
    <BMBasicPage
      :title="t('物料称量')"
      :confirm-text="confirmButtonText"
      :cancel-text="t('上一步')"
      :default-padding="false"
      :loading="nextLoading"
      @left-click="toBack"
      @confirm="handleNextStep"
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
              />
            </wd-col>
          </wd-row>
        </view>
      </view>
    </BMBasicPage>
    <!-- 更换物料批次 -->
    <BMSignModal
      ref="materialBatchSignModalRef"
      v-model:show="showMaterialBatch"
      v-model="materialBatchSignValue"
      :title="t('是否直接更换物料批次')"
      :label-list="materialBatchLabelList"
      :signature-data="{
        requirementId,
      }"
    >
      <template #buttons>
        <wd-row :gutter="16">
          <wd-col :span="12">
            <wd-button type="info" block @click="showMaterialBatch = false">
              {{ t("取消") }}
            </wd-button>
          </wd-col>
          <wd-col :span="12">
            <wd-button type="warning" block @click="materialBatchSignConfirm">
              {{ t("确定更换") }}
            </wd-button>
          </wd-col>
        </wd-row>
      </template>
    </BMSignModal>
    <!-- 直接完成称量 -->
    <BMSignModal
      ref="completeWeighingSignModalRef"
      v-model:show="showCompleteWeighing"
      v-model="completeWeighingSignValue"
      :title="t('是否直接完成称量')"
      :label-list="completeWeighingLabelList"
      :signature-data="{
        requirementId,
        finisherId: completeWeighingSignValue.userId1,
      }"
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

    <!-- 物料详情 -->
    <BMModal
      v-model="showMaterialDetail"
      :title="t('物料详情')"
      size="large"
      :closable="false"
      :close-on-click-modal="false"
      :show-cancel-button="false"
    >
      <BMInfoDisplay
        is-show-one
        :title="t('物料信息')"
        icon="chengpinwuliao"
        :basic-items="[
          {
            label: t('物料信息'),
            field: 'materialInfo',
          },
          {
            label: t('物料批号'),
            field: 'storageMaterialBatchNo',
          },
          {
            label: t('物料总量'),
            field: 'batchConsumeTotalQuantityUnit',
          },
          {
            label: t('剩余量'),
            field: 'remainingQuantityUnit',
          },
        ]"
        :info-data="requirementDetail"
      />
      <template #buttons>
        <wd-row :gutter="16">
          <wd-col :span="24">
            <wd-button block @click="showMaterialDetail = false">
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
        <view class="result-info-box">
          <text class="label">
            {{ t("产品信息") }}：
            <text class="value">
              {{ weighingResultDetail.productMergeCode }}-{{ weighingResultDetail.productName }}
            </text>
          </text>
          <text class="label">
            {{ t("生产批号") }}：
            <text class="value">
              {{ weighingResultDetail.batchNo }}
            </text>
          </text>
        </view>
        <view class="data-info-box">
          <DataInfo
            :basic-items="resultDetailDataInfoItems"
            :info-data="weighingResultDetail"
          />
        </view>
        <view class="result-table-title">
          {{ t("称量信息") }}
        </view>
        <view class="result-table-box">
          <BMTable ref="resultTableRef" v-bind="resultTableProps" />
        </view>
      </view>
      <template v-if="weighingResultDetail.nextProcess?.value === 4" #buttons>
        <wd-row :gutter="16">
          <wd-col :span="6">
            <wd-button type="warning" block @click="showQuitModal = true">
              {{ t("退出称量") }}
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
    <!-- 退出称量确认 -->
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
            <wd-button type="warning" block @click="exitWeighing">
              {{ t('退出') }}
            </wd-button>
          </wd-col>
        </wd-row>
      </template>
    </BMMessageBox>
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
  showQuitModal,
  completeWeighingSignModalRef,
  materialBatchSignModalRef,
  weighingMachineValue,
  weighingMachineProps,
  actionNumber,
  confirmButtonText,
  infoItems,
  dataInfoItems,
  requirementDetail,
  showMaterialBatch,
  materialBatchSignValue,
  materialBatchLabelList,
  showCompleteWeighing,
  completeWeighingSignValue,
  completeWeighingLabelList,
  showResidualMaterialSign,
  residualMaterialSignValue,
  residualMaterialLabelList,
  showMaterialDetail,
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
  materialBatchSignConfirm,
  completeWeighingSignConfirm,
  residualMaterialSignConfirm,
  toBack,
  handlePreviousStep,
  handleNextStep,
  toResult,
  deleteMaterialConfirm,
  onScanSuccess,
  onScanFail,
  onScanComplete,
  onScanConfirm,
  materialAddConfirm,
  onPositionScanSelect,
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
  :deep(.bm-table) {
    width: 1752px;
  }
}

.result-content {
  height: 273.05rpx;

  .result-info-box {
    background-color: var(--bmos-bg-form);
    border-radius: 4.69rpx;
    padding: 4.69rpx 9.38rpx;
    display: flex;
    font-size: 11.72rpx;
    .label {
      flex: 1;
      color: var(--bmos-color-text-sub);
    }
    .value {
      color: var(--bmos-color-text-main);
    }
  }
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
    :deep(.bm-table) {
      width: 1568px;
    }
  }
}
</style>
