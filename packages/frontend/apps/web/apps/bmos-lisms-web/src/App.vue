<script setup lang="ts">
  import { BMConfigProvider } from '@bmos/components';
  import { getLang } from './utils/i18n';
  import { usePlasmaStation, useConfig } from '@/stores';

  const router = useRouter();
  const routerMap = ref<any>({});

  const { setPlasmaStation } = usePlasmaStation();
  const { refreshConfig } = useConfig();

  onBeforeMount(async () => {
    router.getRoutes().forEach((item: any) => {
      if (item.meta?.id) {
        routerMap.value[item.meta?.id] = item.path;
      }
    });

    // 监听message事件
    window.addEventListener('message', event => {
      if (event.data.menuKey === '/app/bmos-lisms/') {
        router.push({ path: routerMap.value[event.data.menuId] });
      }
    });
  });

  onMounted(async () => {
    await setPlasmaStation();
    await refreshConfig();
  });
</script>

<template>
  <BMConfigProvider prefixCls="lisms" :lang="getLang()">
    <router-view></router-view>
  </BMConfigProvider>
</template>
<style scoped>
  header {
    max-height: 100vh;
    line-height: 1.5;
  }
</style>
