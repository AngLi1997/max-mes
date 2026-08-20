<template>
  <BMLayout>
    <BMBasicPage
      :title="t('配液产出')"
      :confirm-text="t('确定')"
      :cancel-text="t('上一步')"
      :default-padding="false"
      @left-click="toBack"
      @confirm="handleNextStep"
      @cancel="handlePreviousStep"
    >
      <template #titleRight>
        <wd-button type="text" @click="toResult">{{ t("产出结果") }}</wd-button>
      </template>
      <view style="height: 100%">
        <view class="steps-box">
          <wd-steps :active="2" align-center>
            <wd-step :title="t('物料信息')" />
            <wd-step :title="t('设备&模式')" />
            <wd-step :title="t('产出')" />
          </wd-steps>
        </view>
        <view class="material-weighing-content">
          <view class="info-box">
            <BMInfoDisplay
              background="#F6F8FA"
              :title="t('产出批次')"
              icon="chengpinwuliao"
              :basic-items="infoItems"
              :info-data="detail"
            />
          </view>
          <view class="machine-box">
            <WeighingMachine v-if="auto" />
            <BMForm v-else ref="formRef" v-bind="formProps" />
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
    <!--称量结果确认-->
    <BMModal
      v-model="showWeighingResult"
      :title="t('结果确认')"
      size="xLarge"
      :closable="false"
      :close-on-click-modal="false"
      :cancel-text="t('签名')"
      :confirm-text="t('继续产出')"
      @cancel="toResult"
      @confirm="resultConfirm"
    >
      <view class="result-content">
        <view class="result-table-title">{{ t("产出信息") }}</view>
        <view class="result-table-box">
          <BMTable ref="resultTableRef" v-bind="resultTableProps" />
        </view>
      </view>
    </BMModal>
    <!--选择货位弹窗-->
    <BMTreeModal
      v-model="positionCode"
      v-model:open="showPositionModal"
      :title="t('暂存货位')"
      :tree-data="treePositionData"
      :field-names="{
        name: 'name',
        key: 'positionCode',
        checkKey: 'level.value',
        checkKeyValue: 4,
        parentId: 'parentId',
        children: 'children',
      }"
      @confirm="confirmPosition"
    />
    <BmosPrinter ref="bmosPrinterInstance" @jump-over="handleWeigh" />
  </BMLayout>
</template>

<script setup>
  import { t } from '@/utils/useBmosI18n.js';
  import {
    BMBasicPage,
    BMScan,
    BMModal,
    BMInfoDisplay,
    BMTable,
    BMLayout,
    BMForm,
    BMTreeModal
  } from '@/BMComponents';
  import WeighingMachine from '@/pages/weighingComponents/weighingMachine';
  import BmosPrinter from '@/components/BmosPrinter';
  import { useOutput } from './hooks/useOutput.js';

  const props = defineProps({
    mode: {
      type: String,
      default: 'false'
    },
    progressId: {
      type: String,
      default: ''
    },
    componentId: {
      type: String,
      default: ''
    }
  });
  const {
    auto,
    infoItems,
    detail,
    formRef,
    formProps,
    resultTableRef,
    resultTableProps,
    showWeighingResult,
    scanContainerValue,
    scanPositionValue,
    positionCode,
    showPositionModal,
    treePositionData,
    bmosPrinterInstance,
    toBack,
    handlePreviousStep,
    handleNextStep,
    toResult,
    onScanSuccess,
    onScanFail,
    onScanComplete,
    onScanConfirm,
    onScanClear,
    onPositionScanSelect,
    confirmPosition,
    resultConfirm,
    handleWeigh
  } = useOutput({ props });
</script>

<style lang="scss" scoped>
.steps-box {
  background-color: var(--bmos-bg-color);
}
.material-weighing-content {
  height: calc(100% - 37.5rpx - 4.69rpx);
  padding: 0 9.38rpx;
  box-sizing: border-box;
  .info-box {
    margin: 11.72rpx auto;
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
  height: 280.08rpx;
  .result-table-title {
    margin-bottom: 9.38rpx;
    font-size: 14.06rpx;
    color: var(--bmos-color-text-title);
  }
  .result-table-box {
    height: 254.3rpx;
  }
}
</style>
