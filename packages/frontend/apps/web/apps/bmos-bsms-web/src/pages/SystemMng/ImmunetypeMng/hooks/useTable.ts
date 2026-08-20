import { usePermissionStore } from '@/stores/permission';
import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

const { hasPermission } = usePermissionStore();

export const useTable = (open: any) => {
  const pageRef = ref<any>(null);
  const columnsFirst: TableColumn[] = [
    {
      title: t('免疫类型'),
      dataIndex: 'immunityName',
      width: 170,
      resizable: true,
    },
    {
      title: t('类型描述'),
      dataIndex: 'immunityDes',
      width: 100,
      resizable: true,
    },
    {
      title: t('标识颜色'),
      dataIndex: 'colour',
      width: 170,
      resizable: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 100,
      actions: ({ record }) => [
        {
          label: t('编辑'),
          ifShow: hasPermission('170110005000001'),
          onClick: () => {
            // look(record);
            open(record);
          },
        },
      ],
    },
  ];

  return {
    pageRef,
    columnsFirst,
  };
};
