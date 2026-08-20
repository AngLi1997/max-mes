import type { FormProps, TableColumn } from '@bmos/components';

export const descriptionItems = reactive([
  {
    label: t('检品来源'),
    field: 'originOrg',
  },
  {
    label: t('检品批号'),
    field: 'inspectionBatchNo',
  },
  {
    label: t('检品数量'),
    field: 'inspectionNumber',
  },
  {
    label: t('合格数量'),
    field: 'qualifiedNumber',
  },
  {
    label: t('不合格数量'),
    field: 'unqualifiedNumber',
  },
  {
    label: t('收检日期'),
    field: 'receiveDate',
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
    label: t('签发人'),
    field: 'issueBy',
  },
  {
    label: t('签发日期'),
    field: 'issueDate',
  },
]);

// 检验不合格信息
export const columns: TableColumn[] = [
  {
    title: t('献浆者编号'),
    dataIndex: 'plasmaDonorNo',
    width: 170,
    resizable: true,
  },
  {
    title: t('姓名'),
    dataIndex: 'name',
    width: 100,
    resizable: true,
    customRender: ({ record }) => {
      return record?.plasmaDonorInfoVO?.name;
    },
  },
  {
    title: t('血型'),
    dataIndex: 'bloodType',
    width: 100,
    resizable: true,
    customRender: ({ record }: any) => {
      return record?.plasmaDonorInfoVO?.bloodType?.name;
    },
  },
  {
    title: t('标本编号'),
    dataIndex: 'sampleNo',
    width: 180,
    resizable: true,
  },
  {
    title: t('血浆编号'),
    dataIndex: 'plasmaNo',
    width: 170,
    resizable: true,
  },
  {
    title: t('采浆日期'),
    dataIndex: 'slurryDate',
    width: 150,
    resizable: true,
  },
  {
    title: t('不合格项目'),
    dataIndex: 'unqualifiedItem',
    width: 170,
    resizable: true,
  },
];

// 检验报告
export const formProps = reactive<FormProps>({
  initialValues: {},
  useMaxLengthRule: false,
  showAdvancedButton: false,
  showActionButtonGroup: false,
  labelWidth: 100,
  baseColProps: {
    span: 24,
  },
  schemas: [
    {
      label: t('文件编号'),
      field: 'fileNo',
      required: true,
      component: 'Span',
    },
    {
      label: t('报告编号'),
      field: 'reportNo',
      required: true,
      component: 'Span',
    },
    {
      label: t('判定依据'),
      field: 'checkBase',
      required: true,
      component: 'Span',
    },
    {
      label: t('检验结论'),
      field: 'conclusion',
      required: true,
      component: 'InputTextArea',
      componentProps: {
        rows: 16,
        disabled: true,
      },
    },
  ],
});
