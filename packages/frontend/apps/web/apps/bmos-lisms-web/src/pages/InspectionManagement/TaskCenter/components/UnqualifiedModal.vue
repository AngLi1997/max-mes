<template>
  <NormalModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('不合格数据')"
    :showOkButton="false"
    :cancelButtonText="t('返回')"
    wrapClassName="modalSizeExtraLarge">
    <div class="unqualified-table">
      <BMTable
        ref="tableRef"
        :search="false"
        :data-source="tableData"
        :columns="columns"
        row-key="id"
        :pagination="false"
        :showToolBar="false"
        :scroll="{ x: 800, y: 400 }"></BMTable>
    </div>
  </NormalModalForm>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMModalForm, Recordable, BMTable, TableColumn, NormalModalForm } from '@bmos/components';

  defineOptions({
    inheritAttrs: false,
  });

  const { InspectionProjectDict } = getDicts();

  const open = defineModel<boolean>('modalOpen', {
    default: false,
  });

  withDefaults(
    defineProps<{
      tableData: Recordable[];
    }>(),
    {
      tableData: () => [],
    },
  );

  const modalFormRef = ref<InstanceType<typeof BMModalForm>>();
  const { getDateFormat } = useConfig();
  const columns: TableColumn[] = [
    {
      title: t('标本编号'),
      dataIndex: 'orgSampleNo',
      width: 160,
    },
    {
      title: t('检验项目'),
      dataIndex: 'code',
      width: 170,
      customRender: ({ record }) => {
        return InspectionProjectDict.find((item: any) => item.value === record.code)?.label ?? '-';
      },
    },
    {
      title: t('检验次数'),
      dataIndex: ['inspectTimes', 'label'],
      width: 100,
    },
    {
      title: t('结果值'),
      dataIndex: 'inspectValue',
      width: 100,
    },
    {
      title: t('检验结果'),
      dataIndex: 'unqualified',
      width: 170,
      customRender: ({ record }) => {
        return record.unqualified ? t('不合格') : t('合格');
      },
    },
    {
      title: t('检验人'),
      dataIndex: 'inspector',
      width: 170,
    },
    {
      title: t('检验日期'),
      dataIndex: 'inspectTime',
      width: 170,
      customRender: ({ record }) => {
        return getDateFormat(record.inspectTime);
      },
    },
    {
      title: t('复核人'),
      dataIndex: 'checkBy',
      width: 140,
    },
    {
      title: t('复核日期'),
      dataIndex: 'checkTime',
      width: 170,
      customRender: ({ record }) => {
        return getDateFormat(record.checkTime);
      },
    },
  ];
</script>

<style lang="less" scoped>
  .unqualified-table {
    height: 100px;
  }
  :deep(.bmos-table .lisms-table-body) {
    border-bottom: none !important;
  }
</style>
