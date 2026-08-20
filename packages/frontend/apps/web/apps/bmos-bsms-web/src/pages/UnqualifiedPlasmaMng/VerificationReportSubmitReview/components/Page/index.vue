<!-- 不合格核查报告送审 -- 列表 -->
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
    :requests="[unqualifiedPlasmaReportPage as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <div class="table-header">
        <Button
          v-hasAuth="170070005000001"
          :disabled="rowSelections[0]?.selectedRowKeys.length === 0"
          style="margin-right: 8px"
          @click="openAudit(operationSelectedRows, 'cancel')">
          {{ t('撤销') }}
        </Button>
        <Button
          v-hasAuth="170070005000002"
          type="primary"
          :disabled="rowSelections[0]?.selectedRowKeys.length === 0"
          style="margin-right: 8px"
          @click="openAudit(operationSelectedRows, 'audit')">
          {{ t('送审') }}
        </Button>
        <Button
          v-hasAuth="170070005000003"
          :disabled="rowSelections[0]?.selectedRowKeys.length !== 1"
          @click="openDetail">
          {{ t('内容预览') }}
        </Button>
      </div>
    </template>
  </BMPageComponent>
  <!-- 送审弹框 -->
  <OperateModal ref="operateModalRef" @submitSuccess="submitSuccess" />
  <!-- 受影响血浆 -->
  <ViewEffectPlasma ref="viewEffectPlasmaRef" />
  <!-- 内容预览 -->
  <Preview ref="previewRef" />
</template>

<script setup lang="ts">
  import { unqualifiedPlasmaReportPage } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { OperateModal, Preview, ViewEffectPlasma } from '../index';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'VerificationReportSubmitReview',
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

  // 审核操作
  const operateModalRef = ref();

  const openAudit = (data: any, type: 'audit' | 'cancel') => {
    operateModalRef.value?.openModal(data, type);
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
