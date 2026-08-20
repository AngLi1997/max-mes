<!-- 数量 -->
<template>
  <Drawer
    v-model:open="open"
    :title="t('详情')"
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
      :showRefresh="false"
      :pagination="paginationSmall"></BMTable>
  </Drawer>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useTable } from './hooks';
  import { paginationSmall } from '@/utils/paginationConfig';
  import { BMTable } from '@bmos/components';
  import { getSortingCheckPlanDetailList } from '@/services';

  const { tableRef, columns } = useTable();

  const loadData = async (params: any, onChangeParams: any): Promise<any> => {
    const datas = {
      ...params,

      ...row.value,
    };
    return await getSortingCheckPlanDetailList(datas);
  };

  const open = ref<boolean>(false);

  const afterOpenChange = (bool: boolean) => {
    console.log('open', bool);
  };

  const row = ref<any>({});

  const showDrawer = (data: any) => {
    row.value = {
      planBatchNo: data.planBatchNo,
      checkNo: data.checkNo,
    };
    open.value = true;
  };

  defineExpose({ showDrawer });
</script>

<style lang="less" scoped>
  // :deep(.bmos-table .bsms-table-wrapper .bsms-table) {
  //   flex: 0;
  // }
</style>
