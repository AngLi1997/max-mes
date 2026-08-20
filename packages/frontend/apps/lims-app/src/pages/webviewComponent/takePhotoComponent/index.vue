<template>
  <view class="mask">
    <view class="history-popup-container">
      <view class="title-box">
        <text class="title">{{ t("历史数据") }}</text>
        <uv-icon
          name="close"
          color="#797C80"
          size="14.07rpx"
          @click="close"
        />
      </view>
      <BmosButton
        height="29.31rpx"
        type="primary"
        :text="t('拍照')"
        @click="openTakePhotos"
      />
      <uv-gap height="11.72rpx" />
      <uv-list class="list-box">
        <uv-list-item
          v-for="(item, index) in historyDataList"
          :key="index"
        >
          <view class="historical-item">
            <view style="display: flex">
              <text class="label">{{ t("值") }}:</text>
              <wd-button type="text" @click="openPhotosDetail(item)">{{ t('拍照详情') }}</wd-button>
            </view>
            <view style="display: flex">
              <text class="label">{{ t("操作") }}:</text>
              <text class="value">{{ item.systemCreate ? t('更新') : operationTypeObj[item.operationType] }}</text>
            </view>
            <view style="display: flex">
              <text class="label">{{ t("操作人") }}:</text>
              <text class="value">
                {{
                  item.systemCreate ? t("系统") : item.operationUsername
                }}
              </text>
            </view>
            <view
              v-if="item.reviewUsername"
              style="display: flex"
            >
              <text class="label">{{ t("复核人") }}:</text>
              <text class="value">{{ item.reviewUsername }}</text>
            </view>
            <view style="display: flex">
              <text class="label">{{ t("时间") }}:</text>
              <text class="value">{{ item.operationTime }}</text>
            </view>
            <view style="display: flex">
              <text class="label">{{ t("备注") }}:</text>
              <text class="value">{{ item.remark }}</text>
            </view>
          </view>
          <uv-gap height="11.72rpx" />
        </uv-list-item>
      </uv-list>
    </view>
  </view>
</template>
<script setup>
  import { t } from '@/utils/useBmosI18n.js';
  import { useSubNvueLinster } from '@/pages/webview/hooks/useSubNvueLinster.js';
  import { ref } from 'vue';
  import BmosButton from '@/components/BmosButton/index.vue';
  import { useHistoryData } from '@/pages/webviewComponent/historyDataComponent/hooks/index.js';
  import { H5AppNavigateBack } from '@/pages/webview/utils/index.js';
  import { getPhotoHisListApi } from '@/api/webViewApi.js';
  const componentData = ref();
  const operationTypeObj = {
    save: t('录入'),
    modify: t('修订'),
    systemCreate: t('更新'),
    update: t('更新')
  };
  const { historyDataList, getFieldDataList } = useHistoryData({
    componentData
  });
  useSubNvueLinster('page-historicalTakeFpoto', (data) => {
    const { component, configInfo } = data;
    componentData.value = component;
    getFieldDataList(component);
  });
  const openTakePhotos = () => {
    const params = {
      curFieldId: componentData.value.fieldId,
      imagesList: componentData.value.value,
      component: JSON.stringify(componentData.value),
      isChange: true
    };
    const query = Object.keys(params)
      .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
      .join('&');
    uni.navigateTo({
      url: `/pages/businessComponents/takePhotos/index?${query}`
    });
  };
  const close = () => {
    H5AppNavigateBack();
  };
  const openPhotosDetail = async(item) => {
    try {
      const { data } = await getPhotoHisListApi({ value: item.value });
      const params = {
        imagesList: data
      };
      const query = Object.keys(params)
        .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
        .join('&');
      uni.navigateTo({
        url: `/pages/webviewComponent/takePhotoComponent/component/photosDetails?${query}`
      });
    } catch (error) {
      uni.showToast({
        title: error.message || t('查询详情失败'),
        icon: 'none'
      });
    }
  };
</script>

<style lang="scss" scoped>
  .mask {
    position: fixed;
    left: 0;
    top: 0;
    right: 0;
    bottom: 0;
    /* #ifndef APP-NVUE */
    display: flex;
    /* #endif */
    justify-content: flex-end;
    align-items: center;
    background-color: rgba(0, 0, 0, 0.4);
    z-index: 99;
    .history-popup-container {
      width: 269.64rpx;
      height: 100%;
      background-color: #ffffff;
      box-sizing: border-box;
      padding: 7.03rpx 9.38rpx;
      display: flex;
      flex-direction: column;

      .title-box {
        display: flex;
        justify-content: space-between;
        align-items: center;
        height: 25.79rpx;
        border-bottom: 1px solid #e1e3e5;
        margin-bottom: 9.38rpx;

        .title {
          font-size: 12.9rpx;
          color: #545659;
        }
      }

      .switch-box {
        display: flex;
        justify-content: flex-end;
        align-items: center;

        .switch-text {
          font-size: 11.72rpx;
          color: #000000;
          margin-right: 11.72rpx;
        }
      }

      .historical-item {
        width: 100%;
        min-height: 109.03rpx;
        box-sizing: border-box;
        padding: 4.69rpx 9.38rpx;
        border-radius: 5.86rpx;
        background: #f5f6f7;
        font-size: 11.72rpx;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        &>view{
          display: flex;
          align-items: center;
        }
        .label {
          color: #545659;
          margin-right: 9.38rpx;
          width: 46.89rpx;
          display: inline-block;
          flex-shrink: 0;
        }

        .value {
          color: #242526;
        }
        .handle-sign-img {
          height: 23.44rpx;
          object-fit: contain;
        }
        :deep(.wd-button){
          padding: 0;
          margin: 0;
          height: 11.72rpx;
          border-radius: 0 !important;
        }
      }

      .list-box {
        flex: 1;
        height: 100%;
        overflow-y: auto;
      }
    }
  }
</style>
