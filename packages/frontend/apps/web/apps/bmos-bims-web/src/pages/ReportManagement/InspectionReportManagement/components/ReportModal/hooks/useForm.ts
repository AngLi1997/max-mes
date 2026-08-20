import { FormProps, ModalFormInstance } from '@bmos/components';

export const useForm = () => {
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {},
    useMaxLengthRule: false,
    schemas: [
      {
        label: t('文件编号'),
        field: 'fileNo',
        required: true,
        component: 'Input',
        componentProps: {
          maxlength: 30,
          showCount: true,
        },
      },
      {
        label: t('报告编号'),
        field: 'reportNo',
        required: true,
        component: 'Input',
        componentProps: {
          maxlength: 30,
          showCount: true,
        },
      },
      {
        label: t('判定依据'),
        field: 'checkBase',
        required: true,
        component: 'Input',
        componentProps: {
          maxlength: 100,
          showCount: true,
        },
      },
      {
        label: t('检验结论'),
        field: 'conclusion',
        required: true,
        component: 'InputTextArea',
        useMaxLengthRule: false,
        componentProps: {
          maxlength: 300,
          showCount: true,
          autoSize: { minRows: 5, maxRows: 10 },
        },
      },
    ],
  });

  const setFormModels = (values: any) => {
    modalFormRef.value?.formRef?.setFormModels(values);
  };

  return {
    modalFormRef,
    formProps,
    setFormModels,
  };
};
