import type { FormProps, ModalFormInstance, RenderCallbackParams } from '@bmos/components';
export const useModalForm = () => {
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {},
    schemas: [
      {
        label: t('出库批号'),
        field: 'batchNo',
        component: 'Input',
        vIf: true,
        // 正则校验9位数字
        rules: [{ required: true, pattern: /^\d{9}$/, message: t('请输入9位数字'), trigger: 'blur' }],
        componentProps: {
          maxlength: 9,
          showCount: true,
        },
        componentSlots: ({ formModel }: RenderCallbackParams) => {
          return {
            addonBefore: () => {
              return <div style={{ width: '40px' }}>{formModel.outType}</div>;
            },
          };
        },
      },
      {
        label: t('备注'),
        field: 'remark',
        vIf: true,
        component: 'InputTextArea',
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

  const changeSchema = (type: 'apply' | 'change') => {
    formProps.schemas[0].vIf = type === 'change';
    formProps.schemas[1].vIf = type === 'apply';
  };

  return {
    modalFormRef,
    formProps,
    setFormModels,
    changeSchema,
  };
};
