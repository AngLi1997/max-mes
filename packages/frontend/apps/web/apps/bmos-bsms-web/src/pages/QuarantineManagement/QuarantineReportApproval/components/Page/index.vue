<!-- 检疫期报告送审 -- 列表 -->
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
          auditStatusList: [1, 4],
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
          v-hasAuth="170050003000001"
          type="primary"
          :disabled="operationSelectedRows?.length === 0"
          style="margin-right: 8px"
          @click="openOperateModal(operationSelectedRows, 'audit')">
          {{ t('送审') }}
        </Button>
        <Button
          v-hasAuth="170050003000002"
          :disabled="operationSelectedRows?.length === 0"
          style="margin-right: 8px"
          @click="openOperateModal(operationSelectedRows, 'cancel')">
          {{ t('撤销') }}
        </Button>
        <Button :disabled="operationSelectedRows?.length !== 1" @click="openPreview(operationSelectedRows[0])">
          {{ t('内容预览') }}
        </Button>
      </div>
    </template>
    <template #tableHeaderToolbar0>
      <Button
        v-hasAuth="170050003000003"
        :disabled="operationSelectedRows?.length !== 1"
        style="margin-right: 8px"
        @click="enterView(operationSelectedRows[0], 3)">
        {{ t('查看报告') }}
      </Button>
      <Button
        v-hasAuth="170050003000003"
        :disabled="operationSelectedRows?.length !== 1"
        @click="enterView(operationSelectedRows[0], 2)">
        {{ t('查看明细') }}
      </Button>
    </template>
  </BMPageComponent>
  <OperateModal ref="operateModalRef" @submitSuccess="submitSuccess" />
  <QuarantineCntModal ref="cntModalRef" />
  <Preview ref="previewRef" />
</template>

<script setup lang="ts">
  import { getQuarantineReportSubmitAuditList } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import QuarantineCntModal from '@/components/QuarantineCntModal/index.vue';
  import { OperateModal, Preview } from '../index';

  const router = useRouter();

  const enterView = (record: any, type: any) => {
    router.push({
      name: 'quarantine-check-detail',
      params: { id: record.quarantineId },
      query: { type },
    });
  };

  // ===========送审/撤销============
  const operateModalRef = ref();

  const openOperateModal = (rows: any, type: 'audit' | 'cancel') => {
    operateModalRef.value?.openModal(rows, type);
  };

  // ============数量查看=============
  const cntModalRef = ref();

  const openCntModal = (row: any, type: string) => {
    cntModalRef.value?.openModal(row, type);
  };

  // ============内容预览=============
  const previewRef = ref();

  const openPreview = (row: any) => {
    previewRef.value?.showDrawer(row);
  };

  const submitSuccess = () => {
    clearSelectedRowKeys();
    pageRef.value?.fetchData();
  };

  const { pageRef, columnsFirst, formFirstProps, rowSelections, clearSelectedRowKeys, operationSelectedRows } =
    useTable(openCntModal, enterView);
</script>

<style lang="less" scoped></style>
