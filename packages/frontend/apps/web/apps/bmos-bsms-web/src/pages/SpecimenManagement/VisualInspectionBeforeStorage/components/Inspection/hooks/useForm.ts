import { FormProps, ModalFormInstance } from '@bmos/components';

export const useForm = () => {
  const { plasmaAppearanceDict } = getDicts();
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {},
    schemas: [
      {
        label: t('标本外观'),
        field: 'appearance',
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
            options: plasmaAppearanceDict,
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

  return {
    modalFormRef,
    formProps,
    setFormModels,
  };
};
