<!-- 检验过程检查 -->
<template>
  <PageExpandCom
    ref="pageExpendRef"
    tableRowKey="sampleBatchNo"
    :tableProps="{
      search: [true],
      hideRightTree: true,
      showHeader: [false],
      showToolBars: [true],
      tableFields: [{ default: { checkStatus, auditStatus: 'AUDITED' } }],
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
      <Segmented v-model:value="checkStatus" :options="options" />
    </template>
  </PageExpandCom>
  <CntModal ref="cntModalRef" />
  <Unqualified ref="unqualifiedRef" />
  <CheckModal
    ref="checkModalRef"
    @submitSuccess="
      () => {
        pageExpendRef?.fetchData();
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
  import { CheckModal, CntModal, AuditCnt, Unqualified } from './components';
  import { t } from '@bmos/i18n';
  import { pdfPreview } from '@/utils';

  defineOptions({
    name: 'ReportingManagerProcessCheck',
    inheritAttrs: false,
  });

  const { checkStatusDict } = getDicts();

  const print = async (record: any) => {
    try {
      const res = await printReport({ sampleBatchNo: record.sampleBatchNo });
      await pdfPreview(res);
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const printCheckRecord = async (record: any) => {
    try {
      const res = await printCheckReport({ sampleBatchNo: record.sampleBatchNo });
      await pdfPreview(res);
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  // 数量弹窗
  const cntModalRef = ref<InstanceType<typeof CntModal>>();

  // 不合格数据详情
  const unqualifiedRef = ref<InstanceType<typeof Unqualified>>();

  // 审核次数
  const auditCntRef = ref<InstanceType<typeof AuditCnt>>();

  // 检查弹窗
  const checkModalRef = ref<InstanceType<typeof CheckModal>>();

  const openCheck = (row: any) => {
    checkModalRef.value?.openModal(row);
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
    print,
    openCheck,
    printCheckRecord,
  );

  const checkStatus = ref('');

  const options = [{ label: t('全部'), value: '' }, ...checkStatusDict];
</script>

<style scoped></style>
