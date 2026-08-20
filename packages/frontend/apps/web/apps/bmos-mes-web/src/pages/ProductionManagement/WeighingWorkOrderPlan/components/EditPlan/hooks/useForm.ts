import { FormProps } from '@bmos/components';

export const useForm = (isEdit: Ref<boolean>, changeFn: () => void) => {
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
    showActionButtonGroup: false,
    schemas: [
      {
        label: t('工单编号'),
        field: 'materialName',
        component: 'Input',
        required: true,
        componentProps: {
          disabled: true,
        },
      },
      {
        label: t('计划生产日期'),
        field: 'planDate',
        component: 'DatePicker',
        required: true,
        componentProps: {
          disabled: !isEdit.value,
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
          onChange: () => changeFn(),
        },
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
