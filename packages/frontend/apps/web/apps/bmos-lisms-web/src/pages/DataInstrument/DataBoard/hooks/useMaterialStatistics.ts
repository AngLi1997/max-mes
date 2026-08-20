import { ChartTypeEnum, MaterialTypeEnum } from '@/types';
import { EChartsOption } from 'echarts';
import { ChartHookParams } from '../types';
export function useMaterialStatistics({ chartData }: ChartHookParams) {
  const materialDta = computed(() => {
    const result: { core: number; normal: number } = { core: 0, normal: 0 };
    chartData.value[ChartTypeEnum.WAREHOUSE_STATISTICS]?.list?.forEach(
      (item: { materialType: MaterialTypeEnum; num: number }) => {
        if (item.materialType === MaterialTypeEnum.CORE_MATERIAL) {
          result.core = item.num;
        } else if (item.materialType === MaterialTypeEnum.NORMAL_MATERIAL) {
          result.normal = item.num;
        }
      },
    );
    return result;
  });
  const materialStatisticsOptions = computed<EChartsOption>(() => {
    return {
      tooltip: {
        trigger: 'item', // 鼠标悬浮时显示提示信息
        confine: true,
      },
      legend: {
        show: false, // 隐藏图例
      },
      series: [
        {
          name: t('物料类型'), // 数据名称
          type: 'pie',
          radius: ['40%', '70%'], // 设置内外半径，形成环形
          avoidLabelOverlap: false,
          data: [
            {
              value: materialDta.value.core,
              name: t('关键物料'),
              itemStyle: { color: '#5470C6' },
              label: {
                show: true,
                formatter: ({ name, value }) => {
                  return `{labelColor|${name}}\n{valueColor|${value}}`;
                },
                rich: {
                  labelColor: {
                    fontSize: 14,
                    color: '#5470C6',
                  },
                  valueColor: {
                    fontSize: 14,
                    color: '#5470C6',
                  },
                },
                position: 'outside', // 标签显示在环形外
              },
            }, // 蓝色
            {
              value: materialDta.value.normal,
              name: t('普通物料'),
              itemStyle: { color: '#FFA500' },
              label: {
                show: true,
                formatter: ({ name, value }) => {
                  return `{labelColor|${name}}\n{valueColor|${value}}`;
                },
                rich: {
                  labelColor: {
                    fontSize: 14,
                    color: '#FFA500',
                  },
                  valueColor: {
                    fontSize: 14,
                    color: '#FFA500',
                  },
                },
                position: 'outside', // 标签显示在环形外
              },
            }, // 橙色
          ],
          labelLine: {
            show: true, // 显示标签的连接线
            length: 12,
            length2: 8,
          },
        },
      ],
    };
  });

  return {
    materialStatisticsOptions,
  };
}
