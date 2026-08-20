import { RemarkDetail } from '@/components/RemarkModal';
import { useDict } from '@/stores/dictStore';
import { UseCategoryEnum } from '@/types';
import { type FormProps, type Recordable, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const { getDict } = useDict();
  const { getDateFormat } = useConfig();
  const { materialWarehouseAreaDict, useCategoryDict } = getDicts();
  // 备注弹窗相关
  const remarkModalOpen = ref<boolean>(false);
  const remarkDetails = ref<RemarkDetail[]>([]);

  const pageRef = ref<any>(null);
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
      title: t('生产日期'),
      dataIndex: 'productionDate',
      width: 170,
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.productionDate),
    },
    {
      title: t('有效日期'),
      dataIndex: 'expireDate',
      width: 170,
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.expireDate),
    },
    {
      title: t('仓库地址'),
      dataIndex: 'warehouseAddressName',
      width: 140,
    },
    {
      title: t('仓库区域'),
      dataIndex: 'warehouseArea',
      width: 100,
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
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.applicantTime),
    },
    {
      title: t('出库状态'),
      dataIndex: 'outStatus',
      width: 100,
      customRender: ({ record }) => record?.outStatus?.label ?? '-',
    },
    {
      title: t('出库数量'),
      dataIndex: 'useCount',
      width: 100,
    },
    {
      title: t('出库人'),
      dataIndex: 'warehouseOperator',
      width: 100,
    },
    {
      title: t('出库日期'),
      dataIndex: 'outTime',
      width: 170,
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.outTime),
    },
    {
      title: t('接收人'),
      dataIndex: 'receiver',
      width: 100,
    },
    {
      title: t('接收日期'),
      dataIndex: 'receiverTime',
      width: 170,
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.receiverTime),
    },
    {
      title: t('领用库'),
      dataIndex: 'targetWarehouseName',
      width: 100,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 80,
      actions: ({ record }) => [
        {
          label: t('备注'),
          // ifShow: hasPermission('170020001000003') && record?.receiveStatus?.code == 0,
          onClick: () => {
            remarkDetails.value = [
              {
                field: 'applicantRemark',
                value: record.applicantRemark,
                label: record?.useType?.label + t('备注'),
              },
              {
                field: 'auditRemark',
                value: record.auditRemark,
                label: record?.useType?.label + t('审核备注'),
              },
              {
                field: 'outRemark',
                value: record.outRemark,
                label: t('出库备注'),
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
    labelAlign: 'left',
    fieldMapToTime: [
      ['applicantTime', ['applicantTimeUp', 'applicantTimeDown'], 'YYYY-MM-DD'],
      ['auditDate', ['auditDateUp', 'auditDateDown'], 'YYYY-MM-DD'],
      ['expireDate', ['expireDateUp', 'expireDateDown'], 'YYYY-MM-DD'],
      ['outDate', ['outDateUp', 'outDateDown'], 'YYYY-MM-DD'],
    ],
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
        label: t('仓库区域'),
        field: 'warehouseArea',
        component: 'Select',
        componentProps: {
          options: materialWarehouseAreaDict,
        },
      },
      {
        label: t('使用类别'),
        field: 'useType',
        component: 'Select',
        componentProps: {
          options: useCategoryDict,
        },
      },
      {
        label: t('申请日期'),
        field: 'applicantTime',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        label: t('出库日期'),
        field: 'outDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        label: t('领用库'),
        field: 'targetWarehouseId',
        component: 'Select',
        componentProps: {
          request: async () => {
            return await getDict('领用库');
          },
        },
      },
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
