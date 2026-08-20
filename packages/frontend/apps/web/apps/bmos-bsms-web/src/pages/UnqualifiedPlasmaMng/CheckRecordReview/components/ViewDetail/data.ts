import { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const columns: TableColumn[] = [
  {
    title: t('操作人'),
    dataIndex: 'createBy',
  },
  {
    title: t('操作日期'),
    dataIndex: 'createTime',
  },
  {
    title: t('操作事项'),
    dataIndex: 'content',
  },
  {
    title: t('操作备注'),
    dataIndex: 'remark',
  },
];

export const columns2: TableColumn[] = [
  {
    title: t('来源单位'),
    dataIndex: 'originOrg',
    width: 220,
  },
  {
    title: t('血浆编号'),
    dataIndex: 'plasmaNo',
    width: 170,
  },
  {
    title: t('采浆日期'),
    dataIndex: 'slurryDate',
    width: 150,
  },
  {
    title: t('重量'),
    dataIndex: 'weight',
    width: 100,
  },
  {
    title: t('入库批号'),
    dataIndex: 'inWarehouseBatchNo',
    width: 140,
  },
  {
    title: t('所在仓库'),
    dataIndex: 'warehouse',
    hideInTable: !getWarehouseConfigByCode.value,
    width: 100,
    customRender: ({ record }) => {
      return record?.warehouse?.name ?? '-';
    },
  },
  {
    title: t('血浆箱/托盘号'),
    dataIndex: 'containerNo',
    width: 140,
  },
  {
    title: t('大托盘号'),
    dataIndex: 'bigContainerNo',
    width: 120,
  },
];
