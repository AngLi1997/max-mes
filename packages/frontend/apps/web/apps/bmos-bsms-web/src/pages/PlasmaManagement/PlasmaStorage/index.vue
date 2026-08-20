<!-- 血浆入库 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['syncBatchNo']"
    :search="[true]"
    :hideRightTree="true"
    :rowSelections="rowSelections"
    :showHeader="[false]"
    :showToolBars="[true]"
    :titles="[t('待入库数据')]"
    :formProps="[formFirstProps]"
    :requests="[getPlasmaInStorageList as DataRequestFn]"
    :paginations="[paginationFirst]"
    :columns="[columnsFirst]">
    <template #tableHeaderToolbar0>
      <Button
        type="primary"
        :disabled="!rowSelections[0]?.selectedRowKeys?.length"
        style="margin-right: 8px"
        @click="openCheckModal(operationSelectedRows)">
        {{ t('入库核对') }}
      </Button>
    </template>
  </BMPageComponent>
  <CheckModal ref="checkModal" @submitSuccess="submitSuccess" />
  <Cnt ref="cntRef" />
  <BatchStorage ref="batchStorageRef" @submitSuccess="submitSuccess" />
</template>

<script setup lang="ts">
  import { getPlasmaInStorageList } from '@/services';
  import { useTable } from './hooks';
  import { BatchStorage, CheckModal, Cnt } from './components';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'PlasmaStorage',
  });

  // ====入库核对====
  const checkModal = ref();

  const openCheckModal = (row: any) => {
    checkModal.value.openModal(row);
  };

  // =====查看数量=====
  const cntRef = ref();
  const openCnt = (row: any) => {
    cntRef.value.showDrawer(row);
  };

  // =======整批入库=======
  const batchStorageRef = ref();
  const openBatchStorage = (row: any) => {
    batchStorageRef.value.openModal(row);
  };

  const { pageRef, columnsFirst, formFirstProps, paginationFirst } = useTable(openCnt, openBatchStorage);

  // 选中的数据
  const operationSelectedRows = ref<any>([]);

  // 单选
  const rowSelections = reactive([
    {
      type: 'checkbox',
      hideSelectAll: true,
      columnWidth: 50,
      fixed: true,
      selectedRowKeys: [] as any[],
      preserveSelectedRowKeys: true,
      getCheckboxProps: (_record: any) => {
        return {
          disabled: false,
        };
      },
      onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
        if (rowSelections[0]?.selectedRowKeys) {
          rowSelections[0].selectedRowKeys = selectedRowKeys.length
            ? [selectedRowKeys[selectedRowKeys.length - 1]]
            : [];
        }
        operationSelectedRows.value = selectedRows[selectedRows.length - 1];
      },
    },
    null,
  ]);

  const submitSuccess = () => {
    if (rowSelections[0]?.selectedRowKeys) {
      rowSelections[0].selectedRowKeys = [];
    }
    operationSelectedRows.value = [];
    pageRef.value?.fetchData();
  };
</script>

<style lang="less" scoped>
  .table-header {
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }
</style>
