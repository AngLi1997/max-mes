import { RemarkDetail } from '@/components/RemarkModal';
import { materialUseDownload } from '@/services';
import { useDict, usePermissionStore } from '@/stores';
import { StatusType } from '@/types';
import { fileDownloadFlow } from '@/utils';
import { BMStateTag, type FormProps, type Recordable, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { message } from 'ant-design-vue';

export const useTable = (openEdit: Function) => {
  const { getDict } = useDict();
  const { hasPermission } = usePermissionStore();
  const { getDateFormat } = useConfig();
  const { materialTypeDict, passResultDict } = getDicts();
  // 备注弹窗相关
  const remarkModalOpen = ref<boolean>(false);
  const remarkDetails = ref<RemarkDetail[]>([]);

  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});
  const columnsFirst: TableColumn[] = [
    {
      title: t('入库单号'),
      dataIndex: 'inWarehouseNo',
      width: 180,
    },
    {
      title: t('入库日期'),
      dataIndex: 'inWarehouseTime',
      width: 170,
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.inWarehouseTime),
    },
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
      title: t('供应商'),
      dataIndex: 'supplierName',
      width: 120,
    },
    {
      title: t('物料类型'),
      dataIndex: 'materialType',
      width: 120,
      customRender: ({ record }) => record?.materialType?.label ?? '-',
    },
    {
      title: t('入库数量'),
      dataIndex: 'quantity',
      width: 100,
    },
    {
      title: t('抽检数量'),
      dataIndex: 'useCount',
      width: 100,
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
      title: t('放行结果'),
      dataIndex: 'passResult',
      width: 100,
      customRender: ({ record }) => {
        const status: keyof typeof StatusType = record?.passResult?.value;
        // @ts-ignore
        return status && status !== 'WAIT' ? (
          <BMStateTag type={StatusType[status]}>{record?.passResult?.label}</BMStateTag>
        ) : (
          '-'
        );
      },
    },
    {
      title: t('审核人'),
      dataIndex: 'auditBy',
      width: 100,
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
      width: 200,
      actions: ({ record }) => [
        {
          label: t('备注'),
          onClick: () => {
            remarkDetails.value = [
              {
                field: 'applicantRemark',
                value: record.applicantRemark,
                label: t('抽检申请备注'),
              },
              {
                field: 'auditRemark',
                value: record.auditRemark,
                label: t('抽检放行备注'),
              },
            ];
            remarkModalOpen.value = true;
          },
        },
        {
          label: t('下载'),
          ifShow: hasPermission('210060008000002') && record?.auditStatus?.value === 'AUDITED',
          onClick: async () => {
            try {
              const res = await materialUseDownload({
                useFormIdentify: record.useFormIdentify,
                inWarehouseNo: record.inWarehouseNo,
              });
              // zip下载
              await fileDownloadFlow(res, 'application/zip');
            } catch (error: any) {
              console.log('error', error);
              error.message && message.error(error.message);
            }
          },
        },
        {
          label: t('编辑'),
          ifShow: hasPermission('210060008000003') && record?.auditStatus?.value === 'AUDITED',
          onClick: async () => {
            await openEdit(record);
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    labelAlign: 'left',
    fieldMapToTime: [['auditDate', ['auditDateUp', 'auditDateDown'], 'YYYY-MM-DD']],
    schemas: [
      {
        label: t('入库单号'),
        field: 'inWarehouseNo',
        component: 'Input',
      },
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
        label: t('物料类型'),
        field: 'materialType',
        component: 'Select',
        componentProps: {
          options: materialTypeDict,
        },
      },
      {
        label: t('放行结果'),
        field: 'passResult',
        component: 'Select',
        componentProps: {
          options: passResultDict,
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
