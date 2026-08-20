<!-- 不合格数量 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('数量')"
    :cancel-button-text="t('关闭')"
    :showOkButton="false"
    wrapClassName="modalSizeLarge">
    <BMTable
      ref="tableRef"
      :search="false"
      :data-request="loadData"
      :columns="columns"
      row-key="id"
      headerTitle=""
      :scroll="{ x: 844, y: 400 }"
      :showRefresh="false"
      :pagination="paginationSmall"></BMTable>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useTable } from './hooks';
  import { getInspectionReportDetailList } from '@/services';
  import { paginationSmall } from '@/utils/paginationConfig';
  import { BMModalForm, BMTable } from '@bmos/components';

  const modalFormRef = ref<any>();

  const { tableRef, columns } = useTable();

  const loadData = async (params: any, onChangeParams: any): Promise<any> => {
    const data = {
      ...params,
      inspectionBatchNo: rowData.value.inspectionBatchNo,
    };
    return await getInspectionReportDetailList(data);
  };

  const rowData = ref<any>({});

  const open = ref<boolean>(false);
  const openModal = (data: any) => {
    rowData.value = data;
    open.value = true;
  };

  defineExpose({ openModal });
</script>

<style lang="less" scoped>
  // :deep(.bmos-table .bsms-table-wrapper .bsms-table) {
  //   flex: 0;
  // }
</style>
