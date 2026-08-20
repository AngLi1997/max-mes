import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('报告单编号'),
      dataIndex: 'reportBillNo',
      hideInSearch: true,
      width: 160,
      resizable: true,
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrg',
      width: 200,
      resizable: true,
    },
    {
      title: t('姓名'),
      dataIndex: 'plasmaDonorName',
      hideInSearch: true,
      width: 80,
      resizable: true,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      hideInSearch: true,
      width: 160,
      resizable: true,
    },
    {
      title: t('不合格项目'),
      dataIndex: 'unqualifiedItems',
      hideInSearch: true,
      width: 120,
      resizable: true,
    },
  ];

  return {
    tableRef,
    columns,
  };
};
