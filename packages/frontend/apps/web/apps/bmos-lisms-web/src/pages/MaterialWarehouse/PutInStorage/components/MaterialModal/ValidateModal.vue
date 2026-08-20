<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('物料接收')"
    wrapClassName="modalSizeMedium"
    :cancel-button-text="t('关闭')"
    :showOkButton="false">
    <template #formBefore>
      <div class="title">{{ t('当前批次与上次提交数据不一致，无法提交') }}</div>
      <div class="title">{{ t('历史数据') }}</div>
      <BMTable
        ref="tableRef"
        :search="false"
        :data-source="tableData"
        :columns="columns"
        row-key="id"
        headerTitle=""
        :scroll="{ x: 400, y: 400 }"
        :showRefresh="false"
        :pagination="false"></BMTable>
    </template>
  </BMModalForm>
</template>

<script setup lang="tsx">
  import { BMModalForm, BMTable, ModalFormInstance } from '@bmos/components';
  import type { TableColumn } from '@bmos/components';
  import { t } from '@bmos/i18n';

  const modalFormRef = ref<ModalFormInstance>();
  const open = ref(false);

  const formModel = ref<any>({});

  const openModal = (model: any, data: any) => {
    formModel.value = model;
    tableData.value = [data];
    open.value = true;
  };

  const tableData = ref<any>([]);

  const columns: TableColumn[] = [
    {
      title: t('生产日期'),
      dataIndex: 'productionDate',
      width: 160,
      resizable: true,
      customRender: ({ record }) => {
        return (
          <span class={record.productionDate != formModel.value.productionDate ? 'danger' : ''}>
            {record.productionDate}
          </span>
        );
      },
    },
    {
      title: t('有效日期'),
      dataIndex: 'expireDate',
      width: 160,
      resizable: true,
      customRender: ({ record }) => {
        return <span class={record.expireDate != formModel.value.expireDate ? 'danger' : ''}>{record.expireDate}</span>;
      },
    },
    {
      title: t('质控品含量'),
      dataIndex: 'qualityControlNumerical',
      width: 160,
      resizable: true,
      customRender: ({ record }) => {
        return (
          <span class={record.qualityControlNumerical != formModel.value.qualityControlNumerical ? 'danger' : ''}>
            {record?.qualityControlNumerical ?? '-'}
          </span>
        );
      },
    },
  ];

  defineExpose({
    openModal,
  });
</script>

<style scoped>
  :deep(.danger) {
    color: red;
  }
  .title {
    width: 100%;
    text-align: center;
  }
</style>
