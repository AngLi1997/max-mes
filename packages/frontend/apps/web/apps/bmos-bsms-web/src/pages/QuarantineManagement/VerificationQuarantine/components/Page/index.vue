<!-- 检疫期核查数据 -- 列表 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :rowSelections="rowSelections"
    :showHeader="[false]"
    :showToolBars="[true]"
    :formProps="[formFirstProps]"
    :requests="[getVerificationQuarantineList as DataRequestFn]"
    :paginations="[paginationFirst]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <Button
        v-hasAuth="170050002000001"
        :disabled="rowSelections[0]?.selectedRowKeys?.length === 0 || operationSelectedRow?.reportStatus?.value !== 0"
        type="primary"
        style="margin-right: 8px"
        @click="openCreateReport(operationSelectedRow)">
        {{ t('创建报告') }}
      </Button>
    </template>
    <template #tableHeaderToolbar0>
      <Button :disabled="rowSelections[0]?.selectedRowKeys?.length === 0" @click="enterView(operationSelectedRow, 2)">
        {{ t('查看明细') }}
      </Button>
    </template>
  </BMPageComponent>
  <CreateReport ref="createReportRef" @submitSuccess="submitSuccess" />
  <QuarantineCntModal ref="cntModalRef" />
</template>

<script setup lang="ts">
  import { getVerificationQuarantineList } from '@/services';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import QuarantineCntModal from '@/components/QuarantineCntModal/index.vue';
  import { CreateReport } from '../index';

  const router = useRouter();

  const enterView = (record: any, type: any) => {
    router.push({
      name: 'quarantine-check-detail',
      params: { id: record.id },
      query: { type },
    });
  };

  // ==================创建报告===================
  const createReportRef = ref();

  const openCreateReport = (row: any, type: 'create' | 'edit' = 'create') => {
    createReportRef.value.openModal(row, type);
  };

  // ============数量查看=============
  const cntModalRef = ref();

  const openCntModal = (row: any, type: string) => {
    cntModalRef.value?.openModal(row, type);
  };

  const { pageRef, columnsFirst, formFirstProps, paginationFirst } = useTable(openCntModal, openCreateReport);

  // 选中的数据
  const operationSelectedRow = ref<any>({});

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
          operationSelectedRow.value = selectedRows[selectedRows.length - 1];
        }
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
