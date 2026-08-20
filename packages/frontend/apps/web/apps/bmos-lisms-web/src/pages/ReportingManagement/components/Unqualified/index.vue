<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('不合格数据')"
    wrapClassName="modalSizeExtraLarge"
    :cancel-button-text="t('关闭')"
    :showOkButton="false"
    @cancelModal="closeModal">
    <template #formBefore>
      <div :style="{ height: tableRef?.getTableData().length > 4 ? '45vh' : 'auto' }">
        <BMTable
          ref="tableRef"
          :search="false"
          :data-request="loadData"
          :columns="columns"
          row-key="id"
          headerTitle=""
          :scroll="{ x: 700, y: 400 }"
          :showRefresh="false"></BMTable>
      </div>
    </template>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { getReportCenterChildPage } from '@/services';
  import { t } from '@bmos/i18n';
  import { useTable } from './useTable';
  import { BMModalForm, BMTable } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { InspectionResultEnum } from '@/types';

  const open = ref(false);

  const modalFormRef = ref();
  const { tableRef, columns } = useTable();

  const rowData = ref<any>({});
  const openModal = async (row: any) => {
    try {
      rowData.value = row;
      open.value = true;
      await nextTick();
      console.log('tableRef', tableRef.value);
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const loadData = async (params: any) => {
    try {
      const data = {
        ...params,
        sampleBatchNo: rowData.value.sampleBatchNo,
        inspectResult: InspectionResultEnum.UNQUALIFIED,
      };
      return await getReportCenterChildPage(data);
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const closeModal = () => {
    open.value = false;
  };

  defineExpose({
    openModal,
    closeModal,
  });
</script>

<style scoped></style>
