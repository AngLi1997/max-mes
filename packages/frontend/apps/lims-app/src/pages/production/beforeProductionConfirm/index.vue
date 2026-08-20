<template>
  <BMLayout>
    <BMBasicPage
      :title="t('生产前确认')"
      :show-buttons="false"
      background-color="var(--bmos-color-bg)"
      @left-click="leftClick"
    >
      <template #titleRight>
        <view class="action">
          <BMFilter v-model="filterData" :form-props="filterFormProps" @confirm="filterConfirmOrReset" @reset="filterConfirmOrReset" />
          <BMFilter v-model="sortData" :title="t('排序')" icon="paixu" :form-props="sortFormProps" @confirm="filterConfirmOrReset" @reset="filterConfirmOrReset" />
        </view>
      </template>
      <view class="list-content">
        <wd-button v-hasAuth="121010002000002" class="addSheet" size="small" @click="toAddSheet">
          {{ t('创建') }}
        </wd-button>
        <scroll-view
          v-if="listData.length"
          class="scroll-class"
          scroll-y="true"
          refresher-enabled="true"
          :refresher-triggered="triggered"
          :refresher-threshold="100"
          :lower-threshold="70"
          refresher-default-style="white"
          @refresherrefresh="onRefresh"
          @scrolltolower="onScrollToLower"
        >
          <view class="list-box">
            <view class="list">
              <ProductionOrderItem v-for="(item) in list1" :key="item.id" :item="item" @click="itemClick(item)" />
            </view>
            <view class="list">
              <ProductionOrderItem v-for="(item) in list2" :key="item.id" :item="item" @click="itemClick(item)" />
            </view>
          </view>
          <wd-loadmore :state="loadMoreStatus" />
        </scroll-view>
        <BMNoData v-else type="emptyData" :text="t('暂无生产计划')" />
      </view>
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
import { BMBasicPage, BMFilter, BMLayout, BMNoData } from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import ProductionOrderItem from './components/ProductionOrderItem.vue';
import { usePage } from './hooks/usePage.js';

const {
  triggered,
  filterData,
  sortData,
  listData,
  list1,
  list2,
  loadMoreStatus,
  filterFormProps,
  sortFormProps,
  leftClick,
  onRefresh,
  onScrollToLower,
  itemClick,
  filterConfirmOrReset,
  toAddSheet,
} = usePage();
</script>

<style lang="scss" scoped>
 .action {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  flex-shrink: 0;
  gap: 11.72rpx;
}
.list-content {
  position: relative;
  height: 100%;
  overflow: hidden;
  .scroll-class {
    height: 100%;
    box-sizing: border-box;
    .list-box {
      display: flex;
      row-gap: 9.38rpx;
      column-gap: 9.38rpx;
      padding: 9.38rpx 0;
      .list {
        width: 100%;
        display: flex;
        flex-direction: column;
        row-gap: 9.38rpx;
      }
    }
  }
  .addSheet {
    position: absolute;
    right: 0;
    bottom: 9.38rpx;
    z-index: 9;
  }
}
</style>
