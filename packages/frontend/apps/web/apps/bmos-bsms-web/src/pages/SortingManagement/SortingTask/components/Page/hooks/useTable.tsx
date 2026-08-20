import { usePermissionStore } from '@/stores/permission';
import type { FormProps, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

const { hasPermission } = usePermissionStore();

export const useTable = (enterView: any) => {
  const pageRef = ref<any>(null);

  const columnsFirst: TableColumn[] = [
    {
      title: t('入库批号'),
      dataIndex: 'inWarehouseBatchNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('核查批号'),
      dataIndex: 'checkNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('数量'),
      dataIndex: 'totalNum',
      hideInSearch: true,
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('总重量'),
      dataIndex: 'totalWeight',
      hideInSearch: true,
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('状态'),
      dataIndex: 'status',
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.status?.name;
      },
    },
    {
      title: t('创建日期'),
      dataIndex: 'createTime',
      hideInSearch: true,
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 120,
      actions: ({ record }) => [
        {
          label: t('查看详情'),
          ifShow: hasPermission('170080011000001'),
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
  };

  return {
    pageRef,
    columnsFirst,
    formFirstProps,
  };
};
