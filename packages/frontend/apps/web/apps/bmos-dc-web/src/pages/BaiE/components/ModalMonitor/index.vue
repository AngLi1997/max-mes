<!-- 实时监控 -->
<template>
  <div class="monitor">
    <ModalTitle :title="t('实时监控')" icon="monitor" @close="emit('close')"></ModalTitle>
    <div class="monitor-content">
      <!-- <VideoComponent :code="code"></VideoComponent> -->
      <img
        :src="images[`/src/assets/baiePng/${currentLng}/defaultMonitor.png`]?.default"
        alt=""
        srcset=""
        width="100%"
        height="100%" />
    </div>
  </div>
</template>

<script setup lang="ts">
  import ModalTitle from '@/pages/BaiE/components/ModalComponents/ModalTitle.vue';
  // import VideoComponent from './components/videoComponent.vue';
  import { t, currentLng } from '@bmos/i18n';
  import { defineEmits } from 'vue';

  const images = import.meta.glob('@/assets/baiePng/*/*.png', { eager: true });

  defineProps({
    code: {
      type: String,
      default: '',
    },
  });
  const emit = defineEmits(['close']);
</script>

<style lang="less" scoped>
  .monitor {
    position: fixed;
    top: calc(50% - 230px);
    left: calc(50% - 320px);
    width: 640px;
    .monitor-content {
      width: 100%;
      height: 420px;
      padding: 12px 10px;
      box-sizing: border-box;
      background: rgba(31, 37, 51, 0.9);
      border-width: 0px 1px 1px 1px;
      border-style: solid;
      border-color: #364159;
      border-radius: 0px 0px 8px 8px;
      color: #fff;
    }
  }
</style>
