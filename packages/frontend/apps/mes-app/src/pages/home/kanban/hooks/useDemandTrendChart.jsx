import { reqWeighDashboardRequirementTrend } from '@/api';
import { px2rpx } from '@/utils';
import { t } from '@/utils/useBmosI18n.js';
import * as echarts from 'echarts';
import { computed, nextTick, ref, watch } from 'vue';

export function useDemandTrendChart() {
  const demandTrendEcharts = ref(null);
  const demandTrendCurrent = ref(7);

  const chartData = ref({
    xAxis: [],
    data: [],
  });

  const timer = ref(null);
  const myChart = ref(null);

  const clearTimer = () => {
    if (timer.value) {
      clearInterval(timer.value);
      timer.value = null;
    }
  };

  const getDemandTrendData = async () => {
    try {
      const { data } = await reqWeighDashboardRequirementTrend({
        recentDays: demandTrendCurrent.value,
      });

      if (data?.length) {
        chartData.value.xAxis = data.map((item) => {
          const date = item.date;
          if (!date)
            return '';
          // 从日期字符串中截取月份和日期部分
          const parts = date.split('-');
          if (parts.length >= 3) {
            return `${parts[1]}-${parts[2]}`;
          }
          return date;
        });
        chartData.value.data = data.map(item => item.count);
      }
      else {
        chartData.value.data = [];
      }
      myChart.value && myChart.value.setOption(
        {
          title: {
            text: '',
          },
          tooltip: {
            trigger: 'axis',
          },
          xAxis: {
            type: 'category',
            data: chartData.value.xAxis,
            axisLabel: {
              color: '#999',
              show: true,
              fontSize: px2rpx(14),
            },
            axisLine: {
              show: false,
              lineStyle: {
                width: px2rpx(2),
              },
            },
            axisTick: {
              show: false,
            },
          },
          yAxis: {
            type: 'value',
            min: 0,
            axisLabel: {
              color: '#999',
              fontSize: px2rpx(14),
            },
            axisLine: {
              show: false,
              lineStyle: {
                width: px2rpx(2),
              },
            },
            axisTick: {
              show: false,
            },
            splitLine: {
              lineStyle: {
                color: '#f5f5f5',
              },
            },
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true,
          },
          series: [
            {
              name: t('数量'),
              type: 'line',
              data: chartData.value.data,
              lineStyle: {
                color: '#4A90E2',
                width: px2rpx(2),
              },
              itemStyle: {
                color: '#4A90E2',
              },
              symbol: 'circle',
              symbolSize: px2rpx(4),
              smooth: false,
            },
          ],
        },
      );
    }
    catch (_error) {
      chartData.value.data = [];
      clearTimer();
    }
  };

  const initChart = async () => {
    await nextTick();
    if (!demandTrendEcharts.value)
      return;
    myChart.value = await demandTrendEcharts.value.init(echarts);
    myChart.value.setOption(
      {
        title: {
          text: '',
        },
        tooltip: {
          trigger: 'axis',
        },
        xAxis: {
          type: 'category',
          data: [],
          axisLabel: {
            color: '#999',
            show: true,
          },
          axisLine: {
            show: false,
          },
          axisTick: {
            show: false,
          },
        },
        yAxis: {
          type: 'value',
          min: 0,
          axisLabel: {
            color: '#999',
          },
          axisLine: {
            show: false,
          },
          axisTick: {
            show: false,
          },
          splitLine: {
            lineStyle: {
              color: '#f5f5f5',
            },
          },
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true,
        },
        series: [
          {
            name: t('数量'),
            type: 'line',
            data: [],
            lineStyle: {
              color: '#4A90E2',
              width: 2,
            },
            itemStyle: {
              color: '#4A90E2',
            },
            symbol: 'circle',
            symbolSize: 4,
            smooth: false,
          },
        ],
      },
    );
    await getDemandTrendData();
    clearTimer();
    timer.value = setInterval(() => {
      getDemandTrendData();
    }, 60000);
    myChart.value.resize(); // 调整图表大小以适应容器
  };

  watch(() => demandTrendCurrent.value, () => {
    getDemandTrendData();
  });

  const showDemandTrendEmpty = computed(() => {
    return !chartData.value.data.length;
  });

  return {
    demandTrendEcharts,
    demandTrendEchartsInit: initChart,
    demandTrendCurrent,
    showDemandTrendEmpty,
    clearDemandTrendTimer: clearTimer,
  };
}
