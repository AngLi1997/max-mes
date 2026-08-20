<template>
  <view v-if="show" class="bm-img-cropper">
    <view v-if="showCrop" class="img-cropper-container">
      <view v-if="showCrop" class="img-cropper">
        <NiceCrop
          id="image-cropper"
          ref="cropper"
          :zoom="1"
          :reset-cut="true"
          :fit="true"
          :angle="0"
          :src="url"
          cut-width="90%"
          canvas-background="#000"
          :image-center="true"
          :disable-translate="true"
          :disable-scale="true"
          :disable-rotate="true"
          @cropped="cropped"
        />
      </view>
    </view>

    <view class="controls">
      <view @click="cancel">
        {{ t('取消') }}
      </view>
      <view @click="rotateLeft">
        <image class="icon" src="./xuanzhuan.png" />
      </view>
      <view @click="reset">
        {{ t('还原') }}
      </view>
      <view @click="returnImg">
        {{ t('完成') }}
      </view>
    </view>
    <BMMessageBox
      v-model="isOpenMessage" :title="t('裁剪')" :content="t('是否放弃所有更改')" @cancel="isOpenMessage = false"
      @confirm="collectionModeSwitch"
    />
  </view>
</template>

<script setup>
import {
  BMMessageBox,
} from '@/BMComponents';
import NiceCrop from '@/BMComponents/NiceCrop/index.vue';
import {
  t,
} from '@/utils/useBmosI18n.js';
import {
  base64ToPath,
} from 'image-tools';
import {
  computed,
  ref,
  watch,
} from 'vue';

const props = defineProps({
  show: {
    type: Boolean,
    default: false,
  },
  imgBase64: {
    type: String,
    default: '',
  },
  imgUrl: {
    type: String,
    default: '',
  },
});

const emits = defineEmits(['update:show', 'success']);

const show = computed({
  get() {
    return props.show;
  },
  set(val) {
    emits('update:show', val);
  },
});

const url = ref('');
const showCrop = ref(false);
watch(
  () => props.show,
  async (val) => {
    if (val && props.imgBase64) {
      base64ToPath(props.imgBase64)
        .then((path) => {
          url.value = path;
          showCrop.value = true;
        })
        .catch((error) => {
          console.error(error);
        });
    }
    else if (val && props.imgUrl) {
      url.value = props.imgUrl;
      showCrop.value = true;
    }
    else {
      showCrop.value = false;
    }
  },
);

const cropper = ref(null);

const returnImgBase64 = ref('');

function collectionModeSwitch() {
  show.value = false;
  showCrop.value = false;
  // #ifdef APP-PLUS
  emits('success', props.imgUrl);
  // #endif
}
const isOpenMessage = ref(false);
const cancel = () => {
  // #ifdef APP-PLUS
  if (returnImgBase64.value) {
    isOpenMessage.value = true;
  }
  else {
    collectionModeSwitch();
  }
  // #endif
  // #ifndef APP-PLUS
  isOpenMessage.value = true;
  // #endif
};

const rotate = ref(0);
const rotateLeft = () => {
  rotate.value -= 90;
  cropper.value.setRotate(rotate.value);
};

const reset = () => {
  rotate.value = 0;
  cropper.value.setRotate(rotate.value);
  cropper.value.resetCutReact();
};

function cropped(imagePath) {
  returnImgBase64.value = imagePath;
}
const returnImg = () => {
  try {
    cropper.value.draw();
  }
  catch (error) {
    console.error(error);
  }
  if (!returnImgBase64.value) {
    show.value = false;
    showCrop.value = false;
    emits('success', url.value);
  }
  else {
    setTimeout(() => {
      show.value = false;
      showCrop.value = false;
      emits('success', returnImgBase64.value || url.value);
    }, 1000);
  }
};
</script>

<style lang="scss">
.bm-img-cropper {
  position: fixed;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
  width: 100vw;
  height: 100vh;
  display: flex;
  z-index: 999;
  background-color: rgba(48, 49, 51);

  .img-cropper-container {
    flex: 1;
    height: 100%;
    background-color: black;
    overflow: hidden;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .img-cropper {
    width: 90%;
    height: 90%;
  }

  .controls {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    align-items: center;
    width: 58.59rpx;
    height: 100%;
    font-size: var(--bmos-font-size-sub);
    color: #fff;
    .icon {
      width: 26rpx;
      height: 26rpx;
    }
  }
}
</style>
