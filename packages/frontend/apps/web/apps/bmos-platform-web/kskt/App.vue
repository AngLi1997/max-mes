<template>
  <BMConfigProvider prefixCls="kskt">
    <Scada1 v-if="show" @change="change"></Scada1>
    <Scada2 v-else @change="change"></Scada2>
  </BMConfigProvider>
</template>

<script setup lang="ts">
  import { ref } from 'vue';
  import Scada1 from './scada1/index.vue';
  import Scada2 from './scada2/index.vue';
  import { BMConfigProvider } from '@bmos/components';

  const show = ref(true);
  const change = () => {
    show.value = !show.value;
  };
</script>

<style scoped></style>
