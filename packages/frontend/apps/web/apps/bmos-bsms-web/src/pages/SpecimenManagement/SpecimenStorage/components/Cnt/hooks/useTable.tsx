import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('标本类型'),
      dataIndex: 'sampleType',
      width: 160,
      resizable: true,
      customRender: ({ record }) => {
        return record?.sampleType?.name;
      },
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('标本箱号'),
      dataIndex: 'boxId',
      width: 160,
      resizable: true,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      width: 150,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.plasmaDonorInfo?.no;
      },
    },
    {
      title: t('姓名'),
      dataIndex: 'name',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.plasmaDonorInfo?.name;
      },
    },
    {
      title: t('性别'),
      dataIndex: 'sex',
      width: 80,
      resizable: true,
      customRender: ({ record }) => {
        return record?.plasmaDonorInfo?.sex?.name;
      },
    },
    {
      title: t('血型'),
      dataIndex: 'bloodType',
      width: 80,
      resizable: true,
      customRender: ({ record }) => {
        return record?.plasmaDonorInfo?.bloodType?.name;
      },
    },
  ];

  return {
    tableRef,
    columns,
  };
};
