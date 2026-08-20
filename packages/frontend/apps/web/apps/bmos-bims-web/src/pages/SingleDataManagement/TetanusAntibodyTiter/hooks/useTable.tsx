import type { FormProps, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const { publishStatusDict } = getDicts();
  const pageRef = ref<any>(null);

  const columnsFirst: TableColumn[] = [
    {
      title: t('检验项目'),
      dataIndex: 'checkItem',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return record?.checkItem?.name;
      },
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
      title: t('配板编号'),
      dataIndex: 'equipmentNo',
      hideInSearch: true,
      width: 160,
      resizable: true,
    },
    {
      title: t('孔位号'),
      dataIndex: 'holeNo',
      hideInSearch: true,
      width: 90,
      resizable: true,
    },
    {
      title: t('检验结果'),
      dataIndex: 'checkResultValue',
      width: 120,
      sorter: true,
      resizable: true,
      // customRender: ({ record }) => {
      //   return record?.checkResult?.name;
      // },
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
      sorter: true,
      width: 150,
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
      sorter: true,
      width: 150,
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
        label: t('配板编号'),
        field: 'equipmentNo',
        component: 'Input',
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
        label: t('检验日期'),
        field: 'checkDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
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
    ],
    fieldMapToTime: [
      ['checkDate', ['checkDateUp', 'checkDateDown'], 'YYYY-MM-DD'],
      ['reCheckDate', ['reCheckDateUp', 'reCheckDateDown'], 'YYYY-MM-DD'],
    ],
  };

  return {
    pageRef,
    columnsFirst,
    formFirstProps,
  };
};
