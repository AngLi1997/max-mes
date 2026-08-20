import { FormProps } from '@bmos/components';

export const useForm = () => {
  const formRef = ref();

  const formProps = reactive<FormProps>({
    initialValues: {},
    layout: 'vertical',
    showAdvancedButton: false,
    showActionButtonGroup: false,
    useMaxLengthRule: false,
    baseColProps: { span: 24 },
    schemas: [
      {
        label: t('核查结果'),
        field: 'jg',
        component: 'TableTitle',
      },
      {
        label: t('核查结果模板'),
        field: 'quarantineWithin',
        vIf: false,
        component: 'InputTextArea',
        componentProps: {
          rows: 5,
          maxlength: 3000,
          showCount: true,
        },
      },
      {
        label: t('检疫期内核查结果模板'),
        field: 'quarantineWithin',
        vIf: true,
        component: 'InputTextArea',
        componentProps: {
          rows: 5,
          maxlength: 3000,
          showCount: true,
        },
      },
      {
        label: t('检疫期超期核查结果模板'),
        field: 'quarantineOutside',
        vIf: true,
        component: 'InputTextArea',
        componentProps: {
          rows: 5,
          maxlength: 3000,
          showCount: true,
        },
      },
      {
        label: t('核查结论'),
        field: 'jl',
        component: 'TableTitle',
      },
      {
        label: t('核查结论模板'),
        field: 'conclusion',
        component: 'InputTextArea',
        componentProps: {
          rows: 5,
          maxlength: 3000,
          showCount: true,
        },
      },
    ],
  });

  const setFormModels = (values: any) => {
    formRef.value?.setFormModels(values);
  };

  const changeSchemas = (type: number) => {
    console.log(type);
    if (type === 1) {
      // formRef.value.updateSchema({
      //   field:
      // })
      formProps.schemas[1].vIf = false;
      formProps.schemas[2].vIf = true;
      formProps.schemas[3].vIf = true;
    } else {
      formProps.schemas[1].vIf = true;
      formProps.schemas[2].vIf = false;
      formProps.schemas[3].vIf = false;
    }
  };

  return {
    formRef,
    formProps,
    setFormModels,
    changeSchemas,
  };
};
