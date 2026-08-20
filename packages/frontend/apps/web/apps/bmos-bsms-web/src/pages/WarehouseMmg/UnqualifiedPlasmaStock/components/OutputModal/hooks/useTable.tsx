import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('批号'),
      dataIndex: 'batchNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('托盘号'),
      dataIndex: 'bigContainerNo',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('箱数'),
      dataIndex: 'containerNum',
      width: 100,
      resizable: true,
    },
    {
      title: t('总数'),
      dataIndex: 'amount',
      width: 100,
      resizable: true,
    },
  ];

  return {
    tableRef,
    columns,
  };
};
