<template>
  <div class="bai-e-container">
    <BMScreenFrame ref="thingjsRef" :url="url" @receive-message="onMessage">
      <BaiELayout ref="layoutRef" @sendMessage="click"></BaiELayout>
    </BMScreenFrame>
  </div>
</template>

<script setup>
  import { BMScreenFrame } from '@bmos/components';
  import BaiELayout from './BaiELayout.vue';
  import { useConfig } from '@/stores/config';
  import { storeToRefs } from 'pinia';

  const store = useConfig();
  const { configs } = storeToRefs(store);

  const baieModelUrlCode = 'platform.dc.baie.modelUrl';
  const url = ref('');
  const thingjsRef = ref();
  const layoutRef = ref();
  function onMessage(data) {
    layoutRef?.value.onMessage(data);
  }
  function click(data) {
    thingjsRef.value.sendMessage({
      data,
    });
  }
  onMounted(async () => {
    if (!configs.value[baieModelUrlCode]) {
      await store.findConfigByCode(baieModelUrlCode);
    }
    url.value =
      configs.value[baieModelUrlCode]?.value ||
      'https://www.thingjs.com/s/e99f06b66498234f53172d00?params=105b0f77fd24654d4eebc434e9';
  });
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
