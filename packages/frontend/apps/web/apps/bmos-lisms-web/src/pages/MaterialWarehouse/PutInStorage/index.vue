<!-- 物料入库 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['receiveIdentify']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :showToolBars="[true]"
    :rowSelections="[rowSelection]"
    :tableFields="[
      {
        default: { storageStatus },
      },
    ]"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :requests="[getMaterialInWarehousePage as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <Segmented v-model:value="storageStatus" :options="options" />
    </template>
    <template #tableHeaderToolbar0>
      <Button
        v-hasAuth="210060004000001"
        :disabled="selectedRows.length === 0"
        type="primary"
        @click="() => openStoreModal('store', selectedRows)">
        {{ t('物料入库') }}
      </Button>
      <Button
        v-hasAuth="210060004000002"
        :disabled="selectedRows.length === 0"
        @click="() => openStoreModal('cancel', selectedRows)">
        {{ t('撤销接收') }}
      </Button>
    </template>
  </BMPageComponent>
  <MaterialModal
    ref="materialModalRef"
    @submitSuccess="
      () => {
        pageRef?.fetchData();
        clearSelect();
      }
    " />
  <StoreModal
    ref="storeModalRef"
    @submitSuccess="
      () => {
        pageRef?.fetchData();
        clearSelect();
      }
    " />
  <RemarkModal v-model:modalOpen="remarkModalOpen" :details="remarkDetails" />
  <ReceiveDetail ref="receiveDetailRef" />
</template>

<script setup lang="ts">
  import { getMaterialInWarehousePage } from '@/services';
  import { useTable } from './hooks';
  import RemarkModal from '@/components/RemarkModal';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { StoreModal, MaterialModal, ReceiveDetail } from './components';
  import { useRowSelection } from '@/hooks';
  import { paginationBig } from '@/utils';

  defineOptions({
    name: 'MaterialInPutInStorage',
    inheritAttrs: false,
  });

  const storageStatus = ref('WAITING_STORAGE');

  const options = [
    { label: t('待入库'), value: 'WAITING_STORAGE' },
    { label: t('已入库'), value: 'STORAGED' },
    { label: t('全部'), value: '' },
  ];

  const { selectedRows, rowSelection, clearSelect } = useRowSelection({
    hideSelectAll: false,
    getCheckboxProps: (record: any) => {
      return {
        disabled: record?.storageStatus?.value !== 'WAITING_STORAGE',
      };
    },
  });

  const receiveDetailRef = ref<InstanceType<typeof ReceiveDetail> | null>(null);

  const materialModalRef = ref<InstanceType<typeof MaterialModal> | null>(null);

  const openModal = (row: any) => {
    materialModalRef.value?.openModal(row);
  };

  const storeModalRef = ref<InstanceType<typeof StoreModal> | null>(null);

  const openStoreModal = (type: 'store' | 'cancel', rows: any[]) => {
    storeModalRef.value?.openModal(type, rows);
  };

  const { pageRef, columnsFirst, formFirstProps, remarkModalOpen, remarkDetails } = useTable(
    (record: any) => receiveDetailRef.value?.openModal(record),
    openModal,
    openStoreModal,
  );
</script>

<style lang="less" scoped></style>
