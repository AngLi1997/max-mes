import { FormProps, TableColumn } from '@bmos/components';

type DescriptionItem = {
  label: string;
  field: string;
  renderFn?: any;
};

export const descriptionItems = reactive<DescriptionItem[]>([
  {
    label: t('来源单位'),
    field: 'originOrg',
  },
  {
    label: t('检品批号'),
    field: 'sampleBatchNo',
  },
  {
    label: t('检品数量'),
    field: 'totalNum',
  },
  {
    label: t('合格数量'),
    field: 'qualifiedNum',
  },
  {
    label: t('不合格数量'),
    field: 'unQualifiedNum',
  },
  {
    label: t('收检日期'),
    field: 'acceptanceDate',
  },
  {
    label: t('报告人'),
    field: 'reportBy',
  },
  {
    label: t('报告日期'),
    field: 'reportDate',
  },
  {
    label: t('审核人'),
    field: 'auditBy',
  },
  {
    label: t('审核日期'),
    field: 'reportDate',
  },
]);

export const formProps = reactive<FormProps>({
  initialValues: {},
  useMaxLengthRule: false,
  showResetButton: false,
  showSubmitButton: false,
  layout: 'vertical',
  baseColProps: {
    span: 24,
  },
  schemas: [
    {
      label: t('文件编号'),
      field: 'documentNo',
      required: true,
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('报告编号'),
      field: 'reportNo',
      required: true,
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('核查批号'),
      field: 'checkNo',
      required: true,
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('判定依据'),
      field: 'checkBase',
      required: true,
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('核查结果'),
      field: 'checkResult',
      required: true,
      component: 'InputTextArea',
      componentProps: {
        disabled: true,
        maxlength: 3000,
        rows: 10,
      },
    },
    {
      label: t('核查结论'),
      field: 'conclusion',
      required: true,
      component: 'InputTextArea',
      componentProps: {
        disabled: true,
        maxlength: 3000,
        rows: 10,
      },
    },
  ],
});

export const columns: TableColumn[] = [
  {
    title: t('操作人'),
    dataIndex: 'createBy',
    width: 100,
    resizable: true,
  },
  {
    title: t('操作日期'),
    dataIndex: 'createTime',
    width: 170,
    resizable: true,
  },
  {
    title: t('操作事项'),
    dataIndex: 'content',
    width: 200,
    resizable: true,
  },
  {
    title: t('操作备注'),
    dataIndex: 'remark',
    width: 100,
    resizable: true,
  },
];
