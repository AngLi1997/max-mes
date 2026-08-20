<template>
  <BMPageComponent
    ref="pageRef"
    :showAllAddIcon="false"
    :showAction="false"
    :rowKeys="['id', 'id']"
    :treeData="treeData"
    :default-selected-keys="['all']"
    :search="[true, false]"
    :formProps="[formFirstProps, {}]"
    :fieldNames="{
      title: 'name',
      key: 'id',
      children: 'children',
    }"
    :treeField="{
      field: {
        materialPositionId: 'id',
      },
    }"
    :tableFields="[
      {},
      {
        field: {
          storageMaterialBatchId: 'id',
        },
      },
    ]"
    :requests="[reqMaterialListReq as DataRequestFn, reqMaterialBatchListReq as DataRequestFn]"
    :columns="[columnsFirst, columnsSecond]"
    @tree-select="treeSelect">
    <template #tableHeaderToolbar0="{ treeNode }">
      <MaterialReceiving
        v-model:open="materialReceivingOpen"
        :rowData="firstRowData"
        :treeData="treeData"
        :treeNode="treeNode"
        @updateTable="updateTable" />

      <Inbound v-model:open="inboundOpen" :treeData="treeData" :treeNode="treeNode" @updateTable="updateTable" />
      <MaterialView v-model:open="materialViewOpen" :rowData="firstRowData" />
      <Button v-hasAuth="120030008000001" type="primary" @click="openMaterialReceivingModal">
        {{ t('物料接收') }}
      </Button>
      <Button v-hasAuth="120030008000002" type="primary" @click="openInboundModal">
        {{ t('物料入库') }}
      </Button>
    </template>

    <template #tableHeaderToolbar1="{ currentNodes }">
      <span>
        <Outbound
          v-model:open="outboundOpen"
          :modalTableData="modalTableData"
          :currentNodes="currentNodes"
          @updateTable="updateTable" />
        <Move
          v-model:open="moveOpen"
          :modalTableData="modalTableData"
          :currentNodes="currentNodes"
          :treeData="treeData"
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
        <Reserve
          v-model:open="reserveOpen"
          :rowData="secondRowData"
          :currentNodes="currentNodes"
          @updateTable="updateTable" />
        <!-- 取消预定 -->
        <SignModal
          v-model:open="cancelReserveSignOpen"
          :signatureData="JSON.stringify(cancelReserveSignatureData)"
          :labelList="cancelReserveLabelList"
          showAlert
          alertType="warning"
          :alertDesc="cancelReserveAlertDesc"
          showRemark
          @signSuccess="cancelReserveSignSuccess"></SignModal>
        <!-- 打印标签弹框 -->
        <BMPrint
          v-model:open="printOpen"
          :getPrinter="reqGetPrintEquipment"
          sceneId="121002003"
          @printConfirm="printConfirm"></BMPrint>
        <!-- 退库 -->
        <SendBack
          v-model:open="sendBackOpen"
          :modalTableData="modalTableData"
          :currentNodes="currentNodes"
          @updateTable="updateTable" />
        <!-- 销毁 -->
        <Destroy
          v-model:open="destroyOpen"
          :modalTableData="modalTableData"
          :currentNodes="currentNodes"
          @updateTable="updateTable" />
        <!-- 使用 -->
        <Use
          v-model:open="useOpen"
          :modalTableData="modalTableData"
          :currentNodes="currentNodes"
          @updateTable="updateTable" />
        <!-- 拆包出库 -->
        <SplitPackage
          v-model:open="splitPackageOpen"
          :rowData="secondRowData"
          :currentNodes="currentNodes"
          @updateTable="updateTable" />
      </span>

      <Button v-hasAuth="120030008000012" @click="() => openSendBackModal(currentNodes)">
        {{ t('物料退库') }}
      </Button>
      <Button v-hasAuth="120030008000013" @click="() => openDestroyModal(currentNodes)">
        {{ t('物料销毁') }}
      </Button>
      <Button v-hasAuth="120030008000014" @click="() => openUseModal(currentNodes)">
        {{ t('物料使用') }}
      </Button>
      <Button v-hasAuth="120030008000005" @click="() => openMoveModal(currentNodes)">
        {{ t('物料移库') }}
      </Button>
      <Button
        v-hasAuth="120030008000004"
        type="primary"
        @click="
          () => {
            outboundModal(currentNodes);
          }
        ">
        {{ t('物料出库') }}
      </Button>
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
  import { reqStorageMaterialBatchPage, reqStorageMaterialPage, reqGetPrintEquipment } from '@/services';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent, BMTableTitle, BMPrint } from '@bmos/components';
  import MaterialReceiving from './components/MaterialReceiving.vue';
  import Inbound from './components/Inbound.vue';
  import Outbound from './components/Outbound.vue';
  import Move from './components/Move.vue';
  import Check from './components/Check.vue';
  import MaterialView from './components/MaterialView.vue';
  import MaterialPartsView from './components/MaterialPartsView.vue';
  import Reserve from './components/Reserve.vue';
  import SignModal from '@/components/SignModal';
  import SendBack from './components/SendBack.vue';
  import Destroy from './components/Destroy.vue';
  import Use from './components/Use.vue';
  import SplitPackage from './components/SplitPackage.vue';

  const reqMaterialListReq = async (params: any) => {
    const { materialPositionId, ...newParams }: any = params;
    if (materialPositionId !== 'all' && materialPositionId) {
      newParams.materialPositionId = materialPositionId;
    }
    return await reqStorageMaterialBatchPage(newParams);
  };

  const curSelect = ref<any>({});
  const treeSelect = (node: any, info: any) => {
    curSelect.value = info.node;
  };

  const reqMaterialBatchListReq = async (params: any) => {
    if (!params.storageMaterialBatchId) return Promise.resolve({ data: [] });
    const newParams = { ...params };
    if (curSelect.value.id !== 'all') {
      newParams.materialPositionId = curSelect.value.id;
    }
    return await reqStorageMaterialPage(newParams);
  };

  const {
    columnsFirst,
    formFirstProps,
    firstRowData,
    columnsSecond,
    secondRowData,
    pageRef,
    treeData,
    updateTable,
    // 物料接收
    materialReceivingOpen,
    openMaterialReceivingModal,
    // 物料入库
    inboundOpen,
    openInboundModal,

    // 物料出库
    outboundOpen,
    outboundModal,

    // 查看
    materialViewOpen,
    materialPartsViewOpen,

    // 物料移库
    moveOpen,
    openMoveModal,
    // 盘点
    checkOpen,
    // 预定
    reserveOpen,
    // 取消预定
    cancelReserveSignOpen,
    cancelReserveSignatureData,
    cancelReserveLabelList,
    cancelReserveSignSuccess,
    cancelReserveAlertDesc,
    // 打印标签
    printOpen,
    printConfirm,
    // 退库
    sendBackOpen,
    openSendBackModal,
    // 销毁
    destroyOpen,
    openDestroyModal,
    // 使用
    useOpen,
    openUseModal,
    // 拆包出库
    splitPackageOpen,

    // 弹窗表格数据
    modalTableData,
  } = useTable({ curSelect });
</script>
