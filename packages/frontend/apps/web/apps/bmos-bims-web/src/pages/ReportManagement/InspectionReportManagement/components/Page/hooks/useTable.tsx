import { usePlasmaStation } from '@/stores/plasmaStation';
import type { FormProps, Key, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { useExpand } from './useExpand';

const { getPlasmaStations } = usePlasmaStation();

export const useTable = (openUnqualifiedModal: any, enterView: any) => {
  const { auditResultDict, effectPriceImmuTypeDict, reportStatusDict } = getDicts();
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  // 展开项的key
  const expandedRowKeys = ref<any>([]);
  // 展开列表的配置
  const expandMap = reactive<any>({});

  const expandChange = async (expandedKeys: Key[]) => {
    expandedRowKeys.value = expandedKeys;
    if (expandedKeys.length === 0) return;
    const newKey = expandedKeys[expandedKeys.length - 1];
    if (!expandMap[newKey]) {
      expandMap[newKey] = useExpand();
    } else {
      expandMap[newKey].fetchData();
    }
  };

  const columnsFirst: TableColumn[] = [
    {
      title: t('检品批号'),
      dataIndex: 'sampleBatchNo',
      width: 140,
      resizable: true,
    },
    {
      title: t('检品数量'),
      dataIndex: 'totalNum',
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('合格数量'),
      dataIndex: 'qualifiedNum',
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('不合格数量'),
      dataIndex: 'unQualifiedNum',
      width: 130,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.unQualifiedNum ? (
          <a onClick={() => openUnqualifiedModal(record)}>{record?.unQualifiedNum}</a>
        ) : (
          '0'
        );
      },
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrg',
      width: 220,
      resizable: true,
    },
    {
      title: t('收检日期'),
      dataIndex: 'acceptanceDate',
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('报告状态'),
      dataIndex: 'reportStatus',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.reportStatus?.name ?? '-'}</span>;
      },
    },
    {
      title: t('报告人'),
      dataIndex: 'reportBy',
      width: 100,
      resizable: true,
    },
    {
      title: t('报告日期'),
      dataIndex: 'reportDate',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('审核状态'),
      dataIndex: 'auditStatus',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.auditStatus?.name ?? '-'}</span>;
      },
    },
    {
      title: t('审核人'),
      dataIndex: 'auditBy',
      width: 100,
      resizable: true,
    },
    {
      title: t('审核日期'),
      dataIndex: 'auditDate',
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 120,
      actions: ({ record }) => [
        // {
        //   label: t('查看详情'),
        //   // ifShow: hasPermission('111020001000002'),
        //   onClick: () => {
        //     // look(record);
        //     enterView(record.id);
        //   },
        // },
        {
          label: t('查看报告'),
          // ifShow: hasPermission('111020001000002'),
          onClick: () => {
            // look(record);
            enterView(record);
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    schemas: [
      {
        label: t('来源单位'),
        field: 'originOrgCode',
        component: 'Select',
        componentProps: {
          request: getPlasmaStations,
        },
      },
      {
        label: t('检品批号'),
        field: 'sampleBatchNo',
        component: 'Input',
      },
      {
        label: t('报告状态'),
        field: 'reportStatus',
        component: 'Select',
        componentProps: {
          options: reportStatusDict,
        },
      },
      {
        label: t('报告日期'),
        field: 'reportDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        label: t('标本编号'),
        field: 'sampleNo',
        component: 'Input',
      },
      {
        label: t('发布日期'),
        field: 'publishDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        label: t('血浆编号'),
        field: 'plasmaNo',
        component: 'Input',
      },
      {
        label: t('审核状态'),
        field: 'auditStatus',
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
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        label: t('献浆者编号'),
        field: 'plasmaDonorNo',
        component: 'Input',
      },
      {
        label: t('免疫类型'),
        field: 'immunityType',
        component: 'Select',
        componentProps: {
          options: [
            ...effectPriceImmuTypeDict,
            {
              value: 7,
              label: t('普通'),
            },
          ],
        },
      },
    ],
    fieldMapToTime: [
      ['publishDate', ['publishDateUp', 'publishDateDown'], 'YYYY-MM-DD'],
      ['reportDate', ['reportDateUp', 'reportDateDown'], 'YYYY-MM-DD'],
      ['auditDate', ['auditDateUp', 'auditDateDown'], 'YYYY-MM-DD'],
    ],
  };

  return {
    pageRef,
    rowData,
    expandMap,
    expandedRowKeys,
    columnsFirst,
    formFirstProps,
    expandChange,
  };
};
