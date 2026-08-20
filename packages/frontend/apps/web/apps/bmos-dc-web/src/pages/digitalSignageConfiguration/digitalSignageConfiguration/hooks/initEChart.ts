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
export const initEChart = (options: any, params: any, data: any, peiData: any) => {
  const { title, xAxis, yAxis, series, radiusAxis, polar, angleAxis } = data;
  options.value = {
    title: {
      ...title,
    },
    legend: {
      top: '5%',
      left: 'center',
    },
    tooltip: {},
    series: [...series],
  };
  if (params.chartType.value == 'CIRCLE') {
    options.value = {
      angleAxis: {
        ...angleAxis,
        startAngle: 90,
        show: false,
      },
      polar: {
        ...polar,
        radius: [30, '60%'],
        center: ['50%', '40%'],
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
    options.value.series = [
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
    options.value.xAxis = {
      show: false,
    };
    options.value.yAxis = {
      show: false,
    };
  } else {
    options.value.xAxis = {
      ...xAxis,
    };
    options.value.yAxis = {
      ...yAxis,
    };
  }
};
