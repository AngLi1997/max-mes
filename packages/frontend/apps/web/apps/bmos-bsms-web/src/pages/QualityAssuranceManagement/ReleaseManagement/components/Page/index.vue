<!-- 放行单管理 -- 列表 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['quarantineId']"
    :search="[true]"
    :hideRightTree="true"
    :rowSelections="rowSelections"
    :showHeader="[false]"
    :showToolBars="[true]"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :requests="[getReleaseManagementList as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <div class="table-header">
        <Button
          v-hasAuth="170060002000001"
          :disabled="rowSelections[0]?.selectedRowKeys?.length === 0"
          type="primary"
          style="margin-right: 8px"
          @click="openCreateNote(operationSelectedRow)">
          {{ t('创建放行单') }}
        </Button>
        <Button
          v-hasAuth="170060002000002"
          :disabled="rowSelections[0]?.selectedRowKeys?.length === 0"
          @click="enterView(operationSelectedRow, 2)">
          {{ t('查看明细') }}
        </Button>
      </div>
    </template>
  </BMPageComponent>
  <CreateNote ref="createNoteRef" @submitSuccess="submitSuccess" />
  <QuarantineCntModal ref="cntModalRef" />
</template>

<script setup lang="ts">
  import { getReleaseManagementList } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { CreateNote } from '../index';
  import QuarantineCntModal from '@/components/QuarantineCntModal/index.vue';
  import { t } from '@bmos/i18n';

  const router = useRouter();

  const enterView = (record: any, type: any) => {
    router.push({
      name: 'quarantine-check-detail',
      params: { id: record.quarantineId },
      query: { type },
    });
  };
  // ==============创建放行单相关===============
  const createNoteRef = ref();

  const openCreateNote = (row: any) => {
    createNoteRef.value.openModal(row);
  };

  // ============数量查看=============
  const cntModalRef = ref();

  const openCntModal = (row: any, type: string) => {
    cntModalRef.value?.openModal(row, type);
  };

  const { pageRef, columnsFirst, formFirstProps } = useTable(openCntModal, openCreateNote, enterView);

  const operationSelectedRow = ref<any>([]); //存表格选中的数据

  // 单选
  const rowSelections = reactive([
    {
      type: 'checkbox',
      hideSelectAll: true,
      columnWidth: 50,
      fixed: true,
      selectedRowKeys: [] as any[],
      preserveSelectedRowKeys: true,
      getCheckboxProps: (record: any) => {
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
        operationSelectedRow.value = selectedRows[selectedRows.length - 1];
      },
    },
    null,
  ]);

  const submitSuccess = () => {
    if (rowSelections[0]?.selectedRowKeys) {
      rowSelections[0].selectedRowKeys = [];
    }
    operationSelectedRow.value = {};
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
