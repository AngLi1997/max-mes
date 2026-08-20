<script setup lang="ts">
  import { BMConfigProvider } from '@bmos/components';
  import { useConfig } from './stores/config';
  import { getLang } from './utils/i18n';
  import { getTeleport } from '@antv/x6-vue-shape';
  import { usePermissionStore } from '@/stores/permission';

  const router = useRouter();
  const routerMap = ref<any>({});

  const { findConfigByCode } = useConfig();

  onBeforeMount(async () => {
    router.getRoutes().forEach((item: any) => {
      if (item.meta?.id) {
        routerMap.value[item.meta?.id] = item.path;
      }
    });
    // 监听message事件
    window.addEventListener('message', event => {
      if (event.data.menuKey === '/app/bmos-mes/') {
        router.push({ path: routerMap.value[event.data.menuId] });
      }
    });
  });

  const { hasPermission } = usePermissionStore();

  onMounted(() => {
    findConfigByCode();
  });
  const TeleportContainer = getTeleport();
</script>

<template>
  <BMConfigProvider prefixCls="mes" :lang="getLang()" :bmosProps="{ hasPermission }">
    <router-view></router-view>
    <TeleportContainer />
  </BMConfigProvider>
</template>
<style scoped>
  header {
    max-height: 100vh;
    line-height: 1.5;
  }
</style>
