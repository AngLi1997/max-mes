import { RemarkDetail } from '@/components/RemarkModal';
import { useDict } from '@/stores/dictStore';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const { keyMaterialCategoryDict, materialTypeDict } = getDicts();
  const { getDict } = useDict();
  const { getDateFormat } = useConfig();
  // 备注弹窗相关
  const remarkModalOpen = ref<boolean>(false);
  const remarkDetails = ref<RemarkDetail[]>([]);

  const pageExpendRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  const columnsFirst: TableColumn[] = [
    {
      title: t('物料编号'),
      dataIndex: 'materialNo',
      width: 120,
    },
    {
      title: t('物料名称'),
      dataIndex: 'materialName',
      width: 140,
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
      width: 120,
    },
    {
      title: t('物料批号'),
      dataIndex: 'batchNo',
      width: 140,
    },
    {
      title: t('结存总量'),
      dataIndex: 'residueCount',
      width: 100,
    },
    {
      title: t('可用库存量'),
      dataIndex: 'useCount',
      width: 120,
    },
    {
      title: t('供应商'),
      dataIndex: 'supplierName',
      width: 120,
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
      title: t('有效日期'),
      dataIndex: 'expireDate',
      width: 170,
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.expireDate),
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 80,
      actions: ({ record }) => [
        {
          label: t('备注'),
          // ifShow: () => {
          //   return hasPermission('170040006000003') && record?.inWarehouseStatus?.code === 4;
          // },
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

  const columnsExpand: TableColumn[] = [
    {
      title: t('登记日期'),
      dataIndex: 'registrantTime',
      width: 170,
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.registrantTime),
    },
    {
      title: t('登记人'),
      dataIndex: 'registrant',
      width: 80,
    },
    {
      title: t('入库数量'),
      dataIndex: 'warehouseCount',
      width: 100,
    },
    {
      title: t('发出量'),
      dataIndex: 'sendCount',
      width: 100,
    },
    {
      title: t('结存数量'),
      dataIndex: 'remainCount',
      width: 100,
    },
    {
      title: t('登记类型'),
      dataIndex: 'recordSource',
      width: 120,
      customRender: ({ record }) => record?.recordSource?.label ?? '-',
    },
    {
      title: t('审核人'),
      dataIndex: 'auditBy',
      width: 80,
    },
    {
      title: t('审核日期'),
      dataIndex: 'auditTime',
      width: 170,
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.auditTime),
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 80,
      actions: ({ record }) => [
        {
          label: t('备注'),
          // ifShow: () => {
          //   return hasPermission('170040006000003') && record?.inWarehouseStatus?.code === 4;
          // },
          onClick: () => {
            const type = record?.recordSource?.label ?? '';
            remarkDetails.value = [
              {
                field: 'applyRemark',
                value: record.applyRemark,
                label: type + t('备注'),
              },
              {
                field: 'auditRemark',
                value: record.auditRemark,
                label: t('审核备注'),
              },
            ];
            remarkModalOpen.value = true;
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    labelWidth: 110,
    labelAlign: 'left',
    schemas: [
      {
        label: t('物料编号'),
        field: 'materialNo',
        component: 'Input',
      },
      {
        label: t('物料名称'),
        field: 'materialName',
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
        label: t('物料批号'),
        field: 'batchNo',
        component: 'Input',
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
        label: t('有效日期'),
        field: 'expireDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
        },
      },
    ],
    fieldMapToTime: [['expireDate', ['expireDateStart', 'expireDateEnd'], 'YYYY-MM-DD']],
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
