<template>
  <NormalModalForm
    v-model:open="videoOpen"
    :title="t('监控')"
    destroyOnClose
    wrap-class-name="modalSizeExtraLarge step-video-modal"
    :footer="null">
    <video class="step-video" src="/video.mp4" autoplay controls></video>
  </NormalModalForm>
</template>

<script setup lang="tsx">
  import { computed } from 'vue';
  import { t } from '@bmos/i18n';
  import { NormalModalForm } from '@bmos/components';

  const emits = defineEmits(['update:open']);
  const props = defineProps({
    open: {
      type: Boolean,
      default: false,
    },
  });

  const videoOpen = computed<boolean>({
    get() {
      return props.open;
    },
    set(val) {
      emits('update:open', val);
    },
  });
</script>

<style lang="less">
  .step-video-modal {
    .mes-modal {
      transform: translateX(-100%);
      .mes-modal-body {
        margin: 0;
        .step-video {
          width: 100%;
          height: 100%;
          display: block;
          max-width: 1010px;
          max-height: 568px;
          background-color: #000;
        }
      }
    }
    .mes-modal-content {
      width: 1010px;
      height: 568px;
    }
  }
</style>
