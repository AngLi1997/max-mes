import { FormProps, ModalFormInstance, RenderCallbackParams } from '@bmos/components';

export const useForm = () => {
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {},
    labelWidth: 140,
    schemas: [
      {
        label: t('停运时间'),
        field: 'stopTime',
        required: true,
        component: 'DatePicker',
        componentProps: ({ formInstance }: RenderCallbackParams) => {
          return {
            showTime: true,
            showNow: true,
            format: 'YYYY-MM-DD HH:mm:ss',
          };
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
