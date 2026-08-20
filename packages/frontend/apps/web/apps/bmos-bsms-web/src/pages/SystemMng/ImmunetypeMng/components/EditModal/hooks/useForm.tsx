import ColorPicker from '@/components/ColorPicker/index.vue';
import type { FormProps, ModalFormInstance, RenderCallbackParams } from '@bmos/components';
export const useForm = () => {
  const modalFormRef = ref<ModalFormInstance>();
  const formProps = reactive<FormProps>({
    initialValues: {},
    labelWidth: 100,
    schemas: [
      {
        label: t('免疫类型'),
        field: 'immunityName',
        component: 'Span',
      },
      {
        label: t('类型描述'),
        field: 'immunityDes',
        component: 'Span',
      },
      {
        label: t('标识颜色'),
        field: 'colour',
        component: ({ formModel }: RenderCallbackParams) => {
          return <ColorPicker v-model={formModel.colour} type='hex8' />;
        },
        required: true,
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
