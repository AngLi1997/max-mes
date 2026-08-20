import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const { getDateFormat } = useConfig();
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('入库单号'),
      dataIndex: 'inWarehouseNo',
      width: 180,
    },
    {
      title: t('入库日期'),
      dataIndex: 'inWarehouseTime',
      width: 170,
      customRender: ({ record }) => getDateFormat(record.inWarehouseTime),
    },
    {
      title: t('物料编号'),
      dataIndex: 'materialNo',
      width: 120,
    },
    {
      title: t('物料名称'),
      dataIndex: 'materialName',
      width: 120,
    },
    {
      title: t('供应商'),
      dataIndex: 'supplierName',
      width: 120,
    },
    {
      title: t('物料类型'),
      dataIndex: 'materialType',
      width: 100,
      customRender: ({ record }) => record?.materialType?.label ?? '-',
    },
    {
      title: t('入库数量'),
      dataIndex: 'quantity',
      width: 100,
    },
    {
      title: t('抽检数量'),
      dataIndex: 'useCount',
      width: 100,
    },
  ];

  return {
    tableRef,
    columns,
  };
};
