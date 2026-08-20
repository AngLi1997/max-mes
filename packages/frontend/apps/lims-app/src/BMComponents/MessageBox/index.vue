<template>
  <wd-popup v-model="show" :="attrs" :custom-style="customStyle">
    <view class="message-modal-container">
      <view class="box">
        <view class="modal-title">
          {{ title }}
        </view>
        <view class="modal-content">
          <slot>
            <view class="content-text">
              {{ content }}
            </view>
          </slot>
        </view>
      </view>
      <view class="modal-button">
        <slot name="buttons">
          <wd-row :gutter="16">
            <wd-col v-if="showCancelButton" :span="spanSize">
              <wd-button type="info" block @click="cancel">
                {{ cancelText }}
              </wd-button>
            </wd-col>
            <wd-col v-if="showConfirmButton" :span="spanSize">
              <wd-button block @click="confirm">
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
import { computed, useAttrs } from 'vue';

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
  title: {
    type: String,
    default: '',
  },
  content: {
    type: String,
    default: '',
  },
  cancelText: {
    type: String,
    default: () => t('取消'),
  },
  confirmText: {
    type: String,
    default: () => t('确定'),
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
// eslint-disable-next-line unused-imports/no-unused-vars, no-unused-vars
const attrs = useAttrs();
const show = computed({
  get: () => props.modelValue,
  set: (val) => {
    emit('update:modelValue', val);
  },
});

const spanSize = computed(() => {
  return props.showCancelButton && props.showConfirmButton ? 12 : 24;
});

// 弹框样式
const customStyle = computed(() => {
  return `width: 246.09rpx;border-radius: 7.03rpx;`;
});

// 弹框事件
const cancel = () => {
  emit('cancel');
  show.value = false;
};
const confirm = () => {
  emit('confirm');
  show.value = false;
};
</script>

<style lang="scss" scoped>
.message-modal-container {
  width: 100%;
  .box {
    padding: 14.06rpx 5.86rpx;
    box-sizing: border-box;
    max-height: 321.09rpx;
    display: flex;
    flex-direction: column;
    .modal-title {
      flex-shrink: 1;
      text-align: center;
      font-size: 14.06rpx;
      color: var(--bmos-color-text-main);
      line-height: 16.41rpx;
      margin-bottom: 9.38rpx;
    }
    .modal-content {
      overflow-y: auto;
      line-height: 14.06rpx;
      color: var(--bmos-color-text-desc);
      font-size: var(--bmos-font-size-sub);
      .content-text {
        text-align: center;
      }
    }
  }
  .modal-button {
    width: 100%;
    padding: 9.38rpx;
    box-sizing: border-box;
  }
}
</style>
