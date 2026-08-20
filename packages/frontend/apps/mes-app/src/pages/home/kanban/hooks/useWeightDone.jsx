import { reqWeighDashboardTicketCompletion } from '@/api';
import { t } from '@/utils/useBmosI18n.js';
import { ref } from 'vue';
import WdTag from 'wot-design-uni/components/wd-tag/wd-tag.vue';

export function useWeightDone() {
  const statusMap = new Map([
    [1, { type: 'primary' }],
    [2, { type: 'warning' }],
    [3, { type: 'success' }],
  ]);

  const weightDoneTableRef = ref(null);
  const weightDoneCurrent = ref(7);
  const tableProps = {
    pagination: {
      pageSize: 10,
      bottomOutRefresh: true,
    },
    intervalRequest: true,
    showNoData: true,
    noDataShowTable: false,
    noDataText: t('暂无工单'),
    dataRequest: reqWeighDashboardTicketCompletion,
    rowKey: 'ticketNo',
    tableColProps: [
      {
        prop: 'ticketNo',
        label: t('工单编号'),
        width: 100,
      },
      {
        prop: 'materialName',
        label: t('物料名称'),
        width: 100,
      },
      {
        prop: 'materialCode',
        label: t('物料编码'),
        width: 100,
      },
      {
        prop: 'weighCentreName',
        label: t('称量中心'),
        width: 100,
      },
      {
        prop: 'requiredTotalQuantity',
        label: t('需求总量'),
        width: 100,
        customRender: ({ row }) => {
          return row.requiredTotalQuantity + row.unit;
        },
      },
      {
        prop: 'completedWeight',
        label: t('完成总量'),
        width: 80,
        customRender: ({ row }) => {
          return row.completedWeight + row.unit;
        },
      },
      {
        prop: 'completionRate',
        label: t('完成率'),
        width: 60,
      },
      {
        prop: 'planExecuteDate',
        label: t('计划执行时间'),
        width: 100,
      },
      {
        prop: 'status',
        label: t('状态'),
        width: 60,
        customRender: ({ row }) => {
          return <WdTag plain type={statusMap.get(row.status.value)?.type}>{row.status?.label}</WdTag>;
        },
      },
    ],
  };

  return {
    weightDoneTableProps: tableProps,
    weightDoneCurrent,
    weightDoneTableRef,
  };
}
