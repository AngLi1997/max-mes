<!-- 不合格核查报告审核 -- 列表 -->
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
    :requests="[unqualifiedPlasmaReportAuditList as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <div class="table-header">
        <Button
          v-hasAuth="170070006000002"
          :disabled="disabledAudit()"
          style="margin-right: 8px"
          @click="openAudit(operationSelectedRows, 'return')">
          {{ t('退回') }}
        </Button>
        <Button
          v-hasAuth="170070006000001"
          type="primary"
          :disabled="disabledAudit()"
          style="margin-right: 8px"
          @click="openAudit(operationSelectedRows, 'audit')">
          {{ t('审核') }}
        </Button>
        <Button
          v-hasAuth="170070006000003"
          :disabled="rowSelections[0]?.selectedRowKeys.length !== 1"
          @click="openDetail">
          {{ t('内容预览') }}
        </Button>
      </div>
    </template>
  </BMPageComponent>
  <!-- 审核弹框 -->
  <AuditModal ref="auditModalRef" @submitSuccess="submitSuccess" />
  <!-- 受影响血浆 -->
  <ViewEffectPlasma ref="viewEffectPlasmaRef" />
  <!-- 内容预览 -->
  <Preview ref="previewRef" />
</template>

<script setup lang="ts">
  import { unqualifiedPlasmaReportAuditList } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { AuditModal, Preview, ViewEffectPlasma } from '../index';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'VerificationReportReview',
  });

  const router = useRouter();

  const enterView = (row: any) => {
    router.push({
      name: 'ReportDetailViewCom',
      query: {
        unqualifiedPlasmaInfoId: row.unqualifiedPlasmaInfoId,
        reportBillNo: row.reportBillNo,
      },
    });
  };

  // 是否禁止审核
  const disabledAudit = () => {
    if (operationSelectedRows.value.length === 0) {
      return true;
    } else {
      return operationSelectedRows.value?.some((item: any) => item?.auditStatus?.value !== 2);
    }
  };

  // 审核操作
  const auditModalRef = ref();

  const openAudit = (data: any, type: 'audit' | 'return') => {
    auditModalRef.value?.openModal(data, type);
  };

  // 查看受影响血浆
  const viewEffectPlasmaRef = ref();

  const openView = (data: any) => {
    viewEffectPlasmaRef.value?.openModal(data.unqualifiedPlasmaInfoId);
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
      getCheckboxProps: (_record: any) => {
        return {
          disabled: false,
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

  // 内容预览
  const previewRef = ref();
  const openDetail = () => {
    if (operationSelectedRows.value.length > 0) {
      previewRef.value?.openModal(operationSelectedRows.value[0]);
    }
  };
</script>

<style lang="less" scoped></style>
