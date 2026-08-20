import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('重量'),
      dataIndex: 'weight',
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('血浆外观'),
      dataIndex: 'applyAppearance',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.applyAppearance?.name}</span>;
      },
    },
    {
      title: t('供浆类型'),
      dataIndex: 'plasmaType',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.plasmaType?.name}</span>;
      },
    },
    {
      title: t('免疫类别'),
      dataIndex: 'immunityType',
      width: 100,
      resizable: true,
    },
    {
      title: t('效价'),
      dataIndex: 'titer',
      width: 80,
      sorter: true,
      resizable: true,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 150,
      sorter: true,
      resizable: true,
    },
    {
      title: t('血浆箱号'),
      dataIndex: 'containerNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('血型'),
      dataIndex: 'bloodType',
      width: 80,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.bloodType?.name}</span>;
      },
    },
  ];

  return {
    tableRef,
    columns,
  };
};
