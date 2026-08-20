import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('血浆箱/托盘号'),
      dataIndex: 'containerNo',
      hideInSearch: true,
      width: 160,
      resizable: true,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      hideInSearch: true,
      width: 130,
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
      width: 80,
      resizable: true,
      customRender: ({ record }) => {
        return record?.plasmaDonorSex?.name ?? '-';
      }
    },
    {
      title: t('血型'),
      dataIndex: 'plasmaDonorBloodType',
      width: 80,
      resizable: true,
      customRender: ({ record }) => {
        return record?.plasmaDonorBloodType?.name ?? '-';
      }
    },
  ];

  return {
    tableRef,
    columns,
  };
};
