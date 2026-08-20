import { FormProps, ModalFormInstance } from '@bmos/components';

export const useForm = () => {
  const modalFormRef = ref<ModalFormInstance>();

  const { auditResultDict } = getDicts();

  const formProps = reactive<FormProps>({
    initialValues: {},
    schemas: [
      {
        label: t('批准结果'),
        field: 'approveResult',
        required: true,
        component: 'Select',
        colProps: {
          span: 12,
          style: {
            marginRight: 'auto',
          },
        },
        componentProps: {
          options: auditResultDict,
        },
      },
      {
        label: t('备注'),
        field: 'approveRemark',
        component: 'InputTextArea',
        colProps: {
          span: 24,
        },
        componentProps: {
          maxlength: 200,
          showCount: true,
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
