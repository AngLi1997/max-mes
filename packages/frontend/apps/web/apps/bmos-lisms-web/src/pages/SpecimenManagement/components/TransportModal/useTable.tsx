import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const { getDateFormat } = useConfig();
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('标本批号'),
      dataIndex: 'batchNo',
      width: 160,
    },
    {
      title: t('运输状态'),
      dataIndex: ['transportStatus', 'label'],
      width: 120,
    },
    {
      title: t('运输温度'),
      dataIndex: 'transportTemperature',
      width: 120,
    },
    {
      title: t('起运时间'),
      dataIndex: 'transportTime',
      width: 170,
      customRender: ({ record }) => getDateFormat(record.transportTime),
    },
    {
      title: t('运抵时间'),
      dataIndex: 'transportArrivalTime',
      width: 170,
      customRender: ({ record }) => getDateFormat(record.transportArrivalTime),
    },
    {
      title: t('运送时间'),
      dataIndex: 'transportSpendTime',
      width: 120,
    },
    {
      title: t('车牌号'),
      dataIndex: 'transportVehiclePlate',
      width: 140,
    },
    {
      title: t('送检人'),
      dataIndex: 'transferBy',
      width: 100,
    },
    {
      title: t('送检日期'),
      dataIndex: 'transferDate',
      width: 170,
      customRender: ({ record }) => getDateFormat(record.transferDate),
    },
    {
      title: t('装箱人'),
      dataIndex: 'packBy',
      width: 100,
    },
    {
      title: t('装箱人电话'),
      dataIndex: 'packPhone',
      width: 140,
    },
    {
      title: t('承运人'),
      dataIndex: 'carrierBy',
      width: 100,
    },
    {
      title: t('承运人电话'),
      dataIndex: 'carrierPhone',
      width: 140,
    },
  ];

  return {
    tableRef,
    columns,
  };
};
