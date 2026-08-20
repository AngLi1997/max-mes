import { FormProps } from '@bmos/components';

export const useForm = () => {
  const myFormRef = ref();

  const setFormModels = (values: any) => {
    myFormRef.value?.setFormModels(values);
  };

  const updateSchema = (obj: any) => {
    myFormRef.value?.updateSchema(obj);
  };
  // 表单配置
  const formProps: Partial<FormProps> = {
    initialValues: {},
    labelWidth: 120,
    layout: 'horizontal',
    baseColProps: {
      span: 8,
    },
    autoAdvancedLine: 10,
    alwaysShowLines: 6,
    actionColOptions: {
      span: 2,
    },
    showActionButtonGroup: false,
    schemas: [
      {
        field: 'productName',
        component: 'Span',
        label: t('产品信息'),
      },
      {
        field: 'bomName',
        component: 'Span',
        label: t('生产BOM'),
      },
      {
        field: 'batchNo',
        component: 'Span',
        label: t('生产批号'),
      },
      {
        field: 'weighCentreName',
        component: 'Span',
        label: t('称量中心'),
      },
      {
        field: 'planDate',
        component: 'Span',
        label: t('计划生产时间'),
      },
      {
        field: 'remark',
        component: 'Span',
        label: t('备注'),
      },
    ],
  };

  return {
    formProps,
    myFormRef,
    setFormModels,
    updateSchema,
  };
};
