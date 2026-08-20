<!-- 检验数据记录 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :hideRightTree="true"
    :paginations="[paginationBig]"
    :formProps="[formFirstProps]"
    :requests="[postLogInspectRecordPage as any]"
    :showToolBars="[false]"
    :columns="[columnsFirst]"></BMPageComponent>
</template>

<script setup lang="tsx">
  import { BMPageComponent } from '@bmos/components';
  import { useTable } from './hooks';
  import { postLogInspectRecordPage } from '@/services';
  import { paginationBig } from '@/utils';

  defineOptions({
    name: 'InspectionDataRecord',
    inheritAttrs: false,
  });

  const { columnsFirst, pageRef, formFirstProps } = useTable();
</script>
