import { RemarkDetail } from '@/components/RemarkModal';
import { useConfig, usePlasmaStation } from '@/stores';
import { SpecimenTypeEnum, StatusType } from '@/types';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { BMStateTag, type FormProps, type Recordable, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = (
  openCntModal: Function,
  openTransportModal: Function,
  openRejectDetail: Function,
  openInspectItems: Function,
) => {
  const { getPlasmaStations } = usePlasmaStation();
  const { getDateFormat, getConfigEnumsValueByParamId } = useConfig();
  const { auditResultDict, receiveStatusDict, sampleCategoryDict, testArticleStatusDict, transportStatusDict } =
    getDicts();
  // 备注弹窗相关
  const remarkModalOpen = ref<boolean>(false);
  const remarkDetails = ref<RemarkDetail[]>([]);

  const pageExpendRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  const columnsFirst: TableColumn[] = [
    {
      title: t('标本批号'),
      dataIndex: 'batchNo',
      width: 160,
    },
    {
      title: t('请验数量'),
      dataIndex: 'transferCount',
      width: 160,
      customRender: ({ record }) => {
        return record.transferCount ? <a onClick={() => openCntModal(record)}>{record.transferCount}</a> : '0';
      },
    },
    {
      title: t('接收数量'),
      dataIndex: 'receiveCount',
      width: 160,
      customRender: ({ record }) => {
        return record.receiveCount ? <a onClick={() => openCntModal(record)}>{record.receiveCount}</a> : '0';
      },
    },
    {
      title: t('拒收数量'),
      dataIndex: 'rejectCount',
      width: 160,
      customRender: ({ record }) => {
        return record.rejectCount ? <a onClick={() => openRejectDetail(record)}>{record.rejectCount}</a> : '0';
      },
    },
    {
      title: t('送检人'),
      dataIndex: 'transferBy',
      width: 170,
    },
    {
      title: t('送检日期'),
      dataIndex: 'transferDate',
      width: 170,
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.transferDate),
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrgName',
      width: 120,
    },
    {
      title: t('运输状态'),
      dataIndex: 'transportStatus',
      width: 120,
      customRender: ({ record }) => {
        if (!record.transportStatus || !record.transportStatus?.length) {
          return '-';
        }
        return (
          <a onClick={() => openTransportModal(record)}>
            {record.transportStatus.map((item: any) => item.label).join(',')}
          </a>
        );
      },
    },
    {
      title: t('接收人'),
      dataIndex: 'applicant',
      width: 100,
    },
    {
      title: t('接收日期'),
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
                label: t('请验备注'),
                field: 'originRemark',
                value: record.originRemark,
              },
              {
                label: t('接收备注'),
                field: 'applyRemark',
                value: record.applyRemark,
              },
              {
                label: t('复核备注'),
                field: 'reviewRemark',
                value: record.reviewRemark,
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
      dataIndex: 'plasmaDonorNo',
      width: 160,
    },
    {
      title: t('姓名'),
      dataIndex: 'plasmaDonorName',
      width: 120,
    },
    {
      title: t('性别'),
      dataIndex: 'sex',
      width: 80,
    },
    {
      title: t('血型'),
      dataIndex: 'bloodType',
      width: 80,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 150,
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.slurryDate),
    },
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      width: 150,
    },
    {
      title: t('血浆外观'),
      dataIndex: 'appearance',
      width: 120,
    },
    {
      title: t('免疫类型'),
      dataIndex: 'immunityType',
      width: 120,
      customRender: ({ record }) => record?.immunityType?.label ?? '-',
    },
    {
      title: t('检品状态'),
      dataIndex: 'testArticleStatus',
      width: 120,
      customRender: ({ record }: any) => {
        const status: keyof typeof StatusType = record?.testArticleStatus?.value;
        if (!status) {
          return '-';
        }
        return (
          <BMStateTag
            type={StatusType[status]}
            isClick={status === 'REJECT'}
            onClick={() => {
              openRejectDetail(record);
            }}>
            <span>{record?.testArticleStatus?.label}</span>
            {status === 'REJECT' && <ExclamationCircleOutlined />}
          </BMStateTag>
        );
      },
    },
    {
      title: t('标本接收状态'),
      dataIndex: 'receiveStatus',
      width: 120,
      customRender: ({ record }) => {
        const status: keyof typeof StatusType = record.receiveStatus.value;
        return <BMStateTag type={StatusType[status]}>{record?.receiveStatus?.label}</BMStateTag>;
      },
    },
    {
      title: t('检验项目'),
      dataIndex: 'inspectionItem',
      width: 120,
      customRender: ({ record }) => {
        return (
          <a type='link' onClick={() => openInspectItems(record)}>
            {t('查看')}
          </a>
        );
      },
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    labelWidth: 100,
    labelAlign: 'left',
    schemas: [
      {
        label: t('送检日期'),
        field: 'transferDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        label: t('标本批号'),
        field: 'batchNo',
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
        label: t('运输状态'),
        field: 'transportStatus',
        component: 'Select',
        componentProps: {
          options: transportStatusDict,
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
        label: t('审核结果'),
        field: 'auditResult',
        component: 'Select',
        componentProps: {
          options: auditResultDict,
        },
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
        label: t('检品状态'),
        field: 'testArticleStatus',
        component: 'Select',
        componentProps: {
          options: testArticleStatusDict,
        },
      },
      {
        label: t('血浆编号'),
        field: 'plasmaNo',
        component: 'Input',
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
        label: t('标本分类'),
        field: 'sampleClassification',
        component: 'Select',
        componentProps: {
          options: sampleCategoryDict,
        },
      },
      {
        label: t('标本接收状态'),
        field: 'receiveStatus',
        component: 'Select',
        componentProps: {
          options: receiveStatusDict,
        },
      },
    ],
    fieldMapToTime: [
      ['receiveDate', ['receiveDateStart', 'receiveDateEnd'], 'YYYY-MM-DD'],
      ['slurryDate', ['slurryDateStart', 'slurryDateEnd'], 'YYYY-MM-DD'],
      ['transferDate', ['transferDateStart', 'transferDateEnd'], 'YYYY-MM-DD'],
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
