import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = (print: Function) => {
  const { getDateFormat } = useConfig();
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('报告人'),
      dataIndex: 'createBy',
      width: 100,
    },
    {
      title: t('报告日期'),
      dataIndex: 'createTime',
      width: 170,
      customRender: ({ record }) => getDateFormat(record.createTime),
    },
    {
      title: t('审核人'),
      dataIndex: 'reviewer',
      width: 100,
    },
    {
      title: t('审核日期'),
      dataIndex: 'updateTime',
      width: 170,
      customRender: ({ record }) => getDateFormat(record.updateTime),
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 80,
      actions: ({ record }) => [
        {
          label: t('查看'),
          onClick: async () => {
            await print(record);
          },
        },
      ],
    },
  ];

  return {
    tableRef,
    columns,
  };
};
