<!-- 数量 -->
<template>
  <Drawer
    v-model:open="open"
    :title="t('数量')"
    placement="right"
    width="900px"
    destroyOnClose
    @after-open-change="afterOpenChange">
    <BMTable
      ref="tableRef"
      :search="false"
      :data-request="loadData"
      :columns="columns"
      row-key="id"
      headerTitle=""
      :scroll="{ x: 844, y: 400 }"
      :showRefresh="false"></BMTable>
  </Drawer>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useTable } from './hooks';
  import { BMTable } from '@bmos/components';
  import { getWaitInStorageDetailList } from '@/services';
  const { tableRef, columns } = useTable();

  const loadData = async (params: any): Promise<any> => {
    const datas = { ...params, batchNo: batchNo.value };
    return await getWaitInStorageDetailList(datas);
  };

  const open = ref<boolean>(false);

  const afterOpenChange = (bool: boolean) => {
    console.log('open', bool);
  };

  const batchNo = ref<string>('');

  const showDrawer = (data: any) => {
    console.log(data);
    batchNo.value = data.syncBatchNo;
    open.value = true;
  };

  defineExpose({ showDrawer });
</script>

<style lang="less" scoped>
  // :deep(.bmos-table .bsms-table-wrapper .bsms-table) {
  //   flex: 0;
  // }
</style>
