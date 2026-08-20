import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('标本批号'),
      dataIndex: 'sampleBatchNo',
      width: 160,
      resizable: true,
    },
    {
      title: t('请验数量'),
      dataIndex: 'transferCount',
      width: 100,
      resizable: true,
    },
    {
      title: t('接收数量'),
      dataIndex: 'receivedCount',
      width: 100,
      resizable: true,
    },
    {
      title: t('合格数量'),
      dataIndex: 'qualifiedCount',
      width: 100,
      resizable: true,
    },
    {
      title: t('不合格数量'),
      dataIndex: 'unqualifiedCount',
      width: 120,
      resizable: true,
    },
    {
      title: t('已发布次数'),
      dataIndex: 'publishCount',
      width: 100,
      resizable: true,
    },
    {
      title: t('来源单位'),
      dataIndex: 'transferFrom',
      width: 120,
      resizable: true,
    },
  ];

  return {
    tableRef,
    columns,
  };
};
