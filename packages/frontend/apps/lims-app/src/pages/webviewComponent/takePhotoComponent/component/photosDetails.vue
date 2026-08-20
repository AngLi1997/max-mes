<template>
  <BMBasicPage
    :default-padding="false"
    :title="t('拍照详情')"
    :show-buttons="false"
    @left-click="toBack"
  >
    <view class="content">
      <view
        v-for="(image, index) in imagesList"
        :key="index"
        class="img_box_item"
        @click="imageClick(image, $event)"
      >
        <uv-image
          :src="image.path"
          width="100%"
          height="202.73rpx"
        />
        <view class="user_msg">
          <view>{{ t("取证人") }}：{{ image.createUsername }}</view>
          <view>{{ t("取证时间") }}：{{ image.createTime }}</view>
          <view>{{ t("备注") }}：{{ image.remark || '-' }}</view>
        </view>
      </view>
      <BmosNoData
        v-if="imagesList.length === 0"
        type="emptyPhoto"
        :text="t('暂无图片')"
      />
    </view>
  </BMBasicPage>
</template>

<script setup>
import { BMBasicPage } from '@/BMComponents';
import BmosNoData from '@/components/BmosNoData/index.vue';
import { IP_CONFIG } from '@/utils/uniStorage/const.js';
import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad } from '@dcloudio/uni-app';
import { ref } from 'vue';

const imagesList = ref([]);
const queryInfo = ref();
const baseUrl
    = `http://${
      getStorageSync(IP_CONFIG) || '172.30.1.160:80'
    }/`;

const toBack = () => {
  uni.navigateBack();
};

// 获取图片
const getImagesList = async () => {
  if (!queryInfo.value.imagesList) {
    toBack();
    return;
  }
  const hisList = JSON.parse(queryInfo.value.imagesList);
  imagesList.value = hisList.map((item) => {
    return {
      ...item,
      path: baseUrl + item.path,
      isCheck: [],
    };
  });
};
  // 点击图片放大
const imageClick = (image) => {
  uni.previewImage({
    current: 0,
    urls: [image.path],
  });
};
onLoad(async (e) => {
  // #ifdef APP-PLUS
  const query = Object.fromEntries(Object.keys(e)
    .map(key => [decodeURIComponent(key), decodeURIComponent(e[key])]));
  queryInfo.value = query;
  // #endif
  // #ifdef H5
  queryInfo.value = e;
  // #endif
  getImagesList();
});
</script>

<style lang="scss" scoped>
  .content {
  background: #f2f3f5;
  width: 100%;
  height: 100%;
  overflow-y: auto;
  padding: 9.38rpx 9.38rpx 0;
  box-sizing: border-box;
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  align-items: flex-start;
  .img_box_item {
    width: calc(50% - 4.69rpx);
    margin-bottom: 9.38rpx;
    position: relative;
    overflow: hidden;
    .user_msg {
      padding: 9.38rpx;
      background-color: white;
      color: #6c6e73;
      font-size: 10.55rpx;
      line-height: 12.89rpx;
    }
    .img_masking {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      :deep(.wd-checkbox-group) {
        background-color: transparent;
      }
      :deep(.wd-checkbox__label) {
        margin: 0;
      }
      .img_check_box {
        position: absolute;
        right: 7.03rpx;
        top: 7.03rpx;
      }
    }
    .show_masking {
      background-color: rgba($color: #000000, $alpha: 0.3);
    }
  }
  .modalText {
    text-align: center;
  }
}
</style>
