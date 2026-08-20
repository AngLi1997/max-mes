<!-- 库存管理 -->
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
        positionId: 'id',
      },
    }"
    :tableFields="[
      {},
      {
        field: {
          inventoryBatchId: 'id',
        },
      },
    ]"
    :requests="[reqMaterialListReq as DataRequestFn, reqMaterialBatchListReq as DataRequestFn]"
    :columns="[columnsFirst, columnsSecond]"
    @tree-select="treeSelect">
    <template #tableHeaderToolbar0="{ treeNode }">
      <Inbound
        v-model:open="inboundOpen"
        :rowData="firstRowData"
        :treeData="treeData"
        :treeNode="treeNode"
        @updateTable="updateTable" />
      <MaterialView v-model:open="materialViewOpen" :rowData="firstRowData" />
      <Button v-hasAuth="150020001000001" type="primary" @click="openInboundModal">
        {{ t('货品入库') }}
      </Button>
    </template>

    <template #tableHeaderToolbar1="{ currentNodes }">
      <Outbound
        v-model:open="outboundOpen"
        :inventoryList="inventoryList"
        :currentNodes="currentNodes"
        :treeId="curSelect"
        @updateTable="updateTable" />

      <Move
        v-model:open="moveOpen"
        :currentNodes="currentNodes"
        :inventoryList="inventoryList"
        :treeData="treeData"
        :curSelect="curSelect"
        @updateTable="updateTable" />
      <Check
        v-model:open="checkOpen"
        :rowData="secondRowData"
        :currentNodes="currentNodes"
        @updateTable="updateTable" />
      <MaterialPartsView
        v-model:open="materialPartsViewOpen"
        :rowData="secondRowData"
        :treeData="treeData"
        :currentNodes="currentNodes" />
      <Button
        v-hasAuth="150020001000003"
        type="primary"
        @click="
          () => {
            outboundModal(currentNodes);
          }
        ">
        {{ t('货品出库') }}
      </Button>
      <Button v-hasAuth="150020001000004" type="primary" @click="() => openMoveModal(currentNodes)">
        {{ t('货品移库') }}
      </Button>
    </template>
    <template #tableHeaderTitle0>
      <BMTableTitle :title="t('货品批次')"></BMTableTitle>
    </template>
    <template #tableHeaderTitle1>
      <BMTableTitle :title="t('货品件')"></BMTableTitle>
    </template>
  </BMPageComponent>
</template>

<script lang="ts" setup>
  import { t } from '@bmos/i18n';
  import { reqStorageCargoBatchPage, reqStorageCargoPage } from '@/services';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent, BMTableTitle } from '@bmos/components';
  import Inbound from './components/InboundModel.vue';
  import Outbound from './components/Outbound.vue';
  import Move from './components/Move.vue';
  import Check from './components/Check.vue';
  import MaterialView from './components/MaterialView.vue';
  import MaterialPartsView from './components/MaterialPartsView.vue';

  const reqMaterialListReq = async (params: any) => {
    const { positionId, ...newParams }: any = params;
    if (positionId !== 'all' && positionId) {
      newParams.positionId = positionId;
    }
    return await reqStorageCargoBatchPage(newParams);
  };

  const curSelect = ref<any>({});
  const treeSelect = (node: any, info: any) => {
    curSelect.value = info.node;
  };

  const reqMaterialBatchListReq = async (params: any) => {
    if (!params.inventoryBatchId) return Promise.resolve({ data: [] });
    const newParams = { ...params };
    if (curSelect.value.id !== 'all') {
      newParams.positionId = curSelect.value.id;
    }
    return await reqStorageCargoPage(newParams);
  };

  const {
    columnsFirst,
    formFirstProps,
    firstRowData,
    columnsSecond,
    secondRowData,
    inventoryList,
    pageRef,
    treeData,
    updateTable,
    // 货品入库
    inboundOpen,
    openInboundModal,

    // 货品出库
    outboundOpen,
    outboundModal,

    // 查看
    materialViewOpen,
    materialPartsViewOpen,

    // 货品移库
    moveOpen,
    openMoveModal,
    // 盘点
    checkOpen,
  } = useTable({ curSelect });
</script>
