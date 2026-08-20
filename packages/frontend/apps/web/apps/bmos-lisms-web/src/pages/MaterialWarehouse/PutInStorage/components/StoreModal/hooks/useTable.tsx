import { useDict } from '@/stores/dictStore';
import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const { getDateFormat } = useConfig();
  const { findDictItem } = useDict();
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('物料名称'),
      dataIndex: 'materialName',
      width: 180,
      resizable: true,
    },
    {
      title: t('物料编号'),
      dataIndex: 'materialNo',
      width: 120,
      resizable: true,
    },
    {
      title: t('物料批号'),
      dataIndex: 'batchNo',
      width: 120,
      resizable: true,
    },
    {
      title: t('供应商'),
      dataIndex: 'supplierName',
      width: 140,
      resizable: true,
    },
    {
      title: t('入库数量'),
      dataIndex: 'quantity',
      width: 100,
      resizable: true,
    },
    {
      title: t('是否抽检'),
      dataIndex: 'needSpotCheck',
      width: 100,
      resizable: true,
      customRender: ({ record }) => record?.needSpotCheck?.label ?? '-',
    },
    {
      title: t('仓库区域'),
      dataIndex: 'warehouseArea',
      width: 120,
      resizable: true,
      customRender: ({ record }) => record?.warehouseArea?.label ?? '-',
    },
    {
      title: t('物料类型'),
      dataIndex: 'materialType',
      width: 120,
      resizable: true,
      customRender: ({ record }) => record?.materialType?.label ?? '-',
    },
    {
      title: t('关键物料品类'),
      dataIndex: 'keyMaterialCategory',
      width: 120,
      resizable: true,
      customRender: ({ record }) => record?.keyMaterialCategory?.label ?? '-',
    },
    {
      title: t('关键物料类型'),
      dataIndex: 'keyMaterialTypeName',
      width: 120,
      resizable: true,
    },
    {
      title: t('质控品含量'),
      dataIndex: 'qualityControlNumerical',
      width: 120,
      resizable: true,
      customRender: ({ record }) => findDictItem('质控品含量', record?.qualityControlNumerical)?.label ?? '-',
    },
    {
      title: t('物料单位'),
      dataIndex: 'unitName',
      width: 140,
      resizable: true,
    },
    {
      title: t('物料规格'),
      dataIndex: 'specificationName',
      width: 120,
      resizable: true,
    },
    {
      title: t('生产日期'),
      dataIndex: 'productionDate',
      width: 170,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => getDateFormat(record?.productionDate),
    },
    {
      title: t('有效日期'),
      dataIndex: 'expireDate',
      width: 170,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => getDateFormat(record?.expireDate),
    },
    {
      title: t('仓库地址'),
      dataIndex: 'warehouseAddressName',
      width: 180,
      resizable: true,
    },
  ];

  return {
    tableRef,
    columns,
  };
};
