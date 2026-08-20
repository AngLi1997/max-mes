import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const { getDateFormat } = useConfig();
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
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
      title: t('领用数量'),
      dataIndex: 'useCount',
      width: 100,
    },
    {
      title: t('领用原因'),
      dataIndex: 'reasonName',
      width: 140,
      sorter: true,
    },
    {
      title: t('领用库'),
      dataIndex: 'targetWarehouseName',
      width: 130,
    },
    {
      title: t('领用人'),
      dataIndex: 'applicant',
      width: 100,
    },
    {
      title: t('领用日期'),
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
