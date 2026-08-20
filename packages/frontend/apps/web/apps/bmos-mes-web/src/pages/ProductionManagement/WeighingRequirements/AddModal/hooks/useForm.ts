import {
  reqAllProductFormulaProcessEnableList,
  reqProductMaterialProductTreeReq,
  reqWeighingCenterTree,
} from '@/services';
import { FormProps, RenderCallbackParams } from '@bmos/components';
import { SelectValue } from 'ant-design-vue/es/select';

export const useForm = () => {
  const myFormRef = ref();

  const setFormModels = (values: any) => {
    myFormRef.value?.formRef?.setFormModels(values);
  };

  const updateSchema = (obj: any) => {
    myFormRef.value?.formRef?.updateSchema(obj);
  };

  const productTree = ref<any[]>([]);
  const fetchProductOptionTree = async () => {
    try {
      const { data } = await reqProductMaterialProductTreeReq();
      // return data;
      // 循环树形结构数据 data, 根据 categoryFlag true 添加属性 selectable false
      const loop = (data: any[]) => {
        return data.map(item => {
          if (item.categoryFlag) {
            item.selectable = false;
          } else {
            item.selectable = true;
          }
          if (item.children) {
            loop(item.children);
          }
          return item;
        });
      };
      productTree.value = data;
      return loop(data);
    } catch (error) {
      //
    }
  };

  const getFormulaEnableList = async (productId: string) => {
    try {
      const { data } = await reqAllProductFormulaProcessEnableList(productId);
      updateSchema({
        field: 'bomVersionId',
        componentProps: {
          options: data.map((item: any) => {
            return {
              ...item,
              label: item.productFormulaName + '-' + item.productFormulaVersionNo,
              value: item.productFormulaVersionId,
            };
          }),
        },
      });
    } catch (error) {
      return [];
    }
  };
  // 表单属性
  const formProps = reactive<FormProps>({
    initialValues: {},
    transformDateFunc: (date: any) => {
      return date?.format?.('YYYY-MM-DD') ?? date;
    },
    labelWidth: 120,
    baseColProps: {
      span: 18,
    },
    layout: 'horizontal',
    autoAdvancedLine: 10,
    alwaysShowLines: 6,
    showActionButtonGroup: false,
    schemas: [
      {
        field: 'productId',
        component: 'TreeSelect',
        required: true,
        label: t('产品信息'),
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            fieldNames: {
              label: 'showName',
              value: 'id',
            },
            request: async () => {
              return await fetchProductOptionTree();
            },
            onChange: (val: SelectValue) => {
              formModel.bomVersionId = undefined;
              if (val) {
                getFormulaEnableList(val as string);
              }
            },
          };
        },
      },
      {
        field: 'bomVersionId',
        component: 'Select',
        required: true,
        label: t('生产BOM'),
        componentProps: () => ({
          showSearch: true,
          filterOption: (input: string, option: any) => {
            return option.label?.toLowerCase().indexOf(input.toLowerCase()) >= 0;
          },
          options: [],
          // onChange: async () => {
          //   if (!formModel.bomVersionId) return;
          //   try {
          //     const { data } = await reqWeighingRequirementsQueryInfo({
          //       bomVersionId: formModel.bomVersionId,
          //     });
          //   } catch (error: any) {
          //     error.message && message.error(error.message);
          //   }
          // },
        }),
      },
      {
        field: 'batchNo',
        component: 'Input',
        required: true,
        label: t('生产批号'),
      },
      {
        field: 'centreWeighId',
        component: 'TreeSelect',
        required: true,
        label: t('称量中心'),
        componentProps: {
          fieldNames: { label: 'name', value: 'id' },
          request: async () => {
            try {
              const { data } = await reqWeighingCenterTree();
              return loopSelectableNotValueTree(data, 'isCategory', false);
            } catch (error) {
              return [];
            }
          },
        },
      },
      {
        field: 'planDate',
        component: 'DatePicker',
        required: true,
        label: t('计划生产时间'),
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        field: 'remark',
        component: 'InputTextArea',
        label: t('备注'),
      },
    ],
  });

  return {
    myFormRef,
    productTree,
    formProps,
    setFormModels,
    updateSchema,
  };
};
