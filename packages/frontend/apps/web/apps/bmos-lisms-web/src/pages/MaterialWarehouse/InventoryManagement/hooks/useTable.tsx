import { RemarkDetail } from '@/components/RemarkModal';
import { useDict, usePermissionStore } from '@/stores';
import { MaterialModelTypeEnum, MaterialWarehouseAreaEnum } from '@/types';
import { type FormProps, type Recordable, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = (
  openDetail: Function,
  openStorageCard: Function,
  openMaterial: Function,
  warehouseArea: Ref<keyof typeof MaterialWarehouseAreaEnum>,
) => {
  const { hasPermission } = usePermissionStore();
  const { getDict } = useDict();
  const { getDateFormat } = useConfig();
  const { keyMaterialCategoryDict, materialTypeDict } = getDicts();
  const pageExpendRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  // 备注弹窗相关
  const remarkModalOpen = ref<boolean>(false);
  const remarkDetails = ref<RemarkDetail[]>([]);

  const columnsFirst: TableColumn[] = [
    {
      title: t('物料编号'),
      dataIndex: 'materialNo',
      width: 120,
    },
    {
      title: t('物料名称'),
      dataIndex: 'materialName',
      width: 160,
    },
    {
      title: t('物料类型'),
      dataIndex: 'materialType',
      width: 100,
      customRender: ({ record }) => record?.materialType?.label ?? '-',
    },
    {
      title: t('关键物料品类'),
      dataIndex: 'keyMaterialCategory',
      width: 120,
      customRender: ({ record }) => record?.keyMaterialCategory?.label ?? '-',
    },
    {
      title: t('关键物料类型'),
      dataIndex: 'keyMaterialTypeName',
      width: 150,
    },
    {
      title: t('物料批号'),
      dataIndex: 'batchNo',
      width: 140,
    },
    {
      title: t('供应商'),
      dataIndex: 'supplierName',
      width: 140,
    },
    {
      title: t('物料单位'),
      dataIndex: 'unitName',
      width: 100,
    },
    {
      title: t('物料规格'),
      dataIndex: 'specificationName',
      width: 100,
    },
    {
      title: t('结存总量'),
      dataIndex: 'inventory',
      width: 100,
    },
    {
      title: t('可用库存量'),
      dataIndex: 'availableStock',
      width: 120,
    },
    {
      title: t('生产日期'),
      dataIndex: 'productionDate',
      width: 170,
      customRender: ({ record }) => getDateFormat(record.productionDate),
    },
    {
      title: t('有效日期'),
      dataIndex: 'expireDate',
      width: 170,
      customRender: ({ record }) => getDateFormat(record.expireDate),
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 200,
      actions: ({ record }) => [
        {
          label: t('备注'),
          onClick: () => {
            remarkDetails.value = [
              {
                field: 'materialRemark',
                value: record.materialRemark,
                label: t('物料备注'),
              },
            ];
            remarkModalOpen.value = true;
          },
        },
        {
          label: t('查看'),
          ifShow: hasPermission('210060005000001'),
          onClick: () => {
            openDetail(record);
          },
        },
        {
          label: t('物料领用'),
          ifShow: hasPermission('210060005000002') && warehouseArea.value === 'PASS' && record.availableStock > 0,
          onClick: () => {
            openMaterial(MaterialModelTypeEnum.RECEIVE, record);
          },
        },
      ],
    },
  ];

  const columnsExpand: TableColumn[] = [
    {
      title: t('入库单号'),
      dataIndex: 'inWarehouseNo',
      width: 140,
    },
    {
      title: t('入库数量'),
      dataIndex: 'quantity',
      width: 100,
    },
    {
      title: t('结存数量'),
      dataIndex: 'inventory',
      width: 100,
    },
    {
      title: t('可用库存量'),
      dataIndex: 'availableStock',
      width: 120,
    },
    {
      title: t('仓库区域'),
      dataIndex: 'warehouseArea',
      width: 100,
      customRender: ({ record }) => record?.warehouseArea?.label ?? '-',
    },
    {
      title: t('仓库地址'),
      dataIndex: 'warehouseAddressName',
      width: 120,
    },
    {
      title: t('入库人'),
      dataIndex: 'inWarehouseBy',
      width: 100,
    },
    {
      title: t('入库日期'),
      dataIndex: 'inWarehouseTime',
      width: 170,
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.inWarehouseTime),
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 280,
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
        {
          label: t('货位卡'),
          onClick: () => {
            openStorageCard(record);
          },
        },
        {
          label: t('物料报废'),
          ifShow: hasPermission('210060005000003') && warehouseArea.value !== 'WAITING' && record.availableStock > 0,
          onClick: () => {
            openMaterial(MaterialModelTypeEnum.SCRAP, record);
          },
        },
        {
          label: t('物料退货'),
          ifShow: hasPermission('210060005000005') && warehouseArea.value === 'NOPASS' && record.availableStock > 0,
          onClick: () => {
            openMaterial(MaterialModelTypeEnum.RETURN, record);
          },
        },
        {
          label: t('物料抽检'),
          ifShow: hasPermission('210060005000004') && warehouseArea.value === 'PASS' && record.availableStock > 0,
          onClick: () => {
            openMaterial(MaterialModelTypeEnum.SPOT_CHECK, record);
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    labelWidth: 100,
    labelAlign: 'left',
    schemas: [
      {
        label: t('物料名称'),
        field: 'materialName',
        component: 'Input',
      },
      {
        label: t('物料编号'),
        field: 'materialNo',
        component: 'Input',
      },
      {
        label: t('物料类型'),
        field: 'materialType',
        component: 'Select',
        componentProps: {
          options: materialTypeDict,
        },
      },
      {
        label: t('关键物料品类'),
        field: 'keyMaterialCategory',
        component: 'Select',
        componentProps: {
          options: keyMaterialCategoryDict,
        },
      },
      {
        label: t('关键物料类型'),
        field: 'keyMaterialTypeId',
        component: 'Select',
        componentProps: {
          request: async () => {
            return await getDict('关键物料类型');
          },
        },
      },
      {
        label: t('物料批号'),
        field: 'batchNo',
        component: 'Input',
      },
      {
        label: t('供应商'),
        field: 'supplierName',
        component: 'Select',
        componentProps: {
          fieldNames: {
            label: 'label',
            value: 'label',
          },
          request: async () => {
            return await getDict('供应商');
          },
        },
      },
      {
        label: t('物料单位'),
        field: 'unitId',
        component: 'Select',
        componentProps: {
          request: async () => {
            return await getDict('物料单位');
          },
        },
      },
      {
        label: t('物料规格'),
        field: 'specificationId',
        component: 'Select',
        componentProps: {
          request: async () => {
            return await getDict('物料规格');
          },
        },
      },
      {
        label: t('生产日期'),
        field: 'productionDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        label: t('有效日期'),
        field: 'expireDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        label: t('入库单号'),
        field: 'inWarehouseNo',
        component: 'Input',
      },
      {
        label: t('仓库地址'),
        field: 'warehouseAddressId',
        component: 'Select',
        componentProps: {
          request: async () => {
            return await getDict('仓库地址');
          },
        },
      },
      {
        label: t('入库日期'),
        field: 'inWarehouseTime',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
        },
      },
    ],
    fieldMapToTime: [
      ['productionDate', ['productionDateUp', 'productionDateDown'], 'YYYY-MM-DD'],
      ['expireDate', ['expireDateUp', 'expireDateDown'], 'YYYY-MM-DD'],
      ['inWarehouseTime', ['inWarehouseTimeUp', 'inWarehouseTimeDown'], 'YYYY-MM-DD'],
    ],
  };

  return {
    pageExpendRef,
    rowData,
    columnsFirst,
    formFirstProps,
    columnsExpand,
    remarkModalOpen,
    remarkDetails,
  };
};
