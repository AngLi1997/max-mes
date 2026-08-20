<script setup lang="ts">
  import { BMConfigProvider } from '@bmos/components';
  import { useConfig } from './stores/config';
  import { getLang } from './utils/i18n';
  import { usePlasmaStation } from './stores/plasmaStation';
  import { useDict } from './stores/dictStore';

  const { setPlasmaStation } = usePlasmaStation();
  const { setImmunityTypeDict } = useDict();
  const { findConfigByCode } = useConfig();

  onMounted(async () => {
    await setPlasmaStation();
    await setImmunityTypeDict();
    await findConfigByCode();
  });
</script>

<template>
  <BMConfigProvider prefixCls="bims" :lang="getLang()">
    <router-view></router-view>
  </BMConfigProvider>
</template>
<style scoped>
  header {
    max-height: 100vh;
    line-height: 1.5;
  }
</style>
