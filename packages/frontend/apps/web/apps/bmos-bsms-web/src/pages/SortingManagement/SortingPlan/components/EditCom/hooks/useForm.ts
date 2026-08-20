import { FormProps, formInstance } from '@bmos/components';
import { t } from '@bmos/i18n';
import { reactive, ref } from 'vue';

export const useForm = () => {
  // form实例
  const formRef = ref<formInstance>();

  const formProps = reactive<FormProps>({
    showAdvancedButton: false,
    showActionButtonGroup: false,
    labelWidth: 100,
    baseColProps: {
      span: 6,
    },
    schemas: [
      {
        label: t('计划批号') + ':',
        field: 'batchNo',
        component: 'Span',
        colProps: {
          span: 5,
        },
      },
      {
        label: t('计划类型') + ':',
        field: 'planType',
        component: 'Span',
        colProps: {
          span: 5,
        },
      },
      {
        label: t('计划描述') + ':',
        field: 'planDescription',
        component: 'Span',
        colProps: {
          span: 5,
        },
      },
      {
        label: t('分拣仓库') + ':',
        field: 'warehouseName',
        vIf: getWarehouseConfigByCode.value,
        component: 'Span',
        colProps: {
          span: 4,
        },
      },
      {
        label: t('预计出库日期') + ':',
        field: 'expectedDate',
        component: 'Span',
        colProps: {
          span: 5,
        },
      },
      {
        label: t('备注') + ':',
        field: 'remark',
        component: 'Span',
        colProps: {
          span: 24,
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
