import { getTestArticlePackage } from '@/services/index';
import { formInstance, FormProps, Recordable, RenderCallbackParams } from '@bmos/components';
import { t } from '@bmos/i18n';
import dayjs, { Dayjs } from 'dayjs';
import { reactive, ref } from 'vue';

export type UseFormParams = {
  productList: Ref<any[]>;
};

export const useForm = () => {
  // const { productList } = useFormContext;
  // form实例
  const setFormRef = ref<formInstance>();

  const packageInfo = ref<any[]>([]);

  const disabledDate = (current: Dayjs) => {
    // Can not select days before today and today
    return current && current >= dayjs().endOf('day');
  };

  const setFormProps = reactive<FormProps>({
    showAdvancedButton: false,
    showActionButtonGroup: false,
    baseColProps: {
      span: 12,
    },
    schemas: [
      {
        field: 'productId',
        label: t('检品名称'),
        component: 'Select',
        required: true,
        componentProps: ({ formInstance }: RenderCallbackParams) => {
          return {
            // disabled: true,
            options: [],
            allowClear: true,
            showSearch: true,
            optionFilterProp: 'label',
            onChange: async (value: any, option: any) => {
              try {
                if (!value) {
                  formInstance.updateSchema({
                    field: 'packageId',
                    componentProps: {
                      options: [],
                    },
                  });
                  setNodeFormData({
                    productCode: undefined,
                    specification: undefined,
                    packageId: undefined,
                  });
                  return;
                }
                const { data } = await getTestArticlePackage(value);
                formInstance?.updateSchema({
                  field: 'packageId',
                  componentProps: {
                    options: data?.map((item: any) => {
                      return {
                        label: item.name,
                        value: item.packageId,
                      };
                    }),
                  },
                });
                setNodeFormData({
                  productCode: option.code,
                  specification: option.specification,
                });
              } catch (error: any) {
                console.error(error.message);
                // message.error(error.message);
              }
            },
          };
        },
      },
      {
        field: 'productCode',
        label: t('检品编码'),
        component: 'Input',
        required: true,
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'batchNo',
        label: t('批号'),
        required: true,
        component: 'Input',
        componentProps: {
          // disabled: true,
          maxlength: 30,
        },
      },
      {
        field: 'specification',
        label: t('规格'),
        component: 'Input',
        required: true,
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'inspectNumber',
        label: t('数量'),
        component: 'Input',
        required: true,
        componentProps: {
          // disabled: true,
          maxlength: 10,
        },
      },
      {
        field: 'level',
        label: t('级别'),
        component: 'Input',
        required: true,
        componentProps: {
          // disabled: true,
          maxlength: 10,
        },
      },
      {
        field: 'productionUnit',
        label: t('生产单位'),
        component: 'Input',
        required: false,
        componentProps: {
          // disabled: true,
          maxlength: 30,
        },
      },
      {
        field: 'supplier',
        label: t('供货单位'),
        component: 'Input',
        required: false,
        componentProps: {
          // disabled: true,
          maxlength: 30,
        },
      },
      {
        field: 'verifier',
        label: t('请验人'),
        component: 'Input',
        required: true,
        componentProps: {
          // disabled: true,
          maxlength: 10,
        },
      },
      {
        field: 'verifyDept',
        label: t('请验部门'),
        component: 'Input',
        required: true,
        componentProps: {
          // disabled: true,
          maxlength: 10,
        },
      },
      {
        field: 'packageId',
        label: t('实验包'),
        component: 'Select',
        required: true,
        componentProps: {
          // disabled: true,
          maxlength: 10,
          options: packageInfo.value,
          allowClear: true,
          showSearch: true,
          optionFilterProp: 'label',
        },
      },
      {
        field: 'verifyTime',
        label: t('请验时间'),
        component: 'DatePicker',
        required: true,
        componentProps: {
          // disabled: true,
          showTime: true,
          disabledDate,
        },
      },
      {
        field: 'remark',
        label: t('备注'),
        component: 'InputTextArea',
        required: false,
        colProps: {
          style: {
            marginRight: 'auto',
          },
        },
        componentProps: {
          // disabled: true,
          maxlength: 100,
        },
      },
      {
        field: 'print',
        label: t('确认后打印'),
        component: 'Checkbox',
      },
    ],
  });

  const setNodeFormData = async (formData: Recordable) => {
    try {
      await nextTick();
      Object.keys(formData).forEach(key => {
        if (key === 'label') {
          setFormRef.value?.formRef.setFormModel('name', formData[key]);
        }
        setFormRef.value?.formRef.setFormModel(key, formData[key]);
      });
    } catch (error) {}
  };

  return {
    setFormRef,
    setFormProps,
    setNodeFormData,
  };
};
