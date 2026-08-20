import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const { publishStatusDict, qualifiedStatusDict } = getDicts();
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});
  const columnsFirst: TableColumn[] = [
    {
      title: t('检验项目'),
      dataIndex: 'checkItem',
      width: 140,
      resizable: true,
    },
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      hideInSearch: true,
      width: 180,
      resizable: true,
    },
    {
      title: t('标本批号'),
      dataIndex: 'sampleBatchNo',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('检验结果'),
      dataIndex: 'checkResult',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.checkResult?.name;
      },
    },
    {
      title: t('检验人'),
      dataIndex: 'inspectionBy',
      width: 100,
      resizable: true,
    },
    {
      title: t('检验日期'),
      dataIndex: 'inspectionDate',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('发布状态'),
      dataIndex: 'publishStatus',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.publishStatus?.name;
      },
    },
    {
      title: t('复核人'),
      dataIndex: 'publishBy',
      width: 100,
      resizable: true,
    },
    {
      title: t('复核日期'),
      dataIndex: 'publishDate',
      width: 160,
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
        label: t('检验日期'),
        field: 'inspectionDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        label: t('复核日期'),
        field: 'publishDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
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
        label: t('发布状态'),
        field: 'publishStatus',
        component: 'Select',
        componentProps: {
          options: publishStatusDict,
        },
      },
    ],
    fieldMapToTime: [
      ['inspectionDate', ['inspectionDateUp', 'inspectionDateDown'], 'YYYY-MM-DD'],
      ['publishDate', ['publishDateUp', 'publishDateDown'], 'YYYY-MM-DD'],
    ],
  };

  return {
    pageRef,
    rowData,
    columnsFirst,
    formFirstProps,
  };
};
