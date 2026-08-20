<!-- HLD01_标本批量同步模板_1 -->
<template>
  <ImportExcel
    ref="importExcelRef"
    type="sample"
    :templateFile="t('HLD01_标本批量同步模板_1')"
    :errorColumns="columns"
    :backFn="backFn"
    :uploadApi="sampleDataSyncImport"></ImportExcel>
</template>

<script setup lang="tsx">
  import ImportExcel from '@/components/ImportExcel/index.vue';
  import { TableColumn } from '@bmos/components';
  import { sampleDataSyncImport } from '@/services';
  import { t } from '@bmos/i18n';

  const importExcelRef = ref();

  const columns: TableColumn[] = [
    {
      title: t('来源单位'),
      dataIndex: 'originOrg',
      width: 220,
    },
    {
      title: t('出库批号'),
      dataIndex: 'outWarehouseBatchNo',
      width: 160,
    },
    {
      title: t('标本编号'),
      dataIndex: 'serialNum',
      width: 180,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 140,
    },
    {
      title: t('标本箱号'),
      dataIndex: 'containerNum',
      width: 170,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'donorNum',
      width: 160,
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

  const router = useRouter();
  const backFn = () => {
    router.push({ name: 'sample-data-sync' });
  };
</script>

<style scoped></style>
