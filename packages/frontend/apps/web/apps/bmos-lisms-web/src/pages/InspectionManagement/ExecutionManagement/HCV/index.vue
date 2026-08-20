<!-- HCV -->
<template>
  <Page :inspectItem />
</template>

<script setup lang="ts">
  import { InspectionProjectEnum } from '@/types';
  import Page from '../components/Page.vue';
  import { Recordable } from '@bmos/components';

  defineOptions({
    name: 'HCV',
  });

  const { InspectionProjectDict } = getDicts();

  const inspectItem = InspectionProjectDict.find(
    (item: any) => item.value === InspectionProjectEnum.AntiHCV,
  ) as Recordable;
</script>

<style scoped></style>
