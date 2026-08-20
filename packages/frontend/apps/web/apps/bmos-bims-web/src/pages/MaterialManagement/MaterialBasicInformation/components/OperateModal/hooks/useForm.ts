import { getSupplierSelectList } from '@/services';
import { FormProps, ModalFormInstance } from '@bmos/components';

export const useForm = () => {
  const { materialTypeDict, unitDict } = getDicts();
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {},
    labelWidth: 110,
    schemas: [
      {
        label: t('物料名称'),
        field: 'name',
        required: true,
        component: 'Input',
        componentProps: {
          maxlength: 30,
          showCount: true,
        },
      },
      {
        label: t('物料分类'),
        field: 'type',
        required: true,
        component: 'Select',
        componentProps: {
          options: materialTypeDict,
        },
      },
      {
        label: t('物料编号'),
        field: 'code',
        required: true,
        component: 'Input',
        componentProps: {
          maxlength: 15,
          showCount: true,
        },
      },
      {
        label: t('供应商'),
        field: 'supplierId',
        required: true,
        component: 'Select',
        componentProps: {
          fieldNames: {
            label: 'name',
            value: 'id',
          },
          request: async () => {
            const { data } = await getSupplierSelectList();
            return data;
          },
        },
      },
      {
        label: t('单位'),
        field: 'unit',
        required: true,
        component: 'Select',
        componentProps: {
          options: unitDict,
        },
      },
      {
        label: t('备注'),
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
