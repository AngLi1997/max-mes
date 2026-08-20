<!-- 物料追溯 -->
<template>
  <KeepAlive>
    <BMPageComponent
      v-if="showType == 'page'"
      ref="pageRef"
      :columns="columns"
      :requests="requests"
      :showAllAddIcon="false"
      :showAction="false"
      :rowKeys="['id', 'id']"
      :search="[true, false]"
      :treeData="treeData"
      :formProps="[formFirstProps]"
      :fieldNames="{
        title: 'showName',
        key: 'id',
      }"
      :treeField="{
        field: {
          productId: 'id',
        },
      }"
      :tableFields="[
        {},
        {
          field: {
            datasetId: 'id',
          },
        },
      ]"
      @tree-select="treeSelect"></BMPageComponent>
  </KeepAlive>
  <!-- 物料详情 -->
  <MaterialDetails v-if="showType == 'detail'" v-model:showType="showType" :row-data="rowData" />
  <!-- 物料谱系 -->
  <MaterialLineage v-if="showType == 'lineage'" v-model:showType="showType" :row-data="rowData" />
</template>

<script setup lang="ts">
  import { BMPageComponent } from '@bmos/components';
  import { useTables } from './hooks/index';
  import MaterialDetails from './component/MaterialDetails/index.vue';
  import MaterialLineage from './component/MaterialLineage/index.vue';

  const { requests, columns, treeData, pageRef, formFirstProps, showType, rowData, treeSelect } = useTables();
</script>

<style scoped lang="less"></style>
