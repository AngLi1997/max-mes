<template>
  <ImportExcel
    ref="importExcelRef"
    :templateFile="t('HLD02_血浆批量同步模板_1')"
    :errorColumns="columns"
    :uploadApi="plasmaDataSyncManualSync"></ImportExcel>
</template>

<script setup lang="tsx">
  import ImportExcel from '@/components/ImportExcel/index.vue';
  import { TableColumn } from '@bmos/components';
  import { plasmaDataSyncManualSync } from '@/services';
  import { t } from '@bmos/i18n';

  const importExcelRef = ref();

  const columns: TableColumn[] = [
    {
      title: t('标本来源'),
      dataIndex: 'originOrg',
      width: 220,
    },
    {
      title: t('血浆编号'),
      dataIndex: 'serialNum',
      width: 160,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 140,
      customRender: ({ record }: any) => {
        return record?.checkResult?.name;
      },
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'donorNum',
      width: 160,
    },
    {
      title: t('出库批号'),
      dataIndex: 'outWarehouseBatchNo',
      width: 160,
    },
    {
      title: t('箱号'),
      dataIndex: 'containerNum',
      width: 170,
    },
    {
      title: t('错误提示'),
      dataIndex: 'failMsg',
      width: 200,
      resizable: true,
      // ellipsis: false,
      fixed: 'right',
      customRender: ({ record }: any) => {
        return <span style={{ color: '#ff5e3d' }}>{record?.failMsg}</span>;
      },
    },
  ];
</script>

<style scoped></style>
