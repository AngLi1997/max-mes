import { usePermissionStore } from '@/stores';
import { RoundingRuleEnum } from '@/types';
import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { TableParams } from '../types';

export const useRoundingParameterTable = ({ firstRowData }: TableParams) => {
  const { hasPermission } = usePermissionStore();
  const { getDateFormat } = useConfig();
  const { decimalUnitDict } = getDicts();
  const editModalOpen = ref<boolean>(false);

  const columnsFirst: TableColumn[] = [
    {
      title: t('项目名'),
      dataIndex: 'description',
      fixed: 'left',
      width: 100,
    },
    {
      title: t('修约规则'),
      dataIndex: 'label',
      width: 100,
      customRender: ({ record }) => {
        return (
          <span>
            {record.label === 'RR001'
              ? `${RoundingRuleEnum[record.label as keyof typeof RoundingRuleEnum]}`
              : `${RoundingRuleEnum[record.label as keyof typeof RoundingRuleEnum]}, ${decimalUnitDict.find(
                  (item: any) => item.value === record.value,
                )?.label}`}
          </span>
        );
      },
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
    roundingParameterColumns: columnsFirst,
    roundingParameterEditModalOpen: editModalOpen,
  };
};
