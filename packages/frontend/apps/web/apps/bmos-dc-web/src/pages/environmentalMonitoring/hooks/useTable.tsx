import { TableColumn } from '@bmos/components';

export const useTable = ({ cleanLevelDict }: { cleanLevelDict: Ref<any[]> }) => {
  const tableRef = ref<any>();
  const tableData = ref<any[]>([]);

  const baseColumns: TableColumn[] = [
    {
      title: t('产线'),
      dataIndex: 'moduleName',
    },
    {
      title: t('楼栋'),
      dataIndex: 'tenementName',
    },
    {
      title: t('楼层'),
      dataIndex: 'floorName',
    },
    {
      title: t('房间'),
      dataIndex: 'name',
    },
    {
      title: t('洁净等级'),
      dataIndex: 'cleanLevel',
      customRender: ({ record }) => {
        const cleanLevel = cleanLevelDict.value.find(item => item.value === record.cleanLevel);
        return cleanLevel ? cleanLevel.label : '-';
      },
    },
  ];

  const columns = ref<TableColumn[]>([...baseColumns]);
  return {
    tableRef,
    columns,
    tableData,
    baseColumns,
  };
};
