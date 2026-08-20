<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('添加物料')"
    :submit="submit"
    :formProps="formProps"
    wrapClassName="modalSizeLarge"
    @cancelModal="cancelModal"></BMModalForm>
</template>

<script setup lang="tsx">
  import { reqWeighingWorkOrderPlanRequirementList } from '@/services';
  import { BMModalForm, BMTable, TableColumn, TableProps, Recordable } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { FormItemRest, message } from 'ant-design-vue';

  const emit = defineEmits(['submit']);

  const open = ref(false);

  const modalFormRef = ref<InstanceType<typeof BMModalForm> | null>(null);

  const tableRef = ref<InstanceType<typeof BMTable> | null>(null);

  const loading = ref(false);

  const tableData = ref([]);

  const columns = ref<TableColumn[]>([
    {
      title: t('物料批号'),
      dataIndex: 'storageMaterialBatchNo',
      width: 150,
    },
    {
      title: t('需求量'),
      dataIndex: 'formulaQuantity',
      width: 100,
      customRender: ({ record }: any) => `${record.formulaQuantity}${record.unit || ''}`,
    },
    {
      title: t('产品名称'),
      dataIndex: 'productName',
      width: 150,
    },
    {
      title: t('产品编码'),
      dataIndex: 'productMergeCode',
      width: 150,
    },
    {
      title: t('生产批号'),
      dataIndex: 'batchNo',
      width: 150,
    },
    {
      title: t('计划生产时间'),
      dataIndex: 'planDate',
      width: 150,
    },
    {
      title: t('需求用途'),
      dataIndex: 'requirementUsage',
      width: 150,
    },
    {
      title: t('备注'),
      dataIndex: 'remark',
      width: 120,
    },
  ]);

  const selectedRowKeys = ref<(string | number)[]>([]);

  const selectedRows = ref<any[]>([]);
  const rowSelection = computed<TableProps['rowSelection']>(() => {
    return {
      selectedRowKeys: selectedRowKeys.value,
      onChange: async (keys: (string | number)[], selectRows: any[]) => {
        // recalculateAmounts();
        selectedRowKeys.value = keys;
        selectedRows.value = selectRows;
      },
    };
  });

  const formProps: any = {
    initialValues: {},
    baseColProps: {
      span: 12,
    },
    layout: 'horizontal',
    showActionButtonGroup: false,
    schemas: [
      {
        label: t('物料名称'),
        field: 'materialName',
        component: 'Span',
      },
      {
        label: t('物料编码'),
        field: 'materialMergeCode',
        component: 'Span',
      },
      {
        label: t('物料规格'),
        field: 'materialSpecification',
        component: 'Span',
      },
      {
        label: t('称量中心'),
        field: 'weighCentreName',
        component: 'Span',
      },
      {
        noLabel: true,
        field: 'table',
        colProps: { span: 24 },
        component: () => {
          return (
            <FormItemRest>
              {/* @ts-ignore */}
              <BMTable
                ref={tableRef}
                rowKey='id'
                loading={loading.value}
                dataSource={tableData.value}
                columns={columns.value}
                pagination={false}
                search={false}
                showToolBar={false}
                scroll={{ x: 800, y: 300 }}
                rowSelection={rowSelection.value}></BMTable>
            </FormItemRest>
          );
        },
      },
    ],
  };

  const openModal = async (params: any, addIds: any, delIds: any) => {
    open.value = true;
    try {
      await nextTick();
      modalFormRef.value?.formRef?.setFormModels(params);
      loading.value = true;
      const { data } = await reqWeighingWorkOrderPlanRequirementList({
        materialMergeCode: params.materialMergeCode,
        storageMaterialBatchId: params.storageMaterialBatchId,
        unitId: params.unitId,
        weighCentreId: params.weighCentreId,
        addRequirementIds: addIds,
        deleteRequirementIds: delIds,
      });
      tableData.value = data;
    } catch (error: any) {
      error.message && message.error(error.message);
    } finally {
      loading.value = false;
    }
  };

  const cancelModal = () => {
    tableData.value = [];
    selectedRowKeys.value = [];
    selectedRows.value = [];
    open.value = false;
  };

  const submit = (_formModal: Recordable) => {
    if (selectedRowKeys.value.length === 0) {
      message.error(t('请勾选物料需求进行规划'));
      return Promise.reject();
    }
    emit('submit', selectedRows.value);
    cancelModal();
    return Promise.resolve();
  };

  defineExpose({
    openModal,
  });
</script>

<style scoped></style>
