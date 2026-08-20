<template>
  <ImportExcel
    ref="importExcelRef"
    :templateFile="t('HLD08_出库血浆明细导入模板')"
    :errorColumns="columns"
    :uploadApi="uploadApi"
    @back="backEdit"></ImportExcel>
</template>

<script setup lang="tsx">
  import ImportExcel from '@/components/ImportExcel/index.vue';
  import { TableColumn } from '@bmos/components';
  import { updateDeliveryPlanImport } from '@/services';
  import { t } from '@bmos/i18n';

  const router = useRouter();
  const route = useRoute();

  const uploadApi = async (formData: FormData) => {
    formData.append('id', route.params.id as string);
    return await updateDeliveryPlanImport(formData);
  };

  const backEdit = () => {
    router.back();
  };

  const importExcelRef = ref();

  const columns: TableColumn[] = [
    {
      title: t('行数'),
      dataIndex: 'line',
      width: 100,
      resizable: true,
    },
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      width: 160,
      resizable: true,
    },
    {
      title: t('分拣单号'),
      dataIndex: 'sortingNo',
      width: 140,
      resizable: true,
    },
    {
      title: t('错误提示'),
      dataIndex: 'errorMessage',
      width: 200,
      resizable: true,
      // ellipsis: true,
      fixed: 'right',
      customRender: ({ record }: any) => {
        return <span style={{ color: '#ff5e3d' }}>{record?.errorMessage}</span>;
      },
    },
  ];
</script>

<style scoped></style>
