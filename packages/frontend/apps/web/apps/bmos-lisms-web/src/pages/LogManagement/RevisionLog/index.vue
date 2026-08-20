<!-- 修约日志 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :hideRightTree="true"
    :paginations="[paginationBig]"
    :formProps="[formFirstProps]"
    :requests="[postLogRoundingPage as any]"
    :showToolBars="[false]"
    :columns="[columnsFirst]"></BMPageComponent>
</template>

<script setup lang="tsx">
  import { BMPageComponent } from '@bmos/components';
  import { useTable } from './hooks';
  import { postLogRoundingPage } from '@/services';
  import { paginationBig } from '@/utils';

  defineOptions({
    name: 'RevisionLog',
    inheritAttrs: false,
  });

  const { columnsFirst, pageRef, formFirstProps } = useTable();
</script>
