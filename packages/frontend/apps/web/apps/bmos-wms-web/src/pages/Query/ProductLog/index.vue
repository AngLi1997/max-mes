<!-- 货品日志 -->
<template>
  <BMPageComponent
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :showIndexs="[true]"
    :showToolBars="[false]"
    :showSearchBorders="[false]"
    :formProps="[formFirstProps]"
    :requests="[reqLogCargoPage as DataRequestFn]"
    :columns="[columnsFirst]"></BMPageComponent>
</template>

<script lang="ts" setup>
  import { reqLogCargoPage } from '@/services';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';

  const { columnsFirst, formFirstProps } = useTable({});
</script>
