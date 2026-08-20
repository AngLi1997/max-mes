import { reqWeighDashboardTicketTrend } from '@/api';
import { px2rpx } from '@/utils';
import { t } from '@/utils/useBmosI18n.js';
import * as echarts from 'echarts';
import { computed, nextTick, ref, watch } from 'vue';

export function useTrendChart() {
  const trendEcharts = ref(null);
  const trendCurrent = ref(7);
  const timer = ref(null);
  const myChart = ref(null);
  const chartData = ref({
    xAxis: [],
    data: [],
  });
  const clearTimer = () => {
    if (timer.value) {
      clearInterval(timer.value);
      timer.value = null;
    }
  };

  const getTrendData = async () => {
    try {
      const { data } = await reqWeighDashboardTicketTrend({
        recentDays: trendCurrent.value,
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
            data: chartData.value.xAxis || [0],
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
              data: chartData.value.data || [0],
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
    if (!trendEcharts.value)
      return;
    myChart.value = await trendEcharts.value.init(echarts);
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
          data: [0],
          axisLabel: {
            color: '#999',
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
    await getTrendData();
    clearTimer();
    timer.value = setInterval(() => {
      getTrendData();
    }, 60000);
    myChart.value.resize(); // 调整图表大小以适应容器
  };

  watch(() => trendCurrent.value, () => {
    getTrendData();
  });
  const showTrendEmpty = computed(() => {
    return !chartData.value.data.length;
  });

  return {
    trendEcharts,
    trendEchartsInit: initChart,
    trendCurrent,
    showTrendEmpty,
    clearTrendTimer: clearTimer,
  };
}
