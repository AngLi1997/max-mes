<template>
  <BMBasicPage
    :title="t('趋势分析')"
    background-color="#F2F3F5"
    :default-padding="false"
    :show-buttons="false"
    @left-click="toBack"
  >
    <view class="content">
      <view class="title_box">
        <view class="title_item title_name">
          {{ t("生产批号") }}
        </view>
        <view class="title_item title_data">
          {{ t("数据") }}
        </view>
        <view class="title_item title_code">
          {{ batchNo }}
        </view>
        <view class="title_item title_num_box">
          <text
            class="title_num"
            :style="isShowDanger ? 'color: #FF4C26;' : ''"
          >
            {{
              clickNum != null && clickNum !== undefined ? clickNum : "-"
            }}
          </text>
          <wd-tag
            v-if="isShowDanger"
            type="danger"
          >
            {{ t("超限") }}
          </wd-tag>
        </view>
      </view>
      <view
        v-if="isShowEcharts"
        class="echarts_box"
      >
        <LEchart
          ref="echartsDom"
          style="height: 300.78rpx"
        />
      </view>
      <view
        v-else
        class="echarts_box"
        style="height: 300.78rpx"
      >
        <BMNoData
          type="emptyData"
          :text="t('暂无数据')"
        />
      </view>
    </view>
  </BMBasicPage>
</template>

<script setup>
import { getFieldTrendAnalysisApi } from '@/api/productionApi.js';
import { BMBasicPage, BMNoData } from '@/BMComponents';
import LEchart from '@/components/l-echart/l-echart.vue';
import {
  componentsMap,
  pageBasicDataRef,
  urlQueryRef,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad } from '@dcloudio/uni-app';
import * as echarts from 'echarts';
import { computed, nextTick, onMounted, ref } from 'vue';

const echartsDom = ref();
const clickNum = ref();
const upperLimit = ref(); // 上限
const upperLimitType = ref(); // 0</1<=
const downLimit = ref(); // 下限
const lowerLimitType = ref(); // 0>/1>=
const queryInfo = ref({});
const batchNo = ref('-');
const isShowEcharts = ref(true);
const isRange = ref(false);
const isShowDanger = computed(() => {
  // 非空判断
  if (clickNum.value == null) {
    return false;
  }
  if (upperLimit.value == null && downLimit.value == null) {
    return false;
  }
  if (!isRange.value) {
    // 数值相等
    return clickNum.value !== downLimit.value;
  }
  // 数值范围
  if (upperLimit.value == null) {
    // 没有配置最大值,大于或等于最小值即可
    if (clickNum.value > downLimit.value) {
      return false;
    }
    if (
      lowerLimitType.value === 1
      && clickNum.value === downLimit.value * 1
    ) {
      // 大于等于
      return false;
    }
  }
  if (downLimit.value == null) {
    // 没有配置最小值,小于或等于最大值即可
    if (clickNum.value < upperLimit.value) {
      return false;
    }
    if (
      upperLimitType.value === 1
      && clickNum.value === upperLimitType.value * 1
    ) {
      // 小于等于最大值
      return false;
    }
  }
  if (upperLimit.value === downLimit.value) {
    return clickNum.value !== upperLimit.value * 1;
  }
  if (clickNum.value < upperLimit.value && clickNum.value > downLimit.value) {
    // 不超过上限和下限
    return false;
  }
  else if (
    upperLimitType.value === 1
    && clickNum.value === upperLimit.value * 1
  ) {
    // 等于上限
    return false;
  }
  else if (
    lowerLimitType.value === 1
    && clickNum.value === downLimit.value * 1
  ) {
    // 等于下限
    return false;
  }
  return true;
});
  // 返回
const toBack = () => {
  uni.navigateBack();
};
onLoad(async (e) => {
  // #ifdef APP-PLUS
  const query = Object.fromEntries(
    Object.keys(e).map(key => [
      decodeURIComponent(key),
      decodeURIComponent(e[key]),
    ]),
  );
  queryInfo.value = query;
  // #endif
  // #ifdef H5
  queryInfo.value = e;
  // #endif
});

