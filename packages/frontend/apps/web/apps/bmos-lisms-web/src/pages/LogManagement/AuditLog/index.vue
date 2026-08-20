<!-- 审核日志 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :hideRightTree="true"
    :paginations="[paginationBig]"
    :formProps="[formFirstProps]"
    :requests="[postLogAuditPage as any]"
    :showToolBars="[false]"
    :columns="[columnsFirst]"></BMPageComponent>
</template>

<script setup lang="tsx">
  import { BMPageComponent } from '@bmos/components';
  import { useTable } from './hooks';
  import { postLogAuditPage } from '@/services';
  import { paginationBig } from '@/utils';

  defineOptions({
    name: 'AuditLog',
    inheritAttrs: false,
  });

  const { columnsFirst, pageRef, formFirstProps } = useTable();
</script>
