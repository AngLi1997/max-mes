import {
  getProductionListApi,
  getProductTreeApi,
} from '@/api/productionApi.js';
import { getProductionBatchListApi } from '@/api/storage.js';
import { t } from '@/utils/useBmosI18n.js';
import { reactive, ref } from 'vue';

export const useData = () => {
  const getChildrenData = (arr) => {
    const newArr = [];
    arr.map((item) => {
      item.categoryFlag = !item.categoryFlag;
      if (item.children.length > 0) {
        item.children = getChildrenData(item.children);
      }
      newArr.push(item);
    });
    return newArr;
  };
  const details = ref([
    {
      title: t('物料名称'),
      dataIndex: 'materialName',
    },
    {
      title: t('物料编码'),
      dataIndex: 'mergeCode',
    },
    {
      title: t('物料批次'),
      dataIndex: 'materialBatchNo',
    },
    {
      title: t('物料件号'),
      dataIndex: 'materialNo',
    },
    {
      title: t('可用量'),
      dataIndex: 'availableQuantity',
      color: '#ff9933',
    },
    {
      title: t('单位'),
      dataIndex: 'unit',
    },
  ]);
  const formsRef = ref();
  // 表单配置
  const formProps = reactive({
    schemas: [
      {
        field: 'productId',
        component: 'BMFormSelect',
        label: t('产品信息'),
        componentProps: ({ formModel, formInstance }) => {
          return {
            request: async () => {
              const { data } = await getProductTreeApi({ categoryType: 2 });
              return getChildrenData(data);
            },
            title: t('产品信息'),
            type: 'tree',
            required: true,
            fieldNames: {
              name: 'showName',
              key: 'id',
              checkKey: 'categoryFlag',
              checkKeyValue: true,
              parentId: 'parentId',
              children: 'children',
            },
            onConfirm: async (val) => {
              const res = await getProductionListApi({
                productId: val.id,
                active: true,
              });
              const options = res.data || [];
              formInstance.updateSchema({
                field: 'processId',
                componentProps: {
                  options,
                },
              });

              // 重置生产工艺和批次号
              formModel.processId = '';
              formModel.batchId = '';
            },
            onClear: () => {
              formInstance.updateSchema({
                field: 'processId',
                componentProps: {
                  options: [],
                },
              });
              formInstance.updateSchema({
                field: 'batchId',
                componentProps: {
                  options: [],
                },
              });
              // 重置生产工艺和批次号
              formModel.processId = '';
              formModel.batchId = '';
            },
          };
        },
        required: true,
      },
      {
        field: 'processId',
        component: 'BMFormSelect',
        label: t('生产工艺'),
        colProps: {
          span: 12,
        },
        componentProps: ({ formModel, formInstance }) => {
          return {
            fieldNames: {
              label: 'name',
              value: 'id',
            },
            options: [],
            title: t('生产工艺'),
            required: true,
            onConfirm: async (val) => {
              const res = await getProductionBatchListApi({
                processId: val.id,
                productId: formModel.productId,
              });
              const options = res.data || [];
              formInstance.updateSchema({
                field: 'batchId',
                componentProps: {
                  options,
                },
              });

              // 重置批次号
              formModel.batchId = '';
            },
            onClear: () => {
              formInstance.updateSchema({
                field: 'batchId',
                componentProps: {
                  options: [],
                },
              });
              // 重置批次号
              formModel.batchId = '';
            },
          };
        },
        required: true,
      },
      {
        field: 'batchId',
        component: 'BMFormSelect',
        label: t('生产批次'),
        colProps: {
          span: 12,
        },
        componentProps: ({ formModel, formInstance }) => {
          return {
            fieldNames: {
              label: 'batchNo',
              value: 'id',
            },
            options: [],
            title: t('生产批次'),
            required: true,
          };
        },
        required: true,
      },
      {
        field: 'remark',
        component: 'Input',
        label: t('备注'),
        required: true,
        colProps: {
          span: 12,
        },
      },
    ],
  });

  return {
    details,
    formsRef,
    formProps,
  };
};
