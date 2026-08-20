import { FormProps, ModalFormInstance } from '@bmos/components';

export const useForm = () => {
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {},
    labelWidth: 140,
    schemas: [
      {
        label: t('排序序号'),
        field: 'orderNum',
        component: 'Input',
        dynamicRules: () => {
          return [
            {
              trigger: 'blur',
              validator: async (_, value) => {
                if (!value || /^[1-9]\d*$/.test(value)) {
                  return Promise.resolve();
                }
                return Promise.reject(new Error(t('请输入正整数')));
              },
            },
            {
              required: true,
              trigger: 'blur',
              message: t('请输入排序序号'),
            },
          ];
        },
      },
      {
        label: t('判断依据'),
        field: 'judgmentBasis',
        required: true,
        component: 'InputTextArea',
        componentProps: {
          rows: 3,
          maxlength: 30,
          showCount: true,
        },
      },
      {
        label: t('启用'),
        field: 'useFlag',
        required: true,
        component: 'Switch',
        componentProps: {
          checkedValue: 1,
          unCheckedValue: 0,
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
