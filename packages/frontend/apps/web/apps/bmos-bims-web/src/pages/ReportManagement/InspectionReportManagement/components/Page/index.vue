<!-- 检验报告管理 -- 列表 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :rowSelections="rowSelections"
    :showHeader="[false]"
    :showToolBars="[true]"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :expandedRowsChanges="[expandChange]"
    :requests="[getLists as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <Button
        v-hasAuth="180040001000001"
        :disabled="disGenerate(operationSelectedRow)"
        type="primary"
        @click="openModal">
        {{ t('生成报告') }}
      </Button>
    </template>
    <template #tableHeaderToolbar0>
      <Button
        v-hasAuth="180040001000002"
        :disabled="disPrint(operationSelectedRow)"
        :loading="loading"
        @click="printReport(operationSelectedRow)">
        {{ t('打印报告') }}
      </Button>
    </template>
    <template #expandColumnTitle0>{{}}</template>
    <template #expandedRowRender0="{ record, instance }">
      <BMPageComponent
        :ref="el => setExpandRef(record.id, el)"
        :rowKeys="['sampleOrgNo']"
        :search="[false]"
        :hideRightTree="true"
        :tableFields="[
          {
            default: {
              ...instance.getQueryFormRef()?.handleFormValues(instance.getQueryFormRef().formModel),
              sampleBatchNo: record.sampleBatchNo,
            },
          },
        ]"
        :isExtraParamsChangeQuerys="[false]"
        :paginations="[paginationBig]"
        :showHeader="[false]"
        :showToolBars="[false]"
        :requests="[getInspectionReportManagementSecondList as DataRequestFn]"
        :columns="[expandMap[record.id].columnsFirst]"></BMPageComponent>
    </template>
  </BMPageComponent>
  <ReportModal ref="reportModalRef" @submitSuccess="submitSuccess" />
  <UnqualifiedModal ref="unqualifiedModalRef" />
</template>

<script setup lang="ts">
  import {
    getInspectionReportManagementList,
    getInspectionReportManagementSecondList,
    printInspectionReportManagement,
  } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { ReportModal, UnqualifiedModal } from '../index';
  import { t } from '@bmos/i18n';
  import { fileStreamDownload } from '@bmos/utils';
  import { message } from 'ant-design-vue';

  defineOptions({
    name: 'InspectionReportManagement',
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

  // 打印不合格核查报告
  const printReport = async (data: any) => {
    try {
      loading.value = true;
      const res = await printInspectionReportManagement(data.id);
      let fileName = res.headers['content-disposition'].split("filename*=utf-8''")[1];
      // 文件名解码
      fileName && fileName.indexOf('%') > -1 && (fileName = decodeURI(fileName));
      downloadFn(res.data, fileName);
    } finally {
      loading.value = false;
    }
  };

  const router = useRouter();

  const enterView = (row: any) => {
    router.push({
      name: 'InspectionReportManagementViewCom',
      params: { id: row.id },
    });
  };

  // 查看不合格数据
  const unqualifiedModalRef = ref();
  const openUnqualifiedModal = (row: any) => {
    unqualifiedModalRef.value.openModal(row);
  };

  const { pageRef, columnsFirst, formFirstProps, expandMap, expandedRowKeys, expandChange } = useTable(
    openUnqualifiedModal,
    enterView,
  );

  const disGenerate = (row: any) => {
    if (!row?.id) {
      return true;
    } else {
      return row?.reportStatus?.value !== 0;
    }
  };

  const disPrint = (row: any) => {
    if (!row?.id) {
      return true;
    } else {
      return row?.reportStatus?.value !== 2;
    }
  };

  // 查询操作
  const getLists = async (params: any) => {
    const datas = {
      ...params,
      //
    };

    const res = await getInspectionReportManagementList(datas);

    const keys = res?.data?.list?.map((item: any) => item.id) || [];
    // 查询二级列表（如果展开了的话）
    expandedRowKeys.value?.forEach((key: any) => {
      if (keys.includes(key)) {
        expandMap[key].fetchData();
      }
    });
    return res;
  };

  // 设置二级列表的ref
  const setExpandRef = (key: any, ref: any) => {
    expandMap[key].setRef(ref);
  };

  const operationSelectedRow = ref<any>({});

  // 单选
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

  // 生成报告
  const reportModalRef = ref();
  const openModal = () => {
    reportModalRef.value?.openModal(operationSelectedRow.value);
  };

  const submitSuccess = () => {
    if (rowSelections[0]?.selectedRowKeys) {
      rowSelections[0].selectedRowKeys = [];
      operationSelectedRow.value = {};
    }
    pageRef.value?.fetchData();
  };
</script>

<style lang="less" scoped>
  .table-header {
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }
</style>
