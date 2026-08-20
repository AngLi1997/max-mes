<!-- 生产进度 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :showAllAddIcon="false"
    :showAction="false"
    :rowKeys="['productPlanId']"
    :treeData="treeData"
    :formProps="[formFirstProps]"
    :fieldNames="{
      title: 'showName',
      key: 'id',
    }"
    :treeField="{
      field: {
        productId: 'id',
        categoryFlag: 'categoryFlag',
      },
    }"
    :requests="[getProductProgressList as any]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <BMTableTitle :title="t('生产进度')"></BMTableTitle>
    </template>
  </BMPageComponent>
</template>

<script setup lang="tsx">
  import { t } from '@bmos/i18n';
  import { BMPageComponent, BMTableTitle } from '@bmos/components';
  import { useTable } from './hooks';
  import { reqFlowPlanProgressPage } from '@/services';

  const getProductProgressList = async (params: any) => {
    const { productId, categoryFlag, ...newParams }: any = params;
    if (productId === 'all') {
      return await reqFlowPlanProgressPage(newParams);
    }
    if (categoryFlag) {
      return await reqFlowPlanProgressPage({ productCategoryId: productId, ...newParams });
    }
    return await reqFlowPlanProgressPage({ productId, ...newParams });
  };

  const { columnsFirst, formFirstProps, pageRef, treeData } = useTable();
</script>
