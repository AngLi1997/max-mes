import type { FormProps, ModalFormInstance } from '@bmos/components';
export const useModalForm = () => {
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {},
    labelWidth: 100,
    schemas: [
      {
        label: t('分拣批号'),
        field: 'sortingNo',
        vIf: true,
        required: true,
        component: 'Input',
      },
      {
        label: t('退回托盘号'),
        field: 'containerNo',
        vIf: false,
        required: true,
        component: 'Input',
      },
    ],
  });

  const changeType = (type: 'batch' | 'tray') => {
    formProps.schemas[0].vIf = type === 'batch';
    formProps.schemas[1].vIf = type === 'tray';
  };

  const setFormModels = (values: any) => {
    modalFormRef.value?.formRef?.setFormModels(values);
  };

  return {
    modalFormRef,
    formProps,
    setFormModels,
    changeType,
  };
};
