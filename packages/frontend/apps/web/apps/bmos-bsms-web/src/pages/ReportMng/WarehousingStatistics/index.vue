<!-- 入库统计 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :titles="[t('入库统计')]"
    :showToolBars="[true]"
    :formProps="[formFirstProps]"
    :paginations="[false]"
    :scrolls="[{ x: 2000, y: 500 }]"
    :requests="[loadData as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #summary0>
      <TableSummary fixed>
        <TableSummaryRow>
          <TableSummaryCell>
            {{ t('合计') }}
          </TableSummaryCell>
          <!-- <TableSummaryCell>
            <TypographyText type="danger">{{ '1' }}</TypographyText>
          </TableSummaryCell> -->
          <TableSummaryCell v-for="(i, index) in 24" :key="i">
            <TypographyText>
              {{ summaryData?.detailList?.[Math.floor(index / 2)]?.[index % 2 === 0 ? 'totalNum' : 'totalWeight'] }}
            </TypographyText>
          </TableSummaryCell>
        </TableSummaryRow>
      </TableSummary>
    </template>
  </BMPageComponent>
</template>

<script setup lang="ts">
  import { getWarehouseStatisticsInList } from '@/services';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { TableSummary, TableSummaryRow, TableSummaryCell, TypographyText } from 'ant-design-vue';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'WarehousingStatistics',
  });

  const { pageRef, columnsFirst, formFirstProps } = useTable();

  const summaryData = ref<any>({});

  const loadData = async (params: any) => {
    const { year, type } = params;
    const { data } = await getWarehouseStatisticsInList({ year, type });
    // 取data最后一项
    summaryData.value = data.pop();
    return {
      data: {
        list: data,
        total: data.length,
      },
    };
  };
</script>

<style lang="less" scoped>
  :deep(.bsms-table-summary) {
    background: #fafafa;
    .bsms-table-cell {
      &:first-child {
        position: sticky;
        left: 0;
        background: #fafafa;
        z-index: 9999;
        &::after {
          position: absolute;
          top: 0;
          right: 0;
          bottom: -1px;
          width: 30px;
          transform: translateX(100%);
          transition: box-shadow 0.3s;
          content: '';
          pointer-events: none;
          box-shadow: inset 10px 0 8px -8px rgba(5, 22, 38, 0.12);
        }
      }
    }
  }
</style>
