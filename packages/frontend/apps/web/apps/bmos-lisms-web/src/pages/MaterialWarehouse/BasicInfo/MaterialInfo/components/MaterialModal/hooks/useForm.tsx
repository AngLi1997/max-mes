import { useDict } from '@/stores/dictStore';
import { MaterialTypeEnum } from '@/types';
import { FormProps, ModalFormInstance } from '@bmos/components';

export const useForm = () => {
  const { InspectionProjectDict, keyMaterialCategoryDict, materialTypeDict } = getDicts();
  const { getDict } = useDict();
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {},
    labelWidth: 120,
    schemas: [
      {
        label: t('物料名称'),
        field: 'materialName',
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
        label: t('物料类型'),
        field: 'materialType',
        required: true,
        component: 'Select',
        componentProps: {
          options: materialTypeDict,
          onChange: (val: string) => {
            if (val !== MaterialTypeEnum.CORE_MATERIAL) {
              setFormModels({
                keyMaterialCategory: null,
                keyMaterialTypeId: null,
                applyItem: null,
              });
            } else {
              setFormModels({
                applyItem: InspectionProjectDict.map((item: any) => item.value),
              });
            }
          },
        },
      },
      {
        label: t('关键物料品类'),
        field: 'keyMaterialCategory',
        required: true,
        vIf: ({ formModel }: any) => formModel.materialType === MaterialTypeEnum.CORE_MATERIAL,
        component: 'Select',
        componentProps: {
          options: keyMaterialCategoryDict,
        },
      },
      {
        label: t('关键物料类型'),
        field: 'keyMaterialTypeId',
        required: true,
        vIf: ({ formModel }: any) => formModel.materialType === MaterialTypeEnum.CORE_MATERIAL,
        component: 'Select',
        componentProps: {
          request: async () => {
            return await getDict('关键物料类型');
          },
        },
      },
      {
        label: t('适用检验项目'),
        field: 'applyItem',
        required: true,
        vIf: ({ formModel }: any) => formModel.materialType === MaterialTypeEnum.CORE_MATERIAL,
        component: 'Select',
        componentProps: {
          // request: async () => {
          //   return await getDict('关键物料类型');
          // },
          mode: 'multiple',
          options: InspectionProjectDict,
        },
      },
      {
        label: t('物料编号'),
        field: 'materialNo',
        required: true,
        component: 'Input',
        componentProps: {
          maxlength: 12,
          showCount: true,
        },
      },
      {
        label: t('供应商'),
        field: 'supplierIdentify',
        required: true,
        component: 'Select',
        componentProps: {
          request: async () => {
            return await getDict('供应商');
          },
        },
      },
      {
        label: t('物料单位'),
        field: 'unitId',
        required: true,
        component: 'Select',
        componentProps: {
          request: async () => {
            return await getDict('物料单位');
          },
        },
      },
      {
        label: t('最低库存量'),
        field: 'minInventory',
        required: true,
        component: 'InputNumber',
        componentProps: {
          min: 0,
          max: 999999,
          precision: 0,
          style: {
            width: '100%',
          },
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
