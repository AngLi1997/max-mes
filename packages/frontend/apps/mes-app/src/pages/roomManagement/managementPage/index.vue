<template>
  <BMLayout>
    <BMBasicPage
      :title="confirmBefore ? t('生产前确认') : t('房间管理')"
      :default-padding="false"
      :show-buttons="false"
      :all-padding="confirmBefore ? false : true"
      background-color="#F2F3F5"
      @left-click="toBack"
    >
      <template #titleRight>
        <view v-if="!confirmBefore" class="right-content">
          <uv-row justify="space-between" gutter="10">
            <uv-col span="6">
              <BMFilter v-model="filterData" :form-props="formProps" @confirm="filterConfirm" @reset="resetConfirm" />
            </uv-col>
            <!-- #ifdef APP-PLUS -->
            <uv-col span="6">
              <BMIcon name="saomiao" size="18.75rpx" @click="scanCode" />
            </uv-col>
            <!-- #endif -->
          </uv-row>
        </view>
      </template>
      <view class="status-content">
        <!-- #ifdef H5 -->
        <view v-if="!confirmBefore" style="width: 50%;margin-bottom: 9.38rpx;">
          <BMScan
            v-model="deviceCode"
            type="input"
            :allow-types="['05']"
            :error-type-placeholder="t('请扫描房间标签')"
            @success="scanRoomCode"
            @confirm="scanConfirm"
          />
        </view>
        <!-- #endif -->
        <scroll-view
          :class="[!confirmBefore ? 'scroll-class' : 'confirmBefore-scroll-class']"
          scroll-y="true"
          refresher-enabled="true"
          :refresher-threshold="100"
          :lower-threshold="70"
          refresher-default-style="white"
          refresher-background="transparent"
          :refresher-triggered="triggered"
          @refresherrefresh="onRefresh"
          @scrolltolower="onScrolltolower"
        >
          <view v-if="roomManList.data.length > 0" class="production-list">
            <uv-waterfall
              v-model="roomManList.data"
              column-count="3"
              column-gap="9.38rpx"
            >
              <!-- 第一列数据 -->
              <template #list1>
                <view class="StewardCard-view">
                  <StewardCard
                    v-for="(item, index) in roomManList.listA"
                    :key="index"
                    :use-item="item"
                    style="margin-bottom: 8.79rpx;"
                    @on-click="tagClick"
                  />
                </view>
              </template>
              <!-- 第二列数据 -->
              <template #list2>
                <view class="StewardCard-view">
                  <StewardCard
                    v-for="(item, index) in roomManList.listB"
                    :key="index"
                    :use-item="item"
                    style="margin-bottom: 8.79rpx;"
                    @on-click="tagClick"
                  />
                </view>
              </template>
              <!-- 第三列数据 -->
              <template #list3>
                <view class="StewardCard-view">
                  <StewardCard
                    v-for="(item, index) in roomManList.listC"
                    :key="index"
                    :use-item="item"
                    style="margin-bottom: 8.79rpx;"
                    @on-click="tagClick"
                  />
                </view>
              </template>
            </uv-waterfall>
            <uv-load-more
              color="#B6B9BF"
              font-size="11.72rpx"
              :status="loadMoreStatus"
              :loading-text="t('正在加载')"
              :loadmore-text="t('加载更多')"
              :nomore-text="t('没有更多了')"
            />
          </view>
          <BmosNoData v-else :text="t('暂无房间')" type="emptyProductionBefore" />
        </scroll-view>
      </view>
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
import { BMBasicPage, BMFilter, BMIcon, BMLayout } from '@/BMComponents';
import { BMScan } from '@/BMComponents/index.js';
import BmosNoData from '@/components/BmosNoData';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad, onShow } from '@dcloudio/uni-app';
import { ref } from 'vue';
import StewardCard from './components/stewardCard';
import { useModel, useParams, useTagContent } from './hooks';

const props = defineProps({
  confirmBefore: { // 生产前确认里的
    type: Boolean,
    default: false,
  },
  productionLineId: {
    type: String,
    default: '',
  },
});
const deviceCode = ref('');
const UseParams = useParams();
const {
  isEsy,
  triggered,
  loadMoreStatus,
  roomManList,
  formProps,
  filterData,
  params,
} = UseParams;
const UseTagContent = useTagContent({ UseParams, props });
const {
  onRefresh,
  onScrolltolower,
  tagClick,
  roomList,
  scanCode,
  scanRoomCode,
  scanConfirm,
} = UseTagContent;
const { filterConfirm, resetConfirm } = useModel({ UseParams, UseTagContent, filterData });
// 返回
const toBack = () => {
  uni.navigateBack();
};
onLoad(() => {
  roomList();
});
onShow(() => {
  if (isEsy.value) {
    params.value = {
      code: null, // 房间编码
      name: null, // 房间名称
      pageNum: 1,
      pageSize: 18,
      total: 0,
    };
    isEsy.value = false;
    roomList();
  }
});
</script>

<style lang="scss" scoped>
  // background: #FFFFFF;
.left-content {
  display: flex;

  .title {
    font-size: 15.24rpx;
    font-weight: 500;
    line-height: 22.27rpx;
    letter-spacing: 0em;
    color: #18191a;
    margin-left: 14.65rpx;
  }
}

.right-content {
  display: flex;

  :deep .uv-button {
    border: none;
  }
  :deep(.wd-radio.is-button-radio) {
    width: 50% !important;
    margin: 5.86rpx 0;
  }
}

.status-content {
  height: 100%;
  width: 100%;
  box-sizing: border-box;
  overflow: hidden;
  padding: 7.03rpx 9.38rpx;
  display: flex;
  flex-direction: column;
  align-items: end;
  position: relative;

  .scroll-class {
    // #ifdef H5
    height: calc(100% - 48.05rpx);
    // #endif
    // #ifdef APP-PLUS
    height: 100%;
    // #endif
  }
  .confirmBefore-scroll-class {
    height: 100%;
  }
  .StewardCard-view {
    display: flex;
    flex-direction: column;
  }
}
</style>
