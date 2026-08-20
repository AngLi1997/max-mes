import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 140,
      resizable: true,
    },
    {
      title: t('标本箱号'),
      dataIndex: 'boxId',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      hideInSearch: true,
      width: 170,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.plasmaDonorInfo?.no}</span>;
      },
    },
    {
      title: t('姓名'),
      dataIndex: 'plasmaDonorName',
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.plasmaDonorInfo?.name}</span>;
      },
    },
    {
      title: t('性别'),
      dataIndex: 'plasmaDonorSex',
      width: 80,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.plasmaDonorInfo?.sex?.name}</span>;
      },
    },
    {
      title: t('血型'),
      dataIndex: 'bloodType',
      width: 80,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.plasmaDonorInfo?.bloodType?.name}</span>;
      },
    },
  ];

  return {
    tableRef,
    columns,
  };
};
