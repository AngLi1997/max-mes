import type { FormProps, ModalFormInstance } from '@bmos/components';

export const useModalForm = () => {
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {},
    labelWidth: 120,
    schemas: [
      {
        label: t('浆站出库批号'),
        field: 'syncBatchNo',
        required: true,
        component: 'Input',
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
