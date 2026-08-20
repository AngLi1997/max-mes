import { useDict } from '@/stores/dictStore';
import { yesOrNoEnum } from '@/types';
import { FormProps, ModalFormInstance } from '@bmos/components';

const { getDict } = useDict();

export const useForm = () => {
  const modalFormRef = ref<ModalFormInstance>();

  const { yesOrNoDict } = getDicts();

  const formProps = reactive<FormProps>({
    initialValues: {
      requireAudit: yesOrNoEnum.NO,
    },
    labelWidth: 100,
    schemas: [
      {
        label: t('供应商名称'),
        field: 'supplierName',
        required: true,
        component: 'Input',
        componentProps: {
          maxlength: 50,
          showCount: true,
        },
      },
      {
        label: t('供应商编号'),
        field: 'supplierNo',
        required: true,
        component: 'Input',
        componentProps: {
          maxlength: 12,
          showCount: true,
        },
      },
      {
        label: t('供应商类型'),
        field: 'supplierTypeId',
        required: true,
        component: 'Select',
        componentProps: {
          request: async () => {
            return await getDict('供应商类型');
          },
        },
      },
      {
        label: t('简称(中)'),
        field: 'cnShortName',
        required: true,
        component: 'Input',
        componentProps: {
          maxlength: 30,
          showCount: true,
        },
      },
      {
        label: t('简称(英)'),
        field: 'enShortName',
        required: true,
        component: 'Input',
        componentProps: {
          maxlength: 30,
          showCount: true,
        },
        // dynamicRules: () => {
        //   return [
        //     {
        //       required: true,
        //       pattern: /^[A-Z]{1,5}$/,
        //       message: t('简称(英)只支持5位以内的大写字母'),
        //     },
        //   ];
        // },
      },
      {
        label: t('负责人'),
        field: 'contactPerson',
        component: 'Input',
        componentProps: {
          maxlength: 10,
          showCount: true,
        },
      },
      {
        label: t('联系方式'),
        field: 'contactPhone',
        component: 'Input',
        componentProps: {
          maxlength: 20,
          showCount: true,
        },
        // dynamicRules: () => {
        //   return [
        //     {
        //       pattern:
        //         /^0\d{2,3}[-]?[1-9]\d{6,7}|[48]00[-]?[1-9]\d{2}[-]?\d{4}|(010|02\d|0[3-9]\d{2})-?(\d{6,8})|(?:0|86|\+86)?1[3-9]\d{9}|^$/,
        //       message: t('请输入正确的联系方式'),
        //     },
        //   ];
        // },
      },
      {
        label: t('地址'),
        field: 'address',
        component: 'Input',
        componentProps: {
          maxlength: 30,
          showCount: true,
        },
      },
      {
        label: t('审计要求'),
        field: 'requireAudit',
        component: 'Select',
        componentProps: {
          options: yesOrNoDict,
          onChange: (val: yesOrNoEnum, _opt: any) => {
            if (val === yesOrNoEnum.YES) {
              updateSchema({
                field: 'expireDate',
                required: true,
              });
            } else {
              updateSchema({
                field: 'expireDate',
                required: false,
              });
            }
          },
        },
      },
      {
        label: t('有效期'),
        field: 'expireDate',
        component: 'DatePicker',
        required: false,
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        label: t('备注'),
        field: 'remark',
        component: 'InputTextArea',
        componentProps: {
          maxlength: 200,
          showCount: true,
        },
      },
    ],
  });

  const setFormModels = (values: any) => {
    modalFormRef.value?.formRef?.setFormModels(values);
  };

  const updateSchema = (obj: any) => {
    modalFormRef.value?.formRef?.updateSchema(obj);
  };

  return {
    modalFormRef,
    formProps,
    setFormModels,
    updateSchema,
  };
};
