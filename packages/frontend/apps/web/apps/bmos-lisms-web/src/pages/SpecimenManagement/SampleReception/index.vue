<!-- 标本接收 -->
<template>
  <PageExpandCom
    ref="pageExpendRef"
    tableRowKey="batchNo"
    :tableProps="{
      search: [true],
      hideRightTree: true,
      showHeader: [false],
      showToolBars: [true],
      rowSelections: [rowSelection],
      formProps: [formFirstProps],
      columns: [columnsFirst],
      showIndexs: [true],
      tableFields: [
        {
          default: { batchReceiveStatus },
        },
      ],
    }"
    :tableLoadApi="getSampleReceivePage"
    :expandLoadApi="getSampleReceiveTwoPage"
    :expandFields="(record: any) => ({ batchNo: record.batchNo })"
    :expandProps="{
      rowKeys: ['sampleNo'],
      search: [false],
      hideRightTree: true,
      showHeader: [false],
      showToolBars: [true],
      formProps: [formFirstProps],
      columns: [columnsExpand],
      showIndexs: [true],
    }">
    <template #tableHeaderTitle0>
      <Segmented v-model:value="batchReceiveStatus" :options="options" />
    </template>
    <template #tableHeaderToolbar0>
      <Button
        v-hasAuth="210020001000001"
        :disabled="disReceive()"
        type="primary"
        @click="() => openRecept(selectedRows)">
        {{ t('整批接收') }}
      </Button>
      <Button
        v-hasAuth="210020001000002"
        :disabled="!selectedRows.length"
        :loading="printListLoading"
        @click="() => printList(selectedRows)">
        {{ t('打印标本清单') }}
      </Button>
      <Button
        v-hasAuth="210020001000003"
        :disabled="disExport()"
        :loading="printRecordLoading"
        @click="() => printRecord(selectedRows)">
        {{ t('打印送检交接记录') }}
      </Button>
      <Button
        v-hasAuth="210020001000004"
        :disabled="disExport()"
        :loading="exportLoading"
        @click="() => exportFile(selectedRows)">
        {{ t('导出') }}
      </Button>
    </template>
    <!-- <template #expandtableHeaderTitle0>
      <span>子表标题</span>
    </template> -->
  </PageExpandCom>
  <CntModal ref="cntModalRef" />
  <ProcessModal ref="processModalRef" />
  <TransportModal ref="transportModalRef" />
  <ReceptModal
    ref="receptModalRef"
    @submitSuccess="
      () => {
        pageExpendRef?.fetchData();
        clearSelect();
      }
    " />
  <RemarkModal v-model:modalOpen="remarkModalOpen" :details="remarkDetails" />
  <RejectDetail ref="rejectDetailRef" />
  <InspectItemsModal ref="inspectItemsModalRef" />
  <ScanModal ref="scanModalRef" @submitSuccess="openRecept" />
</template>

<script setup lang="ts">
  import {
    getSampleReceiveExport,
    getSampleReceivePage,
    getSampleReceiveReport,
    getSampleReceiveDownload,
    getSampleReceiveTwoPage,
  } from '@/services';
  import { PageExpandCom } from '@/components/PageExpandCom';
  import { Segmented, message } from 'ant-design-vue';
  import { useTable } from './hooks';
  import RemarkModal from '@/components/RemarkModal';
  import {
    CntModal,
    ProcessModal,
    TransportModal,
    ReceptModal,
    RejectDetail,
    InspectItemsModal,
    ScanModal,
  } from './components';
  import { t } from '@bmos/i18n';
  import { useRowSelection } from '@/hooks';
  import { fileDownloadFlow, pdfPreview } from '@/utils';
  import { PLimit } from '@/utils/pLimit';

  defineOptions({
    name: 'SampleReception',
    inheritAttrs: false,
  });

  const limit = new PLimit(2);

  // 导出相关
  const exportLoading = ref(false);

  const exportFile = async (rows: any[]) => {
    try {
      exportLoading.value = true;
      // 批量导出，使用promise.all
      const promises = rows.map((row: any) =>
        limit.createTask(() => getSampleReceiveExport({ sampleBatchNo: row.batchNo })),
      );
      const res = await Promise.all(promises);
      await Promise.all(res.map((item: any) => fileDownloadFlow(item)));
    } catch (error: any) {
      error.message && message.error(error.message);
    } finally {
      exportLoading.value = false;
    }
  };

  // 打印标本清单
  const printListLoading = ref(false);
  const printList = async (rows: any[]) => {
    try {
      printListLoading.value = true;
      const promises = rows.map((row: any) =>
        limit.createTask(() => getSampleReceiveDownload({ sampleBatchNo: row.batchNo })),
      );
      const res = await Promise.all(promises);
      await Promise.all(res.map((item: any) => pdfPreview(item)));
    } catch (error: any) {
      error.message && message.error(error.message);
    } finally {
      printListLoading.value = false;
    }
  };

  // 打印送检交接记录
  const printRecordLoading = ref(false);
  const printRecord = async (rows: any[]) => {
    try {
      printRecordLoading.value = true;
      const promises = rows.map((row: any) =>
        limit.createTask(() => getSampleReceiveReport({ sampleBatchNo: row.batchNo })),
      );
      // const res = await getSampleReceiveReport({ batchNo: row.batchNo });
      const res = await Promise.all(promises);
      await Promise.all(res.map((item: any) => pdfPreview(item)));
      // await pdfPreview(res);
    } catch (error: any) {
      error.message && message.error(error.message);
    } finally {
      printRecordLoading.value = false;
    }
  };

  // 禁止导出
  const disExport = () => {
    return (
      selectedRows.value.length === 0 ||
      selectedRows.value.some((item: any) => item.batchReceiveStatus?.value !== 'RECEIVED')
    );
  };

  // 禁止接收
  const disReceive = () => {
    return (
      selectedRows.value.length === 0 ||
      selectedRows.value.some((item: any) => item.batchReceiveStatus?.value !== 'RECEIVING')
    );
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

  // 进度弹窗
  const processModalRef = ref<InstanceType<typeof ProcessModal>>();

  // 运输弹窗
  const transportModalRef = ref<InstanceType<typeof TransportModal>>();

  // 拒收详情弹窗
  const rejectDetailRef = ref<InstanceType<typeof RejectDetail>>();

  // 检查项弹窗
  const inspectItemsModalRef = ref<InstanceType<typeof InspectItemsModal>>();

  // 接收弹窗
  const receptModalRef = ref<InstanceType<typeof ReceptModal>>();
  const openRecept = (rows: any[]) => {
    receptModalRef.value?.openModal(rows);
  };

  // 扫码弹窗
  const scanModalRef = ref<InstanceType<typeof ScanModal>>();
  const openScan = (row: any) => {
    scanModalRef.value?.openModal(row);
  };

  const { pageExpendRef, columnsFirst, formFirstProps, columnsExpand, remarkModalOpen, remarkDetails } = useTable(
    (record: any) => {
      cntModalRef.value?.openModal(record);
    },
    (record: any) => {
      processModalRef.value?.openModal(record);
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
    openRecept,
    openScan,
  );

  const batchReceiveStatus = ref('RECEIVING');

  const options = [
    { label: t('待接收'), value: 'RECEIVING' },
    { label: t('待审核'), value: 'AUDITING' },
    { label: t('已接收'), value: 'RECEIVED' },
    { label: t('全部'), value: '' },
  ];
</script>

<style scoped></style>
