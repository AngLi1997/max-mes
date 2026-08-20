import { generateSortingPlanNo } from '@/services';
import type { FormProps, ModalFormInstance, RenderCallbackParams } from '@bmos/components';
import { message } from 'ant-design-vue';

export const useModalForm = () => {
  const { warehouseDict } = getDicts();
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {},
    labelWidth: 140,
    schemas: [
      {
        label: t('计划类型'),
        field: 'sortingTypeName',
        required: true,
        component: 'Select',
        componentProps: ({ formInstance }: RenderCallbackParams) => {
          return {
            fieldNames: {
              label: 'sortingTypeName',
              value: 'sortingTypeName',
            },
            options: [],
            onChange: (val: number, option: any) => {
              // console.log(val, option);
              setFormModels({
                batchNo: undefined,
                systemSortingManageId: undefined,
              });
              if (!val) {
                formInstance?.updateSchema({
                  field: 'systemSortingManageId',
                  componentProps: {
                    options: [],
                  },
                });
                return;
              }
              formInstance?.updateSchema({
                field: 'systemSortingManageId',
                componentProps: {
                  options: option.sortingManageDescList,
                },
              });
            },
          };
        },
      },
      {
        label: t('计划描述'),
        field: 'systemSortingManageId',
        required: true,
        component: 'Select',
        componentProps: {
          fieldNames: {
            label: 'typeDescribe',
            value: 'systemSortingManageId',
          },
          options: [],
          onChange: async (val: any) => {
            if (!val) {
              setFormModels({
                batchNo: undefined,
              });
            } else {
              try {
                const { data } = await generateSortingPlanNo({
                  systemSortingManageId: val,
                });
                setFormModels({
                  batchNo: data,
                });
              } catch (error: any) {
                error.message && message.error(error.message);
              }
            }
          },
        },
      },
      {
        label: t('分拣仓库'),
        field: 'warehouse',
        vIf: getWarehouseConfigByCode.value,
        required: true,
        component: 'Select',
        componentProps: {
          options: warehouseDict,
        },
      },
      {
        label: t('计划批号'),
        field: 'batchNo',
        required: true,
        component: 'Input',
      },
      {
        label: t('预计出库日期'),
        field: 'expectedDate',
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
