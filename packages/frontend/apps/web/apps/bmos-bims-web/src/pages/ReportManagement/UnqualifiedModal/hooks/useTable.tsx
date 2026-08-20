import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      width: 190,
      resizable: true,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 170,
      sorter: true,
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
      width: 100,
      resizable: true,
    },
    {
      title: t('性别'),
      dataIndex: 'sex',
      width: 80,
      resizable: true,
      customRender: ({ record }) => {
        return record?.sex?.name;
      },
    },
    {
      title: t('血型'),
      dataIndex: 'bloodType',
      width: 80,
      resizable: true,
      customRender: ({ record }) => {
        return record?.bloodType?.name;
      },
    },
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      width: 190,
      resizable: true,
    },
    {
      title: t('免疫类型'),
      dataIndex: 'immunityType',
      width: 140,
      resizable: true,
      customRender: ({ record }) => {
        return record?.immunityType?.name ?? '-';
      },
    },
    {
      title: t('检测免疫类型'),
      dataIndex: 'titerType',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return record?.titerType?.name ?? '-';
      },
    },
    {
      title: t('不合格项目'),
      dataIndex: 'unQualifiedItems',
      width: 170,
      resizable: true,
    },
  ];

  return {
    tableRef,
    columns,
    // setRef,
    // fetchData,
  };
};
