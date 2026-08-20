<!-- 详情 -->
<template>
  <OutboundDetail :title="t('投料出库计划详情')" :type="rowData.type?.value" :rowData="rowData"></OutboundDetail>
</template>

<script setup lang="ts">
  import OutboundDetail from '@/components/OutboundDetail/index.vue';
  import { t } from '@bmos/i18n';

  const route = useRoute();

  const rowData = computed<any>(() => {
    return route.query.rowData ? JSON.parse(route.query.rowData as string) : {};
  });
</script>

<style scoped></style>
