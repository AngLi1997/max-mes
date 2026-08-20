import type { FormProps, ModalFormInstance } from '@bmos/components';
export const useModalForm = () => {
  const modalFormRef = ref<ModalFormInstance>();

  const type = ref<'apply' | 'editNo'>('apply');

  const formProps = reactive<FormProps>({
    initialValues: {},
    labelWidth: 80,
    schemas: [
      {
        label: t('出库批号'),
        field: 'newNo',
        vIf: type.value === 'editNo',
        required: true,
        component: 'Input',
        dynamicRules: () => {
          return [
            {
              required: true,
              validator: async (_rule: any, value: any) => {
                // 1 ~ 15位数字或者字母
                const reg = /^[a-zA-Z0-9]{1,15}$/;
                if (!reg.test(value)) {
                  return Promise.reject(t('计划批号格式不正确'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        label: t('申请备注'),
        field: 'remark',
        component: 'InputTextArea',
        vIf: type.value === 'apply',
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

  const changeType = (value: 'apply' | 'editNo') => {
    type.value = value;
    formProps.schemas[0].vIf = type.value === 'editNo';
    formProps.schemas[1].vIf = type.value === 'apply';
  };

  return {
    modalFormRef,
    formProps,
    setFormModels,
    type,
    changeType,
  };
};
