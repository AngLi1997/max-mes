import { FormProps, ModalFormInstance } from '@bmos/components';

export const useForm = () => {
  const { auditResultDict } = getDicts();
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    labelWidth: 140,
    schemas: [
      {
        label: t('审核结果'),
        field: 'auditResult',
        required: true,
        colProps: {
          span: 12,
          style: {
            marginRight: 'auto',
          },
        },
        component: 'Select',
        componentProps: {
          options: auditResultDict.filter((item: any) => item.value !== 0),
        },
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
