import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('来源单位'),
      dataIndex: 'originOrgCode',
      width: 170,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.originOrgInfo?.originOrg}</span>;
      },
    },
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('血浆箱号'),
      dataIndex: 'containerNo',
      width: 140,
      resizable: true,
    },
    {
      title: t('血浆类型'),
      dataIndex: 'plasmaType',
      width: 140,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.plasmaType?.name}</span>;
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
