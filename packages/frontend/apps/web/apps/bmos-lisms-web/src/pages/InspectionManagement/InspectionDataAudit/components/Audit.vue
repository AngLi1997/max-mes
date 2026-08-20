<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('检验结果发布审核')"
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
          :dataRequest="dataRequest"
          :columns="columns"
          :extraParams="{ sampleBatchNo: props.sampleBatchNo, auditStatus: 'TO_AUDIT' }"
          row-key="publishId"
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
          row-key="publishId"
          :rowClassName="(record: any) => {
            return record.inspectResult?.value === InspectionResultEnum.UNQUALIFIED ? 'unqualified-row' : 'qualified-row'
          }"
          :showToolBar="false"
          :scroll="{ x: 800, y: 400 }"></BMTable>
      </div>
    </template>
  </BMModalForm>
  <Sign ref="signRef" :signatureAction="1007" :afterSign="signSuccess" />
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMModalForm, FormProps, Recordable, BMTable, TableColumn } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { InspectionProjectEnum, InspectionResultEnum } from '@/types';
  import { postInspectAlldataAudit, postInspectDatapubCheck } from '@/services';
  import { Sign } from '@/components/Sign';
  import { arrayToObject, isEmpty } from '@bmos/utils';

  defineOptions({
    inheritAttrs: false,
  });
  const { getDateFormat } = useConfig();
  const { auditResultDict, InspectionProjectDict } = getDicts();

  const open = defineModel<boolean>('modalOpen', {
    default: false,
  });

  const emit = defineEmits(['ok']);
  const props = withDefaults(
    defineProps<{
      tableData: Recordable[];
      hasRequest?: boolean;
      sampleBatchNo?: string;
    }>(),
    {
      tableData: () => [],
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
    schemas: [
      {
        label: t('审核结果'),
        field: 'auditResult',
        required: true,
        component: 'Select',
        colProps: {
          span: 12,
          style: {
            marginRight: 'auto',
          },
        },
        componentProps: {
          options: auditResultDict,
        },
      },
      {
        label: t('备注'),
        field: 'reviewRemark',
        component: 'InputTextArea',
        colProps: {
          span: 24,
        },
      },
    ],
  });

  const columns: TableColumn[] = [
    {
      title: t('标本批号'),
      dataIndex: 'sampleBatchNo',
      width: 160,
    },
    {
      title: t('标本编号'),
      dataIndex: 'orgSampleNo',
      width: 170,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'donorTime',
      width: 170,
      customRender: ({ record }) => {
        return getDateFormat(record.donorTime);
      },
    },
    {
      title: t('检品状态'),
      dataIndex: ['testArticleStatus', 'label'],
      width: 120,
    },
    {
      title: t('检验结论'),
      dataIndex: ['inspectResult', 'label'],
      width: 120,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'donorNo',
      width: 170,
    },
  ];

  const tableRef = ref<InstanceType<typeof BMTable>>();

  const dataRequest = async (params: any) => {
    try {
      const res = await postInspectDatapubCheck({
        ...params,
        fetchSampleDetail: true,
      });
      return Promise.resolve({
        ...res,
        data: {
          ...res.data,
          list: res.data?.list?.map((item: any) => {
            return {
              ...item,
              ...(item.inspectItemList && arrayToObject(item.inspectItemList, 'code')),
            };
          }),
        },
      });
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        tableRef.value?.addColumnAfter(
          InspectionProjectDict?.map((item: any) => {
            return {
              title: item?.label,
              dataIndex: [item.value, 'result', 'label'],
              width: 150,
              customRender: ({ record }: any) => {
                if (isEmpty(record[`${item.value}`]?.code)) {
                  return '-';
                }
                switch (record[`${item.value}`].code) {
                  case InspectionProjectEnum.HBsAg:
                  case InspectionProjectEnum.AntiHCV:
                  case InspectionProjectEnum.HIVAgAb:
                  case InspectionProjectEnum.AntiTP:
                    if (record[`${item.value}`]?.result?.value) {
                      return record[`${item.value}`]?.result?.value === InspectionResultEnum.UNQUALIFIED
                        ? t('阳性')
                        : t('阴性');
                    } else {
                      return '-';
                    }

                  default:
                    return record[`${item.value}`]?.result?.label ?? '-';
                }
              },
            };
          }),
          'donorNo',
        );
      }
    },
  );
  const submitParams = ref<any>({}); // 提交参数
  const signRef = ref<InstanceType<typeof Sign>>();
  const { updateTable } = inject('page') as { updateTable: () => void };
  const submit = async (formModal: Recordable) => {
    try {
      submitParams.value = {
        ...(props.hasRequest
          ? { sampleBatchNo: props.sampleBatchNo }
          : {
              publishIds: props.tableData.map((item: any) => item.publishId),
              sampleNo: props.tableData.map((item: any) => item.sampleNo).join(','),
            }),
        auditResult: formModal.auditResult,
        auditRemark: formModal.reviewRemark,
      };
      await signRef.value?.openSign(submitParams.value);
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const signSuccess = async (signUrl: string) => {
    try {
      await postInspectAlldataAudit({
        ...submitParams.value,
        auditUrl: signUrl,
      });
      emit('ok');
      if (props.hasRequest) {
        updateTable();
      }
      message.success(t('操作成功'));
      open.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
</script>

<style lang="less"></style>
