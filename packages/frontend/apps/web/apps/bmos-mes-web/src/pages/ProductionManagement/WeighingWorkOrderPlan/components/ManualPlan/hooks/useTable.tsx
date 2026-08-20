import { reqProductMaterialProductTreeReq } from '@/services';
import { type FormProps, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { ref } from 'vue';

export const useTable = () => {
  const tableRef = ref<any>();

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

      return loop(data);
    } catch (error) {
      //
    }
  };

  // 表格列定义
  const columns: TableColumn[] = [
    {
      title: t('物料名称'),
      dataIndex: 'materialName',
      width: 130,
    },
    {
      title: t('物料编码'),
      dataIndex: 'materialMergeCode',
      width: 150,
    },
    {
      title: t('物料规格'),
      dataIndex: 'materialSpecification',
      width: 100,
    },
    {
      title: t('称量中心'),
      dataIndex: 'weighCentreName',
      width: 150,
    },
    {
      title: t('物料批号'),
      dataIndex: 'storageMaterialBatchNo',
      width: 150,
    },
    {
      title: t('需求量'),
      dataIndex: 'formulaQuantity',
      width: 100,
      customRender: ({ record }: any) => {
        return `${record.formulaQuantity}${record.unit}`;
      },
    },
    {
      title: t('产品名称'),
      dataIndex: 'productName',
      width: 150,
    },
    {
      title: t('产品编码'),
      dataIndex: 'productMergeCode',
      width: 150,
    },
    {
      title: t('生产批号'),
      dataIndex: 'batchNo',
      width: 150,
    },
    {
      title: t('计划生产日期'),
      dataIndex: 'planDate',
      width: 150,
    },
    {
      title: t('需求用途'),
      dataIndex: 'requirementUsage',
      width: 150,
    },
    {
      title: t('备注'),
      dataIndex: 'remark',
      width: 120,
    },
  ];

  // 表单配置
  const formProps: Partial<FormProps> = {
    schemas: [
      {
        label: t('物料名称'),
        field: 'materialName',
        component: 'Input',
      },
      {
        label: t('物料编码'),
        field: 'materialMergeCode',
        component: 'Input',
      },
      {
        label: t('物料批号'),
        field: 'storageMaterialBatchNo',
        component: 'Input',
      },
      {
        label: t('称量中心'),
        field: 'weighCentreName',
        component: 'Input',
      },
      {
        label: t('计划生产日期'),
        field: 'planDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        field: 'productId',
        component: 'TreeSelect',
        label: t('产品信息'),
        componentProps: {
          fieldNames: {
            label: 'showName',
            value: 'id',
          },
          request: async () => {
            return await fetchProductOptionTree();
          },
        },
      },
      {
        label: t('生产批号'),
        field: 'batchNo',
        component: 'Input',
      },
    ],
    fieldMapToTime: [['planDate', ['planDateEnd', 'planDateStart'], 'YYYY-MM-DD']],
  };

  return {
    columns,
    tableRef,
    formProps,
  };
};
