<!-- 检验结果 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :showToolBars="[true]"
    :tableFields="[
      {
        default: { flag: 1 },
      },
    ]"
    :titles="[t('检验结果')]"
    :formProps="[formFirstProps]"
    :requests="[getExaminationResultPageList as DataRequestFn]"
    :paginations="[paginationFirst]"
    :columns="[columnsFirst]"></BMPageComponent>
</template>

<script setup lang="ts">
  import { getExaminationResultPageList } from '@/services';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'ExperimentalResult',
  });

  const { pageRef, columnsFirst, formFirstProps, paginationFirst } = useTable();
</script>

<style lang="less" scoped></style>
