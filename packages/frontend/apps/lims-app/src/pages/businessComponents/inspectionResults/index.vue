<!-- 检验结果组件 -->
<template>
  <BMLayout>
    <BMBasicPage
      v-if="showType === 'list'"
      :title="t('检验结果')"
      :default-padding="false"
      :show-buttons="false"
      @left-click="toBack"
    >
      <view style="height: 100%;">
        <scroll-view
          v-if="dataList.length"
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
          <view class="content">
            <DataItem v-for="item, index in dataList" :key="index" :results-data="item" @to-result="toResult" @to-detail="toDetail" />
          </view>
          <uv-load-more
            color="#B6B9BF"
            font-size="11.72rpx"
            :status="loadMoreStatus"
            :loading-text="t('正在加载')"
            :loadmore-text="t('加载更多')"
            :nomore-text="t('没有更多了')"
          />
        </scroll-view>
        <BMNoData
          v-else
          type="emptyData"
          :text="t('暂无检验结果')"
        />
      </view>
    </BMBasicPage>
    <Result v-if="showType === 'result'" :results-data="clickData" @cancel="cancel" @submit="resultSubmit" />
    <Detail v-if="showType === 'detail'" :results-data="clickData" @cancel="cancel" />
  </BMLayout>
</template>

<script setup>
import { BMBasicPage, BMLayout } from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import DataItem from './component/DataItem.vue';
import Detail from './component/Detail.vue';
import Result from './component/Result.vue';
import { useData } from './hooks/useData';

const { dataList, toResult, toDetail, clickData, showType, cancel, triggered, onRefresh, loadMoreStatus, onScrollToLower, resultSubmit } = useData();
// 返回
const toBack = () => {
  uni.navigateBack();
};
</script>

<style lang="scss">
.scroll-class {
  height: calc(100% - 16.25rpx);
  box-sizing: border-box;
}
.content {
  width: calc(100% - 18.75rpx);
  margin: auto;
  display: flex;
  justify-content: space-between;
  gap: 9.37rpx;
  flex-wrap: wrap;
}
</style>
