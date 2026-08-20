import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = (deleteFile: Function) => {
  const tableRef = ref<any>(null);

  const tableData = ref<any>([]);

  const columns: TableColumn[] = [
    {
      title: t('文件'),
      dataIndex: 'fileName',
      width: 160,
      resizable: true,
    },
    {
      title: t('大小'),
      dataIndex: 'fileSize',
      width: 100,
      resizable: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 80,
      actions: ({ record }) => [
        {
          label: t('删除'),
          // ifShow: hasPermission('170020001000003') && record?.receiveStatus?.code == 0,
          danger: true,
          onClick: () => {
            deleteFile(record);
          },
        },
      ],
    },
  ];

  return {
    tableRef,
    columns,
    tableData,
  };
};
