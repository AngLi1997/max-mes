import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const { getDateFormat } = useConfig();
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('物料名称'),
      dataIndex: 'materialName',
      width: 160,
      resizable: true,
    },
    {
      title: t('物料编号'),
      dataIndex: 'materialNo',
      width: 140,
      resizable: true,
    },
    {
      title: t('物料批号'),
      dataIndex: 'batchNo',
      width: 160,
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
      width: 100,
      resizable: true,
      customRender: ({ record }) => record?.warehouseArea?.label ?? '-',
    },
    {
      title: t('仓库地址'),
      dataIndex: 'warehouseAddressName',
      width: 160,
      resizable: true,
    },
    {
      title: t('接收人'),
      dataIndex: 'createBy',
      width: 90,
      resizable: true,
    },
    {
      title: t('接收日期'),
      dataIndex: 'createTime',
      width: 170,
      resizable: true,
      customRender: ({ record }) => getDateFormat(record?.createTime),
    },
  ];

  return {
    tableRef,
    columns,
  };
};
