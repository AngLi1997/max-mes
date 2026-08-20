<!-- 标本入库单 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['inWarehouseBatchNo']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :titles="[t('标本入库单')]"
    :rowSelections="[rowSelection]"
    :showToolBars="[true]"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :requests="[getSampleReceiptList as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderToolbar0>
      <Button
        v-hasAuth="170120002000001"
        type="primary"
        :disabled="rowSelection.selectedRowKeys.length === 0"
        :loading="loading"
        @click="printReceipt">
        {{ t('打印标本入库单') }}
      </Button>
    </template>
  </BMPageComponent>
</template>

<script setup lang="ts">
  import { getSampleReceiptList, getSampleReceiptPrint } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { fileStreamDownload } from '@bmos/utils';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'SpecimenReceipt',
  });

  const { pageRef, columnsFirst, formFirstProps } = useTable();

  const operationSelectedRows = ref<any>([]);

  // 多选
  const rowSelection = reactive({
    type: 'checkbox',
    hideSelectAll: false,
    columnWidth: 50,
    fixed: true,
    selectedRowKeys: [] as any[],
    preserveSelectedRowKeys: true,
    getCheckboxProps: (record: any) => {
      return {
        disabled: false,
      };
    },
    onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
      rowSelection.selectedRowKeys = selectedRowKeys;
      operationSelectedRows.value = selectedRows;
    },
  });

  const downloadFn = (data: any, fileName: string) => {
    try {
      const uint8Array = new Uint8Array(data);
      const decoder = new TextDecoder();
      const jsonString = decoder.decode(uint8Array);
      const error = JSON.parse(jsonString);
      error.message && message.error(error.message);
    } catch (error) {
      fileStreamDownload(data, fileName);
    }
  };

  const loading = ref(false);
  const printReceipt = async () => {
    try {
      loading.value = true;
      const data = operationSelectedRows.value?.map((item: any) => item.inWarehouseBatchNo);
      const res = await getSampleReceiptPrint(data);
      let fileName = res.headers['content-disposition']?.split("filename*=utf-8''")[1];
      // 文件名解码
      fileName && fileName.indexOf('%') > -1 && (fileName = decodeURI(fileName));
      downloadFn(res.data, fileName);
    } finally {
      loading.value = false;
    }
  };
</script>

<style lang="less" scoped></style>
