import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('来源单位'),
      dataIndex: 'originOrg',
      hideInSearch: true,
      width: 220,
      resizable: true,
    },
    {
      title: t('出库批号'),
      dataIndex: 'syncBatchNo',
      width: 120,
      resizable: true,
    },
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('标本类型'),
      dataIndex: 'sampleType',
      hideInSearch: true,
      width: 140,
      resizable: true,
      customRender: ({ record }) => {
        return record?.sampleType?.name;
      },
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      hideInSearch: true,
      width: 130,
      sorter: true,
      resizable: true,
    },
    {
      title: t('标本箱号'),
      dataIndex: 'boxId',
      width: 170,
      resizable: true,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('姓名'),
      dataIndex: 'name',
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
  ];

  const failColumns: TableColumn[] = [
    {
      title: t('来源单位'),
      dataIndex: 'originOrgInfo',
      hideInSearch: true,
      width: 190,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.originOrgInfo?.originOrg}</span>;
      },
    },
    {
      title: t('出库批号'),
      dataIndex: 'syncBatchNo',
      width: 120,
      resizable: true,
    },
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      hideInSearch: true,
      width: 160,
      resizable: true,
    },
    {
      title: t('标本类型'),
      dataIndex: 'sampleType',
      hideInSearch: true,
      width: 140,
      resizable: true,
      customRender: ({ record }) => {
        return record?.sampleType?.name;
      },
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      hideInSearch: true,
      width: 130,
      sorter: true,
      resizable: true,
    },
    {
      title: t('标本箱号'),
      dataIndex: 'boxId',
      width: 170,
      resizable: true,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      width: 140,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.plasmaDonorInfo?.no}</span>;
      },
    },
    {
      title: t('错误提示'),
      dataIndex: 'errorMsg',
      hideInSearch: true,
      width: 200,
      resizable: true,
      ellipsis: false,
      customRender: ({ record }) => {
        return <span style={{ color: 'red' }}>{record?.errorMsg}</span>;
      },
    },
  ];
  return {
    tableRef,
    columns,
    failColumns,
    // setRef,
    // fetchData,
  };
};
