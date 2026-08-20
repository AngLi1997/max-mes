<!-- 核查查询 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :showToolBars="[true]"
    :titles="[t('核查结果明细')]"
    :formProps="[formFirstProps]"
    :requests="[getCheckQueryList as DataRequestFn]"
    :paginations="[paginationFirst]"
    :columns="[columnsFirst]"></BMPageComponent>
</template>

<script setup lang="ts">
  import { getCheckQueryList } from '@/services';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'CheckQuery',
  });

  const { pageRef, columnsFirst, formFirstProps, paginationFirst } = useTable();

  const route = useRoute();

  onActivated(() => {
    pageRef.value.getQueryFormRef().setFormModels(route.query);
    pageRef.value.fetchData(0);
  });

  onMounted(() => {
    pageRef.value.getQueryFormRef().setFormModels(route.query);
  });
</script>

<style scoped></style>
