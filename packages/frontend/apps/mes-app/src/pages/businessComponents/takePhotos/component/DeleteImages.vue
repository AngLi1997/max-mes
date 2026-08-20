<template>
  <BMLayout>
    <BMBasicPage
      :default-padding="false"
      :title="t('拍照')"
      :confirm-text="t('删除')"
      @left-click="toBack"
      @cancel="toBack"
      @confirm="deleteImages"
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
          />
          <view class="user_msg">
            <view>{{ t("取证人") }}：{{ image.createUsername }}</view>
            <view>{{ t("取证时间") }}：{{ image.createTime }}</view>
            <view>{{ t("备注") }}：{{ image.remark || '-' }}</view>
          </view>
          <view id="imageCheckBox" class="img_masking" :class="{ show_masking: image.isCheck.length > 0 }" @click="imageClick(image, $event)">
            <wd-checkbox-group v-model="image.isCheck" class="img_check_box" inline @change="checkChange(image)">
              <wd-checkbox :model-value="image.id" />
            </wd-checkbox-group>
          </view>
        </view>
        <BmosNoData v-if="imagesList.length === 0" type="emptyPhoto" :text="t('暂无图片')" />
        <BMMessageBox
          v-model="isSuccessModal"
          :title="t('是否删除照片')"
          :cancel-text="t('否')"
          :confirm-text="t('是')"
          @cancel="isSuccessModal = false"
          @confirm="deleteImage"
        >
          <view class="modalText">
            {{ t('照片删除后无法恢复') }}
          </view>
        </BMMessageBox>
      </view>
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
import {
  BMBasicPage,
  BMLayout,
  BMMessageBox,
} from '@/BMComponents';
import BmosNoData from '@/components/BmosNoData/index.vue';
import { t } from '@/utils/useBmosI18n.js';
import { ref } from 'vue';
import { useNotify } from 'wot-design-uni';

const props = defineProps({
  imagesList: {
    type: Array,
    default: () => [],
  },
});
const emit = defineEmits(['close', 'delete']);
const { showNotify } = useNotify();
const checkedList = ref([]);// 已勾选的图片
const isSuccessModal = ref(false);
const toBack = () => {
  emit('close');
};

// 勾选或取消图片
const checkChange = (item) => {
  if (checkedList.value.includes(item.id)) {
    checkedList.value.splice(checkedList.value.indexOf(item.id), 1);
  }
  else {
    checkedList.value.push(item.id);
  }
};
// 删除选中图片
const deleteImages = () => {
  if(checkedList.value.length === 0) {
    showNotify({
      type: 'warning',
      message: t('请选择图片'),
    });
    return;
  }
  isSuccessModal.value = true;
};
const deleteImage = () => {
  isSuccessModal.value = false;
  const newList = props.imagesList.filter(item => !checkedList.value.includes(item.id));
  emit('delete', newList);
}
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