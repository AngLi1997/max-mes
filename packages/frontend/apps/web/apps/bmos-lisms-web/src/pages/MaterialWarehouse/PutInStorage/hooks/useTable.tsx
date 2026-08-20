import { RemarkDetail } from '@/components/RemarkModal';
import { useDict, usePermissionStore } from '@/stores';
import { type FormProps, type Recordable, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = (openReceiveDetail: Function, openModal: Function, openStoreModal: Function) => {
  const { getDict } = useDict();
  const { hasPermission } = usePermissionStore();
  const { getDateFormat } = useConfig();
  const { materialTypeDict, materialWarehouseAreaDict, yesOrNoDict } = getDicts();

  // 备注弹窗相关
  const remarkModalOpen = ref<boolean>(false);
  const remarkDetails = ref<RemarkDetail[]>([]);

  getDict('质控品含量');
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});
  const columnsFirst: TableColumn[] = [
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
      title: t('入库状态'),
      dataIndex: 'storageStatus',
      width: 100,
      resizable: true,
      customRender: ({ record }) => record?.storageStatus?.label ?? '-',
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
      customRender: ({ record }) => getDateFormat(record.productionDate),
    },
    {
      title: t('有效日期'),
      dataIndex: 'expireDate',
      width: 170,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => getDateFormat(record.expireDate),
    },
    {
      title: t('入库人'),
      dataIndex: 'inWarehouseBy',
      width: 90,
      resizable: true,
    },
    {
      title: t('入库日期'),
      dataIndex: 'inWarehouseTime',
      width: 170,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => getDateFormat(record.inWarehouseTime),
    },
    {
      title: t('仓库地址'),
      dataIndex: 'warehouseAddressName',
      width: 180,
      resizable: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 200,
      actions: ({ record }) => [
        {
          label: t('编辑'),
          ifShow: hasPermission('210060004000003') && record?.storageStatus?.value === 'WAITING_STORAGE',
          onClick: () => {
            openModal(record);
          },
        },
        {
          label: t('入库'),
          ifShow: hasPermission('210060004000001') && record?.storageStatus?.value === 'WAITING_STORAGE',
          onClick: () => {
            openStoreModal('store', [record]);
          },
        },
        {
          label: t('撤销'),
          ifShow: hasPermission('210060004000002') && record?.storageStatus?.value === 'WAITING_STORAGE',
          onClick: () => {
            openStoreModal('cancel', [record]);
          },
        },
        {
          label: t('备注'),
          onClick: () => {
            remarkDetails.value = [
              {
                label: t('物料备注'),
                field: 'materialRemark',
                value: record.materialRemark,
              },
              {
                label: t('接收备注'),
                field: 'receiveRemark',
                value: record.receiveRemark,
              },
              {
                label: t('入库备注'),
                field: 'inStockRemark',
                value: record.inStockRemark,
              },
            ];
            remarkModalOpen.value = true;
          },
        },
        {
          label: t('查看'),
          onClick: () => {
            openReceiveDetail(record);
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    labelAlign: 'left',
    labelWidth: 100,
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
        label: t('是否抽检'),
        field: 'needSpotCheck',
        component: 'Select',
        componentProps: {
          options: yesOrNoDict,
        },
      },
      {
        label: t('仓库区域'),
        field: 'warehouseArea',
        component: 'Select',
        componentProps: {
          options: materialWarehouseAreaDict,
        },
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
        label: t('入库日期'),
        field: 'inWarehouseTime',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
        },
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
    ],
    fieldMapToTime: [
      ['productionDate', ['productionDateUp', 'productionDateDown']],
      ['inWarehouseTime', ['inWarehouseTimeUp', 'inWarehouseTimeDown']],
      ['expireDate', ['expireDateUp', 'expireDateDown']],
    ],
  };

  return {
    pageRef,
    rowData,
    columnsFirst,
    formFirstProps,
    remarkModalOpen,
    remarkDetails,
  };
};
