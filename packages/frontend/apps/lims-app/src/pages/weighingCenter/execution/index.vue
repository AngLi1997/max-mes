<template>
  <BMLayout>
    <BMBasicPage
      :title="t('物料称量')"
      :confirm-text="t('下一步')"
      :default-padding="false"
      @left-click="leftClick"
      @confirm="handleNextStep"
      @cancel="handleCancel"
    >
      <template #titleRight>
        <wd-button type="text" @click="toResult">
          {{ t("称量结果") }}
        </wd-button>
      </template>
      <view style="height: 100%">
        <view class="steps-box">
          <wd-steps :active="0" align-center>
            <wd-step :title="t('物料信息')" />
            <wd-step :title="t('设备&模式')" />
            <wd-step :title="t('清零去皮')" />
            <wd-step :title="t('称量')" />
          </wd-steps>
        </view>
        <view class="execution-content">
          <view class="info-box">
            <view class="info">
              <Info :basic-items="infoItems" :info-data="taskDetail" />
              <DataInfo
                :basic-items="dataInfoItems"
                :info-data="selectedMaterialRequire || taskDetail.weighExecuteRequirement || {}"
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
    <!-- 选择物料需求 -->
    <BMRadioModal
      v-model="materialRequireValue"
      v-model:open="showMaterialRequire"
      :title="t('选择物料需求')"
      :options="materialRequireOptions"
      :field-names="{
        label: 'requirementQuantityUnit',
        value: 'id',
      }"
      :sub-labels="materialRequireSubLabels"
      :has-search="false"
      @confirm="confirmMaterialRequirement"
    />
    <BMSignModal
      v-model:show="showSign"
      v-model="signValue"
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
  </BMLayout>
</template>

<script setup>
import {
  BMBasicPage,
  BMLayout,
  BMMessageBox,
  BMRadioModal,
  BMScan,
  BMSignModal,
  BMTable,
} from '@/BMComponents';
import DataInfo from '@/pages/weighingComponents/dataInfo';
import Info from '@/pages/weighingComponents/info';
import StatisticalInfo from '@/pages/weighingComponents/statisticalInfo';
import { t } from '@/utils/useBmosI18n.js';
import { onShow } from '@dcloudio/uni-app';
import { onMounted } from 'vue';
import { useExecution } from './hooks/useExecution.jsx';

const props = defineProps({
  id: {
    type: String,
    default: '',
  },
});
const {
  infoItems,
  dataInfoItems,
  statisticalInfoItems,
  taskDetail,
  scanValue,
  tableRef,
  tableProps,
  showSign,
  signValue,
  signParams,
  showMaterialRequire,
  materialRequireValue,
  materialRequireOptions,
  materialRequireSubLabels,
  selectedMaterialRequire,
  statisticalInfoData,
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
  getWeighCenterExecuteTaskById,
  getWeighCenterExecutePendingRequirementListByTaskId,
  confirmMaterialRequirement,
  deleteMaterialConfirm,
} = useExecution({ props });

onShow(() => {
  getWeighCenterExecutePendingRequirementListByTaskId();
  getWeighCenterExecuteTaskById();
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
    :deep(.uni-table) {
      width: 1062.89rpx;
    }
  }
}
</style>
