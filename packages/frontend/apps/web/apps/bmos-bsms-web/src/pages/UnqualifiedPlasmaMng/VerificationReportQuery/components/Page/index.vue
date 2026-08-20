<!-- 不合格核查报告查询 -- 列表 -->
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
    :requests="[unqualifiedPlasmaReportQueryList as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <Button
        v-hasAuth="170070007000001"
        type="primary"
        :disabled="rowSelections[0]?.selectedRowKeys.length === 0"
        style="margin-right: 8px"
        :loading="downloadLoading"
        @click="printReport(operationSelectedRow)">
        {{ t('打印不合格核查报告') }}
      </Button>
    </template>
  </BMPageComponent>
  <!-- 受影响血浆 -->
  <ViewEffectPlasma ref="viewEffectPlasmaRef" />
</template>

<script setup lang="ts">
  import { unqualifiedPlasmaReportQueryList, unqualifiedPlasmaReportPrint } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { ViewEffectPlasma } from '../index';
  import { fileStreamDownload } from '@bmos/utils';
  import { t } from '@bmos/i18n';
  import { message } from 'ant-design-vue';

  defineOptions({
    name: 'VerificationReportQuery',
  });

  const downloadFn = (data: any, fileName: string) => {
    try {
      const uint8Array = new Uint8Array(data);
      const decoder = new TextDecoder();
      const jsonString = decoder.decode(uint8Array);
      const error = JSON.parse(jsonString);
      error.message && message.error(error.message);
    } catch (error) {
      fileStreamDownload(data, fileName);
    }
  };

  const downloadLoading = ref(false);

  // 打印不合格核查报告
  const printReport = async (data: any) => {
    try {
      downloadLoading.value = true;
      const res = await unqualifiedPlasmaReportPrint(data.reportBillNo);
      let fileName = res.headers['content-disposition']?.split("filename*=utf-8''")[1];
      // 文件名解码
      fileName && fileName.indexOf('%') > -1 && (fileName = decodeURI(fileName));
      downloadFn(res.data, fileName);
    } finally {
      downloadLoading.value = false;
    }
  };

  const router = useRouter();

  const enterView = (row: any) => {
    router.push({
      name: 'CheckRecordReviewViewCom',
      params: { id: row.id },
    });
  };

  // 查看受影响血浆
  const viewEffectPlasmaRef = ref();

  const openView = (data: any) => {
    viewEffectPlasmaRef.value?.openModal(data.unqualifiedPlasmaInfoId);
  };

  const { pageRef, columnsFirst, formFirstProps } = useTable(openView, enterView);

  // 选中的数据
  const operationSelectedRow = ref<any>({});

  // 多选
  const rowSelections = reactive([
    {
      type: 'checkbox',
      hideSelectAll: true,
      columnWidth: 50,
      fixed: true,
      selectedRowKeys: [] as any[],
      preserveSelectedRowKeys: true,
      getCheckboxProps: (record: any) => {
        return {
          disabled: record?.auditStatus?.value !== 3,
        };
      },
      onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
        if (rowSelections[0]?.selectedRowKeys) {
          rowSelections[0].selectedRowKeys = selectedRowKeys?.length
            ? [selectedRowKeys[selectedRowKeys.length - 1]]
            : [];
          operationSelectedRow.value = selectedRows[selectedRows.length - 1];
        }
      },
    },
    null,
  ]);
</script>

<style lang="less" scoped></style>
