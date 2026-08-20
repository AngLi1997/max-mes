import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('标本批号'),
      dataIndex: 'batchNo',
      width: 160,
      resizable: true,
    },
    {
      title: t('请验信息'),
      dataIndex: 'checkInfo',
      width: 300,
      resizable: true,
      children: [
        {
          title: t('请验数量'),
          dataIndex: 'transferCount',
          width: 100,
        },
        {
          title: t('血浆标本'),
          dataIndex: 'transferPlasmaSpecimen',
          width: 100,
        },
        {
          title: t('血清标本'),
          dataIndex: 'transferSerumSpecimen',
          width: 100,
        },
      ],
    },
    {
      title: t('接收信息'),
      dataIndex: 'receiveInfo',
      width: 300,
      resizable: true,
      children: [
        {
          title: t('接收数量'),
          dataIndex: 'receiveCount',
          width: 100,
        },
        {
          title: t('血浆标本'),
          dataIndex: 'receivePlasmaSpecimen',
          width: 100,
        },
        {
          title: t('血清标本'),
          dataIndex: 'receiveSerumSpecimen',
          width: 100,
        },
      ],
    },
    {
      title: t('拒收信息'),
      dataIndex: 'refuseInfo',
      width: 300,
      resizable: true,
      children: [
        {
          title: t('拒收数量'),
          dataIndex: 'rejectCount',
          width: 100,
        },
        {
          title: t('血浆标本'),
          dataIndex: 'rejectPlasmaSpecimen',
          width: 100,
        },
        {
          title: t('血清标本'),
          dataIndex: 'rejectSerumSpecimen',
          width: 100,
        },
      ],
    },
  ];

  return {
    tableRef,
    columns,
  };
};
