<!-- 不合格数据 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('不合格数据')"
    wrapClassName="modalSizeLarge"
    :cancelButtonText="t('关闭')"
    :showOkButton="false">
    <template #formBefore>
      <div style="height: 50vh">
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
      </div>
    </template>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { paginationSmall } from '@/utils/paginationConfig';
  import { t } from '@bmos/i18n';
  import { useTable } from './hooks/useTable';
  import { BMModalForm, BMTable } from '@bmos/components';
  import { getInspectionReportManagementNegativeInfo } from '@/services';

  const open = ref(false);

  const modalFormRef = ref();
  const { tableRef, columns } = useTable();

  const rowData = ref<any>({});

  const openModal = async (row: any) => {
    rowData.value = row;
    open.value = true;
  };

  const loadData = async (params: any) => {
    return await getInspectionReportManagementNegativeInfo({
      ...params,
      id: rowData.value.id,
    });
  };

  defineExpose({ openModal });
</script>

<style scoped></style>
