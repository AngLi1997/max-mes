import { FormProps, ModalFormInstance } from '@bmos/components';

export const useForm = () => {
  const { maintainStatusDict } = getDicts();
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {
      maintainStatus: 1,
    },
    schemas: [
      {
        label: t('维护状态'),
        field: 'maintainStatus',
        required: true,
        component: 'Select',
        colProps: {
          span: 12,
          style: {
            marginRight: 'auto',
          },
        },
        componentProps: () => {
          return {
            options: maintainStatusDict,
          };
        },
      },
      {
        label: t('维护备注'),
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

  return {
    modalFormRef,
    formProps,
    setFormModels,
  };
};
