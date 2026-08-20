<template>
  <div class="bai-e-container">
    <BMScreenFrame ref="thingjsRef" :url="url" @receive-message="onMessage">
      <VideoPopup v-if="videoShow" :key="code" :code="code" @close="close"></VideoPopup>
    </BMScreenFrame>
  </div>
</template>

<script setup>
  import { BMScreenFrame } from '@bmos/components';
  import VideoPopup from './components/videoPopup.vue';
  import { getItem } from '@/utils';

  const videoShow = ref(false);
  const code = ref('');
  const thingjsRef = ref();
  function onMessage(data) {
    if (data.fn === 'showVideo') {
      code.value = data.code;
      videoShow.value = true;
    }
  }
  const token = getItem('BMOS-ACCESS-TOKEN');
  const url = `http://www.thingjs.com/s/ec69cf5923986db9aebcec37?token=${token}`;
  function close() {
    videoShow.value = false;
    thingjsRef.value.sendMessage({
      data: {
        fn: 'closeVideo',
        code: code.value,
      },
    });
    code.value = '';
  }
</script>
<style lang="less">
  .dc-content {
    padding: 0px !important;
  }
</style>
<style lang="less" scoped>
  .bai-e-container {
    width: 100%;
    height: 100%;
  }
</style>
