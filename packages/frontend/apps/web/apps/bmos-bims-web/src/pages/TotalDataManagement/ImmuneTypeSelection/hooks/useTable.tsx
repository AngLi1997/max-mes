import type { FormProps, Key, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { useExpand } from './useExpand';

export const useTable = () => {
  const { effectPriceImmuTypeDict } = getDicts();
  const pageRef = ref<any>(null);

  const fetchData = async (index: number = 0, params?: any) => {
    pageRef.value.fetchData(index, params);
  };

  // 展开项的key`
  const expandedRowKeys = ref<any>([]);
  // 展开列表的配置
  const expandMap = reactive<any>({});

  const expandChange = async (expandedKeys: Key[]) => {
    expandedRowKeys.value = expandedKeys;
    if (expandedKeys.length === 0) return;
    const newKey = expandedKeys[expandedKeys.length - 1];
    if (!expandMap[newKey]) {
      expandMap[newKey] = useExpand(fetchData);
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
      customRender: ({ record }) => {
        return record?.checkItem?.name;
      },
      formItemProps: {
        order: 7,
        component: 'Select',
        componentProps: {
          options: effectPriceImmuTypeDict,
        },
      },
    },
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      width: 190,
      resizable: true,
      formItemProps: {
        order: 2,
      },
    },
    {
      title: t('标本批号'),
      dataIndex: 'sampleBatchNo',
      width: 120,
      resizable: true,
      formItemProps: {
        order: 1,
      },
    },
    {
      title: t('检验结果'),
      dataIndex: 'checkResultValue',
      width: 120,
      hideInSearch: true,
      sorter: true,
      resizable: true,
    },
    {
      title: t('试剂批号'),
      dataIndex: 'reagentBatchNo',
      width: 190,
      resizable: true,
      formItemProps: {
        order: 3,
      },
    },
    {
      title: t('质控品批号'),
      dataIndex: 'qualityControllerBatchNo',
      width: 190,
      resizable: true,
      formItemProps: {
        order: 4,
      },
    },
    {
      title: t('检验人'),
      dataIndex: 'checkBy',
      width: 100,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('检验日期'),
      dataIndex: 'checkDate',
      width: 170,
      sorter: true,
      resizable: true,
      formItemProps: {
        order: 5,
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
    },
    {
      title: t('复核人'),
      dataIndex: 'reCheckBy',
      width: 100,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('复核日期'),
      dataIndex: 'reCheckDate',
      width: 170,
      sorter: true,
      resizable: true,
      formItemProps: {
        order: 6,
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    fieldMapToTime: [
      ['checkDate', ['checkDateUp', 'checkDateDown'], 'YYYY-MM-DD'],
      ['reCheckDate', ['reCheckDateUp', 'reCheckDateDown'], 'YYYY-MM-DD'],
    ],
  };

  return {
    pageRef,
    expandMap,
    expandedRowKeys,
    expandChange,
    columnsFirst,
    formFirstProps,
  };
};
