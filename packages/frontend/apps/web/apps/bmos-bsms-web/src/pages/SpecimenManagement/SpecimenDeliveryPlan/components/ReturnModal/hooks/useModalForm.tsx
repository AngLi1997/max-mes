import type { FormProps, ModalFormInstance } from '@bmos/components';
export const useModalForm = () => {
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {},
    labelWidth: 140,
    schemas: [
      {
        label: t('退回批号'),
        field: 'sortingPlanBatchNo',
        vIf: true,
        required: true,
        component: 'Input',
      },
      {
        label: t('退回托盘号'),
        field: 'palletNo',
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
