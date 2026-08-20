<!-- 货位日志 -->
<template>
  <BMPageComponent
    :showAllAddIcon="false"
    :showAction="false"
    :treeData="treeData"
    :fieldNames="{
      title: 'name',
      key: 'id',
    }"
    :treeField="{
      field: {
        positionOrStorageId: 'id',
      },
    }"
    :rowKeys="['id']"
    :requests="[getCargoSpaceLogPage as DataRequestFn]"
    :titles="[t('货位日志')]"
    :columns="[columnsFirst]"
    :search="[true]"
    :formProps="[formFirstProps]"></BMPageComponent>
</template>

<script setup lang="tsx">
  import { t } from '@bmos/i18n';
  import { BMPageComponent, DataRequestFn } from '@bmos/components';
  import { useTable } from './hooks';
  import { reqLogPositionPage } from '@/services';

  const { columnsFirst, formFirstProps, treeData } = useTable();

  const getCargoSpaceLogPage = (params: any) => {
    return reqLogPositionPage({
      ...params,
      positionOrStorageId: params.positionOrStorageId === 'all' ? void 0 : params.positionOrStorageId,
    });
  };
</script>
