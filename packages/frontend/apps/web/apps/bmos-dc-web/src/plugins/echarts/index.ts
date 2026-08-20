import * as echarts from 'echarts/core';

import { BarChart, LineChart, MapChart, PictorialBarChart, PieChart, RadarChart, SankeyChart } from 'echarts/charts';

import {
  AriaComponent,
  DataZoomComponent,
  GridComponent,
  LegendComponent,
  MarkLineComponent,
  ParallelComponent,
  PolarComponent,
  TitleComponent,
  TooltipComponent,
} from 'echarts/components';

import { CanvasRenderer } from 'echarts/renderers';

echarts.use([
  LegendComponent,
  TitleComponent,
  TooltipComponent,
  GridComponent,
  PolarComponent,
  AriaComponent,
  ParallelComponent,
  BarChart,
  LineChart,
  PieChart,
  MapChart,
  CanvasRenderer,
  PictorialBarChart,
  RadarChart,
  MarkLineComponent,
  DataZoomComponent,
  SankeyChart,
]);

export default echarts;
