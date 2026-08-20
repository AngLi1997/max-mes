<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="inspectItem.label + t('核对')"
    :formProps="formProps"
    wrapClassName="modalSizeExtraLarge"
    :getContainer="getContainer"
    :submit="submit">
    <template #formBefore>
      <div :style="{ height: tableData.length > 4 ? '40vh' : 'auto' }">
        <BMTable
          v-if="hasRequest"
          ref="tableRef"
          :search="false"
          :dataRequest="postInspectSingledataList"
          :columns="columns"
          :extraParams="{
            sampleBatchNo: props.sampleBatchNo,
            inspectItemCode: props.inspectItem.value,
            inspectDataStatus: 'CHECKED',
          }"
          row-key="id"
          :rowClassName="(record: any) => {
            return record.inspectResult?.value === InspectionResultEnum.UNQUALIFIED ? 'unqualified-row' : 'qualified-row'
          }"
          :showToolBar="false"
          :scroll="{ x: 800, y: 400 }"></BMTable>
        <BMTable
          v-else
          ref="tableRef"
          :search="false"
          :data-source="tableData"
          :columns="columns"
          row-key="id"
          :rowClassName="(record: any) => {
            return record.inspectResult?.value === InspectionResultEnum.UNQUALIFIED ? 'unqualified-row' : 'qualified-row'
          }"
          :showToolBar="false"
          :scroll="{ x: 800, y: 400 }"></BMTable>
      </div>
    </template>
  </BMModalForm>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMModalForm, FormProps, Recordable, BMTable, TableColumn } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { InspectionProjectEnum, InspectionResultEnum } from '@/types';
  import { postInspectSingledataCancelcheck, postInspectSingledataList } from '@/services';
  import { isEmpty } from '@bmos/utils';

  defineOptions({
    inheritAttrs: false,
  });

  const open = defineModel<boolean>('modalOpen', {
    default: false,
  });

  const emit = defineEmits(['ok']);
  const props = withDefaults(
    defineProps<{
      tableData?: Recordable[];
      inspectItem: Recordable;
      hasRequest?: boolean;
      sampleBatchNo?: string;
    }>(),
    {
      tableData: () => [],
      inspectItem: () => ({}),
      hasRequest: false,
      sampleBatchNo: '',
    },
  );

  // document 获取 bmos-page-component-container class 的节点
  const getContainer = (): HTMLElement => {
    return document.querySelector('.bmos-page-component-container') as unknown as HTMLElement;
  };

  const modalFormRef = ref<InstanceType<typeof BMModalForm>>();
  const formProps = reactive<FormProps>({
    schemas: [],
  });
  const { getDateFormat } = useConfig();
  const columns: TableColumn[] = [
    {
      title: t('检验项目'),
      dataIndex: 'inspectItemName',
      width: 200,
    },
    {
      title: t('标本编号'),
      dataIndex: 'orgSampleNo',
      width: 200,
    },
    {
      title: t('标本批号'),
      dataIndex: 'sampleBatchNo',
      width: 160,
    },
    {
      title: t('检品状态'),
      dataIndex: ['testArticleStatus', 'label'],
      width: 120,
    },
    {
      title: t('检验次数'),
      dataIndex: ['inspectTimes', 'label'],
      width: 120,
    },
    {
      title: t('结果值'),
      dataIndex: 'inspectValue',
      width: 150,
      sorter: true,
      customRender: ({ record }) => {
        if (isEmpty(record.inspectValue)) return '-';
        switch (props.inspectItem.value) {
          case InspectionProjectEnum.HBsAg:
          case InspectionProjectEnum.AntiHCV:
          case InspectionProjectEnum.HIVAgAb:
          case InspectionProjectEnum.AntiTP:
            return record.inspectValue === '-' ? `-(${t('阴性')})` : `+(${t('阳性')})`;
          default:
            return record.inspectValue;
        }
      },
    },
    {
      title: t('检验结果'),
      dataIndex: ['inspectResult', 'label'],
      width: 120,
      customRender: ({ record }) => {
        if (isEmpty(record.inspectValue)) return '-';
        switch (props.inspectItem.value) {
          case InspectionProjectEnum.HBsAg:
          case InspectionProjectEnum.AntiHCV:
          case InspectionProjectEnum.HIVAgAb:
          case InspectionProjectEnum.AntiTP:
            return record.inspectValue === '-' ? `${t('阴性')}` : `${t('阳性')}`;
          default:
            return record.inspectResult?.label;
        }
      },
    },
    {
      title: t('检验人'),
      dataIndex: 'inspector',
      width: 120,
    },
    {
      title: t('检验日期'),
      dataIndex: 'inspectTime',
      width: 170,
      customRender: ({ record }) => {
        return getDateFormat(record.inspectTime);
      },
    },
  ];

  const tableRef = ref<InstanceType<typeof BMTable>>();
  const { updateTable } = inject('page') as { updateTable: () => void };
  const submit = async () => {
    try {
      await postInspectSingledataCancelcheck({
        ...(props.hasRequest
          ? { sampleBatchNo: props.sampleBatchNo }
          : {
              sampleNos: props.tableData.map((item: any) => item.sampleNo),
              sampleNo: props.tableData.map((item: any) => item.sampleNo).join(','),
            }),
        inspectItemCode: props.inspectItem.value,
      });
      if (props.hasRequest) {
        updateTable();
      }
      emit('ok');
      message.success(t('操作成功'));
      open.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
</script>

<style lang="less"></style>
