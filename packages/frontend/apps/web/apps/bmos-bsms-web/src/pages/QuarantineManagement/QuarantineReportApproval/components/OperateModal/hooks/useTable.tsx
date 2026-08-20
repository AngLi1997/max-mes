import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('报告编号'),
      dataIndex: 'reportNo',
      hideInSearch: true,
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrg',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('检品批号'),
      dataIndex: 'inWarehouseBatchNo',
      hideInSearch: true,
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('核查批号'),
      dataIndex: 'checkNo',
      hideInSearch: true,
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('所在仓库'),
      dataIndex: 'warehouseId',
      hideInSearch: true,
      hideInTable: !getWarehouseConfigByCode.value,
      width: 120,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.warehouse?.name;
      },
    },
    {
      title: t('核查份数'),
      dataIndex: 'checkNumber',
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('报告日期'),
      dataIndex: 'reportTime',
      width: 170,
      sorter: true,
      resizable: true,
    },
  ];

  return {
    tableRef,
    columns,
  };
};
