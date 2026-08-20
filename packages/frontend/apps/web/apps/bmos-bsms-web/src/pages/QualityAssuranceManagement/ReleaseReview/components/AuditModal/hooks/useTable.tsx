import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('报告编号'),
      dataIndex: 'reportNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('检品批号'),
      dataIndex: 'inWarehouseBatchNo',
      width: 140,
      resizable: true,
    },
    {
      title: t('核查批号'),
      dataIndex: 'checkNo',
      width: 160,
      resizable: true,
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrg',
      width: 240,
      resizable: true,
    },
  ];

  return {
    tableRef,
    columns,
    // setRef,
    // fetchData,
  };
};
