import { RemarkDetail } from '@/components/RemarkModal';
import { useDict } from '@/stores/dictStore';
import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useDes = (openEditModal: Function) => {
  const { getDict } = useDict();
  const { getDateFormat } = useConfig();
  getDict('质控品含量');
  // 备注弹窗相关
  const remarkModalOpen = ref<boolean>(false);
  const remarkDetails = ref<RemarkDetail[]>([]);

  // 物料基础信息
  const materialBaseColumns: TableColumn[] = [
    {
      title: t('物料编号'),
      dataIndex: 'materialNo',
      width: 100,
      resizable: true,
    },
    {
      title: t('物料名称'),
      dataIndex: 'materialName',
      width: 130,
      resizable: true,
    },
    {
      title: t('简称(英)'),
      dataIndex: 'enShortName',
      width: 100,
      resizable: true,
    },
    {
      title: t('物料类型'),
      dataIndex: 'materialType',
      width: 100,
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
      title: t('供应商'),
      dataIndex: 'supplierName',
      width: 100,
      resizable: true,
    },
    {
      title: t('物料单位'),
      dataIndex: 'unitName',
      width: 100,
      resizable: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 80,
      actions: ({ record }) => [
        {
          label: t('备注'),
          onClick: () => {
            remarkDetails.value = [
              {
                field: 'remark',
                value: record.remark,
                label: t('物料备注'),
              },
            ];
            remarkModalOpen.value = true;
          },
        },
      ],
    },
  ];

  // 供应商基础信息
  const supplierBaseColumns: TableColumn[] = [
    {
      title: t('供应商名称'),
      dataIndex: 'supplierName',
      width: 120,
      resizable: true,
    },
    {
      title: t('简称(中)'),
      dataIndex: 'cnShortName',
      width: 100,
      resizable: true,
    },
    {
      title: t('简称(英)'),
      dataIndex: 'enShortName',
      width: 100,
      resizable: true,
    },
    {
      title: t('供应商编号'),
      dataIndex: 'supplierNo',
      width: 120,
      resizable: true,
    },
    {
      title: t('供应商类型'),
      dataIndex: 'supplierTypeName',
      width: 120,
      resizable: true,
    },
    {
      title: t('负责人'),
      dataIndex: 'contactPerson',
      width: 100,
      resizable: true,
    },
    {
      title: t('联系方式'),
      dataIndex: 'contactPhone',
      width: 140,
      resizable: true,
    },
    {
      title: t('地址'),
      dataIndex: 'address',
      width: 200,
      resizable: true,
    },
    {
      title: t('审计要求'),
      dataIndex: 'requireAudit',
      width: 100,
      resizable: true,
      customRender: ({ record }) => record?.requireAudit?.label ?? '-',
    },
    {
      title: t('有效期'),
      dataIndex: 'expireDate',
      width: 140,
      resizable: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 80,
      actions: ({ record }) => [
        {
          label: t('备注'),
          onClick: () => {
            // openRemark(record);
            remarkDetails.value = [
              {
                field: 'remark',
                value: record.remark,
                label: t('供应商备注'),
              },
            ];
            remarkModalOpen.value = true;
          },
        },
      ],
    },
  ];

  // 物料批信息
  const materialBatchColumns: TableColumn[] = [
    {
      title: t('物料批号'),
      dataIndex: 'batchNo',
      width: 120,
      resizable: true,
    },
    {
      title: t('物料规格'),
      dataIndex: 'specificationName',
      width: 100,
      resizable: true,
    },
    {
      title: t('质控品含量'),
      dataIndex: 'qualityControlNumerical',
      width: 140,
      resizable: true,
      customRender: ({ record }) => {
        console.log('record?.qualityControlNumerical', record?.qualityControlNumerical);
        return record?.qualityControlNumerical ? (
          <div>
            <span style={{ marginRight: '5px' }}>{record?.qualityControlNumerical}</span>
            <a onClick={() => openEditModal(record)}>{t('编辑')}</a>
          </div>
        ) : (
          '-'
        );
      },
    },
    {
      title: t('生产日期'),
      dataIndex: 'productionDate',
      width: 170,
      resizable: true,
      customRender: ({ record }) => getDateFormat(record.productionDate),
    },
    {
      title: t('有效日期'),
      dataIndex: 'expireDate',
      width: 170,
      resizable: true,
      customRender: ({ record }) => getDateFormat(record.expireDate),
    },
    {
      title: t('结存总量'),
      dataIndex: 'inventory',
      width: 100,
      resizable: true,
    },
    {
      title: t('可用库存量'),
      dataIndex: 'availableStock',
      width: 110,
      resizable: true,
    },
  ];

  // 入库记录
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('入库单号'),
      dataIndex: 'inWarehouseNo',
      width: 120,
      resizable: true,
    },
    {
      title: t('入库数量'),
      dataIndex: 'quantity',
      width: 100,
      resizable: true,
    },
    {
      title: t('结存数量'),
      dataIndex: 'inventory',
      width: 100,
      resizable: true,
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
      width: 140,
      resizable: true,
    },
    {
      title: t('接收结果'),
      dataIndex: 'receiveResult',
      width: 100,
      resizable: true,
      customRender: ({ record }) => record?.receiveResult?.label ?? '-',
    },
    {
      title: t('接收人'),
      dataIndex: 'createBy',
      width: 80,
      resizable: true,
    },
    {
      title: t('接收日期'),
      dataIndex: 'createTime',
      width: 170,
      resizable: true,
      customRender: ({ record }) => getDateFormat(record.createTime),
    },
    {
      title: t('入库人'),
      dataIndex: 'inWarehouseBy',
      width: 80,
      resizable: true,
    },
    {
      title: t('入库日期'),
      dataIndex: 'inWarehouseTime',
      width: 170,
      resizable: true,
      customRender: ({ record }) => getDateFormat(record.inWarehouseTime),
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 80,
      actions: ({ record }) => [
        {
          label: t('备注'),
          onClick: () => {
            remarkDetails.value = [
              {
                field: 'receiveRemark',
                value: record.receiveRemark,
                label: t('接收备注'),
              },
              {
                field: 'inWarehouseRemark',
                value: record.inWarehouseRemark,
                label: t('入库备注'),
              },
            ];
            remarkModalOpen.value = true;
          },
        },
      ],
    },
  ];

  return {
    materialBaseColumns,
    supplierBaseColumns,
    materialBatchColumns,
    tableRef,
    columns,
    remarkModalOpen,
    remarkDetails,
  };
};
