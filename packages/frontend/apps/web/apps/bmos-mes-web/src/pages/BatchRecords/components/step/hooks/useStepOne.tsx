import {
  getPlanProcessList,
  reqLotRecordsManageProductTree,
  reqLotRecordsTemplateVersionNormal,
  reqPlanInfoListUnTerminatePlanByProcessId,
} from '@/services';
import { FormProps, RenderCallbackParams } from '@bmos/components';
import { loopSelectableTree } from '@bmos/utils';

export type UseStepOneParams = {
  props: any;
};
export const useStepOne = ({ props }: UseStepOneParams) => {
  const stepOneRef = ref<any>();

  const configFormProps: Ref<FormProps> = ref({
    showActionButtonGroup: false,
    baseColProps: { span: 24 },
    labelWidth: 100,
    schemas: [
      {
        field: 'lotRecordsTemplateId',
        component: 'Select',
        label: t('批记录模板'),
        required: true,
        componentProps: {
          disabled: true,
          request: async () => {
            return [{ label: props.formValue?.name, value: props.formValue.lotRecordsTemplateId }];
          },
        },
      },
      {
        field: 'lotRecordsVersion',
        component: 'Select',
        label: t('批记录版本'),
        required: true,
        componentProps: {
          disabled: true,
          request: async () => {
            return [{ label: props.formValue?.lotRecordsVersion, value: props.formValue.lotRecordsVersion }];
          },
        },
      },
      {
        field: 'productId',
        component: 'TreeSelect',
        label: t('产品'),
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            fieldNames: {
              label: 'showName',
              value: 'id',
            },
            showSearch: true,
            treeNodeFilterProp: 'showName',
            request: async () => {
              try {
                const { data } = await reqLotRecordsManageProductTree(props.formValue.lotRecordsTemplateId);
                return loopSelectableTree(data, 'categoryFlag', true);
              } catch (error) {}
            },
            onChange: () => {
              formModel.processId = undefined;
              formModel.batchNo = undefined;
            },
          };
        },
      },
      {
        field: 'processId',
        component: 'Select',
        label: t('工艺'),
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            fieldNames: {
              label: 'name',
              value: 'id',
            },
            showSearch: true,
            request: {
              watchFields: ['productId'],
              options: {
                immediate: true,
              },
              callback: async () => {
                try {
                  if (!formModel.productId) {
                    return [];
                  }
                  const { data } = await getPlanProcessList({
                    productId: formModel.productId,
                  });
                  return data;
                } catch (error) {
                  return [];
                }
              },
            },
            onChange: () => {
              formModel.batchNo = undefined;
            },
          };
        },
      },
      {
        field: 'planId',
        component: 'Select',
        label: t('生产批号'),
        required: true,
        componentProps: ({ formModel, formInstance }: RenderCallbackParams) => {
          return {
            options: [],
            showSearch: true,
            fieldNames: {
              label: 'batchNo',
              value: 'id',
            },
            request: {
              watchFields: ['processId'],
              options: {
                immediate: true,
              },
              callback: async () => {
                try {
                  if (!formModel.processId) {
                    return [];
                  }
                  const { data } = await reqPlanInfoListUnTerminatePlanByProcessId(formModel.processId);
                  return data;
                } catch (error) {
                  return [];
                }
              },
            },
          };
        },
      },
    ],
  });

  const manageFormProps: Ref<FormProps> = ref({
    showActionButtonGroup: false,
    baseColProps: { span: 24 },
    labelWidth: 100,
    schemas: [
      {
        field: 'lotRecordsTemplateId',
        component: 'Select',
        label: t('批记录模板'),
        required: true,
        componentProps: {
          disabled: true,
          request: async () => {
            return [{ label: props.formValue?.name, value: props.formValue?.lotRecordsTemplateId }];
          },
        },
      },
      {
        field: 'lotRecordsVersion',
        component: 'Select',
        label: t('批记录版本'),
        required: true,
        componentProps: {
          fieldNames: {
            label: 'version',
            value: 'id',
          },
          request: async () => {
            try {
              if (!props.formValue?.lotRecordsTemplateId) {
                return [];
              }
              const { data } = await reqLotRecordsTemplateVersionNormal({
                templateInfoId: props.formValue?.lotRecordsTemplateId,
              });
              return data;
            } catch (error) {
              return [];
            }
          },
        },
      },
      {
        field: 'productId',
        component: 'TreeSelect',
        label: t('产品'),
        required: true,
        componentProps: {
          disabled: true,
          request: async () => {
            return [{ label: props.formValue?.productName, value: props.formValue?.productId }];
          },
        },
      },
      {
        field: 'processId',
        component: 'Select',
        label: t('工艺'),
        required: true,
        componentProps: {
          disabled: true,
          request: async () => {
            return [{ label: props.formValue?.processName, value: props.formValue?.processId }];
          },
        },
      },
      {
        field: 'planId',
        component: 'Select',
        label: t('生产批号'),
        required: true,
        componentProps: {
          disabled: true,
          request: async () => {
            return [{ label: props.formValue?.batchNo, value: props.formValue?.planId }];
          },
        },
      },
    ],
  });

  const stepOneFormProps = computed(() => {
    if (props.isMange) {
      return manageFormProps.value;
    } else {
      return configFormProps.value;
    }
  });

  return {
    stepOneRef,
    stepOneFormProps,
  };
};
