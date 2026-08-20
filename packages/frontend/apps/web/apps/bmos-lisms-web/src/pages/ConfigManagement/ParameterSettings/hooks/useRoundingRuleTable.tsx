import { usePermissionStore } from '@/stores';
import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { TableParams } from '../types';

export const useRoundingRuleTable = ({ firstRowData }: TableParams) => {
  const { hasPermission } = usePermissionStore();
  const { getDateFormat } = useConfig();
  const editModalOpen = ref<boolean>(false);
  const columnsFirst: TableColumn[] = [
    {
      title: t('修约规则名称'),
      dataIndex: 'label',
      fixed: 'left',
      width: 100,
    },
    {
      title: t('修约编码'),
      dataIndex: 'enumsValue',
      width: 100,
    },
    {
      title: t('修约描述'),
      dataIndex: 'description',
      width: 160,
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
            firstRowData.value = record;
            editModalOpen.value = true;
          },
        },
      ],
    },
  ];

  return {
    roundingRuleColumns: columnsFirst,
    roundingRuleEditModalOpen: editModalOpen,
  };
};
