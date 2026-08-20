import { RemarkDetail } from '@/components/RemarkModal';
import { useDict } from '@/stores/dictStore';
import { StatusType } from '@/types';
import { BMStateTag, type FormProps, type Recordable, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const { getDict } = useDict();
  const { getDateFormat } = useConfig();
  const { approveStatusDict, auditResultDict } = getDicts();
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
      title: t('入库单号'),
      dataIndex: 'inWarehouseNo',
      width: 120,
    },
    {
      title: t('入库数量'),
      dataIndex: 'quantity',
      width: 100,
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
      title: t('退货数量'),
      dataIndex: 'useCount',
      width: 100,
    },
    {
      title: t('退货原因'),
      dataIndex: 'reasonName',
      width: 120,
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
      title: t('审核状态'),
      dataIndex: 'auditStatus',
      width: 100,
      customRender: ({ record }) => record?.auditStatus?.label ?? '-',
    },
    {
      title: t('审核结果'),
      dataIndex: 'auditResult',
      width: 100,
      customRender: ({ record }) => {
        const status: keyof typeof StatusType = record?.auditResult?.value;
        return status ? <BMStateTag type={StatusType[status]}>{record?.auditResult?.label}</BMStateTag> : '-';
      },
    },
    {
      title: t('审核人'),
      dataIndex: 'auditBy',
      width: 170,
    },
    {
      title: t('审核日期'),
      dataIndex: 'auditTime',
      width: 170,
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.auditTime),
    },
    {
      title: t('批准状态'),
      dataIndex: 'approveStatus',
      width: 100,
      customRender: ({ record }) => record?.approveStatus?.label ?? '-',
    },
    {
      title: t('批准结果'),
      dataIndex: 'approveResult',
      width: 100,
      customRender: ({ record }) => {
        const status: keyof typeof StatusType = record?.approveResult?.value;
        return status ? <BMStateTag type={StatusType[status]}>{record?.approveResult?.label}</BMStateTag> : '-';
      },
    },
    {
      title: t('批准人'),
      dataIndex: 'approveBy',
      width: 100,
    },
    {
      title: t('批准日期'),
      dataIndex: 'approveDate',
      width: 170,
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.approveDate),
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
                label: t('退货备注'),
              },
              {
                field: 'auditRemark',
                value: record.auditRemark,
                label: t('审核备注'),
              },
              {
                field: 'approveRemark',
                value: record.approveRemark,
                label: t('批准备注'),
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
      ['approveDate', ['approveDateUp', 'approveDateDown'], 'YYYY-MM-DD'],
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
        label: t('退货原因'),
        field: 'reasonId',
        component: 'Select',
        componentProps: {
          request: async () => {
            return await getDict('退货原因');
          },
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
        label: t('审核结果'),
        field: 'auditResult',
        component: 'Select',
        componentProps: {
          options: auditResultDict,
        },
      },
      {
        label: t('审核日期'),
        field: 'auditDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        label: t('批准状态'),
        field: 'approveStatus',
        component: 'Select',
        componentProps: {
          options: approveStatusDict,
        },
      },
      {
        label: t('批准结果'),
        field: 'approveResult',
        component: 'Select',
        componentProps: {
          options: auditResultDict,
        },
      },
      {
        label: t('批准日期'),
        field: 'approveDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
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
