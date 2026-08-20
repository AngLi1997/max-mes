<!-- 检疫期报告审核 -- 列表 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :showToolBars="[true]"
    :tableFields="[
      {
        default: {
          auditStatusList: [2],
        },
      },
    ]"
    :rowSelections="rowSelections"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :requests="[getQuarantineReportSubmitAuditList as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <div style="display: flex; align-items: center; justify-content: flex-start">
        <Button
          v-hasAuth="170050004000001"
          :disabled="operationSelectedRows?.length === 0"
          style="margin-right: 8px"
          @click="openOperateModal(operationSelectedRows, 'return')">
          {{ t('退回') }}
        </Button>
        <Button
          v-hasAuth="170050004000002"
          type="primary"
          :disabled="operationSelectedRows?.length === 0"
          style="margin-right: 8px"
          @click="openOperateModal(operationSelectedRows, 'audit')">
          {{ t('审核') }}
        </Button>
      </div>
    </template>
    <template #tableHeaderToolbar0>
      <Button
        v-hasAuth="170050004000003"
        :disabled="operationSelectedRows?.length !== 1"
        style="margin-right: 8px"
        @click="enterView(operationSelectedRows[0], 3)">
        {{ t('查看报告') }}
      </Button>
      <Button
        v-hasAuth="170050004000003"
        :disabled="operationSelectedRows?.length !== 1"
        @click="enterView(operationSelectedRows[0], 2)">
        {{ t('查看明细') }}
      </Button>
    </template>
  </BMPageComponent>
  <OperateModal ref="operateModalRef" @submitSuccess="submitSuccess" />
  <QuarantineCntModal ref="cntModalRef" />
</template>

<script setup lang="ts">
  import { getQuarantineReportSubmitAuditList } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import QuarantineCntModal from '@/components/QuarantineCntModal/index.vue';
  import { OperateModal } from '../index';

  const router = useRouter();

  const enterView = (record: any, type: any) => {
    router.push({
      name: 'quarantine-check-detail',
      params: { id: record.quarantineId },
      query: { type },
    });
  };
  // ===========审核============
  const operateModalRef = ref();

  const openOperateModal = (rows: any, type: 'audit' | 'return') => {
    operateModalRef.value?.openModal(rows, type);
  };

  // ============数量查看=============
  const cntModalRef = ref();

  const openCntModal = (row: any, type: string) => {
    cntModalRef.value?.openModal(row, type);
  };

  const { pageRef, columnsFirst, formFirstProps } = useTable(openCntModal, enterView);

  // 选中的数据
  const operationSelectedRows = ref<any>([]);

  // 多选
  const rowSelections = reactive([
    {
      type: 'checkbox',
      hideSelectAll: false,
      columnWidth: 50,
      fixed: true,
      selectedRowKeys: [] as any[],
      preserveSelectedRowKeys: true,
      getCheckboxProps: (record: any) => {
        return {
          disabled: record?.auditStatus?.value != 2,
        };
      },
      onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
        if (rowSelections[0]?.selectedRowKeys) {
          rowSelections[0].selectedRowKeys = selectedRowKeys;
        }
        operationSelectedRows.value = selectedRows;
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

<style lang="less" scoped></style>
