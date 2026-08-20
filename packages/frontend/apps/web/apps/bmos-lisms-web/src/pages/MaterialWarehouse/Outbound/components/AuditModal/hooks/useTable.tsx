import { UseCategoryEnum } from '@/types';
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
      title: t('仓库地址'),
      dataIndex: 'warehouseAddressName',
      width: 100,
    },
    {
      title: t('仓库区域'),
      dataIndex: 'warehouseArea',
      width: 140,
      sorter: true,
      customRender: ({ record }) => record?.warehouseArea?.label ?? '-',
    },
    {
      title: t('使用类别'),
      dataIndex: 'useType',
      width: 100,
      customRender: ({ record }) => {
        const color = UseCategoryEnum[record?.useType?.value as keyof typeof UseCategoryEnum] ?? '#000000';
        return <span style={{ color }}>{record?.useType?.label ?? '-'}</span>;
      },
    },
    {
      title: t('申请人'),
      dataIndex: 'applicant',
      width: 100,
    },
    {
      title: t('申请日期'),
      dataIndex: 'applicantTime',
      width: 170,
      customRender: ({ record }) => getDateFormat(record.applicantTime),
    },
    {
      title: t('出库数量'),
      dataIndex: 'useCount',
      width: 100,
    },
  ];

  return {
    tableRef,
    columns,
  };
};
