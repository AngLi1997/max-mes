// import { usePermissionStore } from '@/stores/permission';
import { RemarkDetail } from '@/components/RemarkModal';
import { getSampleRejectRemark } from '@/services';
import { useConfig, useDict, usePlasmaStation } from '@/stores';
import { SpecimenTypeEnum, StatusType } from '@/types';
import { BMStateTag, type FormProps, type Recordable, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const { getDict } = useDict();
  const { getPlasmaStations } = usePlasmaStation();
  const { getConfigEnumsValueByParamId, getDateFormat } = useConfig();
  const { auditResultDict, sampleCategoryDict, yesOrNoDict } = getDicts();

  // 备注弹窗相关
  const remarkModalOpen = ref<boolean>(false);
  const remarkDetails = ref<RemarkDetail[]>([]);

  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});
  const columnsFirst: TableColumn[] = [
    {
      title: t('标本批号'),
      dataIndex: 'batchNo',
      width: 170,
    },
    {
      title: t('标本编号'),
      dataIndex: 'orgSampleNo',
      width: 220,
    },
    {
      title: t('标本类型'),
      dataIndex: 'sampleType',
      width: 140,
      customRender: ({ record }) => {
        return record?.sampleType?.label ?? '-';
      },
    },
    {
      title: t('标本分类'),
      dataIndex: 'sampleClassification',
      width: 120,
      customRender: ({ record }: any) => {
        if (record.sampleClassification?.value === SpecimenTypeEnum.SERUM_SPECIMEN) {
          return (
            <span
              style={{
                color: getConfigEnumsValueByParamId('血清标本颜色'),
              }}>
              {record.sampleClassification?.label}
            </span>
          );
        }
        return record.sampleClassification?.label ?? '-';
      },
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      width: 160,
    },
    {
      title: t('姓名'),
      dataIndex: 'plasmaDonorName',
      width: 120,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 170,
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.slurryDate),
    },
    {
      title: t('拒收原因'),
      dataIndex: 'refuseReasonName',
      width: 120,
    },
    {
      title: t('是否补样'),
      dataIndex: 'needSupplement',
      width: 120,
      customRender: ({ record }) => {
        return record?.needSupplement?.label ?? '-';
      },
    },
    {
      title: t('拒收人'),
      dataIndex: 'applicant',
      width: 100,
    },
    {
      title: t('拒收日期'),
      dataIndex: 'applicantTime',
      width: 170,
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.applicantTime),
    },
    {
      title: t('审核状态'),
      dataIndex: 'status',
      width: 100,
      customRender: ({ record }) => {
        return record?.status?.label ?? '-';
      },
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
      dataIndex: 'reviewer',
      width: 100,
    },
    {
      title: t('审核日期'),
      dataIndex: 'reviewerTime',
      width: 170,
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.reviewerTime),
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrgName',
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
          onClick: async () => {
            const { data } = await getSampleRejectRemark({
              sampleNo: record.sampleNo,
              batchNo: record.batchNo,
              receiveStatus: record.receiveStatus?.value,
            });
            remarkDetails.value = [
              {
                label: t('拒收申请备注'),
                field: 'rejectApplyRemark',
                value: data.rejectApplyRemark,
              },
              {
                label: t('拒收审核备注'),
                field: 'rejectAuditRemark',
                value: data.rejectAuditRemark,
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
    labelWidth: 120,
    fieldMapToTime: [
      ['slurryDate', ['slurryDateStart', 'slurryDateEnd'], 'YYYY-MM-DD'],
      ['rejectDate', ['rejectDateStart', 'rejectDateEnd'], 'YYYY-MM-DD'],
    ],
    schemas: [
      {
        label: t('标本批号'),
        field: 'batchNo',
        component: 'Input',
      },
      {
        label: t('标本编号'),
        field: 'orgSampleNo',
        component: 'Input',
      },
      {
        label: t('来源单位'),
        field: 'originOrgCode',
        component: 'Select',
        componentProps: {
          request: getPlasmaStations,
        },
      },
      {
        label: t('标本分类'),
        field: 'sampleClassification',
        component: 'Select',
        componentProps: {
          options: sampleCategoryDict,
        },
      },
      {
        label: t('献浆者姓名'),
        field: 'plasmaDonorName',
        component: 'Input',
      },
      {
        label: t('献浆者编号'),
        field: 'plasmaDonorNo',
        component: 'Input',
      },
      {
        label: t('采浆日期'),
        field: 'slurryDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        label: t('拒收原因'),
        field: 'refuseReason',
        component: 'Select',
        componentProps: {
          request: async () => {
            return await getDict('拒收原因');
          },
        },
      },
      {
        label: t('是否补样'),
        field: 'needSupplement',
        component: 'Select',
        componentProps: {
          options: yesOrNoDict,
        },
      },
      {
        label: t('拒收日期'),
        field: 'rejectDate',
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
