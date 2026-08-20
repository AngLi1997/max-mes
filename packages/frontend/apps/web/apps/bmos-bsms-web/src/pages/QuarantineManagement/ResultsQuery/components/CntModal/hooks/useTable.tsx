import type { FormProps, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('血浆编号'),
      dataIndex: 'syncUser',
      width: 170,
      resizable: true,
    },
    {
      title: t('对应编号'),
      dataIndex: 'syncTime',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('血浆箱/托盘号'),
      dataIndex: 'syncCount',
      width: 100,
      resizable: true,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'successCount',
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('血浆状态'),
      dataIndex: 'failCount',
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('对应类型'),
      dataIndex: 'plasmaStationNo',
      width: 100,
      resizable: true,
    },
    {
      title: t('姓名'),
      dataIndex: 'batchNo',
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('编号'),
      dataIndex: 'batchNo',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
  ];

  // 不合格列表字段
  const columnsUnqualified: TableColumn[] = [
    {
      title: t('不合格项目'),
      dataIndex: 'syncUser',
      hideInSearch: true,
      width: 140,
      resizable: true,
    },
  ];

  // 额外字段
  const columnsExtra: TableColumn[] = [
    {
      title: t('免疫类型'),
      dataIndex: 'syncTime',
      hideInSearch: true,
      width: 140,
      resizable: true,
    },
    {
      title: t('效价'),
      dataIndex: 'syncCount',
      hideInSearch: true,
      width: 140,
      resizable: true,
    },
  ];
  const formProps: Partial<FormProps> = {
    showAdvancedButton: false,
    baseColProps: {
      span: 8,
    },
    actionColOptions: {
      span: 24,
    },
    labelWidth: 100,
    labelAlign: 'left',
  };

  return {
    tableRef,
    columns,
    columnsUnqualified,
    columnsExtra,
    formProps,
  };
};
