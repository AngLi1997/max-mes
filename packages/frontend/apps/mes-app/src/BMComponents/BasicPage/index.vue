<template>
  <view class="basic-page">
    <BMNavBar
      :title="title"
      @left-click="emit('leftClick')"
      @right-click="emit('rightClick')"
    >
      <template v-if="slots.titleLeft" #left>
        <slot name="titleLeft" />
      </template>
      <template v-if="slots.titleRight" #right>
        <slot name="titleRight" />
      </template>
    </BMNavBar>

    <scroll-view
      scroll-y="auto"
      class="basic-page-content" :class="[
        showButtons ? '' : 'has-no-buttons',
        defaultPadding ? '' : 'no-padding',
        topBottomPadding ? '' : 'no-topBottom-padding',
        allPadding ? '' : 'no-allPadding',
      ]"
      :style="{ background: backgroundColor }"
    >
      <slot />
    </scroll-view>
    <view v-if="showButtons" class="button-box">
      <slot name="buttons">
        <wd-row :gutter="16">
          <wd-col :span="12">
            <wd-button type="info" block @click="emit('cancel')">
              {{ cancelText }}
            </wd-button>
          </wd-col>
          <wd-col :span="12">
            <wd-button block :loading="loading" :disabled="disabledConfirm" @click="emit('confirm')">
              {{ confirmText }}
            </wd-button>
          </wd-col>
        </wd-row>
      </slot>
    </view>
  </view>
</template>

<script setup>
import { t } from '@/utils/useBmosI18n.js';
import { useSlots } from 'vue';
import { BMNavBar } from '../index.js';

defineProps({
  title: {
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
  // 是否显示底部按钮
  showButtons: {
    type: Boolean,
    default: true,
  },
  defaultPadding: {
    type: Boolean,
    default: true,
  },
  backgroundColor: {
    type: String,
    default: 'var(--bmos-color-white)',
  },
  loading: {
    type: Boolean,
    default: false,
  },
  disabledConfirm: {
    type: Boolean,
    default: false,
  },
  topBottomPadding: { // 有无上下padding
    type: Boolean,
    default: true,
  },
  allPadding: { // 有无全部padding
    type: Boolean,
    default: true,
  },
});

const emit = defineEmits(['leftClick', 'rightClick', 'cancel', 'confirm']);

const slots = useSlots();
</script>

<style lang="scss" scoped>
.basic-page {
  width: 100%;
  height: 100vh;
  overflow: hidden;
  position: relative;
  .basic-page-content {
    height: 100%;
    padding: 46.88rpx 9.38rpx 53.91rpx;
    box-sizing: border-box;
  }
  .has-no-buttons {
    padding-bottom: 0 !important;
  }
  .no-padding {
    padding: 46.88rpx 0 53.91rpx;
  }
  .no-topBottom-padding {
    padding: 0rpx 9.38rpx 0rpx;
  }
  .no-allPadding {
    padding: 0rpx;
  }
  .button-box {
    width: 100%;
    padding: 9.38rpx;
    border-top: 1px solid var(--bmos-color-border);
    box-sizing: border-box;
    position: absolute;
    left: 0;
    bottom: 0;
    background-color: var(--bmos-color-white);
    z-index: 9;
  }
}
</style>
