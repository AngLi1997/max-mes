import type { FormProps, ModalFormInstance } from '@bmos/components';
export const useModalForm = () => {
  const { outTypeDict, qualifiedStatusDict, warehouseDict } = getDicts();
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {
      qualityStatus: 0,
      type: 4,
    },
    labelWidth: 140,
    schemas: [
      {
        label: t('质量状态'),
        field: 'qualityStatus',
        required: true,
        component: 'Select',
        componentProps: () => {
          return {
            options: qualifiedStatusDict.filter((item: any) => item.value === 0),
          };
        },
      },
      {
        label: t('出库类别'),
        field: 'type',
        required: true,
        component: 'Select',
        componentProps: {
          options: outTypeDict.filter((item: any) => item.value === 4),
        },
      },
      {
        label: t('出库仓库'),
        field: 'warehouseId',
        vIf: getWarehouseConfigByCode.value,
        required: true,
        component: 'Select',
        componentProps: {
          options: warehouseDict,
        },
      },
      {
        label: t('计划批号'),
        field: 'outPlanBatchNo',
        required: true,
        component: 'Input',
        dynamicRules: () => {
          return [
            {
              required: true,
              validator: async (_rule: any, value: any) => {
                // 1 ~ 15位数字或者字母
                const reg = /^[a-zA-Z0-9]{1,15}$/;
                if (!reg.test(value)) {
                  return Promise.reject(t('计划批号格式不正确'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        label: t('出库日期'),
        field: 'outPlanDate',
        required: true,
        component: 'DatePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        label: t('备注'),
        field: 'remark',
        component: 'InputTextArea',
        useMaxLengthRule: false,
        componentProps: {
          maxlength: 300,
          showCount: true,
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
