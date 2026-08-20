import { StatusType } from '@/types';
import { BMStateTag, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const { getDateFormat } = useConfig();
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('标本批号'),
      dataIndex: 'sampleBatchNo',
      width: 160,
    },
    {
      title: t('签发人'),
      dataIndex: 'reportBy',
      width: 100,
    },
    {
      title: t('签发结果'),
      dataIndex: 'result',
      width: 120,
      customRender: ({ record }) => {
        const status: keyof typeof StatusType = record?.result?.value;
        return status ? <BMStateTag type={StatusType[status]}>{record?.result?.label}</BMStateTag> : '-';
      },
    },
    {
      title: t('签发日期'),
      dataIndex: 'reportTime',
      width: 170,
      customRender: ({ record }) => getDateFormat(record.reportTime),
    },
  ];

  return {
    tableRef,
    columns,
  };
};
