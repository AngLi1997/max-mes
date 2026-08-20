<!-- 不合格核查记录审核 -- 列表 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :showToolBars="[true]"
    :rowSelections="rowSelections"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :requests="[getUnqualifiedCheckRecordList as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <div class="table-header">
        <Button
          v-hasAuth="170070003000001"
          :disabled="rowSelections[0]?.selectedRowKeys.length === 0"
          style="margin-right: 8px"
          @click="openAudit(operationSelectedRows, 'return')">
          {{ t('退回') }}
        </Button>
        <Button
          v-hasAuth="170070003000002"
          type="primary"
          :disabled="rowSelections[0]?.selectedRowKeys.length === 0"
          @click="openAudit(operationSelectedRows, 'audit')">
          {{ t('审核') }}
        </Button>
      </div>
    </template>
  </BMPageComponent>
  <!-- 审核弹框 -->
  <AuditModal ref="auditModalRef" @submitSuccess="submitSuccess" />
  <!-- 受影响血浆 -->
  <ViewEffectPlasma ref="viewEffectPlasmaRef" />
</template>

<script setup lang="ts">
  import { getUnqualifiedCheckRecordList } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { AuditModal, ViewEffectPlasma } from '../index';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'CheckRecordReview',
    inheritAttrs: false,
  });

  const router = useRouter();

  const enterView = (row: any) => {
    router.push({
      name: 'CheckRecordReviewViewCom',
      params: { id: row.id },
    });
  };

  // 审核操作
  const auditModalRef = ref();

  const openAudit = (data: any, type: 'audit' | 'return') => {
    auditModalRef.value?.openModal(data, type);
  };

  // 查看受影响血浆
  const viewEffectPlasmaRef = ref();

  const openView = (data: any) => {
    viewEffectPlasmaRef.value?.openModal(data.id);
  };

  const { pageRef, columnsFirst, formFirstProps } = useTable(openView, enterView);

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
          disabled: record?.auditStatus?.value !== 0,
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

  const submitSuccess = () => {
    if (rowSelections[0]?.selectedRowKeys) {
      rowSelections[0].selectedRowKeys = [];
      operationSelectedRows.value = [];
    }
    pageRef.value?.fetchData();
  };
</script>

<style lang="less" scoped></style>
