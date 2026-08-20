<template>
  <view
    class="nav-bar-container"
    :class="{ 'nav-bar-container-fixed': fixed, 'border-bottom': borderBottom }"
  >
    <view class="left-content" @click="emit('leftClick')">
      <slot name="left">
        <view class="title-box">
          <wd-icon name="thin-arrow-left" size="14.06rpx" color="#434C59" />
          {{ title }}
        </view>
      </slot>
    </view>
    <view class="right-content" @click="emit('rightClick')">
      <slot name="right" />
    </view>
  </view>
</template>
<script setup>
  import { toRefs } from 'vue';
  const props = defineProps({
    title: {
      type: String,
      default: ''
    },
    background: {
      type: String,
      default: '#FFFFFF'
    },
    fixed: {
      type: Boolean,
      default: true
    },
    borderBottom: {
      type: Boolean,
      default: true
    }
  });
  const { background, fixed, borderBottom } = toRefs(props);
  const emit = defineEmits(['leftClick', 'rightClick']);
</script>

<style lang="scss" scoped>
.nav-bar-container {
  display: flex;
  width: 100%;
  height: 46.88rpx;
  padding: 14.65rpx 9.38rpx;
  box-sizing: border-box;
  justify-content: space-between;
  align-items: center;
  background: v-bind("background");

  &-fixed {
    position: fixed;
    top: 0;
    left: 0;
    z-index: 10;
  }

  .left-content {
    .title-box {
      font-size: 15.23rpx;
      color: #18191a;
      line-height: 17.58rpx;
      height: 17.58rpx;
    }
  }

  .border-bottom {
    border: 1px solid #e1e3e5;
  }
}
</style>
