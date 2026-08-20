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
        field: 'ticketNo',
        component: 'Span',
        label: t('工单编号'),
      },
      {
        field: 'materialName',
        component: 'Span',
        label: t('物料信息'),
      },
      {
        field: 'materialSpecification',
        component: 'Span',
        label: t('物料规格'),
      },
      {
        field: 'storageMaterialNo',
        component: 'Span',
        label: t('物料批号'),
      },
      {
        field: 'weighCentreName',
        component: 'Span',
        label: t('称量中心'),
      },
      {
        field: 'planDate',
        component: 'Span',
        label: t('计划执行时间'),
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
