import { FormProps, Recordable, formInstance } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useForm = () => {
  // form实例
  const setFormRef = ref<formInstance>();

  const setFormProps = reactive<FormProps>({
    layout: 'vertical',
    showAdvancedButton: false,
    showActionButtonGroup: false,
    baseColProps: {
      span: 24,
    },
    schemas: [
      {
        field: 'name',
        component: 'Input',
        label: t('流程名称'),
        required: true,
        componentProps: {
          maxLength: 10,
        },
      },
      {
        field: 'remark',
        component: 'InputTextArea',
        label: t('备注'),
        componentProps: {
          maxLength: 50,
          rows: 4,
          class: 'add-flow-modal-textarea',
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
