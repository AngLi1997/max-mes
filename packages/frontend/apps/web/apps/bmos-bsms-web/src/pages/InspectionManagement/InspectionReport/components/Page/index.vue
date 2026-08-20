<!-- 检验报告 -- 列表 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['inspectionBatchNo']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :showToolBars="[true]"
    :rowSelections="rowSelections"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :expandedRowsChanges="[expandChange]"
    :requests="[getLists as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <Button
        v-hasAuth="170030003000001"
        type="primary"
        :disabled="!operationSelectedRow?.inspectionBatchNo"
        :loading="loading"
        @click="printReport(operationSelectedRow)">
        {{ t('打印报告') }}
      </Button>
    </template>
    <template #expandColumnTitle0>{{}}</template>
    <template #expandedRowRender0="{ record, instance }">
      <BMPageComponent
        :ref="el => setExpandRef(record.inspectionBatchNo, el)"
        :rowKeys="['id']"
        :search="[false]"
        :hideRightTree="true"
        :tableFields="[
          {
            default: {
              ...instance.getQueryFormRef()?.handleFormValues(instance.getQueryFormRef().formModel),
              inspectionBatchNo: record.inspectionBatchNo,
              flag: 2,
            },
          },
        ]"
        :isExtraParamsChangeQuerys="[false]"
        :paginations="[paginationBig]"
        :showHeader="[false]"
        :showToolBars="[false]"
        :requests="[getExaminationResultPageList as DataRequestFn]"
        :columns="[expandMap[record.inspectionBatchNo].columnsFirst]"></BMPageComponent>
    </template>
  </BMPageComponent>
  <!-- 不合格数量弹框 -->
  <Cnt ref="cntRef" />
</template>

<script setup lang="ts">
  import { getInspectionReportList, getExaminationResultPageList, printInspectionReport } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { Cnt } from '../index';
  import { t } from '@bmos/i18n';
  import { fileStreamDownload } from '@bmos/utils';
  import { message } from 'ant-design-vue';

  defineOptions({
    name: 'InspectionReport',
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

  const loading = ref(false);

  // 打印报告
  const printReport = async (data: any) => {
    try {
      loading.value = true;
      const res = await printInspectionReport(data);
      let fileName = res.headers['content-disposition'].split("filename*=utf-8''")[1];
      // 文件名解码
      fileName && fileName.indexOf('%') > -1 && (fileName = decodeURI(fileName));
      downloadFn(res.data, fileName);
    } finally {
      loading.value = false;
    }
  };

  // 不合格数量弹框
  const cntRef = ref();

  const openCnt = (data: any) => {
    cntRef.value?.openModal(data);
  };

  const router = useRouter();

  const enterView = (row: any) => {
    router.push({
      name: 'InspectionReportViewCom',
      params: {
        inspectionBatchNo: row.inspectionBatchNo,
      },
    });
  };

  const { pageRef, columnsFirst, formFirstProps, expandMap, expandedRowKeys, expandChange } = useTable(
    openCnt,
    enterView,
  );

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
      getCheckboxProps: (_record: any) => {
        return {
          disabled: false,
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

  // 查询操作
  const getLists = async (params: any) => {
    const datas = {
      ...params,
    };
    const res = await getInspectionReportList(datas);

    const keys = res?.data?.list?.map((item: any) => item.inspectionBatchNo) || [];

    // 查询二级列表（如果展开了的话）
    expandedRowKeys.value?.forEach((key: any) => {
      if (keys.includes(key)) {
        expandMap[key].fetchData();
      }
    });
    return res;
  };

  // 设置二级列表的ref
  const setExpandRef = (syncBatchNo: any, ref: any) => {
    expandMap[syncBatchNo].setRef(ref);
  };

  // const submitSuccess = () => {
  //   if (rowSelections[0]?.selectedRowKeys) {
  //     rowSelections[0].selectedRowKeys = [];
  //   }
  //   operationSelectedRows.value = [];
  //   pageRef.value?.fetchData();
  // };
</script>

<style lang="less" scoped></style>
