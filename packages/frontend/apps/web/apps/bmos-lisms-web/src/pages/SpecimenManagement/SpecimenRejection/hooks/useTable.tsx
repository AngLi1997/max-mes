// import { usePermissionStore } from '@/stores/permission';
import { RemarkDetail } from '@/components/RemarkModal';
import { getSampleRejectRemark } from '@/services';
import { useConfig, usePermissionStore, usePlasmaStation } from '@/stores';
import { SpecimenTypeEnum, StatusType } from '@/types';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { BMStateTag, type FormProps, type Recordable, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = (openReject: (record: Recordable) => void, openRejectDetail: Function) => {
  const { getPlasmaStations } = usePlasmaStation();
  const { getConfigEnumsValueByParamId, getDateFormat } = useConfig();
  const { hasPermission } = usePermissionStore();
  const { sampleCategoryDict, testArticleStatusDict } = getDicts();
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
      width: 100,
    },
    {
      title: t('血型'),
      dataIndex: 'bloodType',
      width: 100,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 170,
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.slurryDate),
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrgName',
      width: 160,
    },
    {
      title: t('检品状态'),
      dataIndex: 'testArticleStatus',
      width: 100,
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
        return record?.receiveStatus?.label ?? '-';
      },
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 120,
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
        {
          label: t('拒收'),
          ifShow: hasPermission('210020003000001') && record?.receiveStatus?.value === 'RECEIVING',
          onClick: () => {
            // look(record);
            openReject([record]);
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    fieldMapToTime: [['slurryDate', ['slurryDateStart', 'slurryDateEnd'], 'YYYY-MM-DD']],
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
        label: t('检品状态'),
        field: 'testArticleStatus',
        component: 'Select',
        componentProps: {
          options: testArticleStatusDict,
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
