<template>
  <div class="modal-title">
    <img class="modal-icon" :src="iconUrl" alt="" width="26px" height="26px" />
    {{ title }}
    <img class="modal-close-icon" :src="closeIconUrl" alt="" width="12px" height="12px" @click="$emit('close')" />
  </div>
</template>

<script setup lang="ts">
  const images = import.meta.glob('@/assets/baiePng/*.png', { eager: true });
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const props = defineProps({
    title: {
      type: String,
      default: '',
    },
    icon: {
      type: String,
      default: 'process',
    },
  });
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const emits = defineEmits(['close']);
  const iconUrl = computed(() => {
    return images[`/src/assets/baiePng/modal-${props.icon}-icon.png`]?.default;
  });
  const closeIconUrl = computed(() => {
    return images[`/src/assets/baiePng/modal-close-icon.png`]?.default;
  });
</script>

<style lang="less" scoped>
  .modal-title {
    width: 100%;
    height: 40px;
    display: flex;
    align-items: center;
    background-image: url('@/assets/baiePng/modal-title-bg.png');
    background-size: 100% 100%;
    font-family: Source Han Sans CN;
    font-weight: 500;
    font-size: 16px;
    color: #fff;
    .modal-icon {
      margin: 0 10px 0 30px;
    }
    .modal-close-icon {
      position: absolute;
      right: 14px;
      top: 14px;
      cursor: pointer;
    }
  }
</style>
