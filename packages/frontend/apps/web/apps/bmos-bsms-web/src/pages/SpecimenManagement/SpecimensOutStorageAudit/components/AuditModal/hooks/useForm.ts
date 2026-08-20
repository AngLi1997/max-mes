import { FormProps, ModalFormInstance } from '@bmos/components';

export const useForm = () => {
  const { auditResultDict } = getDicts();
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {},
    schemas: [
      {
        label: t('审核结果'),
        field: 'auditStatus',
        required: true,
        vIf: true,
        component: 'Select',
        colProps: {
          span: 12,
          style: {
            marginRight: 'auto',
          },
        },
        componentProps: () => {
          return {
            options: auditResultDict,
          };
        },
      },
      {
        label: t('备注'),
        field: 'remark',
        component: 'InputTextArea',
        colProps: {
          span: 24,
        },
        componentProps: {
          maxlength: 30,
          showCount: true,
        },
      },
    ],
  });

  const setFormModels = (values: any) => {
    modalFormRef.value?.formRef?.setFormModels(values);
  };

  const showCheck = (type: 'audit' | 'return') => {
    formProps.schemas[0].vIf = type === 'audit';
  };

  return {
    modalFormRef,
    formProps,
    setFormModels,
    showCheck,
  };
};
