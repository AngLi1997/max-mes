import { postQueryEnergyGetReport } from '@/services/modules/energyMonitoring';
import { PickerMode } from 'ant-design-vue/es/vc-picker/interface';
import dayjs from 'dayjs';
import { EChartsOption } from 'echarts';
import { DateMap } from '../type';

export const useHeatingChart = () => {
  const date = ref<string>(dayjs().format('YYYY-MM-DD'));
  const picker = ref<PickerMode>('date');
  const changePicker = (value: PickerMode) => {
    picker.value = value;
    setData();
  };
  const chartData = reactive<{
    xAxis: string[];
    series: string[];
  }>({
    xAxis: [],
    series: [],
  });
  const options = computed<EChartsOption>(() => {
    return {
      grid: {
        top: '5%', // 控制上侧内边距
        bottom: '8%', // 控制下侧内边距
      },
      tooltip: {
        trigger: 'axis', // 鼠标悬浮触发
        axisPointer: {
          type: 'line', // 指示器类型：线
        },
        valueFormatter: (value: any) => {
          return `${value} MJ`;
        },
      },
      xAxis: {
        type: 'category',
        boundaryGap: false, // 起始点对齐
        data: chartData.xAxis,
        axisLine: {
          lineStyle: {
            color: 'rgba(185, 232, 255, 0.50)', // X轴颜色
          },
        },
        axisLabel: {
          color: '#ffffff', // X轴标签颜色
        },
      },
      yAxis: {
        type: 'value',
        axisLine: {
          lineStyle: {
            color: '#86A7BF', // Y轴颜色
          },
        },
        axisLabel: {
          color: '#86A7BF', // Y轴标签颜色
          formatter: '{value}', // 格式化
        },
        splitLine: {
          lineStyle: {
            color: '#3b4c5c', // 网格线颜色
            type: 'dashed', // 网格线类型
          },
        },
      },
      series: [
        {
          type: 'line',
          data: chartData.series,
          smooth: true, // 平滑曲线
          lineStyle: {
            color: '#0BE5E5', // 折线颜色
            width: 2, // 折线宽度
          },
          itemStyle: {
            color: '#00c8ff', // 点颜色
            // 不显示点
            opacity: 0,
          },
        },
      ],
    };
  });
  const chartRef = ref<any>();
  const height = ref<number>(300);

  const isInit = ref<boolean>(false);

  const initChart = () => {
    height.value = chartRef.value.offsetHeight;
    setData();
    if (!isInit.value) {
      isInit.value = true;
    }
  };
  const setData = async () => {
    try {
      const { data } = await postQueryEnergyGetReport({
        reportType: DateMap.get(picker.value),
        time: date.value,
      });
      chartData.xAxis = [];
      chartData.series = [];
      data.forEach((item: any) => {
        chartData.xAxis.push(item.time);
        chartData.series.push(item.value);
      });
    } catch (error) {
      // 错误处理
    }
  };
  return {
    heatingChartDate: date,
    heatingChartPicker: picker,
    changeHeatingChartPicker: changePicker,
    heatingChartOptions: options,
    setHeatingChartInit: initChart,
    heatingChartHeight: height,
    heatingChartRef: chartRef,
    setHeatingChartData: setData,
    heatingChartIsInit: isInit,
  };
};
