import { usePermissionStore } from '@/stores';
import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { TableParams } from '../types';

export const useMaterialTable = ({ firstRowData }: TableParams) => {
  const { hasPermission } = usePermissionStore();
  const { getDateFormat } = useConfig();
  const editModalOpen = ref<boolean>(false);
  const editFun = (record: any) => {
    firstRowData.value = record;
    editModalOpen.value = true;
  };
  const columnsFirst: TableColumn[] = [
    {
      title: t('项目名'),
      dataIndex: 'description',
      fixed: 'left',
      width: 100,
    },
    {
      title: t('参数'),
      dataIndex: 'enumsValue',
      width: 100,
    },
    {
      title: t('单位'),
      dataIndex: 'unit',
      width: 100,
    },
    {
      title: t('操作人'),
      dataIndex: 'updateBy',
      width: 100,
    },
    {
      title: t('更新日期'),
      dataIndex: 'updateTime',
      width: 100,
      customRender: ({ record }) => {
        return getDateFormat(record.updateTime);
      },
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 80,
      actions: ({ record }: any) => [
        {
          label: t('编辑'),
          ifShow: hasPermission('210080002000001'),
          onClick: () => {
            editFun(record);
          },
        },
      ],
    },
  ];

  return {
    materialColumns: columnsFirst,
    materialEditModalOpen: editModalOpen,
  };
};
