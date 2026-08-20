<!-- 外观不合格审核 -- 标本 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['auditId']"
    :search="[true]"
    :hideRightTree="true"
    :rowSelections="rowSelections"
    :showHeader="[false]"
    :showToolBars="[true]"
    :formProps="[formFirstProps]"
    :requests="[getSpecimenAppearanceUnqualifiedList as DataRequestFn]"
    :paginations="[paginationFirst]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <div class="table-header">
        <Button
          v-hasAuth="170020013000001"
          :disabled="operationSelectedRows.length === 0"
          style="margin-right: 8px"
          @click="openAudit(operationSelectedRows, 'return')">
          {{ t('退回') }}
        </Button>
        <Button
          v-hasAuth="170020013000002"
          type="primary"
          :disabled="operationSelectedRows.length === 0"
          @click="openAudit(operationSelectedRows, 'audit')">
          {{ t('审核') }}
        </Button>
      </div>
    </template>
  </BMPageComponent>
  <!-- 审核弹框 -->
  <AuditModal ref="auditModalRef" @submitSuccess="submitSuccess" />
</template>

<script setup lang="ts">
  import { getSpecimenAppearanceUnqualifiedList } from '@/services';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { AuditModal } from '../index';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'AppearanceUnqualifiedAudit',
    inheritAttrs: false,
  });

  const router = useRouter();

  const enterView = (row: any) => {
    router.push({
      name: 'AppearanceUnqualifiedAuditViewCom',
      params: { orgSampleNo: row?.orgSampleNo },
    });
  };

  // 审核操作
  const auditModalRef = ref();

  const openAudit = (data: any, type: 'audit' | 'return') => {
    auditModalRef.value?.openModal(data, type);
  };

  const { pageRef, columnsFirst, formFirstProps, paginationFirst } = useTable(enterView);

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
          disabled: record?.auditResult?.value !== 0,
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

<style lang="less" scoped>
  .table-header {
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }
</style>
