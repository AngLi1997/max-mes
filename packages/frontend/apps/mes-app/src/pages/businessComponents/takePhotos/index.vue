<!-- 拍照组件 -->
<template>
  <BMLayout>
    <BMBasicPage
      v-if="showType === 'list'"
      :default-padding="false"
      :title="t('拍照')"
      @left-click="valueChangeCheck"
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
          <wd-col :span="3" custom-class="custom_class">
            <view class="icon_box">
              <wd-icon
                class-prefix="bmos-app-icon"
                name="shanchu2"
                size="18.75rpx"
                color="#434C59"
                @click="showType = 'delete'"
              />
              <wd-icon
                class-prefix="bmos-app-icon"
                name="kebianji"
                size="18.75rpx"
                color="#434C59"
                @click="showType = 'remark'"
              />
            </view>
          </wd-col>
          <wd-col :span="10">
            <wd-button
              type="info"
              block
              @click="signConfirm"
            >
              {{ t("拍照") }}
            </wd-button>
          </wd-col>
          <wd-col :span="11">
            <wd-button
              block
              @click="confirmPage"
            >
              {{ t("确定") }}
            </wd-button>
          </wd-col>
        </wd-row>
      </template>
    </BMBasicPage>
    <PhotoComponent
      v-if="showType === 'add'"
      @success="takePhotoSuccess"
      @fail="takePhotoFail"
      @close="closePhotoComponent"
    />
    <!-- 删除照片 -->
    <DeleteImages v-if="showType === 'delete'" :images-list="imagesList" @close="showType = 'list'" @delete="deleteImageConfirm" />
    <!-- 添加备注 -->
    <SetImageRemark v-if="showType === 'remark'" :images-list="imagesList" @close="showType = 'list'" @confirm="setImageRemarkConfirm" />
    <!-- 签名 -->
    <BMSignModal
      v-model:show="showSign"
      v-model="signValue"
      v-model:current-time="currentTime"
      :label-list="labelList"
      :title="t('数据修订')"
      show-remark
      :signature-data="curParams"
      :remark-required="true"
      @confirm="confirmSignPopup"
    />
    <BMMessageBox
      v-model="showWarnMessageBox"
      :title="t('照片未保存，是否要退出')"
      :content="t('这将放弃所有当前编辑内容，请谨慎操作。')"
      :confirm-text="t('退出')"
      @cancel="showWarnMessageBox = false"
      @confirm="toBack"
    />
    <BMMessageBox
      v-model="showMessageBox"
      :title="t('提示')"
      :content="t('无拍照图片保存时默认录入空值')"
      @cancel="showMessageBox = false"
      @confirm="toSubmit"
    />
    <BMImgCropper v-model:show="showImgCropper" :img-url="imgUrl" @success="cropperSuccess" />
  </BMLayout>
</template>

<script setup>
import { postModifyExecuteDataApi } from '@/api/webViewApi.js';
import {
  BMBasicPage,
  BMImgCropper,
  BMLayout,
  BMMessageBox,
  BMSignModal,
} from '@/BMComponents';
import BmosNoData from '@/components/BmosNoData/index.vue';
import {
  getCurrentCopyRecordItem,
  initFillData2,
  pageBasicDataRef,
  setComponentValue,
  signOptionsRef,
  urlQueryRef,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { nullValueRef } from '@/utils/systemConfig/index.js';
import { BMOS_ACCESS_TOKEN, IP_CONFIG } from '@/utils/uniStorage/const.js';
import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad } from '@dcloudio/uni-app';
import { onMounted, ref } from 'vue';
import { useNotify } from 'wot-design-uni';
import DeleteImages from './component/DeleteImages.vue';
import SetImageRemark from './component/SetImageRemark.vue';
import PhotoComponent from './PhotoComponent.vue';

const { showNotify } = useNotify();
const showType = ref('list');
const imagesList = ref([]);
const baseUrl
    = `http://${
      getStorageSync(IP_CONFIG) || '172.30.1.160:80'
    }/`;
