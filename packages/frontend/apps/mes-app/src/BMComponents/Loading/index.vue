<template>
  <view v-if="visible" class="bm-loading">
    <view class="loading-backdrop" @click.stop="handleBackdropClick" />
    <view class="loading-content">
      <view class="loading-spinner">
        <view class="spinner-circle" />
      </view>
      <text v-if="text" class="loading-text">
        {{ text }}
      </text>
    </view>
  </view>
</template>

<script setup>
// 定义 props
const props = defineProps({
  visible: {
    type: Boolean,
    default: false,
  },
  text: {
    type: String,
    default: '加载中...',
  },
  maskClosable: {
    type: Boolean,
    default: false,
  },
});

// 定义 emits
const emit = defineEmits(['close']);

// 处理背景点击
const handleBackdropClick = () => {
  if (props.maskClosable) {
    emit('close');
  }
};
</script>

<style lang="scss" scoped>
.bm-loading {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 9999;
  display: flex;
  align-items: center;
  justify-content: center;

  .loading-backdrop {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.6);
  }

  .loading-content {
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: rgba(255, 255, 255, 1);
    border-radius: 4.69rpx;
    min-width: 96.09rpx;
    padding: 7.03rpx 11.72rpx;
    box-sizing: border-box;

    .loading-spinner {
      margin-right: 9.96rpx;
      .spinner-circle {
        width: 17.58rpx;
        height: 17.58rpx;
        border: 1.76rpx solid #e5e7eb;
        border-top: 1.76rpx solid #3b82f6;
        border-radius: 50%;
        animation: spin 1s linear infinite;
        box-sizing: border-box;
      }
    }

    .loading-text {
      font-size: 11.72rpx;
      color: #6c6e73;
      text-align: center;
      line-height: 14.06rpx;
      font-weight: 400;
    }
  }
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
