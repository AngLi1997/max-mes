import { type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Flex } from 'ant-design-vue';
import { ref } from 'vue';

export const useTable = () => {
  const tableRef = ref<any>();

  const tableData = ref<any[]>([]);

  // 表格列定义
  const columns: TableColumn[] = [
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
      title: t('需求量'),
      dataIndex: 'requirementQuantity',
      width: 120,
    },
    {
      title: t('需求用途'),
      dataIndex: 'requirementUsage',
      width: 150,
    },
    {
      title: t('状态'),
      dataIndex: 'status',
      width: 150,
      customRender: ({ record }: any) => {
        const colorMap = {
          1: '#FF9A2F',
          2: '#2871FF',
          3: '#59BF78',
        };
        // @ts-ignore
        const color = colorMap[record.status?.value] || '#000';
        return (
          <Flex align='center' gap={8}>
            <div style={{ width: '7px', height: '7px', borderRadius: '50%', backgroundColor: color }}></div>
            <span style={{ fontSize: '14px', color }}>{record.status?.label}</span>
          </Flex>
        );
      },
    },
  ];

  // 子表格列定义
  const subColumns: TableColumn[] = [
    {
      title: t('物料件号'),
      dataIndex: 'storageMaterialNo',
      width: 150,
    },
    {
      title: t('净重'),
      dataIndex: 'netWeight',
      width: 100,
    },
    {
      title: t('皮重'),
      dataIndex: 'tareWeight',
      width: 100,
    },
    {
      title: t('毛重'),
      dataIndex: 'grossWeight',
      width: 100,
    },
    {
      title: t('单位'),
      dataIndex: 'unit',
      width: 100,
    },
    {
      title: t('称量人'),
      dataIndex: 'weigherName',
    },
    {
      title: t('复核人'),
      dataIndex: 'recheckerName',
    },
    {
      title: t('称量时间'),
      dataIndex: 'weighTime',
    },
  ];

  return {
    columns,
    subColumns,
    tableRef,
    tableData,
  };
};
