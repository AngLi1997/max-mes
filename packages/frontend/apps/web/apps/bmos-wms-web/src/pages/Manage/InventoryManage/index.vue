<!-- 货品管理 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :showAllAddIcon="false"
    :showAction="false"
    :rowKeys="['id', 'id']"
    :treeData="treeData"
    :search="[true, false]"
    :formProps="[formFirstProps, {}]"
    :fieldNames="{
      title: 'name',
      key: 'id',
      children: 'children',
    }"
    :treeField="{
      field: {
        id: 'id',
        isCategory: 'isCategory',
      },
    }"
    :tableFields="[
      {},
      {
        field: {
          cargoId: 'id',
        },
      },
    ]"
    :requests="[reqMaterialListReq as DataRequestFn, reqMaterialBatchListReq as DataRequestFn]"
    :columns="[columnsFirst, columnsSecond]">
    <template #tableHeaderToolbar1="{ currentNodes }">
      <AddInventoryBatchModal
        v-model:open="addInventoryBatchModalOpen"
        :rowData="secondRowData"
        :treeData="treeData"
        :currentNodes="currentNodes"
        @updateTable="updateTable" />
      <EditInventoryBatchModal
        v-model:open="editInventoryBatchModalOpen"
        :rowData="secondRowData"
        :treeData="treeData"
        :currentNodes="currentNodes"
        @updateTable="updateSecondTable" />
      <viewInventoryBatchModal
        v-model:open="viewInventoryBatchModalOpen"
        :rowData="secondRowData"
        :treeData="treeData"
        :currentNodes="currentNodes" />
      <Button v-hasAuth="150020002000004" type="primary" @click="addInventoryBatch">
        {{ t('新增批次') }}
      </Button>
    </template>
    <template #tableHeaderTitle0>
      <BMTableTitle :title="t('货品库存')"></BMTableTitle>
    </template>
    <template #tableHeaderTitle1>
      <BMTableTitle :title="t('货品批次')"></BMTableTitle>
    </template>
  </BMPageComponent>
</template>

<script lang="ts" setup>
  import { t } from '@bmos/i18n';
  import { reqInventoryInventoryPageByCargoId, reqInventoryBatchPageByCargoIds } from '@/services';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent, BMTableTitle } from '@bmos/components';
  import AddInventoryBatchModal from './components/AddInventoryBatchModal.vue';
  import EditInventoryBatchModal from './components/EditInventoryBatchModal.vue';
  import viewInventoryBatchModal from './components/ViewInventoryBatchModal.vue';

  const reqMaterialListReq = async (params: any) => {
    const { id, isCategory, ...newParams }: any = params;
    if (id !== 'all') {
      if (isCategory) {
        newParams.cargoCategoryId = id;
      } else {
        newParams.cargoId = id;
      }
    }
    return await reqInventoryInventoryPageByCargoId(newParams);
  };

  const reqMaterialBatchListReq = async (params: any) => {
    if (!params.cargoId) return Promise.resolve({ data: [] });
    return await reqInventoryBatchPageByCargoIds(params);
  };

  const {
    updateTable,
    updateSecondTable,
    columnsFirst,
    formFirstProps,
    columnsSecond,
    secondRowData,
    pageRef,
    treeData,
    // 新增批次
    addInventoryBatchModalOpen,
    addInventoryBatch,
    // 编辑货品批次
    editInventoryBatchModalOpen,
    // 查看货品批次
    viewInventoryBatchModalOpen,
  } = useTable({});
</script>
