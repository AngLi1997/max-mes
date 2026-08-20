import { FormProps, ModalFormInstance } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useForm = () => {
  const { plasmaAcceptanceStatusDict } = getDicts();
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {
      result: 1,
      restrictedFlag: 0,
    },
    labelWidth: 140,
    schemas: [
      {
        label: t('审核结果'),
        field: 'result',
        required: true,
        component: 'Select',
        colProps: {
          span: 12,
        },
        componentProps: {
          options: plasmaAcceptanceStatusDict.filter((item: any) => item.value !== 0),
        },
      },
      {
        label: t('限制级血浆'),
        field: 'restrictedFlag',
        required: true,
        component: 'Switch',
        colProps: {
          span: 12,
        },
        componentProps: {
          checkedValue: 1,
          unCheckedValue: 0,
        },
      },
      {
        label: t('备注'),
        field: 'remark',
        component: 'InputTextArea',
        colProps: {
          span: 24,
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
