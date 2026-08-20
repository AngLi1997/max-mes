import { UseCategoryEnum } from '@/types';
import { type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const { getDateFormat } = useConfig();
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('发出量'),
      dataIndex: 'quantity',
      width: 100,
      resizable: true,
    },
    {
      title: t('结存量'),
      dataIndex: 'inventory',
      width: 100,
      resizable: true,
    },
    {
      title: t('使用类别'),
      dataIndex: 'useType',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        const color = UseCategoryEnum[record?.useType?.value as keyof typeof UseCategoryEnum] ?? '#000000';
        return <span style={{ color }}>{record?.useType?.label ?? '-'}</span>;
      },
    },
    {
      title: t('申请人'),
      dataIndex: 'applicantBy',
      width: 100,
    },
    {
      title: t('申请日期'),
      dataIndex: 'applicantTime',
      width: 170,
      resizable: true,
      customRender: ({ record }) => getDateFormat(record.applicantTime),
    },
    {
      title: t('审核人'),
      dataIndex: 'auditBy',
      width: 100,
      resizable: true,
    },
    {
      title: t('审核日期'),
      dataIndex: 'auditTime',
      width: 170,
      resizable: true,
      customRender: ({ record }) => getDateFormat(record.auditTime),
    },
    {
      title: t('出库人'),
      dataIndex: 'warehouseOperator',
      width: 100,
      resizable: true,
    },
    {
      title: t('出库日期'),
      dataIndex: 'outTime',
      width: 170,
      resizable: true,
      customRender: ({ record }) => getDateFormat(record.outTime),
    },
  ];

  return {
    tableRef,
    columns,
  };
};
