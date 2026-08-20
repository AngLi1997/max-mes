<template>
  <div class="video-popup">
    <div class="video-popup-title">
      <span>视频预览</span>
      <div class="video-popup-title-close" @click="close">
        <img src="@/assets/img/close.png" alt="" />
      </div>
    </div>
    <div class="video-content">
      <videoComponent ref="video" :mode="0" :code="code" />
    </div>
  </div>
</template>

<script setup lang="ts">
  import videoComponent from './videoComponent.vue';
  const props = defineProps({
    code: {
      type: String,
      default: '',
    },
  });

  const emit = defineEmits(['close']);
  function close() {
    emit('close');
  }
</script>

<style lang="less" scoped>
  .video-popup {
    width: 640px;
    height: 360px;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    .video-popup-title {
      width: 100%;
      height: 40px;
      padding: 0 12px 0 37px;
      background-image: url('@/assets/img/video-title-bg.png');
      background-size: 100% 100%;
      display: flex;
      align-items: center;
      justify-content: space-between;
      font-size: 16px;
      color: #fff;
      .video-popup-title-close {
        cursor: pointer;
        img {
          width: 20px;
          height: 20px;
        }
      }
    }
    .video-content {
      border-radius: 0 0 8px 8px;
      background: linear-gradient(0deg, rgba(38, 42, 51, 0.8), rgba(38, 42, 51, 0.8)),
        linear-gradient(0deg, rgba(0, 0, 0, 0.2), rgba(0, 0, 0, 0.2));
      width: 100%;
      height: 320px;
      padding: 12px 9px;
      box-sizing: border-box;
    }
  }
</style>
