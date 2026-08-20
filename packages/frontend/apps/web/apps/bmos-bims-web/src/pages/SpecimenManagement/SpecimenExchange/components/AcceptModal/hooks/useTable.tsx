import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('检品批号'),
      dataIndex: 'inspectionBatchNo',
      width: 150,
      resizable: true,
    },
    {
      title: t('检品数量'),
      dataIndex: 'inspectionNumber',
      width: 100,
      sorter: (a, b) => a.inspectionNumber - b.inspectionNumber,
      resizable: true,
    },
    {
      title: t('请验人'),
      dataIndex: 'inspectionBy',
      width: 100,
      resizable: true,
    },
    {
      title: t('请验日期'),
      dataIndex: 'inspectionDate',
      width: 150,
      sorter: (a, b) => new Date(a.inspectionDate).getTime() - new Date(b.inspectionDate).getTime(),
      resizable: true,
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrg',
      width: 220,
      resizable: true,
    },
  ];

  return {
    tableRef,
    columns,
  };
};
