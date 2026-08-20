<!-- 不合格核查记录查询 -- 列表 -->
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
    :requests="[getUnqualifiedCheckRecordList as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <Button
        v-hasAuth="170070004000001"
        type="primary"
        :disabled="rowSelections[0]?.selectedRowKeys.length === 0"
        :loading="loading"
        @click="exportExcel">
        {{ t('打印不合格核查记录') }}
      </Button>
    </template>
  </BMPageComponent>
  <!-- 受影响血浆 -->
  <ViewEffectPlasma ref="viewEffectPlasmaRef" />
</template>

<script setup lang="ts">
  import { getUnqualifiedCheckRecordList, unqualifiedCheckRecordPrint } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { ViewEffectPlasma } from '../index';
  import { fileStreamDownload } from '@bmos/utils';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'CheckRecordQuery',
    inheritAttrs: false,
  });

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
    viewEffectPlasmaRef.value?.openModal(data.id);
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
          disabled: record?.auditStatus?.value !== 1,
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
  const exportExcel = async () => {
    try {
      loading.value = true;
      const res = await unqualifiedCheckRecordPrint(operationSelectedRow.value.id);
      let fileName = res.headers['content-disposition']?.split("filename*=utf-8''")[1];
      // 文件名解码
      fileName && fileName.indexOf('%') > -1 && (fileName = decodeURI(fileName));
      downloadFn(res.data, fileName);
    } finally {
      loading.value = false;
    }
  };
</script>

<style lang="less" scoped></style>
