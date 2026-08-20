<!-- 出库血浆明细 -->
<template>
  <Drawer
    v-model:open="open"
    :title="t('出库血浆明细')"
    placement="right"
    width="1200px"
    destroyOnClose
    @after-open-change="afterOpenChange">
    <BMTable
      ref="tableRef"
      :data-request="loadData"
      :columns="columns"
      row-key="id"
      headerTitle=""
      :formProps="formProps"
      :scroll="{ x: 1000, y: 400 }"
      :showRefresh="false"
      :pagination="paginationSmall">
      <template #toolbar>
        <Button type="primary" :loading="loading" @click="exportExcel()">{{ t('导出') }}</Button>
      </template>
    </BMTable>
  </Drawer>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useTable } from './hooks';
  import { paginationSmall } from '@/utils/paginationConfig';
  import { getDeliveryPlanSelectedList, exportDeliveryPlanDetailList } from '@/services';
  import { BMTable } from '@bmos/components';
  import { fileStreamDownload } from '@bmos/utils';
  import { message } from 'ant-design-vue';

  const { tableRef, formProps, columns } = useTable();

  const loadData = async (params: any, onChangeParams: any) => {
    const datas = {
      ...params,
      bigContainerNo: rowData.value?.bigContainerNo,
      sortingBatchNo: rowData.value?.sortingPlanBatchNo,
      batchNo: rowData.value?.batchNo,
    };
    if (!datas?.batchNo) {
      return {
        data: [],
      };
    }
    return await getDeliveryPlanSelectedList(datas);
  };

  const open = ref<boolean>(false);

  const rowData = ref<any>({});

  const afterOpenChange = (bool: boolean) => {
    console.log('open', bool);
  };

  const showDrawer = (data: any) => {
    rowData.value = data;
    console.log(rowData.value);
    open.value = true;
  };

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
  const exportExcel = async () => {
    try {
      loading.value = true;
      const res = await exportDeliveryPlanDetailList({
        ...tableRef.value.getQueryFormRef().formModel,
        bigContainerNo: rowData.value?.bigContainerNo,
        sortingBatchNo: rowData.value?.sortingPlanBatchNo,
        batchNo: rowData.value?.batchNo,
      });
      let fileName = res.headers['content-disposition']?.split("filename*=utf-8''")[1];
      // 文件名解码
      fileName && fileName.indexOf('%') > -1 && (fileName = decodeURI(fileName));
      downloadFn(res.data, fileName);
    } finally {
      loading.value = false;
    }
  };

  defineExpose({ showDrawer });
</script>

<style lang="less" scoped>
  // :deep(.bmos-table .bsms-table-wrapper .bsms-table) {
  //   flex: 0;
  // }
</style>
