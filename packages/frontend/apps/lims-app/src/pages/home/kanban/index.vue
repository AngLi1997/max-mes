<template>
  <BMLayout>
    <wd-tabs v-model="tab">
      <wd-tab :title="t('称量看板')" :name="0">
        <view class="weight-kanban-content">
          <view class="weight-kanban-item overview-echarts">
            <view class="weight-kanban-item-title">
              {{ t('工单概览') }}
              <wd-segmented v-model:value="overviewCurrent" :options="overviewSegmentedOptions" size="small" class="overview-segmented">
                <template #label="{ option }">
                  <view class="segmented-name">
                    {{ option.label }}
                  </view>
                </template>
              </wd-segmented>
            </view>
            <BMNoData v-if="showOverviewEmpty" type="emptyData" :text="t('暂无工单')" />
            <LEchart ref="overviewEcharts" :class="{ 'chart-no-data': showOverviewEmpty }" custom-style="height: calc(100% - 14.06rpx - 7.03rpx);" />
          </view>
          <view class="weight-kanban-item today-order">
            <view class="weight-kanban-item-title">
              {{ t('今日工单') }}
            </view>
            <BMTable ref="todayOrderTableRef" v-bind="todayOrderTableProps" />
          </view>
          <view class="weight-kanban-item kanban-item-all">
            <view class="weight-kanban-item-title">
              {{ t('生产批次配料完成情况') }}
              <wd-segmented v-model:value="productBatchCurrent" :options="segmentedOptions" size="small">
                <template #label="{ option }">
                  <view class="segmented-name">
                    {{ option.label }}
                  </view>
                </template>
              </wd-segmented>
            </view>
            <BMTable
              ref="productBatchTableRef"
              v-bind="productBatchTableProps"
              :extra-params="{
                recentDays: productBatchCurrent,
              }"
            />
          </view>
          <view class="weight-kanban-item kanban-item-half">
            <view class="weight-kanban-item-title">
              {{ t('称量工单趋势') }}
              <wd-segmented v-model:value="trendCurrent" :options="segmentedOptions" size="small">
                <template #label="{ option }">
                  <view class="segmented-name">
                    {{ option.label }}
                  </view>
                </template>
              </wd-segmented>
            </view>
            <BMNoData v-if="showTrendEmpty" type="emptyData" :text="t('暂无内容')" />
            <LEchart ref="trendEcharts" :class="{ 'chart-no-data': showTrendEmpty }" custom-style="height: calc(100% - 14.06rpx - 7.03rpx);" />
          </view>
          <view class="weight-kanban-item kanban-item-half">
            <view class="weight-kanban-item-title">
              {{ t('称量需求趋势') }}
              <wd-segmented v-model:value="demandTrendCurrent" :options="segmentedOptions" size="small">
                <template #label="{ option }">
                  <view class="segmented-name">
                    {{ option.label }}
                  </view>
                </template>
              </wd-segmented>
            </view>
            <BMNoData v-if="showDemandTrendEmpty" type="emptyData" :text="t('暂无内容')" />
            <LEchart ref="demandTrendEcharts" :class="{ 'chart-no-data': showDemandTrendEmpty }" custom-style="height: calc(100% - 14.06rpx - 7.03rpx);" />
          </view>
          <view class="weight-kanban-item kanban-item-all">
            <view class="weight-kanban-item-title">
              {{ t('称量工单完成情况') }}
              <wd-segmented v-model:value="weightDoneCurrent" :options="segmentedOptions" size="small">
                <template #label="{ option }">
                  <view class="segmented-name">
                    {{ option.label }}
                  </view>
                </template>
              </wd-segmented>
            </view>
            <BMTable
              ref="weightDoneTableRef"
              v-bind="weightDoneTableProps"
              :extra-params="{
                recentDays: weightDoneCurrent,
              }"
            />
          </view>
        </view>
      </wd-tab>
    </wd-tabs>
  </BMLayout>
</template>

<script setup>
import { BMLayout, BMNoData, BMTable } from '@/BMComponents';
import LEchart from '@/components/l-echart/l-echart.vue';
import { useTabbarStore } from '@/stores/tabbar.js';
import { t } from '@/utils/useBmosI18n.js';
import { onHide, onShow } from '@dcloudio/uni-app';
import { storeToRefs } from 'pinia';
import { onUnmounted, ref, watch } from 'vue';
import {
  useDemandTrendChart,
  useOverviewChart,
  useProductBatch,
  useTodayOrder,
  useTrendChart,
  useWeightDone,
} from './hooks';

const tabBarStore = useTabbarStore();
const { selectedId } = storeToRefs(tabBarStore);
const tab = ref(0);

const segmentedOptions = [
  { label: t('近7天'), value: 7 },
  { label: t('近15天'), value: 15 },
];

