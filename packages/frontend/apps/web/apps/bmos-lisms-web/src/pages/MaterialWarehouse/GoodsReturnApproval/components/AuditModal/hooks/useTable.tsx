import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const { getDateFormat } = useConfig();
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('入库单号'),
      dataIndex: 'inWarehouseNo',
      width: 130,
    },
    {
      title: t('物料编号'),
      dataIndex: 'materialNo',
      width: 120,
    },
    {
      title: t('物料名称'),
      dataIndex: 'materialName',
      width: 150,
    },
    {
      title: t('物料批号'),
      dataIndex: 'batchNo',
      width: 120,
    },
    {
      title: t('供应商'),
      dataIndex: 'supplierName',
      width: 120,
    },
    {
      title: t('退货数量'),
      dataIndex: 'useCount',
      width: 100,
    },
    {
      title: t('退货原因'),
      dataIndex: 'reasonName',
      width: 140,
      sorter: true,
    },
    {
      title: t('申请人'),
      dataIndex: 'applicant',
      width: 130,
    },
    {
      title: t('申请日期'),
      dataIndex: 'applicantTime',
      width: 170,
      customRender: ({ record }) => getDateFormat(record.applicantTime),
    },
  ];

  return {
    tableRef,
    columns,
  };
};
