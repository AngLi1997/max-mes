import { FormProps, ModalFormInstance } from '@bmos/components';

export const useForm = () => {
  const { supplierTypeDict } = getDicts();
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {},
    labelWidth: 110,
    schemas: [
      {
        label: t('供应商名称'),
        field: 'name',
        required: true,
        component: 'Input',
        componentProps: {
          maxlength: 30,
          showCount: true,
        },
      },
      {
        label: t('供应商编号'),
        field: 'code',
        required: true,
        component: 'Input',
        componentProps: {
          maxlength: 15,
          showCount: true,
        },
      },
      {
        label: t('供应商类型'),
        field: 'type',
        required: true,
        component: 'Select',
        componentProps: {
          options: supplierTypeDict,
        },
      },
      {
        label: t('负责人'),
        field: 'principal',
        required: true,
        component: 'Input',
        componentProps: {
          maxlength: 5,
          showCount: true,
        },
      },
      {
        label: t('联系方式'),
        field: 'contactWay',
        required: true,
        component: 'Input',
        componentProps: {
          maxlength: 15,
          showCount: true,
        },
      },
      {
        label: t('地址'),
        field: 'address',
        required: true,
        component: 'InputTextArea',
        componentProps: {
          maxlength: 50,
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
