import { ChartTypeEnum } from '@/types';
import { EChartsOption } from 'echarts';
import { ChartHookParams, DateType } from '../types';
export function useCheckStatistics({ chartData }: ChartHookParams) {
  const checkDateSelectValue = ref<`${DateType}`>(DateType.Month);
  const checkDateSelectOptions = [
    { label: t('周'), value: DateType.Week },
    { label: t('月'), value: DateType.Month },
    { label: t('年'), value: DateType.Year },
  ];
  const axisData = computed<{
    week: { xAxis: string[]; series: number[] };
    month: { xAxis: string[]; series: number[] };
    year: { xAxis: string[]; series: number[] };
  }>(() => {
    const week: { xAxis: string[]; series: number[] } = {
      xAxis: [],
      series: [],
    };
    chartData.value[ChartTypeEnum.INSPECT_SUBMISSION_BATCH_WEEK]?.result?.forEach(
      (item: { label: string; value: number }) => {
        week.xAxis.push(item.label);
        week.series.push(item.value);
      },
    );
    const month: { xAxis: string[]; series: number[] } = {
      xAxis: [],
      series: [],
    };
    chartData.value[ChartTypeEnum.INSPECT_SUBMISSION_BATCH_MONTH]?.result?.forEach(
      (item: { label: string; value: number }) => {
        month.xAxis.push(item.label);
        month.series.push(item.value);
      },
    );
    const year: { xAxis: string[]; series: number[] } = {
      xAxis: [],
      series: [],
    };
    chartData.value[ChartTypeEnum.INSPECT_SUBMISSION_BATCH_YEAR]?.result?.forEach(
      (item: { label: string; value: number }) => {
        year.xAxis.push(item.label);
        year.series.push(item.value);
      },
    );
    return {
      week,
      month,
      year,
    };
  });
  const checkStatisticsOptions = computed<EChartsOption>(() => {
    return {
      xAxis: {
        type: 'category',
        data: axisData.value[checkDateSelectValue.value].xAxis,
        axisLine: {
          lineStyle: {
            color: '#ccc',
          },
        },
        axisLabel: {
          color: '#666',
        },
      },
      yAxis: {
        type: 'value',
        axisLine: { show: false },
        splitLine: {
          lineStyle: {
            color: '#f0f0f0',
          },
        },
        axisLabel: {
          color: '#666',
        },
      },
      tooltip: {
        trigger: 'axis',
        show: true,
      },
      series: [
        {
          data: axisData.value[checkDateSelectValue.value].series?.map((item: any) => {
            return {
              value: item,
              itemStyle: {
                borderRadius: [20, 20, 0, 0],
              },
            };
          }),
          name: `${t('送检')}(${t('批次')})`,
          type: 'bar',
          barWidth: '30%',
          itemStyle: {
            color: '#4285F4',
          },
        },
      ],
      grid: {
        left: '10%',
        right: '10%',
        bottom: '15%',
        top: '10%',
        containLabel: true,
      },
    };
  });

  return {
    checkDateSelectValue,
    checkDateSelectOptions,
    checkStatisticsOptions,
  };
}
