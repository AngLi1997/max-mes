<!-- 数据看板 -->
<template>
  <div class="data-board-container">
    <div class="card-data-container">
      <template v-for="item in cardDataList" :key="item.title">
        <Card v-bind="item" />
      </template>
    </div>
    <div class="axis-chart">
      <div class="header">
        <BMTableTitle :title="t('检验趋势')" />
        <Select v-model:value="dateSelectValue" :options="dateSelectOptions" style="width: 100px" />
      </div>
      <Echarts :options="inspectionTrendsOptions" :height="285" />
    </div>
    <div class="footer-chart">
      <div class="material-inventory-statistics">
        <BMTableTitle :title="t('物料库在库统计')" />
        <Echarts :options="materialStatisticsOptions" :height="180" />
      </div>
      <div class="inspection-progress">
        <BMTableTitle :title="t('检验进度')" />
        <div class="chart">
          <Progress
            type="dashboard"
            :percent="inspectProgress"
            :size="200"
            strokeColor="#FFCE43"
            :success="{
              strokeColor: '#FFCE43',
            }"
            :strokeWidth="20"
            strokeLinecap="butt"
            trailColor="#59BF78">
            <template #format="percent">
              <span class="dashboard-label">{{ percent }}%</span>
              <br />
              <span class="dashboard-label">{{ t('检验进度') }}</span>
            </template>
          </Progress>
        </div>
      </div>
      <div class="check-statistics">
        <div class="header">
          <BMTableTitle :title="t('请验统计')" />
          <Select v-model:value="checkDateSelectValue" :options="checkDateSelectOptions" style="width: 100px" />
        </div>
        <Echarts :options="checkStatisticsOptions" :height="240" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import Card from './components/Card.vue';
  import Echarts from '@/components/Echarts/index.vue';
  import { BMTableTitle, Recordable } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { useInspectionTrends, useMaterialStatistics, useCheckStatistics } from './hooks';
  import { useWebSocket } from '@vueuse/core';
  import { ChartTypeEnum } from '@/types';

  defineOptions({
    name: 'DataBoard',
  });

  const chartData = ref<Recordable>({});
  const { data } = useWebSocket(`ws://${location.hostname}/api/centralized-lims/ws/statistics/refresh`, {
    autoReconnect: {
      retries: 3,
      delay: 1000,
    },
    heartbeat: {
      message: 'ping',
      interval: 1000 * 60 * 4,
    },
  });
  watch(data, () => {
    try {
      if (data.value) {
        chartData.value = JSON.parse(data.value)?.data;
      }
    } catch (error) {
      console.error(error);
    }
  });
  const cardDataList = computed(() => {
    return [
      {
        icon: 'BatchSendInspection',
        title: `${t('当日送检')} (${t('批次')})`,
        data: chartData.value[ChartTypeEnum.INSPECT_SUBMISSION_BATCH_DAY]?.result?.[0]?.value,
        percent: chartData.value[ChartTypeEnum.INSPECT_SUBMISSION_BATCH_DAY]?.result?.[0]?.ratio,
      },
      {
        icon: 'BatchReceiveDay',
        title: `${t('当日接收')} (${t('批次')})`,
        data: chartData.value[ChartTypeEnum.RECEIVED_BATCH_DAY]?.result?.[0]?.value,
        percent: chartData.value[ChartTypeEnum.RECEIVED_BATCH_DAY]?.result?.[0]?.ratio,
      },
      {
        icon: 'CopiesInspectionDay',
        title: `${t('当日待检')} (${t('份')})`,
        data: chartData.value[ChartTypeEnum.TO_SINGLE_BATCH_DAY]?.result?.[0]?.value,
        percent: chartData.value[ChartTypeEnum.TO_SINGLE_BATCH_DAY]?.result?.[0]?.ratio,
      },
      {
        icon: 'BatchBacklogReceive',
        title: `${t('待接收')} (${t('批次')})`,
        data: chartData.value[ChartTypeEnum.TO_RECEIVE_BATCH_DAY]?.result?.[0]?.value,
        percent: chartData.value[ChartTypeEnum.TO_RECEIVE_BATCH_DAY]?.result?.[0]?.ratio,
      },
      {
        icon: 'batchDataSign',
        title: `${t('数据待签发')} (${t('批次')})`,
        data: chartData.value[ChartTypeEnum.TO_PUBLISH_DATA_BATCH_DAY]?.result?.[0]?.value,
        percent: chartData.value[ChartTypeEnum.TO_PUBLISH_DATA_BATCH_DAY]?.result?.[0]?.ratio,
      },
      {
        icon: 'BatchDataSignReport',
        title: `${t('报告待签发')} (${t('批次')})`,
        data: chartData.value[ChartTypeEnum.TO_PUBLISH_REPORT_BATCH_DAY]?.result?.[0]?.value,
        percent: chartData.value[ChartTypeEnum.TO_PUBLISH_REPORT_BATCH_DAY]?.result?.[0]?.ratio,
      },
      {
        icon: 'PieceRequestInspection',
        title: `${t('当日送检')} (${t('份')})`,
        data: chartData.value[ChartTypeEnum.INSPECT_SUBMISSION_SINGLE_DAY]?.result?.[0]?.value,
        percent: chartData.value[ChartTypeEnum.INSPECT_SUBMISSION_SINGLE_DAY]?.result?.[0]?.ratio,
      },
      {
        icon: 'PieceReceiveDay',
        title: `${t('当日接收')} (${t('份')})`,
        data: chartData.value[ChartTypeEnum.RECEIVED_SINGLE_DAY]?.result?.[0]?.value,
        percent: chartData.value[ChartTypeEnum.RECEIVED_SINGLE_DAY]?.result?.[0]?.ratio,
      },
      {
        icon: 'PieceInspectionDay',
        title: `${t('当日已检')} (${t('份')})`,
        data: chartData.value[ChartTypeEnum.INSPECTED_SINGLE_DAY]?.result?.[0]?.value,
        percent: chartData.value[ChartTypeEnum.INSPECTED_SINGLE_DAY]?.result?.[0]?.ratio,
      },
      {
        icon: 'PieceBacklogReceive',
        title: `${t('待接收')} (${t('份')})`,
        data: chartData.value[ChartTypeEnum.TO_RECEIVE_SINGLE_DAY]?.result?.[0]?.value,
        percent: chartData.value[ChartTypeEnum.TO_RECEIVE_SINGLE_DAY]?.result?.[0]?.ratio,
      },
      {
        icon: 'PieceBacklogSign',
        title: `${t('数据待签发')} (${t('份')})`,
        data: chartData.value[ChartTypeEnum.TO_PUBLISH_DATA_SINGLE_DAY]?.result?.[0]?.value,
        percent: chartData.value[ChartTypeEnum.TO_PUBLISH_DATA_SINGLE_DAY]?.result?.[0]?.ratio,
      },
      {
        icon: 'PieceBacklogSignReport',
        title: `${t('业务待审核')}`,
        data: chartData.value[ChartTypeEnum.TO_AUDIT_SINGLE_DAY]?.result?.[0]?.value,
        percent: chartData.value[ChartTypeEnum.TO_AUDIT_SINGLE_DAY]?.result?.[0]?.ratio,
      },
    ];
  });

  const inspectProgress = computed(() => {
    // 四舍五入
    return Math.round(
      ((chartData.value[ChartTypeEnum.INSPECTED_SINGLE_DAY]?.result?.[0]?.value ?? 0) /
        (chartData.value[ChartTypeEnum.TO_SINGLE_BATCH_DAY]?.result?.[0]?.value ?? 0)) *
        100,
    );
  });

  const { dateSelectValue, dateSelectOptions, inspectionTrendsOptions } = useInspectionTrends({
    chartData,
  });

  const { materialStatisticsOptions } = useMaterialStatistics({
    chartData,
  });

  const { checkDateSelectValue, checkDateSelectOptions, checkStatisticsOptions } = useCheckStatistics({
    chartData,
  });
</script>

<style scoped lang="less">
  .data-board-container {
    display: flex;
    height: 100%;
    overflow-y: scroll;
    flex-direction: column;
    gap: 12px;
  }
  .card-data-container {
    // 每行6个，间距 为12px
    display: grid;
    grid-template-columns: repeat(6, 1fr);
    gap: 12px;
  }
  .axis-chart {
    background-color: #fff;
    .header {
      padding: 16px 24px 0;
      display: flex;
      justify-content: space-between;
    }
  }
  .footer-chart {
    display: flex;
    gap: 12px;
    flex: 1;
    overflow: hidden;
    .material-inventory-statistics,
    .inspection-progress,
    .check-statistics {
      width: 293px;
      height: 100%;
      min-height: 264px;
      background-color: #fff;
      display: flex;
      flex-direction: column;
      .chart {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: center;
      }
    }
    .material-inventory-statistics,
    .inspection-progress {
      .bmos-table-title {
        margin: 16px 24px 0;
      }
    }
    .check-statistics {
      flex: 1;
      .header {
        padding: 16px 24px 0;
        display: flex;
        justify-content: space-between;
      }
    }
  }
  .dashboard-label {
    color: #59bf78;
    font-size: 20px;
  }
</style>
