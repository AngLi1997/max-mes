import { FormProps, Recordable, formInstance } from '@bmos/components';
import { t } from '@bmos/i18n';
import { reactive, ref } from 'vue';

export const useForm = () => {
  // form实例
  const setFormRef = ref<formInstance>();

  const setFormProps = reactive<FormProps>({
    showAdvancedButton: false,
    showActionButtonGroup: false,
    baseColProps: {
      span: 6,
    },
    schemas: [
      {
        field: 'result',
        label: t('检验结论'),
        component: 'InputTextArea',
        required: true,
        componentProps: {
          maxLength: 200,
          autoSize: { minRows: 3, maxRows: 3 },
        },
      },
    ],
  });

  const setNodeFormData = async (formData: Recordable) => {
    try {
      await nextTick();
      Object.keys(formData).forEach(key => {
        if (key === 'label') {
          setFormRef.value?.formRef.setFormModel('name', formData[key]);
        }
        setFormRef.value?.formRef.setFormModel(key, formData[key]);
      });
    } catch (error) {}
  };

  return {
    setFormRef,
    setFormProps,
    setNodeFormData,
  };
};
