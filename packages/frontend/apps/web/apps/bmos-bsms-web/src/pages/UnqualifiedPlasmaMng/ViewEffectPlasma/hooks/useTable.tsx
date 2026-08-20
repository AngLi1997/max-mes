import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('来源单位'),
      dataIndex: 'originOrg',
      width: 220,
      resizable: true,
    },
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      width: 190,
      resizable: true,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 140,
      resizable: true,
    },
    {
      title: t('重量'),
      dataIndex: 'weight',
      width: 100,
      resizable: true,
    },
    {
      title: t('入库批号'),
      dataIndex: 'inWarehouseBatchNo',
      width: 160,
      resizable: true,
    },
    {
      title: t('所在仓库'),
      dataIndex: 'warehouse',
      hideInTable: !getWarehouseConfigByCode.value,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.warehouse?.name ?? '-';
      },
    },
    {
      title: t('血浆箱/托盘号'),
      dataIndex: 'containerNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('大托盘号'),
      dataIndex: 'bigContainerNo',
      width: 140,
      resizable: true,
    },
    {
      title: t('姓名'),
      dataIndex: 'name',
      width: 100,
      resizable: true,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('血型'),
      dataIndex: 'bloodType',
      width: 80,
      resizable: true,
      customRender: ({ record }) => {
        return record?.bloodType?.name ?? '-';
      },
    },
    {
      title: t('不合格项目'),
      dataIndex: 'unqualifiedItems',
      width: 140,
      resizable: true,
    },
  ];

  return {
    tableRef,
    columns,
  };
};
