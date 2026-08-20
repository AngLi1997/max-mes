import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('血浆编号'),
      dataIndex: 'no',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('姓名'),
      dataIndex: 'plasmaDonorName',
      width: 100,
      resizable: true,
    },
    {
      title: t('编号'),
      dataIndex: 'plasmaDonorNo',
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('血型'),
      dataIndex: 'bloodType',
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.bloodType?.name ?? '-';
      },
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      hideInSearch: true,
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('箱号'),
      dataIndex: 'primeContainerNo',
      width: 160,
      resizable: true,
    },
  ];

  const failColumns: TableColumn[] = [
    {
      title: t('来源单位'),
      dataIndex: 'originOrgName',
      hideInSearch: true,
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('出库批号'),
      dataIndex: 'batchNo',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      hideInSearch: true,
      width: 100,
      sorter: true,
      resizable: true,
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
      sorter: true,
      resizable: true,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      width: 160,
      sorter: true,
      resizable: true,
    },
    {
      title: t('错误提示'),
      dataIndex: 'errorMsg',
      hideInSearch: true,
      width: 160,
      fixed: 'right',
      customRender: ({ record }: any) => {
        return <span style={{ color: '#ff5e3d' }}>{record?.errorMsg}</span>;
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
