<template>
  <BMPageComponent 
    :data="[tableData, tableData]"
    :columns="[columns, columns]"
  />
</template>
<script lang="tsx" setup>
  import { BMPageComponent } from '@bmos/components';
  import { useTable } from './hooks/useTable';

  const { tableData, columns } = useTable();

</script>
