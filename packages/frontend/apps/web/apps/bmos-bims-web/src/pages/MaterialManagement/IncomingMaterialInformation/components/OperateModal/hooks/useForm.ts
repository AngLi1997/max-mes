import { getMaterialDetail, getMaterialSelectList, getSupplierSelectList } from '@/services';
import { FormProps, ModalFormInstance } from '@bmos/components';

export const useForm = () => {
  const { materialTypeDict, unitDict } = getDicts();
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {},
    labelWidth: 110,
    schemas: [
      {
        label: t('物料名称'),
        field: 'materialId',
        required: true,
        component: 'Select',
        componentProps: {
          fieldNames: {
            label: 'name',
            value: 'id',
          },
          request: async () => {
            const { data } = await getMaterialSelectList();
            return data;
          },
          onChange: async (val: any) => {
            if (!val) {
              setFormModels({
                type: undefined,
                supplierId: undefined,
                unit: undefined,
              });
              return;
            }
            const { data } = await getMaterialDetail(val);
            setFormModels({
              type: data?.type?.value,
              supplierId: data.supplierId,
              unit: data?.unit?.value,
            });
          },
        },
      },
      {
        label: t('物料分类'),
        field: 'type',
        required: true,
        component: 'Select',
        componentProps: {
          disabled: true,
          options: materialTypeDict,
        },
      },
      {
        label: t('供应商'),
        field: 'supplierId',
        required: true,
        component: 'Select',
        componentProps: {
          fieldNames: {
            label: 'name',
            value: 'id',
          },
          disabled: true,
          request: async () => {
            const { data } = await getSupplierSelectList();
            return data;
          },
        },
      },
      {
        label: t('单位'),
        field: 'unit',
        required: true,
        component: 'Select',
        componentProps: {
          disabled: true,
          options: unitDict,
        },
      },
      {
        label: t('规格'),
        field: 'specification',
        required: true,
        component: 'Input',
        componentProps: {
          maxlength: 100,
          showCount: true,
        },
      },
      {
        label: t('批号'),
        field: 'batchNo',
        required: true,
        component: 'Input',
        componentProps: {
          maxlength: 30,
          showCount: true,
        },
      },
      {
        label: t('生产日期'),
        field: 'generateData',
        required: true,
        component: 'DatePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        label: t('有效日期'),
        field: 'effectiveDate',
        required: true,
        component: 'DatePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
    ],
  });

  const setFormModels = (values: any) => {
    modalFormRef.value?.formRef?.setFormModels(values);
  };

  const showCheck = (type: 'audit' | 'return') => {
    formProps.schemas[0].vIf = type === 'audit';
  };

  return {
    modalFormRef,
    formProps,
    setFormModels,
    showCheck,
  };
};
