import { FormProps, ModalFormInstance } from '@bmos/components';

export const useForm = () => {
  const { reportAuditStatusDict } = getDicts();
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {},
    schemas: [
      {
        label: t('审核结果'),
        field: 'auditResult',
        vIf: true,
        required: true,
        component: 'Select',
        componentProps: () => {
          return {
            options: reportAuditStatusDict.filter((item: any) => {
              return [3, 4].includes(item.value);
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
