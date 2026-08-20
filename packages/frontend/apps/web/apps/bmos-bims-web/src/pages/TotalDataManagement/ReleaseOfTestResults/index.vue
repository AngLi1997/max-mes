<!-- 检验结果汇总发布 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :titles="[t('检验结果汇总发布')]"
    :rowSelections="rowSelections"
    :showHeader="[false]"
    :showToolBars="[true]"
    :rowClassNames="[rowClassName]"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :requests="[getReleaseOfTestResultsList as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderToolbar0>
      <div>
        <Button
          v-hasAuth="180030002000001"
          type="primary"
          style="margin-right: 8px"
          :disabled="rowSelections[0]?.selectedRowKeys.length === 0"
          @click="openReceive(operationSelectedRows)">
          {{ t('发布') }}
        </Button>
        <Button v-hasAuth="180030002000002" type="primary" @click="openBatch()">
          {{ t('批量发布') }}
        </Button>
      </div>
    </template>
  </BMPageComponent>
  <BatchModal ref="batchRef" @submitSuccess="nextModal" />
  <Publish ref="publishRef" @submitSuccess="submitSuccess" />
  <ErrorModal ref="errorRef" />
</template>

<script setup lang="ts">
  import { Publish, BatchModal, ErrorModal } from './components';
  import { getReleaseOfTestResultsList } from '@/services';
  import { paginationBig } from '@/utils/paginationConfig';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'ReleaseOfTestResults',
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
          disabled: record?.publishStatus?.value !== 1,
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

  const publishRef = ref();
  // 发布
  const openReceive = (rows: any) => {
    publishRef.value?.openModal(JSON.parse(JSON.stringify(rows)));
  };

  const submitSuccess = (data: any) => {
    if (data && data.length > 0) {
      openError(data);
      return;
    }
    if (rowSelections[0]?.selectedRowKeys) {
      rowSelections[0].selectedRowKeys = [];
    }
    operationSelectedRows.value = [];
    pageRef.value?.fetchData();
  };

  // 发布错误
  const errorRef = ref();

  const openError = (rows: any) => {
    errorRef.value?.openModal(rows);
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
