<template>
  <BMLayout>
    <BMBasicPage
      v-show="showType === 'index'"
      :title="t('异常管理')"
      :show-buttons="false"
      @left-click="toBack"
    >
      <template #titleRight>
        <BMFilter v-model="screenData" :form-props="formProps" @confirm="filterConfirm" @reset="filterConfirm" />
      </template>
      <wd-segmented
        v-model:value="currentSegmented"
        :options="[
          {
            label: t('调查中'),
            value: 'investigation',
          },
          {
            label: t('已关闭'),
            value: 'closed',
          },
        ]"
      >
        <template #label="{ option }">
          {{ option.label }}
        </template>
      </wd-segmented>
      <view class="btnBox">
        <wd-button v-hasAuth="121040001000001" @click="addExceptionClick">
          {{ t("新增异常") }}
        </wd-button>
      </view>
      <view class="table_box">
        <InvestigationTable v-if="currentSegmented === 'investigation'" ref="tableRef" />
        <ClosedTable v-if="currentSegmented === 'closed'" ref="tableRef2" />
      </view>
    </BMBasicPage>
    <!-- 新增\编辑 -->
    <AddException ref="addException" v-model:show-type="showType" :tree-modal-data="treeModalData" :row-data="rowData" @submit="getTableList" />
    <!-- 处理/重新调查/作废 -->
    <Handling v-model:open="showHandling" :show-data="showData" :row-data="rowData" @submit="getTableList" />
    <!-- 历史记录 -->
    <History v-model:open="showHistory" :data-list="historyDataList" :field-name="fieldName" />
  </BMLayout>
</template>

<script setup>
import { BMBasicPage, BMFilter, BMLayout } from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import AddException from './component/addException/index.vue';
import ClosedTable from './component/closedTable/index.vue';
import Handling from './component/handling/index.vue';
import History from './component/history/index.vue';
import InvestigationTable from './component/investigationTable/index.vue';
import { useData } from './hooks/useDatas';

const fieldName = {
  exceptionType: t('异常类型'),
  reInvestigateReason: t('重新调查原因'),
  handleResult: t('处理结果'),
  handleTime: t('处理时间'),
  recordMode: t('记录方式'),
  productFullName: t('产品信息'),
  batchNo: t('生产批号'),
  processName: t('所属工艺'),
  procedureName: t('所属工序'),
  procedureStepName: t('所属工序步骤/任务'),
  exceptionDescription: t('异常描述'),
  recordTime: t('记录时间'),
  processVersion: t('工艺版本'),
  handleUserName: t('处理人名称'),
  cancelUserName: t('作废人名称'),
  cancelReason: t('作废原因'),
  cancelTime: t('作废时间'),
};

const {
  currentSegmented,
  treeModalData,
  showType,
  rowData,
  showHandling,
  showData,
  showHistory,
  historyDataList,
  addException,
  tableRef,
  tableRef2,
  screenData,
  formProps,
  addExceptionClick,
  filterConfirm,
  getTableList,
} = useData();

const toBack = () => {
  uni.reLaunch({
    url: `/pages/home/index`,
  });
};
</script>

<style lang="scss" scoped>
  .btnBox {
    text-align: right;
    margin: 5.86rpx 0;
  }
  :deep(.bm-form){
    width: 100%;
  }
  :deep(.wd-popup){
    overflow-y: hidden;
  }
  .table_box {
    height: calc(100% - 82.73rpx);
    :deep(.uni-table){
      min-width: 1860.81rpx !important;
    }
  }
</style>
