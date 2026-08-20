import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('来源单位'),
      dataIndex: 'originOrgInfo',
      width: 220,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.originOrgInfo?.originOrg}</span>;
      },
    },
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('标本箱号'),
      dataIndex: 'boxId',
      width: 160,
      resizable: true,
    },
    {
      title: t('标本类型'),
      dataIndex: 'sampleType',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.sampleType?.name}</span>;
      },
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 140,
      resizable: true,
    },
  ];

  return {
    tableRef,
    columns,
  };
};
