<template>
  <div class="cold-chain-transport">
    <div ref="chart" style="width: 100%; height: 100%"></div>
  </div>
</template>

<script setup lang="ts">
  import * as echarts from 'echarts';
  import dataMap from '../../../public/chinaMap.json';
  const chart = ref<echarts.ECharts>();
  const chartInstance = ref<echarts.ECharts | null>(null);
  import companyIcon from './assets/company.png'; // 引入本地PNG图标
  import titleIcon from './assets/title.png'; // 引入本地PNG图标
  import iconIcon from './assets/icon.png'; // 引入本地PNG图标
  import coldCarBox from './assets/coldCarBox.png'; // 引入本地PNG图标
  import coldCarLine from './assets/coldCarLine.png'; // 引入本地PNG图标
  import { getColdLinkCarData } from '@/services';

  const coldCarData = ref<any[]>([]);

  const seriesData = computed(() => {
    return coldCarData.value.map(item => ({
      name: item.carCode,
      value: [item.longitude, item.latitude],
      carCode: item.carCode,
      temperature: item.temperature,
      humidity: item.humidity,
      premark: item.premark,
    }));
  });
  const initChart = () => {
    echarts.registerMap('china', dataMap);

    // 初始化 ECharts 实例
    chartInstance.value = echarts.init(chart.value);

    // 配置 ECharts 图表
    const option = {
      geo: {
        map: 'china',
        roam: true,
        zoom: 1.5,
        label: {
          show: true,
          color: '#86A7BF',
          emphasis: {
            color: '#fff',
          },
        },
        itemStyle: {
          areaColor: 'rgba(20, 48, 102, 0.3)',
          borderColor: 'rgba(51, 153, 255, 0.8)',
          emphasis: {
            areaColor: '#486AAE', // 鼠标悬浮时省份颜色
          },
        },
        center: [104.114129, 37.550339],
      },
      series: [
        {
          type: 'scatter', // 使用scatter来添加公司总部位置
          coordinateSystem: 'geo',
          data: [
            {
              name: '公司总部', // 标记名称
              value: [113.91546228, 35.28996329], // 坐标位置
              symbol: `image://${companyIcon}`, // 自定义图标URL
              symbolSize: [40, 63], // 图标的宽度和高度
            },
          ],
          itemStyle: {
            color: 'yellow', // 标记颜色
          },
        },
        {
          type: 'scatter',
          coordinateSystem: 'geo',
          data: seriesData.value,
          label: {
            show: true,
            position: [-65, -37],
            formatter: (data: any) => {
              return `{car|            ${data.name}}\n                   {line| }`;
            },
            rich: {
              car: {
                backgroundColor: {
                  image: coldCarBox,
                },
                width: 140,
                height: 30,
                color: '#fff',
              },
              line: {
                backgroundColor: {
                  image: coldCarLine,
                  width: 12,
                  height: 60,
                },
              },
            },
          },
          emphasis: {
            label: {
              show: true,
              backgroundColor: '#262A33',
              borderWidth: '1',
              borderType: 'solid',
              borderColor: '#1281e3',
              padding: 16,
              borderRadius: '8px',
              color: '#fff',
              formatter: ({ data }: any) => {
                const list = [
                  `{title| }`,
                  `    {img|}  ${data.carCode}`,
                  `    {label|温度：}{text|${data.temperature}}{label|湿度：}{text|${data.humidity}}`,
                  `    {label|经度：}{text|${data.value[0]}}{label|纬度：}{text|${data.value[1]}}`,
                ];
                if (data.premark.length < 26) {
                  list.push(`    {label|位置明细：}{remark|${data.premark}}`);
                } else {
                  list.push(`    {label|位置明细：}{remark|${data.premark.substring(0, 26)}}`);
                  for (let index = 26; index < data.premark.length; index += 26) {
                    const endIndex = index + 26 > data.premark.length ? data.premark.length : index + 26;
                    const text = data.premark.substring(index, endIndex);
                    list.push(`    {remark|${text}}`);
                  }
                }
                return list.join('\n');
              },
              rich: {
                title: {
                  backgroundColor: {
                    image: titleIcon,
                  },
                  width: 85,
                  height: 30,
                },
                img: {
                  backgroundColor: {
                    image: iconIcon,
                  },
                  width: 20,
                  height: 20,
                  lineHeight: 30,
                },
                label: {
                  color: '#86a7bf',
                  lineHeight: 30,
                },
                text: {
                  width: 150,
                  lineHeight: 30,
                },
                remark: {
                  width: 150,
                },
              },
            },
          },
        },
      ],
    };
    chartInstance.value.setOption(option);
  };
  onMounted(async () => {
    const { data } = await getColdLinkCarData();
    coldCarData.value = data;
    initChart();
  });
</script>
<style lang="less">
  .dc-content {
    padding: 0px !important;
  }
</style>

<style lang="less" scoped>
  .cold-chain-transport {
    padding: 20px;
    width: 100%;
    height: 100%;
    background: radial-gradient(61.36% 50% at 50% 50%, #334466 0%, #292c33 100%);
  }
</style>
