<!-- 分拣任务 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :titles="[t('分拣任务')]"
    :showHeader="[false]"
    :showToolBars="[true]"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :requests="[getSortingTaskList as DataRequestFn]"
    :columns="[columnsFirst]"></BMPageComponent>
</template>

<script setup lang="ts">
  import { getSortingTaskList } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'SortingTask',
    inheritAttrs: false,
  });

  const router = useRouter();

  const enterView = (row: any) => {
    router.push({
      name: 'SortingTaskViewCom',
      query: { rowData: JSON.stringify(row) },
    });
  };

  const { pageRef, columnsFirst, formFirstProps } = useTable(enterView);
</script>

<style lang="less" scoped></style>
