<!-- 标本库存预警 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :showToolBars="[true]"
    :titles="[t('预警数据')]"
    :formProps="[formFirstProps]"
    :rowClassNames="[
      (data: any) => {
        return data.remainingTime === 0 ? 'row-color' : '';
      },
    ]"
    :requests="[getSampleInventoryWarningList as DataRequestFn]"
    :paginations="[paginationFirst]"
    :columns="[columnsFirst]"></BMPageComponent>
</template>

<script setup lang="ts">
  import { getSampleInventoryWarningList } from '@/services';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'sample-stock-warning',
    inheritAttrs: false,
  });

  const { pageRef, columnsFirst, formFirstProps, paginationFirst } = useTable();
</script>

<style lang="less" scoped>
  :deep(.row-color) {
    // background-color: #ffecd8;
    color: red;
  }
</style>
