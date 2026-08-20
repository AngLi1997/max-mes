import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const { getDateFormat } = useConfig();
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('标本批号'),
      dataIndex: 'sampleBatchNo',
      width: 160,
    },
    {
      title: t('请验数量'),
      dataIndex: 'transferCount',
      width: 100,
    },
    {
      title: t('接收数量'),
      dataIndex: 'receivedCount',
      width: 100,
    },
    {
      title: t('合格数量'),
      dataIndex: 'qualifiedCount',
      width: 100,
    },
    {
      title: t('不合格数量'),
      dataIndex: 'unqualifiedCount',
      width: 120,
    },
    {
      title: t('报告人'),
      dataIndex: 'auditBy',
      width: 100,
    },
    {
      title: t('报告日期'),
      dataIndex: 'auditDate',
      width: 170,
      customRender: ({ record }) => getDateFormat(record.auditDate),
    },
    {
      title: t('来源单位'),
      dataIndex: 'transferFrom',
      width: 120,
    },
  ];

  return {
    tableRef,
    columns,
  };
};
