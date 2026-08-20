import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('浆站出库批号'),
      dataIndex: 'syncBatchNo',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('标本箱号起'),
      dataIndex: 'boxIdUp',
      width: 170,
      resizable: true,
    },
    {
      title: t('标本箱号止'),
      dataIndex: 'boxIdDown',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('标本编号起'),
      dataIndex: 'sampleNoUp',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('运输温度'),
      dataIndex: 'temperature',
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('运输时间'),
      dataIndex: 'transitTime',
      width: 140,
      resizable: true,
    },
    {
      title: t('出库日期'),
      dataIndex: 'beginTime',
      width: 140,
      resizable: true,
    },
  ];

  return {
    tableRef,
    columns,
  };
};
