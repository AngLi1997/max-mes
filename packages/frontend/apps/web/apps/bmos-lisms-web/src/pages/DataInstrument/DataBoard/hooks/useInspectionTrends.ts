import { ChartTypeEnum } from '@/types';
import { EChartsOption } from 'echarts';
import { ChartHookParams, DateType } from '../types';

export function useInspectionTrends({ chartData }: ChartHookParams) {
  const dateSelectValue = ref<`${DateType}`>(DateType.Month);
  const dateSelectOptions = [
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
    chartData.value[ChartTypeEnum.INSPECTED_SINGLE_WEEK]?.result?.forEach((item: { label: string; value: number }) => {
      week.xAxis.push(item.label);
      week.series.push(item.value);
    });
    const month: { xAxis: string[]; series: number[] } = {
      xAxis: [],
      series: [],
    };
    chartData.value[ChartTypeEnum.INSPECTED_SINGLE_MONTH]?.result.forEach((item: { label: string; value: number }) => {
      month.xAxis.push(item.label);
      month.series.push(item.value);
    });
    const year: { xAxis: string[]; series: number[] } = {
      xAxis: [],
      series: [],
    };
    chartData.value[ChartTypeEnum.INSPECTED_SINGLE_YEAR]?.result.forEach((item: { label: string; value: number }) => {
      year.xAxis.push(item.label);
      year.series.push(item.value);
    });
    return {
      week,
      month,
      year,
    };
  });
  const inspectionTrendsOptions = computed<EChartsOption>(() => {
    return {
      tooltip: {
        trigger: 'axis',
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true,
      },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: axisData.value[dateSelectValue.value].xAxis,
      },
      yAxis: {
        type: 'value',
      },
      series: [
        {
          data: axisData.value[dateSelectValue.value].series,
          type: 'line',
          smooth: false,
          areaStyle: {
            color: 'rgba(72, 118, 255, 0.2)',
          },
          name: `${t('检验')}(${t('件数')})`,
          lineStyle: {
            color: '#4876FF',
            width: 2,
          },
          itemStyle: {
            color: '#4876FF',
            borderColor: '#fff',
            borderWidth: 2,
          },
        },
      ],
    };
  });

  return {
    dateSelectValue,
    dateSelectOptions,
    inspectionTrendsOptions,
  };
}
