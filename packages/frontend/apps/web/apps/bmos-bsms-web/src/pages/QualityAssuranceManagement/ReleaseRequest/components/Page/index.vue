<!-- 放行单查询 -- 列表 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['noteId']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :showToolBars="[true]"
    :rowSelections="rowSelections"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :requests="[getQualityGuaranteeReleaseList as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <Button
        v-hasAuth="170060004000001"
        type="primary"
        :disabled="rowSelections[0]?.selectedRowKeys.length === 0"
        :loading="loading"
        @click="printReport(operationSelectedRow)">
        {{ t('打印放行单') }}
      </Button>
    </template>
  </BMPageComponent>
  <QuarantineCntModal ref="cntModalRef" />
</template>

<script setup lang="ts">
  import { getQualityGuaranteeReleaseList, printQualityGuaranteeRelease } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import QuarantineCntModal from '@/components/QuarantineCntModal/index.vue';
  import { fileStreamDownload } from '@bmos/utils';
  import { message } from 'ant-design-vue';

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

  const loading = ref(false);
  // 打印放行单
  const printReport = async (data: any) => {
    try {
      loading.value = true;
      const res = await printQualityGuaranteeRelease(data.noteId);
      let fileName = res.headers['content-disposition']?.split("filename*=utf-8''")[1];
      // 文件名解码
      fileName && fileName.indexOf('%') > -1 && (fileName = decodeURI(fileName));
      downloadFn(res.data, fileName);
    } finally {
      loading.value = false;
    }
  };

  const router = useRouter();

  const enterView = (row: any) => {
    // emits('enterView', row);
    router.push({
      name: 'view-com-detail',
      params: { id: row?.noteId },
    });
  };

  // ============数量查看=============
  const cntModalRef = ref();

  const openCntModal = (row: any, type: string) => {
    cntModalRef.value?.openModal(row, type);
  };

  const { pageRef, columnsFirst, formFirstProps } = useTable(openCntModal, enterView);

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
          disabled: record?.noteAuditStatus?.value !== 1,
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
