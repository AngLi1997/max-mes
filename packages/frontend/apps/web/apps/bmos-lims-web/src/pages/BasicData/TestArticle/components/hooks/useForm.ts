import { FormProps, Recordable, formInstance } from '@bmos/components';
import { t } from '@bmos/i18n';
import { reactive, ref } from 'vue';

export type UseFormParams = {
  watchStatus: Boolean;
};

export const useForm = (useFormContext: UseFormParams) => {
  const { watchStatus } = useFormContext;

  // form实例
  const setFormRef = ref<formInstance>();

  const setFormProps = reactive<FormProps>({
    showAdvancedButton: false,
    showActionButtonGroup: false,
    labelAlign: 'left',
    baseColProps: {
      span: 6,
    },
    schemas: [
      {
        field: 'name',
        label: t('检品名称'),
        component: 'Span',
        // required: true,
        // componentProps: {
        //   disabled: true,
        // },
      },
      {
        field: 'mergeCode',
        label: t('检品编码'),
        // required: true,
        component: 'Span',
        // componentProps: {
        //   disabled: true,
        // },
      },
      {
        field: 'specification',
        label: t('规格'),
        component: 'Span',
        // required: true,
        // componentProps: {
        //   disabled: true,
        // }
      },
      {
        field: 'unit',
        label: t('单位'),
        component: 'Span',
        // required: true,
        // componentProps: {
        //   disabled: true,
        // }
      },
      {
        field: 'xxx',
        label: '',
        component: 'Divider',
        colProps: {
          span: 24,
        },
        // required: true,
        // componentProps: {
        //   disabled: true,
        // }
      },
      {
        field: 'description',
        component: watchStatus ? 'Span' : 'Input',
        label: t('备注'),
        componentProps: {
          maxLength: 100,
          // autoSize: { minRows: 3, maxRows: 3 }
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
