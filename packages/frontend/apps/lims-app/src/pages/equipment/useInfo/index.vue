<!-- 设备状态、设备使用日志填报 -->
<template>
  <BMLayout>
    <BMBasicPage
      v-if="!showForm"
      :title="isDeviceStatus ? t('设备管理') : confirmBefore ? t('生产前确认') : t('设备使用日志填报')"
      :show-buttons="false"
      :top-bottom-padding="confirmBefore ? false : true"
      background-color="#F2F3F5"
      @left-click="toBack"
    >
      <template #titleRight>
        <view v-if="!confirmBefore" class="title_icons">
          <BMFilter v-model="filterData" :form-props="formProps" @confirm="filterConfirm" @reset="filterConfirm" />
          <wd-icon
            v-if="!isWindows"
            class-prefix="bmos-app-icon"
            name="saomiao"
            size="18.75rpx"
            color="#434C59"
            style="margin-left: 9rpx;"
            @click="openScan"
          />
        </view>
      </template>
      <view class="content">
        <view v-if="isWindows && !confirmBefore" class="scanning_box">
          <view class="scanning_content">
            <BMScan
              v-model="liquidScan"
              type="input"
              :allow-types="['04']"
              :placeholder="t('设备编码')"
              @success="onScanSuccess"
              @fail="onScanFail"
              @confirm="onScanSuccess"
            />
          </view>
        </view>
        <scroll-view
          class="scroll-class"
          :style="[isWindows && !confirmBefore ? 'height: calc(100% - 70.25rpx);' : 'height: calc(100% - 10.25rpx);padding-top: 9.38rpx;']"
          scroll-y="true"
          refresher-enabled="true"
          :refresher-triggered="triggered"
          :refresher-threshold="100"
          :lower-threshold="70"
          refresher-default-style="white"
          @refresherrefresh="onRefresh"
          @scrolltolower="onScrolltolower"
        >
          <view class="equipment_list_box">
            <view
              v-for="(item, index) in equipmentList"
              :key="index"
              class="equipment_item"
              @click="openAddInfo(item)"
            >
              <view class="equipment_item_title">
                <view class="equipment_title">
                  {{ item.name }}
                </view>
                <wd-tag v-if="states[item.status]" :type="states[item.status].type" plain>
                  {{ item.statusName }}
                </wd-tag>
              </view>
              <view class="equipment_data">
                {{ t("设备编号") }}：{{ item.code }}
              </view>
              <view class="equipment_data" :style="{ visibility: item.status === 2 ? 'hidden' : 'visible' }">
                {{ t("有效期至") }}：{{ item.expireDateTime || '-' }}
              </view>
            </view>
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
      </view>
    </BMBasicPage>
    <AddUseInfo v-else :equipment-data="itemValue" @close="closeAdduseInfo" />
  </BMLayout>
</template>

<script lang="ts" setup>
import { BMBasicPage, BMFilter, BMLayout, BMScan } from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import AddUseInfo from './component/addUseInfo.vue';
import { useData } from './hooks/useData';

const props = defineProps({
  deviceStatus: {
    type: String,
    default: '',
  },
  confirmBefore: { // 生产前确认里的
    type: Boolean,
    default: false,
  },
  productionLineId: {
    type: String,
    default: '',
  },
});
const {
  showForm,
  liquidScan,
  itemValue,
  formProps,
  equipmentList,
  isWindows,
  triggered,
  isDeviceStatus,
  loadMoreStatus,
  filterData,
  onScanSuccess,
  onScanFail,
  filterConfirm,
  openAddInfo,
  closeAdduseInfo,
  openScan,
  onRefresh,
  onScrolltolower,
} = useData({ props });
const states = [
  {
    type: 'success',
    label: t('可用'),
  },
  {
    type: 'success',
    label: t('可用'),
  },
  {
    type: 'warning',
    label: t('不可用'),
  },
  {
    type: 'primary',
    label: t('占用'),
  },
  {
    type: 'danger',
    label: t('故障'),
  },
];

const toBack = () => {
  uni.navigateBack();
};
</script>

<style scoped lang="scss">
  .title_icons {
  display: flex;
}
.scroll-class {
  box-sizing: border-box;
}
.content {
  height: 100%;
  .scanning_box {
    width: 100%;
    padding: 9.38rpx 0 9.38rpx;
    position: sticky;
    z-index: 9;
    top: 0;
    left: 0;
    background-color: rgb(242, 243, 245);
    .scanning_content {
      width: 50%;
      margin-left: 50%;
    }
  }
  .equipment_list_box {
    display: flex;
    flex-wrap: wrap;
    gap: 1%;
    .equipment_item {
      width: 32.6%;
      box-sizing: border-box;
      padding: 9.38rpx 11.72rpx;
      background-color: #fff;
      border-radius: 4.69rpx;
      margin-bottom: 9.38rpx;
      .equipment_item_title {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 7.03rpx;
        .equipment_title {
          color: #242526;
          font-size: 14.06rpx;
          text-overflow: ellipsis;
          overflow: hidden;
          white-space: nowrap;
        }
        .equipment_status_tag {
          padding: 2.34rpx 5.86rpx 2.34rpx 5.86rpx;
          border-radius: 2.34rpx;
          text-align: center;
          font-size: 9.38rpx;
        }
        .equipment_status_success {
          background-color: #dcf2eb;
          color: #59bf78;
        }
        .equipment_status_warning {
          background-color: #ffecd9;
          color: #ff9933;
        }
        .equipment_status_primary {
          background-color: #ebf2ff;
          color: #2871ff;
        }
        .equipment_status_danger {
          background-color: #ffd5cc;
          color: #ff4c26;
        }
      }
      .equipment_data {
        color: #6c6e73;
        font-size: 11.72rpx;
        margin-bottom: 5.86rpx;
      }
    }
  }
}
.screen_box {
  :deep(.modal-content) {
    width: 240.78rpx;
  }
}
</style>
