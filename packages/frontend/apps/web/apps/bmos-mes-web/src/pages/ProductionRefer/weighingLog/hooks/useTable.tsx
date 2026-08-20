import type { TableColumn } from '@bmos/components';
export const useTable = () => {
  const columns: TableColumn[] = [
    {
      title: t('物料件号'),
      dataIndex: 'materialNo',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('净重'),
      dataIndex: 'netWeight',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('皮重'),
      dataIndex: 'tareWeight',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('毛重'),
      dataIndex: 'grossWeight',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('单位'),
      dataIndex: 'unitName',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('称量类型'),
      dataIndex: 'weighType',
      width: 140,
      hideInSearch: true,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record.weighType?.name}</span>;
      },
    },
    {
      title: t('称量人'),
      dataIndex: 'weigherName',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('复核人'),
      dataIndex: 'reCheckerName',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('称量时间'),
      dataIndex: 'weighTime',
      width: 170,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('物料名称'),
      dataIndex: 'materialName',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('物料编码'),
      dataIndex: 'materialMergeCode',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('物料批号'),
      dataIndex: 'materialBatchNo',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('设备名称'),
      dataIndex: 'equipmentName',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('设备编号'),
      dataIndex: 'equipmentCode',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('校准状态'),
      dataIndex: 'equipmentStatus',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('校准效期'),
      dataIndex: 'equipmentExpireDate',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('产品名称'),
      dataIndex: 'productName',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('产品编码'),
      dataIndex: 'productMergeCode',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('生产批号'),
      dataIndex: 'productBatchNo',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
  ];
  return {
    columns,
  };
};
