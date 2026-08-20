import { FormProps, Recordable, formInstance } from '@bmos/components';
import { t } from '@bmos/i18n';
import { reactive, ref } from 'vue';

export type UseFormParams = {
  status: Ref<Boolean>;
};

export const useForm = (useFormContext: UseFormParams) => {
  // const status = ref(false)
  const { status } = useFormContext;
  // form实例
  const setFormRef = ref<formInstance>();

  const setFormProps = reactive<FormProps>({
    showAdvancedButton: false,
    showActionButtonGroup: false,
    baseColProps: {
      span: 24,
    },
    schemas: [
      {
        field: 'reason',
        label: t('原因'),
        component: 'Input',
        required: true,
        vIf: () => {
          return status.value === true;
        },
        componentProps: {
          // disabled: true,
        },
      },
      {
        field: 'amount',
        label: t('取样量'),
        component: 'Input',
        required: true,
        vIf: () => {
          return status.value === false;
        },
        componentProps: {
          // disabled: true,
        },
      },
      {
        field: 'name',
        label: t('用户名'),
        component: 'Input',
        required: true,
        componentProps: {
          // disabled: true,
        },
      },
      {
        field: 'password',
        label: t('密码'),
        required: true,
        component: 'Input',
        componentProps: {
          // disabled: true,
        },
      },
    ],
  });

  const setNodeFormData = async (formData: Recordable) => {
    try {
      await nextTick();
      Object.keys(formData).forEach(key => {
        if (key === 'label') {
          setFormRef.value?.setFormModel('name', formData[key]);
        }
        setFormRef.value?.setFormModel(key, formData[key]);
      });
    } catch (error) {}
  };

  return {
    setFormRef,
    setFormProps,
    setNodeFormData,
  };
};
