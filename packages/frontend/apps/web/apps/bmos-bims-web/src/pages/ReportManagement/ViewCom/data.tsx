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
      field: 'fileNo',
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
      label: t('判定依据'),
      field: 'checkBase',
      required: true,
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('检验结论'),
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
    title: t('标本编号'),
    dataIndex: 'sampleNo',
    width: 190,
    resizable: true,
  },
  {
    title: t('采浆日期'),
    dataIndex: 'slurryDate',
    width: 140,
    sorter: true,
    resizable: true,
  },
  {
    title: t('献浆者编号'),
    dataIndex: 'plasmaDonorNo',
    width: 150,
    sorter: true,
    resizable: true,
  },
  {
    title: t('姓名'),
    dataIndex: 'plasmaDonorName',
    width: 100,
    resizable: true,
  },
  {
    title: t('性别'),
    dataIndex: 'sex',
    width: 80,
    resizable: true,
    customRender: ({ record }) => {
      return record?.sex?.name;
    },
  },
  {
    title: t('血型'),
    dataIndex: 'bloodType',
    width: 80,
    resizable: true,
    customRender: ({ record }) => {
      return record?.bloodType?.name;
    },
  },
  {
    title: t('血浆编号'),
    dataIndex: 'plasmaNo',
    width: 170,
    resizable: true,
  },
  {
    title: t('免疫类型'),
    dataIndex: 'immunityType',
    width: 140,
    resizable: true,
    customRender: ({ record }) => {
      return record?.immunityType?.name ?? '-';
    },
  },
  {
    title: t('检测免疫类型'),
    dataIndex: 'titerType',
    width: 120,
    resizable: true,
    customRender: ({ record }) => {
      return record?.titerType?.name ?? '-';
    },
  },
  {
    title: t('不合格项目'),
    dataIndex: 'unQualifiedItems',
    width: 140,
    resizable: true,
  },
];
