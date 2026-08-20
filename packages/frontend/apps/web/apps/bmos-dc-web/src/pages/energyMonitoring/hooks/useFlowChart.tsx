import { EChartsOption } from 'echarts';

export const useFlowChart = () => {
  const options = computed<EChartsOption>(() => {
    return {
      tooltip: {
        trigger: 'item',
        formatter: '{b}',
      },
      series: [
        {
          left: '10%',
          type: 'sankey',
          layout: 'none',
          nodeWidth: 40,
          data: [
            {
              name: '华兰工业园区',
              itemStyle: { color: '#036CFE' },
              label: { fontSize: 16, fontWeight: 400, position: 'left' },
            },
            { name: '6#楼', itemStyle: { color: '#00D68A' } },
            { name: '10#楼', itemStyle: { color: '#0BE5E5' } },
            { name: '10#楼1层', itemStyle: { color: '#B2DFFF' } },
            { name: '10#楼2层', itemStyle: { color: '#B2DFFF' } },
            { name: '10#楼3层', itemStyle: { color: '#B2DFFF' } },
            { name: '10#楼4层', itemStyle: { color: '#B2DFFF' } },
            { name: '10#楼5层', itemStyle: { color: '#B2DFFF' } },
            { name: '原液1#生产线', itemStyle: { color: '#3288FF' } },
            { name: '原液2#生产线', itemStyle: { color: '#3288FF' } },
            { name: '原液3#生产线', itemStyle: { color: '#3288FF' } },
            { name: '仓装2#生产线', itemStyle: { color: '#3288FF' } },
            { name: '仓装3#生产线', itemStyle: { color: '#3288FF' } },
            { name: '6#楼1层', itemStyle: { color: '#B2DFFF' } },
            { name: '仓储2号柜', itemStyle: { color: '#3288FF' } },
            { name: '仓储1号柜', itemStyle: { color: '#3288FF' } },
          ],
          links: [
            { source: '华兰工业园区', target: '6#楼', value: 4 },
            { source: '华兰工业园区', target: '10#楼', value: 10 },
            { source: '6#楼', target: '6#楼1层', value: 4 },
            { source: '6#楼1层', target: '仓储2号柜', value: 2 },
            { source: '6#楼1层', target: '仓储1号柜', value: 2 },
            { source: '10#楼', target: '10#楼1层', value: 2 },
            { source: '10#楼', target: '10#楼2层', value: 2 },
            { source: '10#楼', target: '10#楼3层', value: 2 },
            { source: '10#楼', target: '10#楼4层', value: 2 },
            { source: '10#楼', target: '10#楼5层', value: 2 },
            { source: '10#楼1层', target: '原液1#生产线', value: 2 },
            { source: '10#楼2层', target: '原液2#生产线', value: 2 },
            { source: '10#楼3层', target: '仓装2#生产线', value: 2 },
            { source: '10#楼4层', target: '仓装3#生产线', value: 2 },
            { source: '10#楼5层', target: '原液3#生产线', value: 2 },
          ],
          lineStyle: {
            color: 'gradient',
            opacity: 0.6,
          },
          itemStyle: {
            borderWidth: 1,
            borderColor: '#aaa',
          },
          label: {
            color: '#ffffff', // 设置文字颜色
            fontSize: 12,
          },
        },
      ],
    };
  });
  const chartRef = ref<any>();
  const height = ref<number>(800);
  const isInit = ref<boolean>(false);

  const initChart = () => {
    height.value = chartRef.value.offsetHeight;
    if (!isInit.value) {
      isInit.value = true;
    }
  };
  return {
    flowChartOptions: options,
    flowChartInit: initChart,
    flowChartHeight: height,
    flowChartRef: chartRef,
    flowChartIsInit: isInit,
  };
};
