import type { FormProps, Key, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { useExpand } from './useExpand';

export const useTable = () => {
  const { checkNumDict, publishStatusDict, qualifiedStatusDict } = getDicts();
  const pageRef = ref<any>(null);

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
      title: t('检验项目'),
      dataIndex: 'checkItem',
      width: 120,
      resizable: true,
    },
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      hideInSearch: true,
      width: 190,
      resizable: true,
    },
    {
      title: t('标本批号'),
      dataIndex: 'sampleBatchNo',
      hideInSearch: true,
      width: 120,
      resizable: true,
    },
    {
      title: t('检验结果'),
      dataIndex: 'checkResultValue',
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('检验次数'),
      dataIndex: 'checkNum',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return record?.checkNum?.name;
      },
    },
    {
      title: t('试剂批号'),
      dataIndex: 'reagentBatchNo',
      width: 190,
      resizable: true,
    },
    {
      title: t('质控品批号'),
      dataIndex: 'qualityControllerBatchNo',
      width: 190,
      resizable: true,
    },
    {
      title: t('检验人'),
      dataIndex: 'checkBy',
      width: 100,
      resizable: true,
    },
    {
      title: t('检验日期'),
      dataIndex: 'checkDate',
      width: 150,
      sorter: true,
      resizable: true,
    },
    {
      title: t('发布状态'),
      dataIndex: 'publishStatus',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return record?.publishStatus?.name;
      },
    },
    {
      title: t('复核人'),
      dataIndex: 'reCheckBy',
      width: 100,
      resizable: true,
    },
    {
      title: t('复核日期'),
      dataIndex: 'reCheckDate',
      width: 150,
      sorter: true,
      resizable: true,
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    schemas: [
      {
        label: t('标本批号'),
        field: 'sampleBatchNo',
        component: 'Input',
      },
      {
        label: t('标本编号'),
        field: 'sampleNo',
        component: 'Input',
      },
      {
        label: t('检验结果'),
        field: 'checkResult',
        component: 'Select',
        componentProps: {
          options: qualifiedStatusDict,
        },
      },
      {
        label: t('检验次数'),
        field: 'checkNum',
        component: 'Select',
        componentProps: {
          options: checkNumDict,
        },
      },
      {
        label: t('试剂批号'),
        field: 'reagentBatchNo',
        component: 'Input',
      },
      {
        label: t('质控品批号'),
        field: 'qualityControllerBatchNo',
        component: 'Input',
      },
      {
        label: t('发布状态'),
        field: 'publishStatus',
        component: 'Select',
        componentProps: {
          options: publishStatusDict,
        },
      },
      {
        label: t('复核日期'),
        field: 'reCheckDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        label: t('检验日期'),
        field: 'checkDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
    ],
    fieldMapToTime: [
      ['checkDate', ['checkDateUp', 'checkDateDown'], 'YYYY-MM-DD'],
      ['reCheckDate', ['reCheckDateUp', 'reCheckDateDown'], 'YYYY-MM-DD'],
    ],
  };

  const rowExpandable = (record: any) => {
    return !!record?.checkNum?.value;
  };

  return {
    pageRef,
    expandMap,
    expandedRowKeys,
    rowExpandable,
    expandChange,
    columnsFirst,
    formFirstProps,
  };
};
