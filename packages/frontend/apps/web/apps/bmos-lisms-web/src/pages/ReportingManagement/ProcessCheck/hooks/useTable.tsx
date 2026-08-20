import { RemarkDetail } from '@/components/RemarkModal';
import { useConfig, usePermissionStore, usePlasmaStation } from '@/stores';
import { InspectionResultEnum, SpecimenTypeEnum, StatusType } from '@/types';
import { BMStateTag, type FormProps, type Recordable, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = (
  openCntModal: Function,
  openUnqualified: Function,
  openAuditCnt: Function,
  print: Function,
  openCheck: Function,
  printCheckRecord: Function,
) => {
  const { getPlasmaStations } = usePlasmaStation();
  const { getConfigEnumsValueByParamId, getDateFormat } = useConfig();
  const { hasPermission } = usePermissionStore();
  const { signResultDict, signStatusDict } = getDicts();
  // 备注弹窗相关
  const remarkModalOpen = ref<boolean>(false);
  const remarkDetails = ref<RemarkDetail[]>([]);

  const pageExpendRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  const columnsFirst: TableColumn[] = [
    {
      title: t('标本批号'),
      dataIndex: 'sampleBatchNo',
      width: 160,
    },
    {
      title: t('请验数量'),
      dataIndex: 'transferCount',
      width: 100,
      customRender: ({ record }) => {
        return record.transferCount ? <a onClick={() => openCntModal(record)}>{record.transferCount}</a> : '0';
      },
    },
    {
      title: t('接收数量'),
      dataIndex: 'receivedCount',
      width: 100,
      customRender: ({ record }) => {
        return record.receivedCount ? <a onClick={() => openCntModal(record)}>{record.receivedCount}</a> : '0';
      },
    },
    {
      title: t('合格数量'),
      dataIndex: 'qualifiedCount',
      width: 100,
    },
    {
      title: t('不合格数量'),
      dataIndex: 'unqualifiedCount',
      width: 120,
      customRender: ({ record }) => {
        return record.unqualifiedCount ? <a onClick={() => openUnqualified(record)}>{record.unqualifiedCount}</a> : '0';
      },
    },
    {
      title: t('已审核次数'),
      dataIndex: 'publishCount',
      width: 120,
      customRender: ({ record }) => {
        return record.publishCount ? <a onClick={() => openAuditCnt(record)}>{record.publishCount}</a> : '0';
      },
    },
    {
      title: t('报告状态'),
      dataIndex: 'auditStatus',
      width: 120,
      customRender: ({ record }) => record?.auditStatus?.label ?? '-',
    },
    {
      title: t('审核人'),
      dataIndex: 'auditBy',
      width: 100,
    },
    {
      title: t('审核日期'),
      dataIndex: 'auditDate',
      width: 170,
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.auditDate),
    },
    {
      title: t('检查状态'),
      dataIndex: 'checkStatus',
      width: 120,
      customRender: ({ record }) => record?.checkStatus?.label ?? '-',
    },
    {
      title: t('检查结果'),
      dataIndex: 'checkResult',
      width: 120,
      customRender: ({ record }) => {
        const status: keyof typeof StatusType = record?.checkResult?.value;
        return status ? <BMStateTag type={StatusType[status]}>{record?.checkResult?.label}</BMStateTag> : '-';
      },
    },
    {
      title: t('检查人'),
      dataIndex: 'checker',
      width: 100,
    },
    {
      title: t('检查日期'),
      dataIndex: 'checkTime',
      width: 170,
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.checkTime),
    },
    {
      title: t('签发状态'),
      dataIndex: 'status',
      width: 120,
      customRender: ({ record }) => record?.status?.label ?? '-',
    },
    {
      title: t('签发结果'),
      dataIndex: 'result',
      width: 120,
      customRender: ({ record }) => {
        const status: keyof typeof StatusType = record?.result?.value;
        return status ? <BMStateTag type={StatusType[status]}>{record?.result?.label}</BMStateTag> : '-';
      },
    },
    {
      title: t('签发人'),
      dataIndex: 'reportBy',
      width: 100,
    },
    {
      title: t('签发日期'),
      dataIndex: 'reportTime',
      width: 170,
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.reportTime),
    },
    {
      title: t('来源单位'),
      dataIndex: 'transferFrom',
      width: 100,
    },
    {
      title: t('接收人'),
      dataIndex: 'receiveBy',
      width: 100,
    },
    {
      title: t('接收日期'),
      dataIndex: 'receiveTime',
      width: 170,
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.receiveTime),
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 260,
      actions: ({ record }) => [
        {
          label: t('检查'),
          ifShow: hasPermission('210040003000001') && record?.checkStatus?.value === 'WAIT_CHECK',
          onClick: async () => {
            await openCheck(record);
          },
        },
        {
          label: t('检验报告'),
          ifShow: hasPermission('210040003000002'),
          onClick: async () => {
            await print(record);
          },
        },
        {
          label: t('控制点记录'),
          ifShow: hasPermission('210040003000003') && record?.checkStatus?.value === 'CHECKED',
          onClick: async () => {
            await printCheckRecord(record);
          },
        },
        {
          label: t('备注'),
          onClick: () => {
            remarkDetails.value = [
              {
                label: t('审核备注'),
                field: 'auditRemark',
                value: record.auditRemark,
              },
              {
                label: t('检查备注'),
                field: 'checkRemark',
                value: record.checkRemark,
              },
              {
                label: t('签发备注'),
                field: 'publishRemark',
                value: record.publishRemark,
              },
              {
                label: t('签发撤回备注'),
                field: 'backRemark',
                value: record.backRemark,
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
      title: t('标本编号'),
      dataIndex: 'orgSampleNo',
      width: 220,
    },
    {
      title: t('标本类型'),
      dataIndex: 'sampleType',
      width: 140,
      customRender: ({ record }) => record?.sampleType?.label ?? '-',
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
      title: t('标本箱号'),
      dataIndex: 'boxId',
      width: 120,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'donorNo',
      width: 160,
    },
    {
      title: t('姓名'),
      dataIndex: 'donorName',
      width: 120,
    },
    {
      title: t('性别'),
      dataIndex: 'donorSex',
      width: 80,
    },
    {
      title: t('血型'),
      dataIndex: 'donorBloodType',
      width: 80,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 170,
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.slurryDate),
    },
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      width: 170,
    },
    {
      title: t('血浆外观'),
      dataIndex: 'appearance',
      width: 120,
      customRender: ({ record }) => record?.appearance?.label ?? '-',
    },
    {
      title: t('免疫类型'),
      dataIndex: ['immunityType', 'label'],
      width: 120,
    },
    {
      title: t('检品状态'),
      dataIndex: 'testArticleStatus',
      width: 100,
      customRender: ({ record }) => {
        const status: keyof typeof StatusType = record?.testArticleStatus?.value;
        if (!status) {
          return '-';
        }
        return <BMStateTag type={StatusType[status]}>{record?.testArticleStatus?.label}</BMStateTag>;
      },
    },
    {
      title: t('检验结论'),
      dataIndex: 'result',
      width: 100,
      customRender: ({ record }) => {
        const status: keyof typeof InspectionResultEnum = record?.result?.value;
        if (!status) {
          return '-';
        }
        return (
          <span style={{ color: status === InspectionResultEnum.UNQUALIFIED ? 'red' : undefined }}>
            {record?.result?.label}
          </span>
        );
      },
    },
    {
      title: t('蛋白质含量'),
      dataIndex: 'protein',
      width: 110,
      customRender: ({ record }) => {
        const status: keyof typeof InspectionResultEnum = record?.protein?.value;
        if (!status) {
          return '-';
        }
        return (
          <span style={{ color: status === InspectionResultEnum.UNQUALIFIED ? 'red' : undefined }}>
            {record?.protein?.label}
          </span>
        );
      },
    },
    {
      title: t('ALT'),
      dataIndex: 'alt',
      width: 100,
      customRender: ({ record }) => {
        const status: keyof typeof InspectionResultEnum = record?.alt?.value;
        if (!status) {
          return '-';
        }
        return (
          <span style={{ color: status === InspectionResultEnum.UNQUALIFIED ? 'red' : undefined }}>
            {record?.alt?.label}
          </span>
        );
      },
    },
    {
      title: t('HBsAg'),
      dataIndex: 'hbsag',
      width: 100,
      customRender: ({ record }) => {
        const status: keyof typeof InspectionResultEnum = record?.hbsag?.value;
        if (!status) {
          return '-';
        }
        return (
          <span style={{ color: status === InspectionResultEnum.UNQUALIFIED ? 'red' : undefined }}>
            {record?.hbsag?.label}
          </span>
        );
      },
    },
    {
      title: t('抗-HCV'),
      dataIndex: 'hcv',
      width: 100,
      customRender: ({ record }) => {
        const status: keyof typeof InspectionResultEnum = record?.hcv?.value;
        if (!status) {
          return '-';
        }
        return (
          <span style={{ color: status === InspectionResultEnum.UNQUALIFIED ? 'red' : undefined }}>
            {record?.hcv?.label}
          </span>
        );
      },
    },
    {
      title: t('抗-HIV'),
      dataIndex: 'hiv',
      width: 100,
      customRender: ({ record }) => {
        const status: keyof typeof InspectionResultEnum = record?.hiv?.value;
        if (!status) {
          return '-';
        }
        return (
          <span style={{ color: status === InspectionResultEnum.UNQUALIFIED ? 'red' : undefined }}>
            {record?.hiv?.label}
          </span>
        );
      },
    },
    {
      title: t('抗-TP'),
      dataIndex: 'tp',
      width: 100,
      customRender: ({ record }) => {
        const status: keyof typeof InspectionResultEnum = record?.tp?.value;
        if (!status) {
          return '-';
        }
        return (
          <span style={{ color: status === InspectionResultEnum.UNQUALIFIED ? 'red' : undefined }}>
            {record?.tp?.label}
          </span>
        );
      },
    },
    {
      title: t('蛋白电泳'),
      dataIndex: 'proteinElectrophoresis',
      width: 100,
      customRender: ({ record }) => {
        const status: keyof typeof InspectionResultEnum = record?.proteinElectrophoresis?.value;
        if (!status) {
          return '-';
        }
        return (
          <span style={{ color: status === InspectionResultEnum.UNQUALIFIED ? 'red' : undefined }}>
            {record?.proteinElectrophoresis?.label}
          </span>
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
      dataIndex: 'auditDate',
      width: 170,
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.auditDate),
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    labelWidth: 120,
    labelAlign: 'left',
    schemas: [
      {
        label: t('标本批号'),
        field: 'sampleBatchNo',
        component: 'Input',
      },
      {
        label: t('来源单位'),
        field: 'transferFrom',
        component: 'Select',
        componentProps: {
          request: getPlasmaStations,
        },
      },
      {
        label: t('签发状态'),
        field: 'status',
        component: 'Select',
        componentProps: {
          options: signStatusDict,
        },
      },
      {
        label: t('签发结果'),
        field: 'result',
        component: 'Select',
        componentProps: {
          options: signResultDict,
        },
      },
      {
        label: t('标本编号'),
        field: 'orgSampleNo',
        component: 'Input',
      },
      {
        label: t('献浆者编号'),
        field: 'donorNo',
        component: 'Input',
      },
      {
        label: t('献浆者姓名'),
        field: 'name',
        component: 'Input',
      },
      {
        label: t('血浆编号'),
        field: 'plasmaNo',
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
        label: t('接收日期'),
        field: 'receiveDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        label: t('总数据发布日期'),
        field: 'dataPublishDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
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
        label: t('检查日期'),
        field: 'checkDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        label: t('签发日期'),
        field: 'reportDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
        },
      },
    ],
    fieldMapToTime: [
      ['slurryDate', ['slurryStartDate', 'slurryEndDate'], 'YYYY-MM-DD'],
      ['receiveDate', ['receiveStartDate', 'receiveEndDate'], 'YYYY-MM-DD'],
      ['reportDate', ['reportStartDate', 'reportEndDate'], 'YYYY-MM-DD'],
      ['auditDate', ['auditStartDate', 'auditEndDate'], 'YYYY-MM-DD'],
      ['dataPublishDate', ['dataPublishStartDate', 'dataPublishEndDate'], 'YYYY-MM-DD'],
      ['checkDate', ['checkStartDate', 'checkEndDate'], 'YYYY-MM-DD'],
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
