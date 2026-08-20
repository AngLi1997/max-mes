import ColorPicker from '@/components/ColorPicker/index.vue';
import type { FormProps, ModalFormInstance, RenderCallbackParams } from '@bmos/components';
export const useModalForm = () => {
  const modalFormRef = ref<ModalFormInstance>();
  const formProps = reactive<FormProps>({
    initialValues: {},
    labelWidth: 100,
    schemas: [
      {
        field: 'colour',
        component: ({ formModel }: RenderCallbackParams) => {
          return <ColorPicker v-model={formModel.colour} type='hex8' />;
        },
        label: t('颜色值'),
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