onMounted(async () => {
  const configInfo = componentsMap.get(queryInfo.value.id)?.configInfo;
  if (configInfo) {
    isRange.value = configInfo.limit === 0;// 0为范围 1为数值相等
    if (isRange.value) {
      upperLimit.value = Number.isNaN(configInfo.scope.scopeMax * 1) ? null : configInfo.scope.scopeMax * 1;
      downLimit.value = Number.isNaN(configInfo.scope.scopeMin * 1) ? null : configInfo.scope.scopeMin * 1;
      upperLimitType.value = configInfo.scope.upperLimit;
      lowerLimitType.value = configInfo.scope.lowerLimit;
    }
    else {
      if (configInfo.numericalValue != null && configInfo.numericalValue !== undefined) {
        // downLimit.value = Number.isNaN(configInfo.numericalValue * 1) ? null : configInfo.scope.scopeMin * 1;
      }
    }
  }
  let series = [];
  if (configInfo && !isRange.value) {
    // 数值相等
    if (configInfo.numericalValue != null && configInfo.numericalValue !== undefined) {
      series = [
        {
          type: 'average',
          name: 'Avg',
          yAxis: configInfo.numericalValue,
          label: {
            formatter: '标准: {c}',
            backgroundColor: '#FFD5CC',
            color: '#FF4C26',
            padding: 5,
            borderRadius: 5,
          },
        },
      ];
    }
  }
  else if (configInfo) {
    // 范围
    if (upperLimit.value && downLimit.value) {
      if (upperLimit.value === downLimit.value) {
        series = [
          {
            type: 'average',
            name: 'Avg',
            yAxis: upperLimit.value,
            label: {
              formatter: '标准: {c}',
              backgroundColor: '#FFD5CC',
              color: '#FF4C26',
              padding: 5,
              borderRadius: 5,
            },
          },
        ];
      }
      else {
        series = [
          {
            type: 'average',
            name: 'Avg',
            yAxis: upperLimit.value,
            label: {
              formatter: '上限: {c}',
              backgroundColor: '#FFD5CC',
              color: '#FF4C26',
              padding: 5,
              borderRadius: 5,
            },
          },
          {
            type: 'average',
            name: 'Avg',
            yAxis: downLimit.value,
            label: {
              formatter: '下限: {c}',
              backgroundColor: '#FFD5CC',
              color: '#FF4C26',
              padding: 5,
              borderRadius: 5,
            },
          },
        ];
      }
    }
    else {
      if (upperLimit.value) {
        series.push({
          type: 'average',
          name: 'Avg',
          yAxis: upperLimit.value,
          label: {
            formatter: '上限: {c}',
            backgroundColor: '#FFD5CC',
            color: '#FF4C26',
            padding: 5,
            borderRadius: 5,
          },
        });
      }
      if (downLimit.value) {
        series.push({
          type: 'average',
          name: 'Avg',
          yAxis: downLimit.value,
          label: {
            formatter: '下限: {c}',
            backgroundColor: '#FFD5CC',
            color: '#FF4C26',
            padding: 5,
            borderRadius: 5,
          },
        });
      }
    }
  }
  const { data } = await getFieldTrendAnalysisApi({
    fieldId: queryInfo.value.id,
    procedureStepId: pageBasicDataRef.value.procedureStepId,
    productPlanId: urlQueryRef.value.productPlanId,
    max: upperLimit.value,
    min: downLimit.value,
  });
  if (data.dataList.length === 0) {
    isShowEcharts.value = false;
    return;
  }
  const xList = [];
  const yList = [];
  data.dataList.forEach((item) => {
    if (data.dataList.length === 1) {
      xList.push(`${item.batchNo}`);
    }
    else {
      xList.push(`${item.batchNo}(${item.serialNo})`);
    }
    yList.push(item.value);
  });
  nextTick(() => {
    echartsDom.value.init(echarts, (chart) => {
      chart.setOption({
        grid: {
          // 调整距离
          bottom: '35%', // 下
        },
        xAxis: {
          type: 'category',
          data: xList,
          axisLabel: {
            interval: 0, // 确保所有标签都显示
            rotate: -30,
            axisLabel: {
              // 增加或减少的距离
              margin: 20,
            },
          },
        },
        tooltip: {
          trigger: 'axis',
          // showContent: false,
          position: (_pos, params) => {
            clickNum.value = Number.isNaN(params[0]?.value * 1) ? null : params[0]?.value * 1;
            batchNo.value = params[0]?.name;
            return '';
          },
        },
        yAxis: {
          type: 'value',
          max() {
            return data.max;
          },
          min() {
            return data.min;
          },
        },
        series: [
          {
            type: 'line',
            data: yList,
            markLine: {
              lineStyle: {
                color: '#FF4C26',
              },
              data: [...series],
            },
          },
        ],
        dataZoom: [
          {
            type: 'slider',
            show: true,
            xAxisIndex: [0],
            start: 1,
            bottom: 20,
            end: (20 / xList.length) * 100,
          },
        ],
      });
    });
  });
});
</script>

<style lang="scss" scoped>
  .charts {
    width: 600rpx;
    height: 260.78rpx;
  }
  .content {
    padding: 9.38rpx;
    .title_box {
      background-color: #fff;
      margin-bottom: 9.38rpx;
      padding: 9.38rpx;
      display: flex;
      align-items: center;
      flex-wrap: wrap;
      border-radius: 4.69rpx;
      .title_item {
        width: 50%;
        font-size: 9.38rpx;
      }
      .title_name {
        color: #6c6e73;
        margin-bottom: 4.69rpx;
      }
      .title_data {
        color: #6c6e73;
        margin-bottom: 4.69rpx;
        position: relative;
        &::before {
          content: "";
          position: absolute;
          top: 0;
          bottom: 0;
          left: -15.72rpx;
          margin: auto;
          width: 9.38rpx;
          height: 9.38rpx;
          background-color: #2871ff;
          border-radius: 50%;
        }
      }
      .title_num_box {
        display: flex;
        align-items: center;
        .title_num {
          margin-right: 4.69rpx;
          line-height: 17.75rpx;
        }
      }
    }
    .echarts_box {
      background-color: #fff;
      padding: 9.38rpx;
      border-radius: 4.69rpx;
    }
  }
</style>
