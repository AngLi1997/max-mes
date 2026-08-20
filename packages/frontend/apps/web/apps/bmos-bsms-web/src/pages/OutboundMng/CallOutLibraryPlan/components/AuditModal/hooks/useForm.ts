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
        vIf: true,
        required: true,
        component: 'Select',
        componentProps: () => {
          return {
            options: auditResultDict.filter((item: any) => {
              return item.value !== 0;
            }),
          };
        },
      },
      {
        label: t('审核备注'),
        field: 'remark',
        component: 'InputTextArea',
        componentProps: {
          maxlength: 100,
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
