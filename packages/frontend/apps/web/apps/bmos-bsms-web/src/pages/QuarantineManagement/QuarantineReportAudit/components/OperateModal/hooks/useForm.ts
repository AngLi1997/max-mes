import { FormProps, ModalFormInstance } from '@bmos/components';

export const useForm = () => {
  const { quarantineAuditStatusDict } = getDicts();
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
            options: quarantineAuditStatusDict,
          };
        },
      },
      {
        label: t('审核备注'),
        field: 'auditRemark',
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
    console.log('type', type);
    formProps.schemas[0].vIf = type === 'audit';
  };

  return {
    modalFormRef,
    formProps,
    setFormModels,
    showCheck,
  };
};
