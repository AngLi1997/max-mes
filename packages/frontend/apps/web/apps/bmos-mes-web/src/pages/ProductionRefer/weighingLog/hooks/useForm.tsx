import { getMaterialLogTreeApi } from '@/services';
import { FormProps, RenderCallbackParams } from '@bmos/components';
import { loopSelectableTree } from '@bmos/utils';
import { message } from 'ant-design-vue';
import dayjs from 'dayjs';
import { weighType } from '../enum';

export const useForm = () => {
  //物料类型初始值
  const materValue = ref<number>();
  //物料信息Select
  const materialInformSelect = ref([]);
  // 表单配置
  const formProps = reactive<Partial<FormProps>>({
    showAdvancedButton: true,
    initialValues: {},
    schemas: [
      {
        field: 'categoryType',
        component: 'Select',
        label: t('物料类型'),
        componentProps: ({ formInstance }: RenderCallbackParams) => {
          return {
            options: [
              { label: t('原辅包'), value: '0' },
              { label: t('中间品'), value: '1' },
            ],
            onChange: async (value: number) => {
              materValue.value = value;
              materialInformSelect.value = [];
              formInstance.setFieldsValue({
                materialId: undefined,
              });
              if (value) {
                await getMaterialInform();
              }
            },
          };
        },
      },
      {
        field: 'materialId',
        component: 'TreeSelect',
        label: t('物料信息'),
        componentProps: ({ formInstance }: RenderCallbackParams) => {
          return {
            treeData: materialInformSelect.value,
            fieldNames: {
              children: 'children',
              label: 'name',
              value: 'id',
            },
            virtual: false,
            height: 200,
            onFocus: (value: number) => {
              if (!formInstance.formModel.categoryType) {
                message.error(t('请选择物料类型'));
              }
            },
          };
        },
      },
      {
        field: 'materialBatchNo',
        component: 'Input',
        label: t('物料批号'),
      },
      {
        field: 'materialNo',
        component: 'Input',
        label: t('物料件号'),
      },
      {
        field: 'weighType',
        component: 'Select',
        label: t('称量类型'),
        componentProps: ({ formInstance }: RenderCallbackParams) => {
          return {
            options: weighType,
          };
        },
      },
      {
        field: 'equipmentInfo',
        component: 'Input',
        label: t('秤具信息'),
      },
      {
        field: 'productInfo',
        component: 'Input',
        label: t('产品信息'),
      },
      {
        field: 'productBatchNo',
        component: 'Input',
        label: t('生产批号'),
      },
      {
        field: 'operationTime',
        component: 'RangePicker',
        label: t('称量时间'),
        defaultValue: [dayjs().subtract(29, 'day').format('YYYY-MM-DD'), dayjs().format('YYYY-MM-DD')],
        componentProps: () => {
          return {
            format: 'YYYY-MM-DD',
            picker: 'date',
            valueFormat: 'YYYY-MM-DD',
          };
        },
      },
    ],
    fieldMapToTime: [['operationTime', ['startTime', 'endTime'], 'YYYY-MM-DD']],
  });
  //获取物料信息
  const getMaterialInform = async () => {
    try {
      const res = await getMaterialLogTreeApi({
        categoryType: materValue.value,
      });
      materialInformSelect.value = loopSelectableTree(res.data, 'categoryFlag', true);
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  return {
    formProps,
  };
};
