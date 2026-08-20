<!-- 生产审核进度 -->
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
        id: 'id',
        categoryFlag: 'categoryFlag',
      },
    }"
    :requests="[getProductProgressList as any]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <BMTableTitle :title="t('生产审核进度')"></BMTableTitle>
    </template>
  </BMPageComponent>
</template>

<script setup lang="tsx">
  import { t } from '@bmos/i18n';
  import { BMPageComponent, BMTableTitle } from '@bmos/components';
  import { useTable } from './hooks';
  import { reqPlanInfoProductionAuditProgressPage } from '@/services';

  const getProductProgressList = async (params: any) => {
    const { id, ...newParams }: any = params;
    if (id === 'all' || !id) {
      return await reqPlanInfoProductionAuditProgressPage({
        ...newParams,
        id: 0,
        categoryFlag: true,
      });
    }
    return await reqPlanInfoProductionAuditProgressPage({ id, ...newParams });
  };

  const { columnsFirst, formFirstProps, pageRef, treeData } = useTable();
</script>
