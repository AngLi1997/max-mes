<!-- 分拣类型统计 -->
<template>
  <DubRowTable ref="dubTableRef" :leftTableProps="leftTableProps" :rightTableProps="rightTableProps"></DubRowTable>
</template>

<script setup lang="ts">
  import DubRowTable from '@/components/DubRowTable/index.vue';
  import { useDubTable } from './hooks/useDubTable';

  defineOptions({
    name: 'SortingTypeStatistics',
    inheritAttrs: false,
  });

  const { dubTableRef, leftTableProps, rightTableProps } = useDubTable();
</script>

<style lang="less" scoped></style>
