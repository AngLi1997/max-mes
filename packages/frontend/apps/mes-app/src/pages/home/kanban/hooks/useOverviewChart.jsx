import { reqWeighDashboardTicketOverview } from '@/api';
import { px2rpx } from '@/utils';
import { t } from '@/utils/useBmosI18n.js';
import * as echarts from 'echarts';
import { computed, nextTick, ref, watch } from 'vue';

export function useOverviewChart() {
  const overviewEcharts = ref(null);

  const overviewSegmentedOptions = [
    { label: t('今天'), value: 1 },
    { label: t('近7天'), value: 7 },
    { label: t('近15天'), value: 15 },
  ];

  const overviewCurrent = ref(1);
  const seriesData = ref([{ value: 1, name: t('已完成'), itemStyle: { color: '#58C99A' } }]);

  const timer = ref(null);
  const myChart = ref(null);

  const clearTimer = () => {
    if (timer.value) {
      clearInterval(timer.value);
      timer.value = null;
    }
  };

  const getOverviewData = async () => {
    try {
      const { data } = await reqWeighDashboardTicketOverview({
        recentDays: overviewCurrent.value,
      });

      // 新增判断：只有一个数据项或者有一项占比100%时
      let hasSingleItem = true;
      if (data && (data.completedCount || data.releasedCount)) {
        const { completedCount, releasedCount } = data;
        seriesData.value = [
          { value: completedCount, name: t('已完成'), itemStyle: { color: '#58C99A' } },
          { value: releasedCount, name: t('已下发'), itemStyle: { color: '#4A89F8' } },
        ];
        if (completedCount === 0 || releasedCount === 0) {
          hasSingleItem = false;
        }
      }
      else {
        seriesData.value = [];
      }
      const legendDataWithRichText = seriesData.value.map((item) => {
        return {
          name: item.name,
          textStyle: {
            rich: {
              name: {
                fontSize: px2rpx(16),
                color: '#6C6E73',
                padding: [0, 15, 0, 6],
              },
              value: {
                fontSize: px2rpx(22),
                color: item.itemStyle.color,
              },
            },
          },
        };
      });

      myChart.value && myChart.value.setOption(
        {
          // 提示框组件
          tooltip: {
            trigger: 'item', // 触发类型：数据项图形触发
            formatter: '{b} : {c} ({d}%)', // 提示框内容格式：名称 : 数据值 (百分比%)
          },
          // 图例组件
          legend: {
            orient: 'vertical', // 图例列表的布局朝向：垂直
            right: '5%', // 图例组件离容器右侧的距离 (可根据实际效果调整)
            top: 'center', // 图例组件离容器上侧的距离：居中
            itemWidth: px2rpx(12), // 图例标记的图形宽度 (根据图片估算)
            itemHeight: px2rpx(12), // 图例标记的图形高度 (根据图片估算)
            icon: 'rect', // 图例项的 icon：设置为方形
            data: legendDataWithRichText,
            // 自定义图例项的文本格式
            formatter(name) {
              const item = seriesData.value.find(dataItem => dataItem.name === name);
              if (item) {
                // 使用 rich text 实现 "名称 值" 的格式，并控制间距和样式
                return `{name|${name}}{value|${item.value}}`;
              }
              return name;
            },
            itemGap: 18, // 图例项之间的间距 (根据图片估算)
          },
          // 系列列表
          series: [
            {
              name: t('工单状态'), // 系列名称，用于tooltip的显示 (可自定义)
              type: 'pie', // 类型：饼图
              radius: ['40%', '58%'], // 环形图的关键：[内半径, 外半径]
              center: ['25%', '50%'], // 饼图的中心（圆心）坐标，调整以适应右侧图例
              avoidLabelOverlap: false, // 是否启用防止标签重叠策略
              // 饼图图形上的文本标签
              label: {
                show: false, // 不显示扇区上的标签
              },
              // 标签的视觉引导线样式
              labelLine: {
                show: false, // 不显示视觉引导线
              },
              data: seriesData.value,
              padAngle: hasSingleItem ? 2 : 0,
            },
          ],
        },
      );
    }
    catch (_error) {
      seriesData.value = [];
      clearTimer();
    }
  };

  const initChart = async () => {
    await nextTick();
    if (!overviewEcharts.value)
      return;
    myChart.value = await overviewEcharts.value.init(echarts);
    await getOverviewData();
    clearTimer();
    timer.value = setInterval(() => {
      getOverviewData();
    }, 60000);
    myChart.value.resize(); // 调整图表大小以适应容器
  };

  watch(() => overviewCurrent.value, () => {
    getOverviewData();
  });

  const showEmpty = computed(() => {
    return !seriesData.value.length;
  });

  return {
    overviewCurrent,
    overviewEcharts,
    overviewEchartsInit: initChart,
    overviewSegmentedOptions,
    showOverviewEmpty: showEmpty,
    clearOverViewTimer: clearTimer,
  };
}
