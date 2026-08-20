<!-- 接收审核 -->
<template>
  <PageExpandCom
    ref="pageExpendRef"
    tableRowKey="auditId"
    :tableProps="{
      search: [true],
      hideRightTree: true,
      showHeader: [false],
      showToolBars: [true],
      rowSelections: [rowSelection],
      tableFields: [
        {
          default: { status: auditStatus },
        },
      ],
      formProps: [formFirstProps],
      columns: [columnsFirst],
      showIndexs: [true],
    }"
    :tableLoadApi="getSampleReceiveAuditPage"
    :expandLoadApi="getSampleReceiveTwoAuditPage"
    :expandFields="(record: any) => ({ batchNo: record.batchNo })"
    :expandProps="{
      rowKeys: ['id'],
      search: [false],
      hideRightTree: true,
      showHeader: [false],
      showToolBars: [true],
      formProps: [formFirstProps],
      columns: [columnsExpand],
      showIndexs: [true],
    }">
    <template #tableHeaderTitle0>
      <Segmented v-model:value="auditStatus" :options="options" />
    </template>
    <template #tableHeaderToolbar0>
      <Button
        v-hasAuth="210020002000001"
        :disabled="selectedRows.length === 0"
        type="primary"
        @click="() => openAudit(selectedRows)">
        {{ t('审核') }}
      </Button>
    </template>
  </PageExpandCom>
  <CntModal ref="cntModalRef" />
  <TransportModal ref="transportModalRef" />
  <AuditModal
    ref="auditModalRef"
    @submitSuccess="
      () => {
        pageExpendRef?.fetchData();
        clearSelect();
      }
    " />
  <RejectDetail ref="rejectDetailRef" />
  <InspectItemsModal ref="inspectItemsModalRef" />
  <RemarkModal v-model:modalOpen="remarkModalOpen" :details="remarkDetails" />
</template>

<script setup lang="ts">
  import { getSampleReceiveAuditPage, getSampleReceiveTwoAuditPage } from '@/services';
  import { PageExpandCom } from '@/components/PageExpandCom';
  import { Segmented } from 'ant-design-vue';
  import { useTable } from './hooks';
  import RemarkModal from '@/components/RemarkModal';
  import { CntModal, TransportModal, AuditModal, RejectDetail, InspectItemsModal } from './components';
  import { t } from '@bmos/i18n';
  import { useRowSelection } from '@/hooks';

  defineOptions({
    name: 'ReceiveReview',
    inheritAttrs: false,
  });

  const { auditStatusDict } = getDicts();

  const { selectedRows, rowSelection, clearSelect } = useRowSelection({
    hideSelectAll: false,
    getCheckboxProps: (record: any) => {
      return {
        disabled: record?.status?.value === 'AUDITED',
      };
    },
  });

  // 数量弹窗
  const cntModalRef = ref<InstanceType<typeof CntModal>>();

  // 运输弹窗
  const transportModalRef = ref<InstanceType<typeof TransportModal>>();

  // 拒收详情弹窗
  const rejectDetailRef = ref<InstanceType<typeof RejectDetail>>();

  // 检查项弹窗
  const inspectItemsModalRef = ref<InstanceType<typeof InspectItemsModal>>();

  // 审核弹窗
  const auditModalRef = ref<InstanceType<typeof AuditModal>>();

  const openAudit = (rows: any) => {
    auditModalRef.value?.openModal(rows);
  };

  const { pageExpendRef, columnsFirst, formFirstProps, columnsExpand, remarkModalOpen, remarkDetails } = useTable(
    (record: any) => {
      cntModalRef.value?.openModal(record);
    },
    (record: any) => {
      transportModalRef.value?.openModal(record);
    },
    (record: any) => {
      rejectDetailRef.value?.openModal(record);
    },
    (record: any) => {
      inspectItemsModalRef.value?.openModal(record);
    },
  );

  const auditStatus = ref('TO_AUDIT');

  const options = [...auditStatusDict, { label: t('全部'), value: '' }];
</script>

<style scoped></style>
