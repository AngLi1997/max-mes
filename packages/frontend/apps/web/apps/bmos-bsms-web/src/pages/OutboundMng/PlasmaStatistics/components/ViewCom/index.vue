<!-- 详情 -->
<template>
  <OutboundDetail
    :title="t('出库计划详情')"
    :type="props.rowData.type?.value"
    :rowData="props.rowData"></OutboundDetail>
</template>

<script setup lang="ts">
  import OutboundDetail from '@/components/OutboundDetail/index.vue';
  import { t } from '@bmos/i18n';

  const props = defineProps({
    rowData: {
      type: Object,
      default: () => {},
    },
  });
</script>

<style scoped></style>
