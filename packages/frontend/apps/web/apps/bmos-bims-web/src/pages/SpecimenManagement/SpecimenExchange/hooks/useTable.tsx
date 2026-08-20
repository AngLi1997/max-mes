import { useDict } from '@/stores/dictStore';
import { usePlasmaStation } from '@/stores/plasmaStation';
import type { FormProps, Key, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { useExpand } from './useExpand';

const { getPlasmaStations } = usePlasmaStation();
const { getImmuniTypeDict } = useDict();

export const useTable = (_enterView: any) => {
  const { receiveStatusDict } = getDicts();
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
      await expandMap[newKey].fetchData();
    }
  };

  const columnsFirst: TableColumn[] = [
    {
      title: t('检品批号'),
      dataIndex: 'inspectionBatchNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('检品数量'),
      dataIndex: 'inspectionNumber',
      width: 100,
      sorter: true,
      resizable: true,
    },
    {
      title: t('请验人'),
      dataIndex: 'inspectionBy',
      width: 100,
      resizable: true,
    },
    {
      title: t('请验日期'),
      dataIndex: 'inspectionDate',
      width: 150,
      sorter: true,
      resizable: true,
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrg',
      width: 220,
      resizable: true,
    },
    {
      title: t('接收状态'),
      dataIndex: 'receiveStatus',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.receiveStatus?.name;
      },
    },
    {
      title: t('收检人'),
      dataIndex: 'receiveBy',
      width: 100,
      resizable: true,
    },
    {
      title: t('收检日期'),
      dataIndex: 'receiveDate',
      width: 100,
      sorter: true,
      resizable: true,
    },
    // {
    //   title: t('操作'),
    //   key: 'ACTION',
    //   fixed: 'right',
    //   width: 120,
    //   actions: ({ record }) => [
    //     {
    //       label: t('查看详情'),
    //       // ifShow: hasPermission('111020001000002'),
    //       onClick: () => {
    //         // look(record);
    //         enterView(record);
    //       },
    //     },
    //   ],
    // },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    schemas: [
      {
        label: t('检品批号'),
        field: 'inspectionBatchNo',
        component: 'Input',
      },
      {
        label: t('接收状态'),
        field: 'receiveStatus',
        component: 'Select',
        componentProps: {
          options: receiveStatusDict,
        },
      },
      {
        label: t('来源单位'),
        field: 'originOrg',
        component: 'Select',
        componentProps: {
          request: getPlasmaStations,
        },
      },
      {
        label: t('标本编号'),
        field: 'sampleNo',
        component: 'Input',
      },
      {
        label: t('献浆者编号'),
        field: 'plasmaDonorNo',
        component: 'Input',
      },
      {
        label: t('请验日期'),
        field: 'inspectionDate',
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
        label: t('免疫类型'),
        field: 'immunityType',
        component: 'Select',
        componentProps: {
          request: getImmuniTypeDict,
        },
      },
      {
        label: t('收检日期'),
        field: 'receiveDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
    ],
    fieldMapToTime: [
      ['inspectionDate', ['inspectionDateUp', 'inspectionDateDown'], 'YYYY-MM-DD'],
      ['receiveDate', ['receiveDateUp', 'receiveDateDown'], 'YYYY-MM-DD'],
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
