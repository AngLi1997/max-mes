import { FormProps, ModalFormInstance } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useForm = () => {
  const { checkTypeDict } = getDicts();
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {
      restrictedFlag: 0,
    },
    labelWidth: 140,
    schemas: [
      {
        label: t('核对方式'),
        field: 'verifyType',
        required: true,
        component: 'Select',
        colProps: {
          span: 12,
          style: {
            marginRight: 'auto',
          },
        },
        componentProps: {
          options: checkTypeDict,
        },
      },
    ],
  });

  const setFormModels = (values: any) => {
    modalFormRef.value?.formRef?.setFormModels(values);
  };

  const updateSchema = (obj: any) => {
    modalFormRef.value?.formRef?.updateSchema(obj);
  };

  return {
    modalFormRef,
    formProps,
    setFormModels,
    updateSchema,
  };
};
