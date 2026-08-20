import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('来源单位'),
      dataIndex: 'originOrgCode',
      width: 220,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.originOrgInfo?.originOrg}</span>;
      },
    },
    {
      title: t('出库批号'),
      dataIndex: 'syncBatchNo',
      width: 150,
      resizable: true,
    },
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      width: 190,
      resizable: true,
    },
    {
      title: t('箱/托盘号'),
      dataIndex: 'containerNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('重量'),
      dataIndex: 'weight',
      width: 80,
      sorter: true,
      resizable: true,
    },
    {
      title: t('血浆类型'),
      dataIndex: 'plasmaType',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.plasmaType?.name}</span>;
      },
    },
    {
      title: t('免疫类型'),
      dataIndex: 'immunityType',
      width: 100,
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
      title: t('限制级血浆'),
      dataIndex: 'restrictedFlag',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.restrictedFlag?.name}</span>;
      },
    },
    {
      title: t('献浆者姓名'),
      dataIndex: 'plasmaDonorName',
      width: 140,
      resizable: true,
    },
  ];

  const failColumns: TableColumn[] = [
    {
      title: t('来源单位'),
      dataIndex: 'originOrgCode',
      width: 220,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.originOrgInfo?.originOrg}</span>;
      },
    },
    {
      title: t('出库批号'),
      dataIndex: 'pbSyncBatchNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('箱/托盘号'),
      dataIndex: 'containerNo',
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
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      width: 160,
      sorter: true,
      resizable: true,
    },
    {
      title: t('错误提示'),
      dataIndex: 'message',
      width: 200,
      resizable: true,
      // ellipsis: false,
      fixed: 'right',
      customRender: ({ record }) => {
        return <span style={{ color: 'red' }}>{record?.message}</span>;
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
