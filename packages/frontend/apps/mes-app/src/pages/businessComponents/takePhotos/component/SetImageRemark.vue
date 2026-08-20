<template>
  <BMLayout>
    <BMBasicPage
      :default-padding="false"
      :title="t('拍照')"
      :confirm-text="t('编辑备注')"
      @left-click="toBack"
      @cancel="toBack"
      @confirm="setImages"
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
          <view id="imageCheckBox" class="img_masking" :class="{ show_masking: image.isCheck.length > 0 }">
            <wd-radio-group v-model="checkedImage" class="img_check_box" shape="dot" inline>
              <wd-radio :value="image.id" />
            </wd-radio-group>
          </view>
        </view>
        <BmosNoData v-if="imagesList.length === 0" type="emptyPhoto" :text="t('暂无图片')" />
      </view>
      <BMModal v-model="open" :title="t('备注信息')" size="medium" :lazy-render="false" @cancel="open = false" @confirm="confirm">
        <view class="form_box">
          <BMForm ref="formRef" v-bind="formProps" />
        </view>
      </BMModal>
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
import {
  BMBasicPage,
  BMForm,
  BMLayout,
  BMModal,
} from '@/BMComponents';
import BmosNoData from '@/components/BmosNoData/index.vue';
import { t } from '@/utils/useBmosI18n.js';
import { reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';

const props = defineProps({
  imagesList: {
    type: Array,
    default: () => [],
  },
});

const emit = defineEmits(['close', 'confirm']);

const { showNotify } = useNotify();

const formRef = ref();
const checkedImage = ref('');// 已勾选的图片
const open = ref(false);
const toBack = () => {
  emit('close');
};

// 添加备注
const confirm = async() => {
  const { remark } = await formRef.value?.validate();
  const newList = props.imagesList.map((item)=>{
    if(item.id === checkedImage.value){
      item.remark = remark;
    }
    return item
  })
  open.value = false;
  emit('confirm', newList);
};
const formProps = reactive({
  initialValues: {
    remark: ''
  },
  schemas: [
    {
      field: 'remark',
      component: 'Textarea',
      label: t('备注'),
      colProps: {
        span: 24,
      },
    },
  ],
});
const setImages = () => {
  if (!checkedImage.value) {
    showNotify({
      type: 'warning',
      message: t('请选择图片'),
    });
    return;
  }
  open.value = true;
  const { remark } = props.imagesList.find(item => item.id === checkedImage.value);
  formRef.value?.setFieldsValue({remark: remark || ''});
};
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
        width: 18.75rpx;
        height: 18.75rpx;
        background-color: transparent;
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
