<template>
  <view class="layout" @click="closeOutside">
    <slot />
    <wd-message-box />
    <wd-toast />
    <wd-notify :safe-height="90" />

    <!-- 全屏Loading -->
    <BMLoading
      :visible="loading"
      :text="loadingText"
      :mask-closable="maskClosable"
      @close="closeLoading"
    />
  </view>
</template>

<script setup>
import { useQueue } from '@/uni_modules/wot-design-uni';
import { useNotify } from 'wot-design-uni';
import BMLoading from '../Loading/index.vue';

// 定义 props
const _props = defineProps({
  loading: {
    type: Boolean,
    default: false,
  },
  loadingText: {
    type: String,
    default: '加载中...',
  },
  maskClosable: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(['closeLoading']);

// eslint-disable-next-line no-unused-vars, unused-imports/no-unused-vars
const { showNotify, closeNotify } = useNotify();
const { closeOutside } = useQueue();

const closeLoading = () => {
  emit('closeLoading');
};
</script>

<style lang="scss">
.layout {
  height: 100%;
  width: 100%;
}
</style>
