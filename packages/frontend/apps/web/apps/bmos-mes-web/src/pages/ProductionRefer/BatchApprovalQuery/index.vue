<!-- 批次审核查询 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :showToolBars="[true]"
    :formProps="[formFirstProps as any]"
    :requests="[reqApprovalQueryListReq as DataRequestFn]"
    :columns="[columnsFirst as any]">
    <template #tableHeaderToolbar0="{ currentNode, instance }:any">
      <Button
        v-hasAuth="120050007000001"
        :disabled="viewDisabled"
        type="primary"
        @click="reportView(currentNode, instance)">
        {{ t('报表查看') }}
      </Button>
      <!-- echarts图弹框 -->
      <reportViewModal ref="conclusionModalRef" :reportInfo="reportInfo" :pieChartData="pieChartData" />
    </template>
    <template #tableHeaderTitle0>
      <BMTableTitle :title="t('批次审核查询')"></BMTableTitle>
    </template>
  </BMPageComponent>
</template>

<script lang="tsx" setup>
  import { getBatchApprovalQueryPage, getProcessNameList, getProcedureNameList, getPieChartData } from '@/services';
  import { DataRequestFn, BMPageComponent, BMTableTitle } from '@bmos/components';
  import type { FormProps } from '@bmos/components';
  import StateTag from '@/components/StateTag/index.vue';
  import { reactive, onMounted } from 'vue';
  import { t } from '@bmos/i18n';
  import { message } from 'ant-design-vue';
  import reportViewModal from './components/reportViewModal.vue';
  const conclusionModalRef = ref<any>();
  const processList = ref<any>([]); //工艺名称下拉
  const pageRef = ref<any>();
  const procedureList = ref<any>([]);
  const reportInfo = ref<any>({
    processName: '',
    procedureName: '',
  });
  const flag = ref<number>(0);
  const pieChartData = ref<any>([]); //饼图数据
  const viewDisabled = ref<boolean>(false); //默认
  const formFirstProps = reactive<Partial<FormProps>>({
    // showAdvancedButton: false, //展示更多
    actionColOptions: {
      // span: 6,
    },
    baseColProps: {
      span: 6,
    },
    fieldMapToTime: [['reportTime', ['startTime', 'endTime'], 'YYYY-MM-DD']],
  });
  const columnsFirst = ref<any>([
    {
      title: t('产品名称'),
      dataIndex: 'productName',
      fixed: 'left',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('产品编码'),
      dataIndex: 'productCode',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('产品规格'),
      dataIndex: 'productSpecification',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('工艺名称'),
      dataIndex: 'processName',
      resizable: true,
      width: 200,
      formItemProps: {
        component: 'Select',
        componentProps: ({ formInstance, formModel }: any) => ({
          options: processList.value,
          onChange: async (value: any, option: any) => {
            await getProcedureList(option?.id);
            formInstance?.updateSchema({
              field: 'procedureName',
              componentProps: {
                options: procedureList.value,
              },
            });
            formModel['procedureName'] = undefined;
          },
        }),
      },
    },
    {
      title: t('生产批号'),
      dataIndex: 'planBatchNo',
      width: 180,
      resizable: true,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('生产开始时间'),
      dataIndex: 'startTime',
      width: 180,
      resizable: true,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('生产结束时间'),
      dataIndex: 'endTime',
      width: 180,
      resizable: true,
      hideInSearch: true,
      sorter: true,
    },

    {
      title: t('工序名称'),
      dataIndex: 'procedureName',
      width: 150,
      resizable: true,
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: () => ({
          options: [],
        }),
      },
    },
    {
      title: t('工序完成时间'),
      dataIndex: 'procedureTime',
      width: 150,
      resizable: true,
      hideInTable: true,
      hideInSearch: true,
    },

    {
      title: t('审核备注'),
      dataIndex: 'remark',
      width: 180,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('审核结论'),
      dataIndex: 'confirmOpinion',
      fixed: 'right',
      width: 150,
      resizable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: [
            {
              label: t('合格'),
              value: 'ELIGIBLE',
            },
            {
              label: t('不合格'),
              value: 'NOT_ELIGIBLE',
            },
            {
              label: t('其他'),
              value: 'RESTS',
            },
          ],
        },
      },
      customRender: ({ record }: any) => {
        return record.confirmOpinion ? (
          <StateTag
            type={
              record.confirmOpinion?.value === 'ELIGIBLE'
                ? 'success'
                : record.confirmOpinion?.value === 'NOT_ELIGIBLE'
                ? 'danger'
                : record.confirmOpinion?.value === 'RESTS'
                ? 'default'
                : ''
            }>
            {record.confirmOpinion?.label || '-'}
          </StateTag>
        ) : (
          '-'
        );
      },
    },
    {
      title: t('填报时间'),
      align: 'left',
      dataIndex: 'reportTime',
      width: 190,
      hideInTable: true,
      resizable: true,
      formItemProps: {
        colProps: { span: 6 },
        component: 'RangePicker',
      },
    },
  ]);
  // 获取表格数据
  const reqApprovalQueryListReq = async (params: any) => {
    if (params?.procedureName && flag.value !== 1) {
      flag.value = 1;
      pageRef.value.getTableRef(0).addColumn(
        [
          {
            title: t('工序名称'),
            dataIndex: 'procedureName',
          },
          {
            title: t('工序完成时间'),
            dataIndex: 'procedureTime',
          },
        ],
        'remark',
      );
    }
    if (!params?.procedureName && flag.value == 1) {
      flag.value = 0;
      pageRef.value.getTableRef(0).removeColumn(['procedureName', 'procedureTime']);
    }
    reportInfo.value = params;
    const data = {
      ...params,
      processId: params?.processName,
      processName: undefined,
    };
    const res: any = await getBatchApprovalQueryPage(data);
    viewDisabled.value = res?.data?.list?.length > 0 ? false : true;
    const res2: any = await getPieChartData(data);
    const option = processList.value.find((item: any) => item.value == data.processId);
    reportInfo.value.processName = option?.label;
    const pass = res2.data.filter((item: any) => item.confirmOpinion?.value === 'ELIGIBLE')[0]?.number || 0;
    const other = res2.data.filter((item: any) => item.confirmOpinion?.value === 'RESTS')[0]?.number || 0;
    const noPass = res2.data.filter((item: any) => item.confirmOpinion?.value === 'NOT_ELIGIBLE')[0]?.number || 0;
    pieChartData.value = [
      {
        name: t('合格'),
        value: pass,
      },
      {
        name: t('其他'),
        value: other,
      },
      {
        name: t('不合格'),
        value: noPass,
      },
    ];
    return res;
  };
  // 获取工艺名称下拉数据
  const getProcessList = async () => {
    try {
      const res = await getProcessNameList();
      processList.value = res.data.map((item: any) => {
        return {
          ...item,
          value: item.id,
        };
      });
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 工艺下拉改变时获取工序名称下拉数据
  const getProcedureList = async (processId: any) => {
    try {
      if (!processId) {
        procedureList.value = [];
        return;
      }
      let res = await getProcedureNameList({ processId });
      procedureList.value = res.data.map((item: any) => {
        return {
          label: item,
          value: item,
        };
      });
    } catch (error: any) {
      message.error(error.message);
      procedureList.value = [];
    }
  };
  // 报表查看
  const reportView = async () => {
    conclusionModalRef.value.openModal();
  };
  // const res = instance.queryFormRef?.getFormValues(); //获取搜索表单

  onMounted(() => {
    getProcessList();
  });
</script>
