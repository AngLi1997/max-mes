<script setup lang="ts">
  import { BMConfigProvider } from '@bmos/components';
  import { useConfig } from './stores/config';
  import { getLang } from './utils/i18n';
  import { usePlasmaStation } from './stores/plasmaStation';
  import { useDict } from './stores/dictStore';

  const router = useRouter();
  const routerMap = ref<any>({});

  const { setPlasmaStation } = usePlasmaStation();
  const { setImmunityTypeDict } = useDict();
  const { findConfigByCode } = useConfig();

  const configLoaded = ref(false);

  onBeforeMount(async () => {
    router.getRoutes().forEach((item: any) => {
      console.log('item', item);
      if (item.meta?.id) {
        routerMap.value[item.meta?.id] = item.path;
      }
    });
    await findConfigByCode();
    configLoaded.value = true;
  });

  onMounted(async () => {
    await setPlasmaStation();
    await setImmunityTypeDict();
    // 监听message事件
    window.addEventListener('message', event => {
      if (event.data.menuKey === '/app/bmos-bsms/') {
        router.push({ path: routerMap.value[event.data.menuId] });
      }
    });
  });
</script>

<template>
  <BMConfigProvider v-if="configLoaded" prefixCls="bsms" :lang="getLang()">
    <router-view></router-view>
  </BMConfigProvider>
</template>
<style scoped>
  header {
    max-height: 100vh;
    line-height: 1.5;
  }
</style>
