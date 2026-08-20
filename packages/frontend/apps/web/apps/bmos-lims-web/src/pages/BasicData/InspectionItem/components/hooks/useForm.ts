import { FormProps, Recordable, formInstance } from '@bmos/components';
import { t } from '@bmos/i18n';
import { reactive, ref } from 'vue';
import { MODAL_STATUS } from '../../types/enum';

export type UseFormParams = {
  watchStatus: Ref<MODAL_STATUS>;
};

export const useForm = (useFormContext: UseFormParams) => {
  const { watchStatus } = useFormContext;

  // form实例
  const setFormRef = ref<formInstance>();

  const setFormProps = reactive<FormProps>({
    showAdvancedButton: false,
    showActionButtonGroup: false,
    baseColProps: {
      span: 8,
    },
    schemas: [
      {
        field: 'name',
        label: t('检验项目名称'),
        component: 'Input',
        required: true,
        componentProps: {
          maxLength: 30,
        },
      },
      {
        field: 'code',
        label: t('检验项目编码'),
        required: true,
        component: 'Input',
        componentProps: {
          maxLength: 30,
          disabled: watchStatus.value !== MODAL_STATUS.ADD,
        },
      },
      {
        field: 'reportName',
        label: t('检验项目报告名'),
        component: 'Input',
        required: true,
        componentProps: {
          maxLength: 30,
          disabled: watchStatus.value !== MODAL_STATUS.ADD,
        },
      },
      {
        field: 'description',
        component: 'Input',
        label: t('备注'),
        labelWidth: 111,
        componentProps: {
          maxLength: 100,
          // autoSize: { minRows: 3, maxRows: 4 }
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
