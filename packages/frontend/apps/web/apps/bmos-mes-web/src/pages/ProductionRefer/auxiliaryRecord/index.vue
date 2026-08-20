<!-- 辅助记录 -->
<template>
  <KeepAlive>
    <BMPageComponent
      v-if="!showView"
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
  <Detail v-if="showView" :rowData="rowData" @close="showView = false"></Detail>
</template>

<script setup lang="ts">
  import { BMPageComponent } from '@bmos/components';
  import { useTables } from './hooks/useData';
  import Detail from './component/detail.vue';

  const { requests, columns, treeData, pageRef, rowData, formFirstProps, showView, treeSelect } = useTables();
</script>

<style scoped lang="less"></style>
