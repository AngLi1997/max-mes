import { getReportTemplateDetailById, getReportTemplatePull } from '@/services';
import { FormProps, ModalFormInstance } from '@bmos/components';

export const useForm = () => {
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {},
    labelWidth: 140,
    useMaxLengthRule: false,
    schemas: [
      {
        label: t('报告编号'),
        field: 'reportBillNo',
        required: true,
        component: 'Span',
        colProps: {
          span: 12,
        },
      },
      {
        label: t('检品名称'),
        field: 'checkArticleName',
        required: true,
        component: 'Input',
        colProps: {
          span: 12,
        },
      },
      {
        label: t('判定依据'),
        field: 'systemReportManageId',
        required: true,
        component: 'Select',
        colProps: {
          span: 12,
          style: {
            marginRight: 'auto',
          },
        },
        componentProps: {
          fieldNames: {
            label: 'judgmentBasis',
            value: 'id',
          },
          request: async () => {
            const { data } = await getReportTemplatePull({ reportType: 2 });
            return data;
          },
          onChange: async (val: any, option: any) => {
            if (!val) {
              setFormModels({
                checkResult: undefined,
                conclusion: undefined,
              });
            }
            const { data } = await getReportTemplateDetailById(val);
            // console.log(val, option, data);
            setFormModels({
              checkResult: data.quarantineWithin,
              conclusion: data.conclusion,
              checkBase: option.judgmentBasis,
            });
          },
        },
      },
      {
        label: t('核查结果'),
        field: 'checkResult',
        required: true,
        component: 'InputTextArea',
        colProps: {
          span: 24,
        },
        componentProps: {
          maxlength: 3000,
          showCount: true,
          autoSize: { minRows: 10, maxRows: 20 },
        },
      },
      {
        label: t('结论'),
        field: 'conclusion',
        required: true,
        component: 'InputTextArea',
        colProps: {
          span: 24,
        },
        componentProps: {
          maxlength: 3000,
          showCount: true,
          autoSize: { minRows: 5, maxRows: 10 },
        },
      },
    ],
  });

  const updateSchemas = (obj: any) => {
    modalFormRef.value?.formRef?.updateSchema(obj);
  };

  const setFormModels = (values: any) => {
    modalFormRef.value?.formRef?.setFormModels(values);
  };

  return {
    modalFormRef,
    formProps,
    setFormModels,
    updateSchemas,
  };
};
