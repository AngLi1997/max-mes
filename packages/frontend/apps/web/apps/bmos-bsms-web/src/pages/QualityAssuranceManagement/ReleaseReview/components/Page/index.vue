<!-- 放行单审核 -- 列表 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['noteId']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :showToolBars="[true]"
    :tableFields="[
      {
        default: {
          auditStatus: 0,
        },
      },
    ]"
    :rowSelections="rowSelections"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :requests="[getQualityGuaranteeReleaseList as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <div class="table-header">
        <Button
          v-hasAuth="170060003000001"
          :disabled="rowSelections[0]?.selectedRowKeys.length === 0"
          style="margin-right: 8px"
          @click="openAudit(operationSelectedRows, 'return')">
          {{ t('退回') }}
        </Button>
        <Button
          v-hasAuth="170060003000002"
          type="primary"
          :disabled="rowSelections[0]?.selectedRowKeys.length === 0"
          @click="openAudit(operationSelectedRows, 'audit')">
          {{ t('审核') }}
        </Button>
      </div>
    </template>
  </BMPageComponent>
  <!-- 审核弹框 -->
  <AuditModal
    ref="auditModalRef"
    @submitSuccess="
      () => {
        if (rowSelections[0]?.selectedRowKeys) {
          rowSelections[0].selectedRowKeys = [];
          operationSelectedRows = [];
        }
        pageRef?.fetchData();
      }
    " />
  <QuarantineCntModal ref="cntModalRef" />
</template>

<script setup lang="ts">
  import { getQualityGuaranteeReleaseList } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { AuditModal } from '../index';
  import QuarantineCntModal from '@/components/QuarantineCntModal/index.vue';
  import { t } from '@bmos/i18n';

  const router = useRouter();

  // 审核操作
  const auditModalRef = ref();

  const openAudit = (data: any, type: 'audit' | 'return') => {
    auditModalRef.value?.openModal(data, type);
  };

  const enterView = (row: any) => {
    // emits('enterView', row);
    router.push({
      name: 'view-com-detail',
      params: { id: row?.noteId },
    });
  };

  // ============数量查看=============
  const cntModalRef = ref();

  const openCntModal = (row: any, type: string) => {
    cntModalRef.value?.openModal(row, type);
  };

  const { pageRef, columnsFirst, formFirstProps } = useTable(openCntModal, enterView);

  // 选中的数据
  const operationSelectedRows = ref<any>({});

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
          disabled: record?.noteAuditStatus?.value !== 0,
        };
      },
      onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
        if (rowSelections[0]?.selectedRowKeys) {
          rowSelections[0].selectedRowKeys = selectedRowKeys;
          operationSelectedRows.value = selectedRows;
        }
      },
    },
    null,
  ]);
</script>

<style lang="less" scoped></style>