const {
  overviewSegmentedOptions,
  overviewCurrent,
  overviewEcharts,
  overviewEchartsInit,
  showOverviewEmpty,
  clearOverViewTimer,
} = useOverviewChart();
const { todayOrderTableProps, todayOrderTableRef } = useTodayOrder();
const { productBatchCurrent, productBatchTableProps, productBatchTableRef } = useProductBatch();
const {
  trendEcharts,
  trendEchartsInit,
  trendCurrent,
  showTrendEmpty,
  clearTrendTimer,
} = useTrendChart();
const {
  demandTrendEcharts,
  demandTrendEchartsInit,
  demandTrendCurrent,
  showDemandTrendEmpty,
  clearDemandTrendTimer,
} = useDemandTrendChart();
const { weightDoneCurrent, weightDoneTableProps, weightDoneTableRef } = useWeightDone();

onShow(() => {
  if (selectedId.value === 3) {
    overviewEchartsInit();
    trendEchartsInit();
    demandTrendEchartsInit();

    todayOrderTableRef.value?.reload(true);
    productBatchTableRef.value?.reload(true);
    weightDoneTableRef.value?.reload(true);
  }
});

watch(selectedId, () => {
  if (selectedId.value === 3) {
    overviewEchartsInit();
    trendEchartsInit();
    demandTrendEchartsInit();

    todayOrderTableRef.value?.reload(true);
    productBatchTableRef.value?.reload(true);
    weightDoneTableRef.value?.reload(true);
  }
});

onHide(() => {
  clearOverViewTimer();
  clearTrendTimer();
  clearDemandTrendTimer();
  todayOrderTableRef.value?.clearTimer();
  productBatchTableRef.value?.clearTimer();
  weightDoneTableRef.value?.clearTimer();
});

onUnmounted(() => {
  clearOverViewTimer();
  clearTrendTimer();
  clearDemandTrendTimer();
  todayOrderTableRef.value?.clearTimer();
  productBatchTableRef.value?.clearTimer();
  weightDoneTableRef.value?.clearTimer();
});
</script>

<style scoped lang="scss">
.wd-tabs {
  height: 100%;
  width: 100%;

  .wd-tabs__container {
    height: calc(100% - 39.84rpx);
  }
}

:deep(.wd-tabs__container) {
  height: calc(100% - 39.84rpx);

  .wd-tab {
    height: 100%;

    .wd-tab__body {
      height: 100%;
    }
  }
}

.weight-kanban-content {
  height: calc(100% - 4.69rpx - 4.69rpx);
  width: calc(100% - 4.69rpx - 4.69rpx);
  display: flex;
  flex-wrap: wrap;
  padding: 4.69rpx;
  background: var(--bmos-bg-color);
  gap: 4.69rpx;
  overflow-y: auto;
}

.weight-kanban-item {
  border-radius: 5.86rpx;
  border: 0.88rpx solid rgba(255, 255, 255, 0.7);
  background: linear-gradient(0deg, #fff 84%, #e5f0ff 100%);
  padding: 7.03rpx;
  height: 165.23rpx;
  position: relative;

  .weight-kanban-item-title {
    color: #242526;
    font-size: 11.72rpx;
    margin-bottom: 7.03rpx;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  .chart-no-data {
    opacity: 0;
  }
}
:deep(.wd-segmented) {
  width: 89.06rpx;
  background-color: #f2f3f5;
  border-radius: 4.69rpx;
  height: 16.41rpx;
  .wd-segmented__item.is-small {
    height: 12.89rpx;
    line-height: 12.89rpx !important;
    min-height: 12.89rpx;
  }
  .segmented-name {
    font-size: 8.2rpx;
    color: #242526;
    line-height: 12.89rpx !important;
  }
}
:deep(.wd-segmented.overview-segmented) {
  width: 124.22rpx;
}
:deep(.wd-segmented__item.is-active) {
  background-color: #ffffff;
  color: #6c6e73;
}

.kanban-item-half {
  width: calc(50% - 4.69rpx * 4);
}
.kanban-item-all {
  width: calc(100% - 2.34rpx);
}

:deep(.bm-table-show-border) {
  border: none;
}
:deep(.bm-table) {
  height: calc(100% - 7.03rpx - 14.06rpx);
}
:deep(.bm-table .bm-table-container .uni-table) {
  min-width: 292.97rpx !important;
  .header {
    height: 25.78rpx;

    .uni-table-th {
      font-size: 9.38rpx;
      padding: 0 7.03rpx;
    }
  }
  .body-tr {
    height: 25.78rpx;
  }
  .body-tr .uni-table-td {
    font-size: 9.38rpx;
    padding: 7.03rpx;
  }
  .wd-tag {
    font-size: 8.2rpx;
    padding: 1.17rpx 3.52rpx !important;
    height: 12.89rpx;
  }
}
:deep(.bm-table .bm-table-container.bottom-out-refresh .uni-table) {
  .table-scroll-class {
    height: calc(100% - 25.78rpx);
  }
}

.overview-echarts {
  width: 210.94rpx;
}

.today-order {
  flex: 1;
  overflow: hidden;
  min-width: 50%;
}
</style>
