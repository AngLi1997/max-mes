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
      title: t('检品批号'),
      dataIndex: 'inWarehouseBatchNo',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('核查批号'),
      dataIndex: 'checkNo',
      hideInSearch: true,
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrg',
      hideInSearch: true,
      width: 160,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.originOrgInfo?.originOrg;
      },
    },
  ];

  return {
    tableRef,
    columns,
  };
};
