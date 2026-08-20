<!-- 预融核对 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :titles="[t('预融核对')]"
    :showHeader="[false]"
    :showToolBars="[true]"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :requests="[getPrethawingPage as DataRequestFn]"
    :columns="[columnsFirst]"></BMPageComponent>
</template>

<script setup lang="ts">
  import { getPrethawingPage } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'PremeltingCheck',
  });

  const router = useRouter();

  const enterView = (row: any) => {
    router.push({
      name: 'PremeltingCheckViewCom',
      params: { batchNo: row.batchNo },
    });
  };

  const { pageRef, columnsFirst, formFirstProps } = useTable(enterView);
</script>

<style lang="less" scoped></style>
