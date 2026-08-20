import type { TableColumn } from '@bmos/components';

export const descriptionItems = reactive([
  {
    label: t('出库批号'),
    field: 'batchNo',
  },
  {
    label: t('出库仓库'),
    field: 'warehouseId',
    vIf: getWarehouseConfigByCode.value,
    renderFn: (row: any) => {
      return row?.warehouse?.name;
    },
  },
  {
    label: t('出库类别'),
    field: 'type',
    renderFn: (row: any) => {
      return row?.type?.name;
    },
  },
  {
    label: t('数量'),
    field: 'num',
  },
  {
    label: t('总重量'),
    field: 'weight',
  },
  {
    label: t('出库日期'),
    field: 'outPlanDate',
  },
]);

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
