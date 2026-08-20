import { FormProps, ModalFormInstance } from '@bmos/components';

export const useForm = () => {
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {},
    labelWidth: 100,
    schemas: [
      {
        label: t('物料编号'),
        field: 'materialNo',
        component: 'Input',
        componentProps: {
          disabled: true,
        },
      },
      {
        label: t('物料名称'),
        field: 'materialName',
        component: 'Input',
        componentProps: {
          disabled: true,
        },
      },
      {
        label: t('供应商'),
        field: 'supplierName',
        component: 'Input',
        componentProps: {
          disabled: true,
        },
      },
      {
        label: t('物料规格'),
        field: 'specificationName',
        component: 'Input',
        componentProps: {
          disabled: true,
        },
      },
      {
        label: t('可用库存量'),
        field: 'availableCount',
        component: 'Input',
        componentProps: {
          disabled: true,
        },
      },
      {
        label: t('消耗数量'),
        field: 'useCount',
        required: true,
        component: 'InputNumber',
        componentProps: {
          style: {
            width: '100%',
          },
          min: 1,
          precision: 0,
        },
      },
      {
        label: t('消耗原因'),
        field: 'reasonId',
        required: true,
        component: 'Select',
        componentProps: {
          options: [],
        },
      },
      {
        label: t('备注'),
        field: 'remark',
        component: 'InputTextArea',
        componentProps: {
          maxlength: 200,
          showCount: true,
        },
      },
    ],
  });

  const setFormModels = (values: any) => {
    modalFormRef.value?.formRef?.setFormModels(values);
  };

  const updateSchema = (obj: any | any[]) => {
    modalFormRef.value?.formRef?.updateSchema(obj);
  };

  return {
    modalFormRef,
    formProps,
    setFormModels,
    updateSchema,
  };
};
