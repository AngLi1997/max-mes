import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      width: 180,
      resizable: true,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('姓名'),
      dataIndex: 'name',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.plasmaDonorInfoVO?.name;
      },
    },
    {
      title: t('血型'),
      dataIndex: 'bloodType',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.plasmaDonorInfoVO?.bloodType?.name;
      },
    },
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 150,
      resizable: true,
    },
    {
      title: t('不合格项目'),
      dataIndex: 'unqualifiedItem',
      width: 170,
      resizable: true,
    },
  ];

  return {
    tableRef,
    columns,
  };
};
