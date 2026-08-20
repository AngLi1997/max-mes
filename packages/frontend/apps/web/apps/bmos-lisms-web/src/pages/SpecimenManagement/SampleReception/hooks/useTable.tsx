import { RemarkDetail } from '@/components/RemarkModal';
import { useWarn } from '@/hooks';
import { sampleReceiveAgain } from '@/services';
import { useConfig, usePermissionStore } from '@/stores';
import { usePlasmaStation } from '@/stores/plasmaStation';
import { InspectionProcessMap, SpecimenTypeEnum, StatusType } from '@/types';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { BMStateTag, type FormProps, type Recordable, type TableActionType, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Button, message } from 'ant-design-vue';

const ProcessCom = defineComponent({
  props: {
    value: {
      type: String,
      default: '',
    },
  },
  emits: ['click'],
  setup(props, { slots, emit }) {
    return () => (
      <Button
        type='link'
        style={{ padding: '0', display: 'flex', alignItems: 'center' }}
        onClick={() => emit('click', props.value)}>
        <div
          style={{
            borderRadius: '50%',
            backgroundColor: '#2D70FF',
            width: '14px',
            height: '14px',
            marginRight: '5px',
            lineHeight: '14px',
            textAlign: 'center',
            color: '#fff',
            fontSize: '12px',
          }}>
          {(InspectionProcessMap[props.value as keyof typeof InspectionProcessMap] ?? 0) + 1}
        </div>
        <a href='#'>
          {{
            default: () => slots.default?.(),
          }}
        </a>
      </Button>
    );
  },
});

export const useTable = (
  openCntModal: Function,
  openProcessModal: Function,
  openTransportModal: Function,
  openRejectDetail: Function,
  openInspectItems: Function,
  openRecept: Function,
  openScan: Function,
) => {
  const { hasPermission } = usePermissionStore();
  const { getPlasmaStations } = usePlasmaStation();
  const { warnModal } = useWarn();
  const { getConfigEnumsValueByParamId, getDateFormat } = useConfig();
  const { inspectionProcessDict, sampleCategoryDict, testArticleStatusDict, transportStatusDict } = getDicts();
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
      width: 100,
      customRender: ({ record }) => {
        return record.transferCount ? <a onClick={() => openCntModal(record)}>{record.transferCount}</a> : '0';
      },
    },
    {
      title: t('接收数量'),
      dataIndex: 'receiveCount',
      width: 100,
      customRender: ({ record }) => {
        return record.receiveCount ? <a onClick={() => openCntModal(record)}>{record.receiveCount}</a> : '0';
      },
    },
    {
      title: t('拒收数量'),
      dataIndex: 'rejectCount',
      width: 100,
      customRender: ({ record }) => {
        return record.rejectCount ? <a onClick={() => openRejectDetail(record)}>{record.rejectCount}</a> : '0';
      },
    },
    {
      title: t('送检人'),
      dataIndex: 'transferBy',
      width: 100,
    },
    {
      title: t('送检日期'),
      dataIndex: 'transferDate',
      width: 170,
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.transferDate),
    },
    {
      title: t('检验进程'),
      dataIndex: 'inspectionProcess',
      width: 140,
      customRender: ({ record }) => {
        return (
          <ProcessCom
            value={record.inspectionProcess.value}
            onClick={() => openProcessModal(record.inspectionProcess.value)}>
            {record.inspectionProcess.label}
          </ProcessCom>
        );
      },
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrgName',
      width: 220,
    },
    {
      title: t('接收状态'),
      dataIndex: 'batchReceiveStatus',
      width: 140,
      customRender: ({ record }) => {
        const status: keyof typeof StatusType = record.batchReceiveStatus.value;
        return (
          <BMStateTag type={StatusType[status]}>
            <span>{record?.batchReceiveStatus?.label}</span>
          </BMStateTag>
        );
      },
    },
    {
      title: t('运输状态'),
      dataIndex: 'transportStatus',
      width: 140,
      customRender: ({ record }) => {
        if (!record.transportStatus || !record.transportStatus?.length) {
          return '-';
        }
        return (
          <a
            onClick={() => {
              openTransportModal(record);
            }}>
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
      title: t('复核人'),
      dataIndex: 'reviewer',
      width: 100,
    },
    {
      title: t('复核日期'),
      dataIndex: 'reviewerTime',
      width: 170,
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.reviewerTime),
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 240,
      actions: ({ record }, { fetchData }: TableActionType) => [
        {
          label: t('扫码接收'),
          ifShow: hasPermission('210020001000006') && record?.batchReceiveStatus?.value === 'RECEIVING',
          onClick: () => {
            openScan(record);
          },
        },
        {
          label: t('整批接收'),
          ifShow: hasPermission('210020001000001') && record?.batchReceiveStatus?.value === 'RECEIVING',
          onClick: () => {
            openRecept([record]);
          },
        },
        {
          label: t('再次接收'),
          ifShow: hasPermission('210020001000005') && record?.batchReceiveStatus?.value === 'RECEIVED',
          onClick: () => {
            warnModal(t('确定要再次接收该数据吗?'), {
              async onOk() {
                try {
                  await sampleReceiveAgain({ sampleBatchNo: record.batchNo });
                  message.success(t('操作成功'));
                  fetchData();
                  return Promise.resolve();
                } catch (error: any) {
                  message.error(error.message);
                  return Promise.reject();
                }
              },
            });
          },
        },
        {
          label: t('备注'),
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
    },
    {
      title: t('免疫类型'),
      dataIndex: 'immunityType',
      width: 120,
      customRender: ({ record }) => {
        return record?.immunityType?.label ?? '-';
      },
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
      customRender: ({ record }: any) => {
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
          <Button type='link' onClick={() => openInspectItems(record)}>
            {t('查看')}
          </Button>
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
        label: t('标本编号'),
        field: 'orgSampleNo',
        component: 'Input',
      },
      {
        label: t('检验进程'),
        field: 'inspectionProcess',
        component: 'Select',
        componentProps: {
          options: inspectionProcessDict,
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
        label: t('接收日期'),
        field: 'receiveDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
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
