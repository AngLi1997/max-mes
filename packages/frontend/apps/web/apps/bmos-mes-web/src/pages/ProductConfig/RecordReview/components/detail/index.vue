<template>
  <Record :id="pageParams.versionId" :pageParams="pageParams"></Record>
</template>

<script setup lang="ts">
  import Record from '../record/index.vue';
  import type { Recordable } from '@bmos/components';

  withDefaults(
    defineProps<{
      pageParams: Recordable;
    }>(),
    {
      pageParams: () => ({}),
    },
  );
</script>

<style scoped lang="less"></style>
