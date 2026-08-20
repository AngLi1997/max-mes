<template>
  <div :class="{ box: true, width100: isShow }">
    <div :class="{ box_title: true, new_title: isShow }">{{ data.title }}</div>
    <div :class="{ box_body: true, is_no_image: isShow }">
      <Echarts v-if="showChart" :options="eChartOptions" :height="350" />
      <div v-if="peiData.length > 0" class="pei_box">
        <div v-for="(item, index) in peiData" :key="index" class="pei">
          <div class="pei_color" :style="{ backgroundColor: item.color }"></div>
          <div class="pei_name">{{ item.name }}</div>
          <div class="pei_line"></div>
          <div class="pei_value">{{ item.value }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { getDashboardInstanceById, getDashboardViewDataById } from '@/services';
  import echarts from '@/plugins/echarts';
  const props = defineProps({
    data: {
      type: Object,
      default: () => {},
    },
    isShow: {
      type: Boolean,
      default: false,
    },
  });
  const eChartOptions = ref();
  const showChart = ref(false);
  const peiData = ref<any>([]);
  const allColor = [
    'rgba(11, 229, 229, ',
    'rgba(3, 108, 254, ',
    'rgba(178, 223, 255, ',
    'rgba(0, 214, 138, ',
    'rgba(250, 144, 38, ',
    'rgba(229, 53, 53, ',
    'rgba(255, 215, 113, ',
    'rgba(10, 200, 247, ',
    'rgba(89, 119, 178, ',
    'rgba(255, 162, 153, ',
  ];
  const initEChart = (params: any, data: any) => {
    const { xAxis, yAxis, series, radiusAxis, polar, angleAxis } = data;
    eChartOptions.value = {
      tooltip: {},
      series: [...series],
    };
    if (params.chartType.value == 'CIRCLE') {
      eChartOptions.value = {
        angleAxis: {
          ...angleAxis,
          startAngle: 90,
          show: false,
        },
        polar: {
          ...polar,
          radius: [30, '60%'],
          center: ['25%', '50%'],
        },
        radiusAxis: {
          show: false,
          ...radiusAxis,
        },
        xAxis: {
          show: false,
        },
        yAxis: {
          show: false,
        },
      };
      eChartOptions.value.series = [
        {
          ...series[0],
          coordinateSystem: 'polar',
          showBackground: true,
          itemStyle: {
            normal: {
              borderRadius: '50%',
              color({ name, value, dataIndex }: any) {
                peiData.value.push({
                  name,
                  value,
                  color: `${allColor[dataIndex]}1)`,
                });
                // 返回每个饼图扇区的颜色
                return `${allColor[dataIndex]}1)`;
              },
            },
          },
        },
      ];
    } else if (params.chartType.value == 'PIE') {
      eChartOptions.value = {
        xAxis: {
          show: false,
        },
        yAxis: {
          show: false,
        },
      };
      eChartOptions.value.series = [
        {
          ...series[0],
          radius: ['40%', '60%'],
          label: {
            show: false,
            position: 'center',
          },
          center: ['25%', '50%'],
          itemStyle: {
            normal: {
              color({ name, value, dataIndex }: any) {
                peiData.value.push({
                  name,
                  value,
                  color: `${allColor[dataIndex]}1)`,
                });
                // 返回每个饼图扇区的颜色
                return `${allColor[dataIndex]}1)`;
              },
            },
          },
        },
      ];
      console.log('=============PIE', eChartOptions.value);
    } else if (params.chartType.value == 'LINE') {
      eChartOptions.value.legend = {
        top: '1%',
        right: '0',
        textStyle: {
          color: '#B9E8FF',
          fontSize: '12px',
        },
      };
      eChartOptions.value.xAxis = {
        ...xAxis,
        axisLine: {
          // 坐标轴线
          lineStyle: {
            color: '#fff',
            fontSize: '12px',
          },
        },
      };
      eChartOptions.value.yAxis = {
        ...yAxis,
        splitLine: {
          show: true,
          lineStyle: {
            type: 'dashed',
            color: '#B9E8FF',
            opacity: '0.3',
          },
        },
        axisLine: {
          // 坐标轴线
          lineStyle: {
            color: '#B9E8FF',
            fontSize: '12px',
          },
        },
      };
    } else {
      eChartOptions.value.xAxis = {
        ...xAxis,
        axisLine: {
          // 坐标轴线
          lineStyle: {
            color: '#fff',
            fontSize: '12px',
          },
        },
      };
      eChartOptions.value.yAxis = {
        ...yAxis,
        splitLine: {
          show: true,
          lineStyle: {
            type: 'dashed',
            color: '#B9E8FF',
            opacity: '0.3',
          },
        },
        axisLine: {
          // 坐标轴线
          lineStyle: {
            color: '#B9E8FF',
            fontSize: '12px',
          },
        },
      };
      eChartOptions.value.series = series.map((item: any, index: number) => {
        item.itemStyle = {
          color: allColor[index]
            ? new echarts.graphic.LinearGradient(
                0,
                0,
                0,
                1, // 渐变方向从上到下
                [
                  { offset: 0, color: `${allColor[index]}1)` }, // 柱图顶部颜色
                  { offset: 1, color: `${allColor[index]}0)` }, // 柱图底部颜色
                ],
              )
            : '',
        };
        return item;
      });
    }
  };
  onMounted(async () => {
    const { data } = await getDashboardInstanceById({ id: props.data.id });
    const res = await getDashboardViewDataById({ id: props.data.id });
    initEChart(data, res.data);
    nextTick(() => {
      showChart.value = true;
    });
  });
</script>
<style lang="less" scoped>
  .box {
    width: calc(33% - 20px);
    height: 380px;
    margin-top: 20px;
    .box_title {
      height: 30px;
      background-image: url('/src/assets/img/title.png');
      background-size: 100% 100%;
      line-height: 30px;
      overflow: hidden;
      padding-left: 30px;
      color: #cce8ff;
    }
    .new_title {
      background-image: url('/src/assets/img/image_title.png');
      background-size: 30% 100%;
      background-repeat: no-repeat;
    }
    .box_body {
      height: calc(100% - 30px);
      background-image: url('/src/assets/img/border.png');
      background-size: 100% 100%;
      position: relative;
      .pei_box {
        width: 50%;
        height: 100%;
        position: absolute;
        top: 0;
        right: 0;
        display: flex;
        flex-direction: column;
        justify-content: center;
        gap: 30px;
        .pei {
          display: flex;
          align-items: center;
          justify-content: space-between;
          padding-right: 40px;
          gap: 10px;
          .pei_color {
            width: 10px;
            height: 10px;
            flex-shrink: 0;
          }
          .pei_name {
            color: #b9e8ff;
            white-space: nowrap;
          }
          .pei_value {
            color: #fff;
          }
          .pei_line {
            border-top: 2px dashed rgba(185, 232, 255, 0.25);
            width: 100%;
          }
        }
      }
    }
    .is_no_image {
      background-image: none;
    }
  }
  .width100 {
    width: 100%;
  }
</style>