const queryInfo = ref();
const showSign = ref(false);
const signValue = ref({
  loginName1: '',
  password1: '',
  userId1: '',
});
const showMessageBox = ref(false);
const labelList = ref([
  {
    label: t('修订人'),
    signatureAction: 1,
    options: signOptionsRef.value.map((item) => {
      return {
        ...item,
        label: item.userName,
      };
    }),
  },
  {
    label: t('复核人'),
    signatureAction: 4,
    options: signOptionsRef.value.map((item) => {
      return {
        ...item,
        label: item.userName,
      };
    }),
  },
]);
const curParams = ref({});
const currentTime = ref();
const initValueJson = ref('');
const showWarnMessageBox = ref(false);

const toBack = () => {
  uni.navigateBack();
};

// 判断有没有照片修订过
const valueChangeCheck = () => {
  if (imagesList.value.length === 0 && initValueJson.value !== '') {
    // 清空了图片,但是初始值不是空
    showWarnMessageBox.value = true;
    return;
  }
  else if (imagesList.value.length !== 0 && initValueJson.value === '') {
    // 初始值是空,添加了照片
    showWarnMessageBox.value = true;
    return;
  }
  else {
    const removePathList = imagesList.value.map((item) => {
      const newItem = { ...item };
      newItem.path = `${newItem.oldPath}`;
      delete newItem.oldPath;
      delete newItem.isCheck;
      return { ...newItem };
    });
    if (initValueJson.value !== JSON.stringify(removePathList)) {
      showWarnMessageBox.value = true;
      return;
    }
  }
  toBack();
};

const toSubmit = async () => {
  showMessageBox.value = false;
  // 数据修订,打开签名
  if (queryInfo.value.isChange) {
    showSign.value = true;
    curParams.value = {
      batchNo: urlQueryRef.value.batchNo,
      processId: urlQueryRef.value.processId,
      processVersion: urlQueryRef.value.processVersion,
      productPlanId: urlQueryRef.value.productPlanId,
      componentId: queryInfo.value.componentData.id,
      procedureStepId: pageBasicDataRef.value.procedureStepId,
      procedureStepModelId: pageBasicDataRef.value.procedureStepModelId,
      recordItemId: pageBasicDataRef.value.recordItemId,
      recordVersionId: pageBasicDataRef.value.recordVersionId,
      reuse: pageBasicDataRef.value.reusable,
    };
    return;
  }
  const removePathList = imagesList.value.map((item) => {
    item.path = `${item.oldPath}`;
    delete item.oldPath;
    return { ...item };
  });
  setComponentValue({
    fieldId: queryInfo.value.curFieldId,
    value: removePathList.length === 0 ? nullValueRef.value : JSON.stringify(removePathList),
    nullValueRef: nullValueRef.value,
    componentType: 'PHOTO',
    emptyValue: removePathList.length === 0,
  });
  // #ifdef APP-PLUS
  // #endif
  // #ifdef H5
  toBack();
  // #endif
};
  // 保存
const confirmPage = async () => {
  // 没有照片要录入空值提示
  if (imagesList.value.length === 0) {
    showMessageBox.value = true;
    return;
  }
  toSubmit();
};
// 签名完成
const confirmSignPopup = async () => {
  showSign.value = false;
  try {
    const removePathList = imagesList.value.map((item) => {
      item.path = `${item.oldPath}`;
      delete item.oldPath;
      return { ...item };
    });
    const copyRecordItem = await getCurrentCopyRecordItem();
    const params = {
      ...curParams.value,
      fieldId: queryInfo.value.curFieldId,
      componentType: 'PHOTO',
      copyVersion: copyRecordItem.version,
      value: removePathList.length === 0 ? nullValueRef.value : JSON.stringify(removePathList),
      reviewUser: signValue.value.userId2,
      remark: signValue.value.remark,
      operationUser: signValue.value.userId1,
      reviewTime: currentTime.value,
      operationTime: currentTime.value,
      processChangeNumber: urlQueryRef.value.processChangeNumber,
      procedureChangeNumber: urlQueryRef.value.procedureChangeNumber,
      emptyValue: removePathList.length === 0,
    };
    await postModifyExecuteDataApi(params);
    initFillData2();
    setComponentValue({
      fieldId: queryInfo.value.curFieldId,
      value: removePathList.length === 0 ? nullValueRef.value : JSON.stringify(removePathList),
      nullValueRef: nullValueRef.value,
      componentType: 'PHOTO',
      emptyValue: removePathList.length === 0,
    });
    // #ifdef APP-PLUS
    // #endif
    // #ifdef H5
    toBack();
    // #endif
  }
  catch (e) {
    if (e.code === 8208003) {
      showNotify({
        type: 'warning',
        message: t('记录数据已被修改，请重新保存'),
      });
    }
    else {
      showNotify({
        type: 'warning',
        message: e.message || t('保存失败'),
      });
    }
  }
};

