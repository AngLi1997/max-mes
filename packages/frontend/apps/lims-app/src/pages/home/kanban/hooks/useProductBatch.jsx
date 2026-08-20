import { reqWeighDashboardProductionCompletion } from '@/api';
import { t } from '@/utils/useBmosI18n.js';
import { ref } from 'vue';
import WdTag from 'wot-design-uni/components/wd-tag/wd-tag.vue';

export function useProductBatch() {
  const statusMap = new Map([
    [1, { type: 'primary' }],
    [2, { type: 'warning' }],
    [3, { type: 'success' }],
  ]);

  const productBatchTableRef = ref(null);
  const productBatchCurrent = ref(7);
  const tableProps = {
    pagination: {
      pageSize: 10,
      bottomOutRefresh: true,
    },
    intervalRequest: true,
    showNoData: true,
    noDataShowTable: false,
    noDataText: t('暂无内容'),
    dataRequest: reqWeighDashboardProductionCompletion,
    rowKey: 'id',
    tableColProps: [
      {
        prop: 'batchNo',
        label: t('生产批号'),
        width: 100,
      },
      {
        prop: 'productName',
        label: t('产品名称'),
        width: 100,
      },
      {
        prop: 'productMergeCode',
        label: t('产品编码'),
        width: 100,
      },
      {
        prop: 'weighCentreName',
        label: t('称量中心'),
        width: 100,
      },
      {
        prop: 'planProductionDate',
        label: t('计划生产时间'),
        width: 100,
      },
      {
        prop: 'completionRate',
        label: t('需求完成率'),
        width: 100,
      },
      {
        prop: 'status',
        label: t('状态'),
        width: 100,
        customRender: ({ row }) => {
          return <WdTag plain type={statusMap.get(row.status.value)?.type}>{row.status?.label}</WdTag>;
        },
      },
    ],
  };

  return {
    productBatchTableProps: tableProps,
    productBatchCurrent,
    productBatchTableRef,
  };
}
