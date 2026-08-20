<template>
  <BMLayout>
    <BMBasicPage
      :title="t('选择房间')"
      :show-buttons="false"
      background-color="#F2F3F5"
      @left-click="toBack"
    >
      <template #titleRight>
        <view class="right-content">
          <uv-row justify="space-between" gutter="10">
            <uv-col span="6">
              <BMFilter v-model="filterData" :form-props="formProps" @confirm="filterTagList" @reset="filterTagList" />
            </uv-col>
          </uv-row>
        </view>
      </template>
      <view class="container">
        <view class="status-content">
          <view class="top-scan">
            <div style="width:50%;">
              <BMScan
                v-model="scanValue"
                type="input"
                :allow-types="['05']"
                :error-type-placeholder="t('房间码无法识别，可选择房间')"
                @success="onScanSuccess"
                @fail="onScanFail"
                @confirm="scanConfirm"
              />
            </div>
          </view>
          <scroll-view
            class="scroll-class"
            scroll-y="true"
            refresher-enabled="true"
            :refresher-threshold="100"
            :lower-threshold="70"
            refresher-default-style="white"
            refresher-background="transparent"
            :refresher-triggered="triggered"
            @refresherrefresh="onRefresh"
          >
            <view v-if="roomManList.data.length > 0" class="production-list">
              <uv-waterfall
                ref="deviceWaterfall"
                v-model="roomManList.data"
                column-count="3"
              >
                <!-- 第一列数据 -->
                <template #list1>
                  <view class="StewardCard-view">
                    <StewardCard
                      v-for="(item, index) in roomManList.listA"
                      :key="index"
                      :use-item="item"
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
                      @on-click="tagClick"
                    />
                  </view>
                </template>
              </uv-waterfall>
            </view>
            <BMNoData v-else type="emptyData" :text="t('暂无房间')" />
          </scroll-view>
        </view>
      </view>
    </BMBasicPage>
  </BMLayout>
</template>
<script setup>
  import {
    BMLayout,
    BMBasicPage,
    BMScan,
    BMNoData,
    BMFilter
  } from '@/BMComponents';

  import { t } from '@/utils/useBmosI18n.js';
  import StewardCard from './components/stewardCard';
  import { useParams, useOperation } from './hooks';
  import { onLoad, onShow } from '@dcloudio/uni-app';
  import { onMounted, ref } from 'vue';
  import { useNotify } from 'wot-design-uni';
  import { getRoomInfoByCodeApi, getRoomInfoApi } from '@/api';

  const { showNotify } = useNotify();
  const UseParams = useParams();
  const { isEsy, triggered, roomManList, currentList, filterData } = UseParams;
  const { onRefresh, tagClick, getTagList, filterTagList, formProps } = useOperation({
    UseParams,
    showNotify
  });

  // 返回
  const toBack = () => {
    uni.navigateBack();
  };
  const scanValue = ref('');
  const onScanSuccess = async(code) => {
    try {
      await getRoomInfoApi(code);
      tagClick({ id: code });
    } catch (error) {
      error.message &&
        uni.showToast({
          title: error.message,
          icon: 'none'
        });
    }
  };
  // win键盘confirm事件
  const scanConfirm = async(code) => {
    try {
      const res = await getRoomInfoByCodeApi(code);
      tagClick({ id: res.data.id });
    } catch (error) {
      error.message &&
        uni.showToast({
          title: error.message,
          icon: 'none'
        });
    }
  };
  const onScanFail = (err) => {
    showNotify({
      type: 'danger',
      message: t('房间码无法识别，可选择房间')
    });
  };
  onLoad(async(e) => {
    // #ifdef APP-PLUS
    const query = Object.fromEntries(
      Object.keys(e).map((key) => [
        decodeURIComponent(key),
        decodeURIComponent(e[key])
      ])
    );
    currentList.value = query;
    await getTagList();
    // #endif
    // #ifdef H5
    currentList.value = e;
    await getTagList();
  // #endif
  });
  onMounted(async() => {
    // #ifdef APP-PLUS
    if (currentList.value.scanError) {
      showNotify({
        type: 'danger',
        message: t('房间编码无法识别，可选择房间')
      });
    }
  // #endif
  });
  onShow(async() => {
    if (isEsy.value) {
      await getTagList();
    }
  });
</script>
<style lang="scss" scoped>
.right-content {
  display: flex;
}
.container {
  width: 100%;
  height: 100%;
  overflow: hidden;
  box-sizing: border-box;

  .status-content {
    height: 100%;
    width: 100%;
    box-sizing: border-box;
    overflow: hidden;
    display: flex;
    flex-direction: column;
    position: relative;
    .top-scan {
      display: flex;
      justify-content: flex-end;
      margin-bottom: 9.38rpx;
      margin-top: 9.38rpx;
    }

    .scroll-class {
      flex: 1;
      overflow: hidden;

      .StewardCard-view {
        display: flex;
        flex-direction: column;
        gap: 8.79rpx;
      }
    }
  }
}
</style>
