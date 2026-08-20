<!-- 检验报告中心 -->
<template>
  <PageExpandCom
    ref="pageExpendRef"
    tableRowKey="sampleBatchNo"
    :tableProps="{
      search: [true],
      hideRightTree: true,
      showHeader: [false],
      showToolBars: [true],
      rowSelections: [rowSelection],
      tableFields: [{ default: { bindStatus } }],
      formProps: [formFirstProps],
      columns: [columnsFirst],
      showIndexs: [true],
    }"
    :tableLoadApi="getReportCenterPage"
    :expandLoadApi="getReportCenterChildPage"
    :expandFields="(record: any) => ({ sampleBatchNo: record.sampleBatchNo })"
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
      <Segmented v-model:value="bindStatus" :options="options" />
    </template>
    <template #tableHeaderToolbar0>
      <Button
        v-hasAuth="210040001000001"
        :disabled="disAudit(selectedRows)"
        type="primary"
        @click="() => openAudit(selectedRows)">
        {{ t('审核') }}
      </Button>
    </template>
  </PageExpandCom>
  <CntModal ref="cntModalRef" />
  <Unqualified ref="unqualifiedRef" />
  <AuditModal
    ref="auditModalRef"
    @submitSuccess="
      () => {
        pageExpendRef?.fetchData();
        clearSelect();
      }
    " />
  <AuditCnt ref="auditCntRef" />
  <RemarkModal v-model:modalOpen="remarkModalOpen" :details="remarkDetails" />
</template>

<script setup lang="ts">
  import { getReportCenterPage, getReportCenterChildPage, printReport, printCheckReport } from '@/services';
  import { PageExpandCom } from '@/components/PageExpandCom';
  import { Segmented, message } from 'ant-design-vue';
  import { useTable } from './hooks';
  import RemarkModal from '@/components/RemarkModal';
  import { CntModal, AuditCnt, AuditModal, Unqualified } from './components';
  import { t } from '@bmos/i18n';
  import { pdfPreview } from '@/utils';
  import { useRowSelection } from '@/hooks';

  defineOptions({
    name: 'ReportingManagerCenter',
    inheritAttrs: false,
  });

  // 是否禁止审核
  const disAudit = (rows: any[]) => {
    return !rows?.length || rows?.some((item: any) => item.auditStatus?.value !== 'TO_AUDIT');
  };

  const loadingPrint = ref(false);
  const print = async (record: any) => {
    try {
      loadingPrint.value = true;
      const res = await printReport({ sampleBatchNo: record.sampleBatchNo });
      await pdfPreview(res);
    } catch (error: any) {
      error.message && message.error(error.message);
    } finally {
      loadingPrint.value = false;
    }
  };

  const { selectedRows, rowSelection, clearSelect } = useRowSelection({
    hideSelectAll: false,
    getCheckboxProps: (_record: any) => {
      return {
        disabled: false,
      };
    },
  });

  // 数量弹窗
  const cntModalRef = ref<InstanceType<typeof CntModal>>();

  // 不合格数据详情
  const unqualifiedRef = ref<InstanceType<typeof Unqualified>>();

  // 审核次数
  const auditCntRef = ref<InstanceType<typeof AuditCnt>>();

  // 审核弹窗
  const auditModalRef = ref<InstanceType<typeof AuditModal>>();

  const openAudit = (rows: any[]) => {
    auditModalRef.value?.openModal(rows);
  };

  const printCheckRecord = async (record: any) => {
    try {
      const res = await printCheckReport({ sampleBatchNo: record.sampleBatchNo });
      await pdfPreview(res);
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const { pageExpendRef, columnsFirst, formFirstProps, columnsExpand, remarkModalOpen, remarkDetails } = useTable(
    (record: any) => {
      cntModalRef.value?.openModal(record);
    },
    (record: any) => {
      unqualifiedRef.value?.openModal(record);
    },
    (record: any) => {
      auditCntRef.value?.openModal(record);
    },
    openAudit,
    print,
    printCheckRecord,
  );

  const bindStatus = ref('');

  const options = [
    { label: t('全部'), value: '' },
    { label: t('待审核'), value: 'TO_AUDIT' },
    { label: t('待检查'), value: 'WAIT_CHECK' },
    { label: t('待签发'), value: 'WAIT_PUBLISH' },
    { label: t('已签发'), value: 'PUBLISHED' },
  ];
</script>

<style scoped></style>
