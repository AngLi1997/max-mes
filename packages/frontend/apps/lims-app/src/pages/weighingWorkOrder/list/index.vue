<template>
  <BMBasicPage
    :title="t('称量工单执行')"
    :show-buttons="false"
    background-color="var(--bmos-color-bg)"
    @left-click="leftClick"
  >
    <template #titleRight>
      <view class="icons-box">
        <wd-icon
          name="shaixuan"
          size="18.75rpx"
          class-prefix="bmos-app-icon"
          color="#434C59"
          @click="handleFilter"
        />
        <wd-icon
          name="paixu"
          size="18.75rpx"
          class-prefix="bmos-app-icon"
          color="#434C59"
          @click="handleSort"
        />
      </view>
    </template>
    <view class="list-content">
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
            <Item v-for="(item) in list1" :key="item.id" :item="item" @click="itemClick(item)" />
          </view>
          <view class="list">
            <Item v-for="(item) in list2" :key="item.id" :item="item" @click="itemClick(item)" />
          </view>
        </view>
        <wd-loadmore :state="loadMoreStatus" />
      </scroll-view>
      <BMNoData v-else type="emptyData" :text="t('暂无称量工单')" />
    </view>
  </BMBasicPage>
  <!-- 筛选 -->
  <BMModal
    v-model="showFilterModal"
    :title="t('筛选')"
    size="small"
    position="right"
    closable
    :cancel-text="t('重置')"
    :close-on-click-modal="false"
    :default-padding="false"
    @confirm="filterConfirm"
    @cancel="filterReset"
  >
    <view class="filter-content">
      <view class="filter-item">
        <view class="item-title">
          {{ t("物料信息") }}
        </view>
        <wd-input v-model="filterData.material" :placeholder="t('请输入物料名称/物料编码')" />
      </view>
      <view class="filter-item">
        <view class="item-title">
          {{ t("称量中心") }}
        </view>
        <wd-input v-model="filterData.centre" :placeholder="t('请输入称量中心名称/编码')" />
      </view>
      <view class="filter-item">
        <view class="item-title">
          {{ t("工单编号") }}
        </view>
        <wd-input v-model="filterData.ticketNo" :placeholder="t('请输入工单编号')" />
      </view>
    </view>
  </BMModal>
  <!-- 排序 -->
  <BMModal
    v-model="showSortModal"
    :title="t('排序')"
    size="small"
    position="right"
    closable
    :cancel-text="t('重置')"
    :close-on-click-modal="false"
    :default-padding="false"
    @confirm="sortConfirm"
    @cancel="sortReset"
  >
    <view class="sort-content">
      <view class="sort-item bottom-border">
        <view class="item-title">
          {{ t("工单下发时间") }}
        </view>
        <wd-radio-group v-model="sortData.sortTaskNo" shape="button" @change="sortData.sortSendTime = ''">
          <wd-radio value="sendTime asc">
            {{ t("顺序排列") }}
          </wd-radio>
          <wd-radio value="sendTime desc">
            {{ t("逆序排列") }}
          </wd-radio>
        </wd-radio-group>
      </view>
      <view class="sort-item">
        <view class="item-title">
          {{ t("工单编号") }}
        </view>
        <wd-radio-group v-model="sortData.sortSendTime" shape="button" @change="sortData.sortTaskNo = ''">
          <wd-radio value="ticketNo asc">
            {{ t("顺序排列") }}
          </wd-radio>
          <wd-radio value="ticketNo desc">
            {{ t("逆序排列") }}
          </wd-radio>
        </wd-radio-group>
      </view>
    </view>
  </BMModal>
</template>

<script setup>
import { BMBasicPage, BMModal, BMNoData } from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import Item from './components/item.vue';
import { usePage } from './hooks/usePage.js';

const {
  triggered,
  showFilterModal,
  showSortModal,
  sortData,
  filterData,
  listData,
  list1,
  list2,
  loadMoreStatus,
  leftClick,
  handleFilter,
  filterConfirm,
  filterReset,
  handleSort,
  sortConfirm,
  sortReset,
  onRefresh,
  onScrollToLower,
  itemClick,
} = usePage();
</script>

<style lang="scss" scoped>
.icons-box {
  display: flex;
  column-gap: 11.72rpx;
}
.filter-content {
  width: 100%;
  height: 100%;
  padding: 11.72rpx;
  box-sizing: border-box;
  .filter-item {
    margin-bottom: 11.72rpx;
    .item-title {
      font-size: 11.72rpx;
      line-height: 14.06rpx;
      color: var(--bmos-color-text-sub);
      margin-bottom: 5.86rpx;
    }
  }
}
.sort-content {
  width: 100%;
  height: 100%;
  padding: 11.72rpx;
  box-sizing: border-box;
  .sort-item {
    margin-bottom: 18.75rpx;
    padding-bottom: 18.75rpx;
    .item-title {
      font-size: 12.89rpx;
      line-height: 15.23rpx;
      color: var(--bmos-color-text-main);
      margin-bottom: 14.06rpx;
    }
  }
  .bottom-border {
    border-bottom: 1px solid var(--bmos-color-border);
  }
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
}
</style>
