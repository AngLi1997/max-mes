<!-- 物料管理 -->
<template>
  <BMPageComponent
    ref="pageRef"
    hideRightTree
    :rowKeys="['storageMaterialBatchId', 'storageMaterialId']"
    :search="[true, false]"
    :formProps="[formFirstProps, {}]"
    :rowClassNames="[judgingDeadline as any]"
    :tableFields="[
      {},
      {
        field: {
          storageMaterialBatchId: 'storageMaterialBatchId',
        },
      },
    ]"
    :isSelects="[true, false]"
    :rowSelections="rowSelections"
    :requests="[reqStorageMaterialManageQueryBatchPage as DataRequestFn, reqMaterialListReq as DataRequestFn]"
    :columns="[columnsFirst, columnsSecond]">
    <template #tableHeaderToolbar0>
      <MaterialBatchModal
        v-model:open="materialBatchModalOpen"
        :operationType="operationType"
        :rowData="firstRowData"
        @updateTable="updateTable" />
      <Button v-hasAuth="120030007000001" type="primary" @click="addMaterialBatch">
        {{ t('新增') }}
      </Button>
    </template>
    <template #tableHeaderToolbar1="{ currentNodes }">
      <Button v-hasAuth="120030007000004" type="primary" @click="() => addMaterial(currentNodes)">
        {{ t('新增') }}
      </Button>
      <Button v-hasAuth="120030007000006" @click="print">
        {{ t('打印标签') }}
      </Button>
      <AddMaterialModal v-model:open="addMaterialModalOpen" :currentNodes="currentNodes" @updateTable="updateTable" />
      <ViewMaterialModal v-model:open="viewMaterialModalOpen" :rowData="secondRowData" />
      <InspectionDetailsModal
        ref="InspectionDetailsModalRef"
        :inspectionRowData="inspectionRowData"></InspectionDetailsModal>
      <!-- 打印标签弹框 -->
      <BMPrint
        v-model:open="printOpen"
        :getPrinter="reqGetPrintEquipment"
        :sceneId="currentNodes[0]?.categoryType === 0 ? '121001003' : '121002003'"
        @printConfirm="printConfirm"></BMPrint>
    </template>
    <template #tableHeaderTitle0>
      <BMTableTitle :title="t('物料批次')"></BMTableTitle>
    </template>
    <template #tableHeaderTitle1>
      <BMTableTitle :title="t('物料件')"></BMTableTitle>
    </template>
  </BMPageComponent>
</template>

<script lang="ts" setup>
  import { t } from '@bmos/i18n';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent, BMTableTitle, BMPrint } from '@bmos/components';
  import MaterialBatchModal from './components/MaterialBatchModal.vue';
  import AddMaterialModal from './components/AddMaterialModal.vue';
  import ViewMaterialModal from './components/ViewMaterialModal.vue';
  import InspectionDetailsModal from '@/pages/ProductionRefer/BatchTraceability/components/InspectionDetailsModal.vue';

  import {
    reqStorageMaterialManageQueryBatchPage,
    reqStorageMaterialManageQueryPage,
    reqGetPrintEquipment,
  } from '@/services';

  const reqMaterialListReq = async (params: any) => {
    const { storageMaterialBatchId }: any = params;
    if (!storageMaterialBatchId) return Promise.resolve({ data: [] });
    return reqStorageMaterialManageQueryPage(params);
  };

  const {
    columnsFirst,
    formFirstProps,
    columnsSecond,
    pageRef,
    addMaterialBatch,
    materialBatchModalOpen,
    operationType,
    firstRowData,
    secondRowData,
    updateTable,
    judgingDeadline,
    addMaterial,
    addMaterialModalOpen,
    viewMaterialModalOpen,
    rowSelections,
    print,
    printOpen,
    printConfirm,
    InspectionDetailsModalRef,
    inspectionRowData,
  } = useTable({});
</script>
