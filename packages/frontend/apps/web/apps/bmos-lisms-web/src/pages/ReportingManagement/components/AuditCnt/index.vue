<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('报告发布记录')"
    wrapClassName="modalSizeLarge"
    :cancel-button-text="t('关闭')"
    :showOkButton="false"
    @cancelModal="closeModal">
    <template #formBefore>
      <BMTable
        ref="tableRef"
        :search="false"
        :data-source="tableData"
        :columns="columns"
        row-key="id"
        headerTitle=""
        :scroll="{ x: 700, y: 400 }"
        :showRefresh="false"
        :pagination="false"></BMTable>
    </template>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { printReport, getAuditRecord } from '@/services';
  import { pdfPreview } from '@/utils';
  import { t } from '@bmos/i18n';
  import { useTable } from './useTable';
  import { BMModalForm, BMTable } from '@bmos/components';
  import { message } from 'ant-design-vue';

  const open = ref(false);

  const print = async (record: any) => {
    try {
      const res = await printReport({ sampleBatchNo: sampleBatchNo.value, auditId: record.auditId });
      await pdfPreview(res);
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const sampleBatchNo = ref<string>('');

  const modalFormRef = ref();
  const { tableRef, columns } = useTable(print);
  const tableData = ref<any[]>([]);

  const openModal = async (row: any) => {
    try {
      sampleBatchNo.value = row.sampleBatchNo;
      const { data } = await getAuditRecord(row.sampleBatchNo);
      tableData.value = data ?? [];
      open.value = true;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const closeModal = () => {
    // open.value = false;
    tableData.value = [];
  };

  defineExpose({
    openModal,
    closeModal,
  });
</script>

<style scoped></style>
