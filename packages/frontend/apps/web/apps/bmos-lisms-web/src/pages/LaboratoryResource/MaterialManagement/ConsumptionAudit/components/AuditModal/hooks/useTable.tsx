import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const { getDateFormat } = useConfig();
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('物料编号'),
      dataIndex: 'materialNo',
      width: 120,
    },
    {
      title: t('物料名称'),
      dataIndex: 'materialName',
      width: 140,
    },
    {
      title: t('物料批号'),
      dataIndex: 'batchNo',
      width: 120,
    },
    {
      title: t('消耗数量'),
      dataIndex: 'useCount',
      width: 100,
    },
    {
      title: t('消耗原因'),
      dataIndex: 'reasonName',
      width: 140,
    },
    {
      title: t('登记人'),
      dataIndex: 'registrant',
      width: 100,
    },
    {
      title: t('登记日期'),
      dataIndex: 'registrantTime',
      width: 170,
      customRender: ({ record }) => getDateFormat(record.registrantTime),
    },
  ];

  return {
    tableRef,
    columns,
  };
};
