<script setup lang="ts">
  import { BMConfigProvider } from '@bmos/components';
  import { useConfig } from './stores/config';
  import { getLang } from './utils/i18n';

  const { findConfigByCode } = useConfig();
  onMounted(async () => {
    findConfigByCode();
  });
</script>

<template>
  <BMConfigProvider prefixCls="dc" :lang="getLang()">
    <router-view></router-view>
  </BMConfigProvider>
</template>
<style scoped>
  header {
    max-height: 100vh;
    line-height: 1.5;
  }
</style>
