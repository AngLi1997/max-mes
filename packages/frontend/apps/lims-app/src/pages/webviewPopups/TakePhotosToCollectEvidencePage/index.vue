<template>
  <view class="absolute_box">
    <BMLayout>
      <BMBasicPage
        v-if="showType === 'list'"
        :default-padding="false"
        :title="t('拍照取证')"
        :show-buttons="!viewOnly || productionRevision"
        @left-click="toBack"
      >
        <view class="content">
          <view
            v-for="(image, index) in imagesList"
            :key="index"
            class="img_box_item"
          >
            <uv-image
              :src="image.path"
              width="100%"
              height="202.73rpx"
              @click="imageClick(image)"
            />
            <view class="user_msg">
              <view>{{ t("取证人") }}：{{ image.createUsername }}</view>
              <view>{{ t("取证时间") }}：{{ image.createTime }}</view>
              <view>{{ t("备注") }}：{{ image.remark || '-' }}</view>
            </view>
          </view>
          <BmosNoData v-if="imagesList.length === 0" type="emptyPhoto" :text="t('暂无图片')" />
        </view>
        <template #buttons>
          <wd-row :gutter="16" custom-class="custom_class">
            <wd-col :span="1" custom-class="custom_class">
              <view class="icon_box">
                <wd-icon
                  class-prefix="bmos-app-icon"
                  name="kebianji"
                  size="18.75rpx"
                  color="#434C59"
                  @click="showType = 'remark'"
                />
              </view>
            </wd-col>
            <wd-col :span="23">
              <wd-button
                type="success"
                block
                @click="signConfirm"
              >
                {{ t("拍照") }}
              </wd-button>
            </wd-col>
          </wd-row>
        </template>
      </BMBasicPage>
      <PhotoComponent v-if="showType === 'add'" @success="takePhotoSuccess" @fail="takePhotoFail" @close="closePhotoComponent" />
      <!-- 添加备注 -->
      <SetImageRemark v-if="showType === 'remark'" :images-list="imagesList" @close="showType = 'list'" @confirm="setImageRemarkConfirm" />
      <BMImgCropper v-model:show="showImgCropper" :img-url="imgUrl" @success="cropperSuccess" />
    </BMLayout>
  </view>
</template>

<script setup>
import {
  BMBasicPage,
  BMImgCropper,
  BMLayout,
} from '@/BMComponents';
import BmosNoData from '@/components/BmosNoData/index.vue';
import {
  getRecordItemFile,
  productionRevision,
  uploadRecordItemFile,
  viewOnly,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { showMenuComponentRef, showTakePhotoPopupRef } from '@/pages/webview/utils/index.js';
import { IP_CONFIG } from '@/utils/uniStorage/const.js';
import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { t } from '@/utils/useBmosI18n.js';
import { ref } from 'vue';
import SetImageRemark from './component/SetImageRemark.vue';
import PhotoComponent from './PhotoComponent.vue';

const showType = ref('list');

const imagesList = ref([]);
const baseUrl = `http://${
  getStorageSync(IP_CONFIG) || '172.30.1.160:80'
}/bmos-product/`;

const getImagesList = async (res = {}) => {
  console.log('res', res);
  const data = (await getRecordItemFile({
    type: 'jpg',
  })) || [];
  console.log('data', data);
  imagesList.value = data.map((item) => {
    return {
      ...item,
      path: baseUrl + item.path,
      isCheck: [],
    };
  });
};
const uploadFail = () => {
  uni.showToast({
    title: t('上传失败'),
    icon: 'none',
  });
};
const showImgCropper = ref(false);
const imgUrl = ref('');
const signConfirm = () => {
  // #ifdef APP-PLUS
  uni.chooseImage({
    count: 1, // 图片数量，这里设置为1
    sizeType: ['compressed'], // 指定选择的图片类型，可以选择原图(original)和压缩图(compressed)，这里设置为二者都有
    sourceType: ['camera'], // 指定选择图片的来源，这里设置为相册(album)和相机(camera)
    success: (res) => {
      imgUrl.value = res.tempFilePaths[0];
      showImgCropper.value = true;
    },
    fail: (err) => {
      console.log('选择图片失败', err);
    },
  });
  // #endif
  // #ifdef H5
  showType.value = 'add';
  // #endif
};
const cropperSuccess = (path) => {
  console.log('path', path);
  uploadRecordItemFile({
    path,
    type: 'jpg',
    success: getImagesList,
    fail: uploadFail,
  });
};

const takePhotoSuccess = () => {
  showType.value = 'list';
  getImagesList();
};
const takePhotoFail = () => {
  showType.value = 'list';
  uploadFail();
};
const closePhotoComponent = () => {
  showType.value = 'list';
};
const toBack = () => {
  //  #ifdef H5
  showMenuComponentRef.value = true;
  showTakePhotoPopupRef.value = false;
  //  #endif
  // #ifdef APP-PLUS
  uni.navigateBack();
  // #endif
};

// 编辑图片备注
const setImageRemarkConfirm = (newList) => {
  imagesList.value = newList;
};
const imageClick = (image) => {
  uni.previewImage({
    current: 0,
    urls: [image.path],
  });
};
getImagesList();
</script>

<style lang="scss" scoped>
.absolute_box {
  position: absolute;
  z-index: 99;
  top: 0;
  left: 0;
  height: 100vh;
  width: 100%;
}
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
.custom_class {
  display: flex;
}
.icon_box {
  height: 100%;
  width: 100%;
  display: flex;
  justify-content: center;
  margin-top: 5.86rpx;
}
</style>
