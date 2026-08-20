<!-- 核查信息汇总 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :titles="[t('核查信息汇总')]"
    :showToolBars="[true]"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :requests="[getVerificationInformationSummaryList as DataRequestFn]"
    :columns="[columnsFirst]"></BMPageComponent>
</template>

<script setup lang="ts">
  import { getVerificationInformationSummaryList } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'VerificationInformationSummary',
  });

  const { pageRef, columnsFirst, formFirstProps, changeColumns } = useTable();

  onMounted(async () => {
    await nextTick();
    await changeColumns();
  });
</script>

<style lang="less" scoped></style>
