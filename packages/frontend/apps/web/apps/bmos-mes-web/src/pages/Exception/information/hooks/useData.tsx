import { getDictNoRulesList, getExceptionBatchPage } from '@/services';
import { BMStateTag, FormProps, TableColumn } from '@bmos/components';
export const useData = () => {
  const statusList = ref<any>(['primary', 'warning', 'success']);
  const historyOpen = ref(false);
  const tableInstance = ref();
  const productPlanId = ref('');
  const rowData = ref();
  const myFormRef = ref();

  // 查询按钮点击
  const searchClick = async () => {
    tableInstance.value.fetchData();
  };
  const columns: TableColumn[] = [
    {
      title: t('工序名称'),
      dataIndex: 'procedureName',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('工序步骤/任务名称'),
      dataIndex: 'procedureStepName',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('异常类型'),
      dataIndex: 'exceptionType',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('异常描述'),
      dataIndex: 'exceptionDescription',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('异常状态'),
      dataIndex: 'exceptionStatus',
      width: 100,
      resizable: true,
      hideInSearch: true,
      customRender: ({ record }) => (
        <BMStateTag type={statusList.value[record.exceptionStatus.value]}>{record.exceptionStatus.name}</BMStateTag>
      ),
    },
    {
      title: t('记录方式'),
      dataIndex: 'recordMode',
      width: 200,
      resizable: true,
      hideInSearch: true,
      customRender: ({ record }) => <div>{record.recordMode.name}</div>,
    },
    {
      title: t('记录人'),
      dataIndex: 'recordUserName',
      width: 170,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('记录时间'),
      dataIndex: 'recordTime',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('处理结果'),
      dataIndex: 'handleResult',
      width: 300,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('处理人'),
      dataIndex: 'handleUserName',
      width: 100,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('处理时间'),
      dataIndex: 'handleTime',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('作废原因'),
      dataIndex: 'cancelReason',
      width: 300,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('作废人'),
      dataIndex: 'cancelUserName',
      width: 100,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('作废时间'),
      dataIndex: 'cancelTime',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 100,
      actions: ({ record }) => [
        {
          label: t('操作历史'),
          onClick: () => {
            rowData.value = record;
            historyOpen.value = true;
          },
        },
      ],
    },
  ];

  // 表单属性
  const formProps: Ref<FormProps> = ref({
    initialValues: {
      //默认值
    },
    transformDateFunc: (date: any) => {
      return date?.format?.('YYYY-MM-DD') ?? date;
    },
    labelWidth: 80,
    schemas: [
      {
        field: 'exceptionStatus',
        component: 'Select',
        label: t('异常状态'),
        colProps: {
          span: 6,
        },
        componentProps: () => {
          return {
            options: [
              { label: t('调查中'), value: '0' },
              { label: t('已关闭'), value: '1' },
              { label: t('已作废'), value: '2' },
            ],
          };
        },
      },
      {
        field: 'exceptionType',
        component: 'Select',
        label: t('异常类型'),
        colProps: {
          span: 6,
        },
        componentProps: () => ({
          request: async () => {
            // 获取设备数据
            try {
              const { data } = await getDictNoRulesList({ dictId: '120090001001' });
              return data;
            } catch (error: any) {
              console.log('======异常类型', error);
            }
          },
        }),
      },
      {
        field: 'exceptionDescription',
        component: 'Input',
        label: t('异常描述'),
        colProps: {
          span: 6,
        },
      },
    ],
  });
  // 详情信息
  const descData = ref([
    {
      label: t('产品名称'),
      value: 'productName',
    },
    {
      label: t('产品编码'),
      value: 'productMergeCode',
    },
    {
      label: t('产品规格'),
      value: 'productSpecification',
    },
    {
      label: t('工艺名称'),
      value: 'processName',
    },
    {
      label: t('生产批号'),
      value: 'batchNo',
    },
    {
      label: t('生产开始时间'),
      value: 'startTime',
    },
    {
      label: t('生产结束时间'),
      value: 'endTime',
    },
  ]);
  const tableReset = () => {
    tableInstance.value.fetchData();
  };
  const getDatasetPageList = async (params: any) => {
    const data = await myFormRef.value?.validate();
    return await getExceptionBatchPage({ ...params, ...data, productPlanId: productPlanId.value });
  };
  return {
    columns,
    historyOpen,
    formProps,
    descData,
    searchClick,
    tableInstance,
    productPlanId,
    rowData,
    myFormRef,
    tableReset,
    getDatasetPageList,
  };
};
