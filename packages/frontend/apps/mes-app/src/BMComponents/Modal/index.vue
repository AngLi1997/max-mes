<template>
  <wd-popup v-model="show" :close-on-click-modal="closeOnClickModal" :="attrs" :custom-style="customStyle">
    <view class="modal-container" @click="closeOutside">
      <slot name="title">
        <view v-if="showTitle" class="modal-title" :class="[rightModal ? 'right-title' : '']">
          {{ title }}
        </view>
      </slot>
      <view class="modal-content" :class="[rightModal ? 'right-content' : '', defaultPadding ? 'modal-padding' : '', hiddenButton ? 'right-content-no-button' : '']" :style="{ overflow }">
        <slot />
      </view>
      <view v-if="!hiddenButton" class="modal-button" :class="[rightModal ? 'right-button' : '']">
        <slot name="buttons">
          <wd-row :gutter="16">
            <wd-col v-if="showCancelButton" :span="showConfirmButton ? 12 : 24">
              <wd-button :type="cancelButtonType" block @click="cancel">
                {{ cancelText }}
              </wd-button>
            </wd-col>
            <wd-col v-if="showConfirmButton" :span="showCancelButton ? 12 : 24">
              <wd-button :type="confirmButtonType" block @click="confirm">
                {{ confirmText }}
              </wd-button>
            </wd-col>
          </wd-row>
        </slot>
      </view>
    </view>
  </wd-popup>
</template>

<script setup>
import { t } from '@/utils/useBmosI18n.js';
import { computed, reactive, useAttrs } from 'vue';
import { useQueue } from 'wot-design-uni';

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
  title: {
    type: String,
    default: '',
  },
  size: {
    type: String,
    default: 'medium',
  },
  cancelText: {
    type: String,
    default: () => t('取消'),
  },
  confirmText: {
    type: String,
    default: () => t('确定'),
  },
  cancelButtonType: {
    type: String,
    default: 'info',
  },
  confirmButtonType: {
    type: String,
    default: 'primary',
  },
  closeOnClickModal: {
    type: Boolean,
    default: false,
  },
  showTitle: {
    type: Boolean,
    default: true,
  },
  defaultPadding: {
    type: Boolean,
    default: true,
  },
  overflow: {
    type: String,
    default: 'auto',
  },
  hiddenButton: {
    type: Boolean,
    default: false,
  },
  maxHeight: {
    type: String,
    default: '280.08rpx',
  },
  showCancelButton: {
    type: Boolean,
    default: true,
  },
  showConfirmButton: {
    type: Boolean,
    default: true,
  },
});

const emit = defineEmits(['update:modelValue', 'confirm', 'cancel']);

const { closeOutside } = useQueue();

const attrs = useAttrs();
const rightModal = computed(() => {
  return attrs.position === 'right';
});
const show = computed({
  get: () => props.modelValue,
  set: (val) => {
    emit('update:modelValue', val);
  },
});

// 弹框宽度
const sizeWidth = reactive({
  small: '304.69rpx',
  medium: '375rpx',
  large: '468.75rpx',
  xLarge: '703.13rpx',
});
  // 弹框样式
const customStyle = computed(() => {
  if (rightModal.value) {
    return '';
  }
  return `width: ${sizeWidth[props.size]};border-radius: 7.03rpx;`;
});

// 弹框事件
const cancel = () => {
  emit('cancel');
};
const confirm = () => {
  emit('confirm');
};
</script>

<style lang="scss" scoped>
.modal-container {
  width: 100%;
  height: 100%;
  overflow: hidden;
  .modal-title {
    padding: 11.72rpx 9.38rpx;
    text-align: center;
    font-size: 15.23rpx;
    color: var(--bmos-color-text-main);
    line-height: 17.58rpx;
  }
  .right-title {
    padding: 14.06rpx 11.72rpx;
    text-align: left;
    font-size: 14.06rpx;
    line-height: 16.41rpx;
    color: var(--bmos-color-text-title);
    border-bottom: 1px solid var(--bmos-color-border);
  }

  .modal-content {
    min-height: 116.02rpx;
    max-height: v-bind(maxHeight);
    overflow-y: auto;
  }

  .modal-button {
    width: 100%;
    padding: 9.38rpx;
    box-sizing: border-box;
  }
  .right-content {
    height: calc(100vh - 41.03rpx - 58.59rpx);
    min-height: unset;
    max-height: unset;
  }
  .right-content-no-button {
    height: calc(100vh - 41.03rpx);
  }
  .modal-padding {
    padding: 0 9.38rpx;
    box-sizing: border-box;
  }
  .right-button {
    padding: 11.72rpx 9.38rpx;
  }
}
</style>
