import { FormProps, ModalFormInstance } from '@bmos/components';

export const useForm = () => {
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    // labelWidth: 140,
    initialValues: {
      fileNo: '',
    },
    schemas: [
      {
        label: t('文件编号'),
        field: 'fileNo',
        required: true,
        colProps: {
          span: 12,
          style: {
            marginRight: 'auto',
          },
        },
        component: 'Input',
      },
      {
        label: t('备注'),
        field: 'remark',
        colProps: {
          span: 24,
        },
        component: 'InputTextArea',
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
