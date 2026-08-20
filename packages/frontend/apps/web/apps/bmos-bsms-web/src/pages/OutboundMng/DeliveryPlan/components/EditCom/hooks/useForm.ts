import { FormProps, formInstance } from '@bmos/components';
import { t } from '@bmos/i18n';
import { reactive, ref } from 'vue';

export const useForm = () => {
  // form实例
  const formRef = ref<formInstance>();

  const formProps = reactive<FormProps>({
    showAdvancedButton: false,
    showActionButtonGroup: false,
    labelWidth: 140,
    baseColProps: {
      span: 6,
    },
    schemas: [
      {
        label: t('出库批号') + ':',
        field: 'batchNo',
        component: 'Span',
      },
      {
        label: t('出库仓库'),
        field: 'warehouse',
        vIf: getWarehouseConfigByCode.value,
        component: 'Span',
      },
      {
        label: t('出库类别') + ':',
        field: 'type',
        component: 'Span',
      },
      {
        label: t('出库日期') + ':',
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
        label: t('出库单血浆类型') + ':',
        field: 'deliveryPlasmaType',
        colProps: {
          span: 24,
        },
        required: true,
        component: 'Input',
        componentProps: {
          maxlength: 30,
          showCount: true,
        },
      },
      {
        label: t('备注') + ':',
        field: 'remark',
        colProps: {
          span: 24,
        },
        component: 'InputTextArea',
        useMaxLengthRule: false,
        componentProps: {
          maxlength: 300,
          autoSize: { minRows: 2, maxRows: 2 },
        },
      },
    ],
  });

  const setFormModels = (values: any) => {
    formRef.value?.setFormModels(values);
  };

  return {
    formRef,
    formProps,
    setFormModels,
  };
};
