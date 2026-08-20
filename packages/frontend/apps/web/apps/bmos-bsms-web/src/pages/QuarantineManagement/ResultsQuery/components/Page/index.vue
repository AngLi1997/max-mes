<!-- 检疫期结果查询 -- 列表 -->
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
    :requests="[getQuarantineReportSubmitAuditList as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0="{ instance }">
      <div style="display: flex; align-items: center; justify-content: flex-start">
        <Button
          v-hasAuth="170050005000001"
          type="primary"
          style="margin-right: 8px"
          @click="exportExcel(instance.getQueryFormRef()?.handleFormValues(instance.getQueryFormRef().formModel))">
          {{ t('导出') }}
        </Button>
        <Button
          v-hasAuth="170050005000002"
          :disabled="operationSelectedRow?.auditStatus?.value !== 3"
          style="margin-right: 8px"
          @click="printReport(operationSelectedRow)">
          {{ t('打印核查报告') }}
        </Button>
        <Button
          v-hasAuth="170050005000003"
          :disabled="operationSelectedRow?.auditStatus?.value !== 3"
          style="margin-right: 8px"
          @click="printResult(operationSelectedRow)">
          {{ t('打印核查结果') }}
        </Button>
        <Button
          v-hasAuth="170050005000004"
          :disabled="operationSelectedRow?.auditStatus?.value !== 3"
          @click="printRecord(operationSelectedRow)">
          {{ t('打印核查记录') }}
        </Button>
      </div>
    </template>
    <template #tableHeaderToolbar0>
      <Button
        v-hasAuth="170050005000005"
        :disabled="rowSelections[0]?.selectedRowKeys?.length === 0"
        style="margin-right: 8px"
        @click="enterView(operationSelectedRow, 3)">
        {{ t('查看报告') }}
      </Button>
      <Button
        v-hasAuth="170050005000005"
        :disabled="rowSelections[0]?.selectedRowKeys?.length === 0"
        @click="enterView(operationSelectedRow, 2)">
        {{ t('查看明细') }}
      </Button>
    </template>
  </BMPageComponent>
  <QuarantineCntModal ref="cntModalRef" />
</template>

<script setup lang="ts">
  import {
    getQuarantineReportSubmitAuditList,
    exportQuarantineReportAudit,
    printQuarantineReportAudit,
    printQuarantineReportAuditResult,
    printQuarantineReportAuditRecord,
  } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import QuarantineCntModal from '@/components/QuarantineCntModal/index.vue';
  import { fileStreamDownload } from '@bmos/utils';
  import { message } from 'ant-design-vue';

  defineOptions({
    name: 'ResultsQueryPage',
    inheritAttrs: false,
  });

  const router = useRouter();

  const enterView = (record: any, type: any) => {
    router.push({
      name: 'quarantine-check-detail',
      params: { id: record.quarantineId },
      query: { type },
    });
  };

  // ============数量查看=============
  const cntModalRef = ref();

  const openCntModal = (row: any, type: string) => {
    cntModalRef.value?.openModal(row, type);
  };

  const { pageRef, columnsFirst, formFirstProps, rowSelections, operationSelectedRow } = useTable(
    openCntModal,
    enterView,
  );

  const downloadFn = (res: any) => {
    let data = res.data;
    try {
      const uint8Array = new Uint8Array(data);
      const decoder = new TextDecoder();
      const jsonString = decoder.decode(uint8Array);
      const error = JSON.parse(jsonString);
      error.message && message.error(error.message);
    } catch (error) {
      let fileName = res.headers['content-disposition']?.split("filename*=utf-8''")[1];
      // 文件名解码
      fileName && fileName.indexOf('%') > -1 && (fileName = decodeURI(fileName));
      fileStreamDownload(data, fileName);
    }
  };

  const loading = ref(false);
  const exportExcel = async (data: any) => {
    try {
      loading.value = true;
      const res = await exportQuarantineReportAudit(data);
      downloadFn(res);
    } finally {
      loading.value = false;
    }
  };

  const loading1 = ref(false);
  const loading2 = ref(false);
  const loading3 = ref(false);

  const printReport = async (data: any) => {
    try {
      loading1.value = true;
      const res = await printQuarantineReportAudit({ checkNo: data.checkNo });
      downloadFn(res);
    } finally {
      loading1.value = false;
    }
  };

  const printResult = async (data: any) => {
    try {
      loading2.value = true;
      const res = await printQuarantineReportAuditResult({ checkNo: data.checkNo });
      downloadFn(res);
    } finally {
      loading2.value = false;
    }
  };

  const printRecord = async (data: any) => {
    try {
      loading3.value = true;
      const res = await printQuarantineReportAuditRecord({ checkNo: data.checkNo });
      downloadFn(res);
    } finally {
      loading3.value = false;
    }
  };
</script>

<style lang="less" scoped></style>
