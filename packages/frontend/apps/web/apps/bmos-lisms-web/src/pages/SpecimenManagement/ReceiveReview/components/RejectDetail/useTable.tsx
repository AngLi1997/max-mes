import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const { getDateFormat } = useConfig();
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('标本编号'),
      dataIndex: 'orgSampleNo',
      width: 220,
      resizable: true,
    },
    {
      title: t('标本批号'),
      dataIndex: 'batchNo',
      width: 160,
      resizable: true,
    },
    {
      title: t('拒收原因'),
      dataIndex: 'refuseReasonName',
      width: 160,
      resizable: true,
    },
    {
      title: t('是否补样'),
      dataIndex: 'needSupplement',
      width: 160,
      resizable: true,
      customRender: ({ record }) => {
        return record?.needSupplement?.label ?? '-';
      },
    },
    {
      title: t('拒收人'),
      dataIndex: 'applicant',
      width: 100,
      resizable: true,
    },
    {
      title: t('拒收日期'),
      dataIndex: 'applicantTime',
      width: 170,
      resizable: true,
      customRender: ({ record }) => getDateFormat(record.applicantTime),
    },
    {
      title: t('审核人'),
      dataIndex: 'reviewer',
      width: 100,
      resizable: true,
    },
    {
      title: t('审核日期'),
      dataIndex: 'reviewerTime',
      width: 170,
      resizable: true,
      customRender: ({ record }) => getDateFormat(record.reviewerTime),
    },
  ];

  return {
    tableRef,
    columns,
  };
};
