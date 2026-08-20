import { FormProps, ModalFormInstance } from '@bmos/components';

export const useForm = () => {
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {
      useFlag: 1,
    },
    labelWidth: 140,
    schemas: [
      {
        label: t('采浆中心名称'),
        field: 'name',
        required: true,
        component: 'Input',
      },
      {
        label: t('简称'),
        field: 'shorterName',
        required: true,
        component: 'Input',
      },
      {
        label: t('联系电话'),
        field: 'tel',
        required: true,
        component: 'Input',
        componentProps: {
          // maxlength: 11,
          // showCount: true,
        },
      },
      {
        label: t('中文简称'),
        field: 'abbr',
        required: true,
        component: 'Input',
      },
      {
        label: t('采浆中心系统地址'),
        field: 'stationUrl',
        required: true,
        component: 'Input',
      },
      {
        label: t('系统编码'),
        field: 'sysNo',
        required: true,
        component: 'Input',
      },
      {
        label: t('启用'),
        field: 'useFlag',
        required: true,
        component: 'Switch',
        componentProps: {
          checkedValue: 1,
          unCheckedValue: 0,
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
