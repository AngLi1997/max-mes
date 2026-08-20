import { RemarkDetail } from '@/components/RemarkModal';
import { getConfigInspectList } from '@/services';
import { usePlasmaStation } from '@/stores';
import { InspectionProjectEnum, InspectionResultEnum, StatusType } from '@/types';
import { BMStateTag, type FormProps, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { BMIcons } from '@bmos/icons';
import { isEmpty } from '@bmos/utils';
import { Space } from 'ant-design-vue';

export const useTable = () => {
  const { getPlasmaStations } = usePlasmaStation();
  const { InspectionProjectDict, InspectionResultDict, auditResultDict, sampleCategoryDict, testArticleStatusDict } =
    getDicts();
  const { getDateFormat } = useConfig();
  const pageRef = ref<any>();
  const updateTableData = () => pageRef.value?.fetchData(0);
  // 第一个table 行数据
  const firstRowData = ref<any>({});

  // 不合格弹窗
  const unqualifiedModalOpen = ref<boolean>(false);

  const remarkModalOpen = ref<boolean>(false);
  const remarkDetails = ref<RemarkDetail[]>([]);
  const columnsFirst: TableColumn[] = [
    {
      title: t('基本信息'),
      children: [
        {
          title: t('标本批号'),
          dataIndex: 'sampleBatchNo',
          width: 160,
        },
        {
          title: t('标本编号'),
          dataIndex: 'orgSampleNo',
          width: 180,
        },
        {
          title: t('标本类型'),
          dataIndex: ['sampleType', 'label'],
          width: 150,
        },
        {
          title: t('标本分类'),
          dataIndex: ['sampleClassification', 'label'],
          width: 150,
        },
        {
          title: t('采浆日期'),
          dataIndex: 'donorTime',
          width: 170,
          sorter: true,
          customRender: ({ record }) => {
            return getDateFormat(record.donorTime);
          },
        },
        {
          title: t('来源单位'),
          dataIndex: 'originOrgCode',
          width: 140,
        },
        {
          title: t('检品状态'),
          dataIndex: ['testArticleStatus', 'label'],
          width: 120,
        },
        {
          title: t('检验结论'),
          dataIndex: 'inspectResult',
          width: 120,
          customRender: ({ record }: any) => {
            return (
              <Space>
                {record.inspectResult?.label ?? '-'}
                {record.inspectResult?.value === 'UNQUALIFIED' ? (
                  <BMIcons
                    icon='Waring'
                    style={{
                      color: '#FF5E3D',
                      fontSize: '16px',
                    }}
                    onClick={() => {
                      firstRowData.value = record;
                      unqualifiedModalOpen.value = true;
                    }}
                  />
                ) : (
                  ''
                )}
              </Space>
            );
          },
        },
      ],
    },
    {
      title: t('献浆者信息'),
      dataIndex: 'donorInfo',
      children: [
        {
          title: t('献浆者编号'),
          dataIndex: 'donorNo',
          width: 170,
        },
        {
          title: t('姓名'),
          dataIndex: 'donorName',
          width: 100,
        },
        {
          title: t('性别'),
          dataIndex: 'donorSex',
          width: 100,
        },
        {
          title: t('血型'),
          dataIndex: 'donorBloodType',
          width: 100,
        },
      ],
    },
    ...InspectionProjectDict.map((item: any) => {
      return {
        title: item?.title,
        dataIndex: 'inspectItem' + item.value,
        children: [
          {
            title: item?.label,
            dataIndex: item.value,
            width: 150,
            sorter: true,
            customRender: ({ record }: any) => {
              if (isEmpty(record[`${item.value}`]?.code)) {
                return '-';
              }
              switch (record[`${item.value}`].code) {
                case InspectionProjectEnum.HBsAg:
                case InspectionProjectEnum.AntiHCV:
                case InspectionProjectEnum.HIVAgAb:
                case InspectionProjectEnum.AntiTP:
                  if (record[`${item.value}`]?.result?.value) {
                    return record[`${item.value}`]?.result?.value === InspectionResultEnum.UNQUALIFIED
                      ? t('阳性')
                      : t('阴性');
                  } else {
                    return '-';
                  }

                default:
                  return record[`${item.value}`]?.result?.label ?? '-';
              }
            },
          },
          {
            title: t('检验人'),
            dataIndex: [item.value, 'inspector'],
            width: 150,
          },
          {
            title: t('复核人'),
            dataIndex: [item.value, 'checkBy'],
            width: 150,
          },
          {
            title: t('试剂批号'),
            dataIndex: [item.value, 'reagentBatchNo'],
            width: 150,
          },
          {
            title: t('质控品批号'),
            dataIndex: [item.value, 'qcBatchNo'],
            width: 150,
          },
        ],
      };
    }),
    {
      title: t('发布审核信息'),
      children: [
        {
          title: t('发布状态'),
          dataIndex: ['publishStatus', 'label'],
          width: 120,
        },
        {
          title: t('总发布人'),
          dataIndex: 'publisher',
          width: 100,
        },
        {
          title: t('总发布日期'),
          dataIndex: 'publishTime',
          width: 180,
          sorter: true,
          customRender: ({ record }) => {
            return getDateFormat(record.publishTime);
          },
        },
        {
          title: t('审核状态'),
          dataIndex: ['auditStatus', 'label'],
          width: 100,
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
          width: 100,
        },
        {
          title: t('审核日期'),
          dataIndex: 'auditTime',
          width: 170,
          sorter: true,
          customRender: ({ record }) => {
            return getDateFormat(record.auditTime);
          },
        },
      ],
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 100,
      actions: ({ record }: any) => [
        {
          label: t('备注'),
          onClick: () => {
            remarkDetails.value = [
              {
                field: 'publishRemark',
                value: record.publishRemark,
                label: t('发布备注'),
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
  const formFirstProps: Ref<Partial<FormProps>> = ref({
    labelWidth: 120,
    fieldMapToTime: [
      ['donorTime', ['donorDateStart', 'donorDateEnd'], 'YYYY-MM-DD'],
      ['auditDate', ['auditDateStart', 'auditDateEnd'], 'YYYY-MM-DD'],
      ['publishDate', ['publishDateStart', 'publishDateEnd'], 'YYYY-MM-DD'],
    ],
    schemas: [
      {
        field: 'sampleBatchNo',
        label: t('标本批号'),
        component: 'Input',
      },
      {
        field: 'orgSampleNo',
        label: t('标本编号'),
        component: 'Input',
      },
      {
        field: 'originOrgCode',
        label: t('来源单位'),
        component: 'Select',
        componentProps: {
          request: async () => {
            return await getPlasmaStations();
          },
        },
      },
      {
        field: 'sampleClassification',
        label: t('标本分类'),
        component: 'Select',
        componentProps: {
          options: sampleCategoryDict,
        },
      },
      {
        field: 'donorTime',
        label: t('采浆日期'),
        component: 'RangePicker',
        componentProps: {
          showTime: false,
        },
      },
      {
        field: 'testArticleStatus',
        label: t('检品状态'),
        component: 'Select',
        componentProps: {
          options: testArticleStatusDict,
        },
      },
      {
        field: 'inspectResult',
        label: t('检验结论'),
        component: 'Select',
        componentProps: {
          options: InspectionResultDict,
        },
      },
      {
        field: 'publishDate',
        label: t('发布日期'),
        component: 'RangePicker',
        componentProps: {
          showTime: false,
        },
      },
      {
        field: 'unqualifiedItem',
        label: t('不合格项目'),
        component: 'Select',
        componentProps: {
          fieldNames: { label: 'itemName', value: 'itemNo' },
          request: async () => {
            try {
              const { data } = await getConfigInspectList();
              return data;
            } catch (_) {
              return [];
            }
          },
        },
      },
      {
        label: t('献浆者姓名'),
        field: 'donorName',
        component: 'Input',
      },
      {
        label: t('献浆者编号'),
        field: 'donorNo',
        component: 'Input',
      },
      {
        field: 'auditResult',
        label: t('审核结果'),
        component: 'Select',
        componentProps: {
          options: auditResultDict,
        },
      },
      {
        field: 'auditDate',
        label: t('审核日期'),
        component: 'RangePicker',
        componentProps: {
          showTime: false,
        },
      },
    ],
  });
  return {
    columnsFirst,
    firstRowData,
    pageRef,
    updateTableData,
    formFirstProps,
    unqualifiedModalOpen,
    remarkDetails,
    remarkModalOpen,
  };
};
