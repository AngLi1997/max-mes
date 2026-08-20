<!-- 检验报告签发 -->
<template>
  <PageExpandCom
    ref="pageExpendRef"
    tableRowKey="sampleBatchNo"
    :tableProps="{
      search: [true],
      hideRightTree: true,
      showHeader: [false],
      showToolBars: [true],
      tableFields: [{ default: { bindStatus, checkStatus: 'CHECKED' } }],
      rowSelections: [rowSelection],
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
      <Button v-hasAuth="210040002000001" :disabled="disSign" type="primary" @click="() => openAudit(selectedRows)">
        {{ t('签发') }}
      </Button>
      <Button
        v-hasAuth="210040002000003"
        :disabled="disSignAgain"
        type="primary"
        @click="() => signAgain(selectedRows)">
        {{ t('再次签发') }}
      </Button>
    </template>
    <!-- <template #expandtableHeaderTitle0>
      <span>子表标题</span>
    </template> -->
  </PageExpandCom>
  <CntModal ref="cntModalRef" />
  <AuditModal
    ref="auditModalRef"
    @submitSuccess="
      () => {
        pageExpendRef?.fetchData();
        clearSelect();
      }
    " />
  <BackModal
    ref="backModalRef"
    @submitSuccess="
      () => {
        pageExpendRef?.fetchData();
        clearSelect();
      }
    " />
  <Unqualified ref="unqualifiedRef" />
  <AuditCnt ref="auditCntRef" />
  <RemarkModal v-model:modalOpen="remarkModalOpen" :details="remarkDetails" />
</template>

<script setup lang="ts">
  import {
    getReportCenterPage,
    getReportCenterChildPage,
    reSignReport,
    printReport,
    printCheckReport,
  } from '@/services';
  import { PageExpandCom } from '@/components/PageExpandCom';
  import { Segmented, message } from 'ant-design-vue';
  import { useTable } from './hooks';
  import RemarkModal from '@/components/RemarkModal';
  import { CntModal, BackModal, AuditCnt, AuditModal, Unqualified } from './components';
  import { t } from '@bmos/i18n';
  import { pdfPreview } from '@/utils';
  import { useRowSelection, useWarn } from '@/hooks';

  defineOptions({
    name: 'ReportingManagerCenter',
    inheritAttrs: false,
  });

  // 是否禁止签发按钮
  const disSign = computed(() => {
    if (!selectedRows.value.length) {
      return true;
    }
    return selectedRows.value.some((item: any) => item.status?.value !== 'WAIT_PUBLISH');
  });

  // 是否禁止再次签发按钮
  const disSignAgain = computed(() => {
    if (!selectedRows.value.length) {
      return true;
    }
    return selectedRows.value.some((item: any) => item.result?.value !== 'RESULT_PASS');
  });

  const { selectedRows, rowSelection, clearSelect } = useRowSelection({
    hideSelectAll: false,
    getCheckboxProps: (_record: any) => {
      return {
        disabled: false,
      };
    },
  });

  // 不合格数据详情
  const unqualifiedRef = ref<InstanceType<typeof Unqualified>>();

  // 审核次数
  const auditCntRef = ref<InstanceType<typeof AuditCnt>>();

  // 数量弹窗
  const cntModalRef = ref<InstanceType<typeof CntModal>>();

  // 审核弹窗
  const auditModalRef = ref<InstanceType<typeof AuditModal>>();

  const openAudit = (rows: any[]) => {
    auditModalRef.value?.openModal(rows);
  };

  // 撤回弹窗
  const backModalRef = ref<InstanceType<typeof BackModal>>();

  const openBack = (rows: any[]) => {
    backModalRef.value?.openModal(rows);
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
    openBack,
    print,
    printCheckRecord,
  );

  const bindStatus = ref('WAIT_PUBLISH');

  const options = [
    { label: t('待签发'), value: 'WAIT_PUBLISH' },
    { label: t('已签发'), value: 'PUBLISHED' },
    { label: t('全部'), value: '' },
  ];

  const { warnModal } = useWarn();

  // 再次签发
  const signAgain = async (rows: any[]) => {
    warnModal(t('请确认要进行再次签发'), {
      async onOk() {
        try {
          const ids = rows.map((item: any) => item.id);
          await reSignReport({ ids, sampleBatchNo: rows.map((item: any) => item.sampleBatchNo).join(',') });
          message.success(t('操作成功'));
          pageExpendRef.value?.fetchData();
          return Promise.resolve();
        } catch (error: any) {
          error.message && message.error(error.message);
          return Promise.reject();
        }
      },
    });
  };
</script>

<style scoped></style>
