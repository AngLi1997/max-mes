import type { FormProps, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';


export const useTable = () => {
  const tableData = ref<any>([]);
  const columns: TableColumn[] = [
    {
      title: t('实验包编码'),
      dataIndex: 'code',
      // resizable: true,
      hideInSearch: true,
      // width: 190,
      formItemProps: {
        defaultValue: '',
      },
    },
    {
      title: t('实验包名称'),
      dataIndex: 'name',
      // resizable: true,
      hideInSearch: true,
      // width: 190,
      formItemProps: {
        defaultValue: '',
      },
    },
  ];

  const innerColumns: TableColumn[] = [
    {
      title: t('分析项名称'),
      dataIndex: 'name',
      // resizable: true,
      hideInSearch: true,
      // width: 160,
    },
    {
      title: t('标准规定'),
      dataIndex: 'standard',
      // resizable: true,
      hideInSearch: true,
      // width: 190,
      ellipsis: true,
    },
    {
      title: t('结果'),
      dataIndex: 'result',
      // resizable: true,
      hideInSearch: true,
      // width: 100,
    },
    {
      title: t('记录人'),
      dataIndex: 'operatorName',
      // resizable: true,
      hideInSearch: true,
      // width: 120,
    },
    {
      title: t('记录时间'),
      dataIndex: 'operateTime',
      // resizable: true,
      hideInSearch: true,
      // width: 170,
    },
    {
      title: t('操作'),
      fixed: 'right',
      key: 'ACTION',
      width: 80,
      actions: (params, action) => [
        {
          label: t('日志'),
          ifShow: true,
          onClick: (e: any) => {
          },
        },
      ],
    },
  ];

  const formProps = reactive<Partial<FormProps>>({
    showAdvancedButton: false,
    actionColOptions: {
      span: 12,
    }
    // fieldMapToTime: [
    //   ['selectTime', ['startTime', 'endTime'], 'YYYY-MM-DD'],
    // ],
  });

  return {
    columns,
    formProps,
    tableData,
    innerColumns
  };
};