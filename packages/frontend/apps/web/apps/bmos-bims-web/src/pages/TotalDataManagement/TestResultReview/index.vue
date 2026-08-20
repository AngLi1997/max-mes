<!-- 检验结果发布审核 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :titles="[t('检验结果发布审核')]"
    :rowSelections="rowSelections"
    :showHeader="[false]"
    :showToolBars="[true]"
    :rowClassNames="[rowClassName]"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :requests="[getTestResultReviewList as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderToolbar0>
      <div>
        <Button
          v-hasAuth="180030003000001"
          type="primary"
          style="margin-right: 8px"
          :disabled="rowSelections[0]?.selectedRowKeys.length === 0"
          @click="openReceive(operationSelectedRows)">
          {{ t('审核') }}
        </Button>
        <Button v-hasAuth="180030003000002" type="primary" @click="openBatch()">
          {{ t('批量审核') }}
        </Button>
      </div>
    </template>
  </BMPageComponent>
  <BatchModal ref="batchRef" @submitSuccess="nextModal" />
  <Audit ref="auditRef" @submitSuccess="submitSuccess" />
</template>

<script setup lang="ts">
  import { Audit, BatchModal } from './components';
  import { getTestResultReviewList } from '@/services';
  import { paginationBig } from '@/utils/paginationConfig';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'TestResultReview',
  });

  const { pageRef, columnsFirst, formFirstProps } = useTable();

  const operationSelectedRows = ref<any>([]);

  // 多选
  const rowSelections = reactive([
    {
      type: 'checkbox',
      hideSelectAll: false,
      columnWidth: 50,
      fixed: true,
      selectedRowKeys: [] as any[],
      preserveSelectedRowKeys: true,
      getCheckboxProps: (record: any) => {
        return {
          disabled: record?.auditStatus?.value !== 0,
        };
      },
      onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
        if (rowSelections[0]?.selectedRowKeys) {
          rowSelections[0].selectedRowKeys = selectedRowKeys;
        }
        operationSelectedRows.value = selectedRows;
      },
    },
    null,
  ]);

  // 批量发布
  const batchRef = ref();
  const openBatch = () => {
    batchRef.value?.openModal();
  };

  const nextModal = (dataList: any) => {
    openReceive(dataList);
  };

  const auditRef = ref();
  // 发布
  const openReceive = (rows: any) => {
    auditRef.value?.openModal(JSON.parse(JSON.stringify(rows)));
  };

  const submitSuccess = () => {
    if (rowSelections[0]?.selectedRowKeys) {
      rowSelections[0].selectedRowKeys = [];
    }
    operationSelectedRows.value = [];
    pageRef.value?.fetchData();
  };

  const inspectItems = reactive(['protein', 'alt', 'hbsAg', 'hcv', 'hiv', 'tp', 'pcrHbv', 'pcrHcv', 'pcrHiv']);

  const rowClassName = (record: any) => {
    for (const item of inspectItems) {
      if (record?.[item]?.value == 0) {
        return 'danger';
      }
    }
    return '';
  };
</script>

<style lang="less" scoped>
  :deep(.danger) {
    color: red;
  }
</style>
