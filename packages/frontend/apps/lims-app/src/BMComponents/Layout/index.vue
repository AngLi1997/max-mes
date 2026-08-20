<template>
  <view class="layout" @click="closeOutside">
    <slot />
    <wd-message-box />
    <wd-toast />
    <wd-notify :safe-height="90" />
  </view>
</template>

<script setup>
import { useQueue } from '@/uni_modules/wot-design-uni';
import { onUnmounted } from 'vue';
import { useNotify } from 'wot-design-uni';

// eslint-disable-next-line no-unused-vars, unused-imports/no-unused-vars
const { showNotify, closeNotify } = useNotify();
const { closeOutside } = useQueue();

onUnmounted(() => {
  // closeNotify();
});
</script>

<style lang="scss">
  .layout {
  height: 100%;
  width: 100%;
}
</style>
