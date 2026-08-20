<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('检验结果')"
    :formProps="formProps"
    :okText="t('确定')"
    wrapClassName="modalSizeLarge"
    @okModal="ok">
    <template #selectA>
      <BMDescriptions :column="2" :list="descData" bordered size="small" :showBottomBorder="false" />
    </template>
    <template #selectB>
      <Descriptions :column="2">
        <DescriptionsItem :label="t('请验单号')">{{ props.inspectionRowData?.inspectNo }}</DescriptionsItem>
        <DescriptionsItem :label="t('汇总检验结果')">
          <span :style="{ color: props.inspectionRowData?.inspectResult?.value === 'QUALIFIED' ? '#59BF78' : '' }">
            {{ resultEnum[props.inspectionRowData?.inspectResult?.value] }}
          </span>
        </DescriptionsItem>
      </Descriptions>
      <BMTable
        ref="tableRef"
        row-key="id"
        :dataSource="dataSource"
        :columns="columns"
        :pagination="false"
        :search="false"
        :showToolBar="false"
        :scroll="{ x: 800, y: 300 }"></BMTable>
    </template>
  </BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm, ModalFormInstance, BMTable, TableColumn, BMDescriptions } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { reactive, ref, nextTick } from 'vue';

  const props = defineProps({
    inspectionRowData: {
      type: Object,
      default: () => {},
    },
  });
  const modalFormRef = ref<ModalFormInstance>();
  const tableRef = ref();
  const open = ref<boolean>(false);
  const resultEnum: any = {
    QUALIFIED: t('合格'),
    QUARANTINE: t('待验'),
    RESTRICTED_RELEASE: t('限制性放行'),
    SAMPLED: t('已取样'),
    UNQUALIFIED: t('不合格'),
  };
  const descData = computed(() => {
    return [
      {
        label: t('产品名称'),
        value: props.inspectionRowData?.productName || '-',
      },
      {
        label: t('产品编码'),
        value: props.inspectionRowData?.productMergeCode || '-',
      },
      {
        label: t('指令单编号'),
        value: props.inspectionRowData?.planNo || '-',
      },
      {
        label: t('生产批号'),
        value: props.inspectionRowData?.batchNo || '-',
      },
      {
        label: t('物料类型'),
        value: props.inspectionRowData?.materialType?.name || '-',
      },
      {
        label: t('物料信息'),
        value: props.inspectionRowData?.materialName || '-',
      },
      {
        label: t('物料批号'),
        value: props.inspectionRowData?.materialBatchNo || '-',
      },
      {
        label: t('请验时间'),
        value: props.inspectionRowData?.inspectTime || '-',
      },
    ];
  });

  // 查看的表单
  const formProps = reactive({
    initialValues: {},
    schemas: [
      {
        field: 'field1',
        component: 'Divider',
        label: t('检验信息'),
        colProps: {
          span: 24,
        },
        componentProps: {
          orientation: 'left',
          orientationMargin: '0px',
          showLeftBorder: true,
        },
      },
      {
        field: 'info',
        label: '',
        noLabel: true,
        colProps: {
          span: 24,
        },
        slot: 'selectA',
      },
      {
        field: 'field2',
        component: 'Divider',
        label: t('检验结果'),
        colProps: {
          span: 24,
        },
        componentProps: {
          orientation: 'left',
          orientationMargin: '0px',
          showLeftBorder: true,
        },
      },
      {
        field: 'result',
        label: '',
        noLabel: true,
        colProps: {
          span: 24,
        },
        slot: 'selectB',
      },
    ],
  });
  const columns: TableColumn[] = [
    {
      title: t('检项代码'),
      dataIndex: 'inspectProgramNo',
      width: 100,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('检验名称'),
      dataIndex: 'inspectProgramName',
      width: 100,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('检验结果'),
      dataIndex: 'inspectResult',
      width: 100,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('检项结论'),
      dataIndex: 'inspectConclusion',
      resizable: true,
      width: 100,
      hideInSearch: true,
      customRender: ({ record }) => {
        return (
          <div style={{ color: record.inspectConclusion?.value === 'UNQUALIFIED' ? 'red' : '' }}>
            {resultEnum[record.inspectConclusion?.value]}
          </div>
        );
      },
    },
  ];
  // 弹框表格数据来源
  const dataSource = ref<any>([]);
  const openModal = () => {
    open.value = true;
  };
  // 弹框确定按钮
  const ok = () => {
    open.value = false;
  };
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        dataSource.value = props.inspectionRowData?.inspectProgramResultVOList;
      }
    },
  );

  defineExpose({ openModal });
</script>
<style lang="less" scoped>
  :deep(.mes-descriptions-view) {
    border-radius: 2px;
  }
</style>