const uploadFail = () => {
  showNotify({
    type: 'warning',
    message: t('上传失败'),
  });
};
const takePhotoSuccess = (res) => {
  showType.value = 'list';
  const { data } = JSON.parse(res.data);
  imagesList.value.push({
    ...data,
    oldPath: data.path,
    path: baseUrl + data.path,
    isCheck: [],
  });
};
const takePhotoFail = () => {
  showType.value = 'list';
  uploadFail();
};
const closePhotoComponent = () => {
  showType.value = 'list';
};
  // 点击图片放大
const imageClick = (image) => {
  uni.previewImage({
    current: 0,
    urls: [image.path],
  });
};
const showImgCropper = ref(false);
const imgUrl = ref('');
// 打开相机
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
  const baseUrl = `http://${getStorageSync(IP_CONFIG) || '172.30.1.160:80'}`;
  const apiUrl = '/api/app/mes/execute/upload';
  const token = getStorageSync(BMOS_ACCESS_TOKEN) || '';
  const header = { 'bmos-access-token': token, token };
  const data = {
    url: baseUrl + apiUrl,
    formData: {},
    header, // header 值
    success: (res) => {
      if (res.statusCode === 200) {
        takePhotoSuccess(res);
      }
      else {
        console.log('上传失败');
      }
    },
    fail: () => {
      console.log('上传失败');
      this.$emit('fail');
    },
  };
    // #ifdef APP-PLUS
  data.name = 'file';
  data.filePath = path;
  // #endif
  // #ifdef H5
  if (process.env.NODE_ENV === 'development') {
    data.url = apiUrl;
  }
  data.files = [{
    name: 'file',
    file: path,
  }];
  // #endif
  uni.uploadFile(data);
};

// 删除图片
const deleteImageConfirm = (newList) => {
  imagesList.value = newList;
};
// 编辑图片备注
const setImageRemarkConfirm = (newList) => {
  imagesList.value = newList;
};

// 获取图盘
const getImagesList = async () => {
  if (!queryInfo.value.imagesList) {
    imagesList.value = [];
  }
  else {
    imagesList.value = JSON.parse(queryInfo.value.imagesList);
    initValueJson.value = queryInfo.value.imagesList;
  }
  imagesList.value.map((item) => {
    item.isCheck = [];
    item.oldPath = item.path || '';
    item.path = baseUrl + item.path || '';
    return item;
  });
};
onMounted(() => {
  getImagesList();
  if (imagesList.value.length === 0) {
    // 打开照相机
    signConfirm();
  }
});
onLoad(async (e) => {
  // #ifdef APP-PLUS
  const query = Object.fromEntries(Object.keys(e)
    .map(key => [decodeURIComponent(key), decodeURIComponent(e[key])]));
  queryInfo.value = query;
  // #endif
  // #ifdef H5
  queryInfo.value = e;
  // #endif
  if (queryInfo.value.component) {
    queryInfo.value.componentData = JSON.parse(queryInfo.value.component);
  }
  // #ifdef APP-PLUS
  plus.screen.lockOrientation('landscape-primary');
  // #endif
  // #ifdef H5
  // #endif
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
.custom_class {
  display: flex;
}
.icon_box {
  height: 100%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-around;
}
</style>
