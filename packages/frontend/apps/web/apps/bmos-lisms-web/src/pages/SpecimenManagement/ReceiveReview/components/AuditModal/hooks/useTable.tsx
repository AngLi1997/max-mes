import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const { getDateFormat } = useConfig();
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('标本批号'),
      dataIndex: 'batchNo',
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
      dataIndex: 'receiveCount',
      width: 100,
      resizable: true,
    },
    {
      title: t('送检人'),
      dataIndex: 'transferBy',
      width: 100,
      resizable: true,
    },
    {
      title: t('送检日期'),
      dataIndex: 'transferDate',
      width: 170,
      resizable: true,
      customRender: ({ record }) => getDateFormat(record.transferDate),
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrgName',
      width: 100,
      resizable: true,
    },
  ];

  return {
    tableRef,
    columns,
  };
};
