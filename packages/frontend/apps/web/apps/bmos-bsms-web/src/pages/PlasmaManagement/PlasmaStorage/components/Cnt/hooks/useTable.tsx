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
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      hideInSearch: true,
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('血浆箱号'),
      dataIndex: 'containerNo',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      width: 150,
      sorter: true,
      resizable: true,
    },
    {
      title: t('姓名'),
      dataIndex: 'plasmaDonorName',
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('性别'),
      dataIndex: 'plasmaDonorSex',
      hideInSearch: true,
      width: 80,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.plasmaDonorSex?.name}</span>;
      },
    },
    {
      title: t('血型'),
      dataIndex: 'plasmaDonorBloodType',
      hideInSearch: true,
      width: 80,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.plasmaDonorBloodType?.name}</span>;
      },
    },
  ];

  return {
    tableRef,
    columns,
  };
};
