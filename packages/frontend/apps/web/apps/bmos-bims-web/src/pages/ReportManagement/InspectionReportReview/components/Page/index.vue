<!-- 检验报告审核 -- 列表 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :rowSelections="rowSelections"
    :showHeader="[false]"
    :showToolBars="[true]"
    :tableFields="[
      {
        default: {
          reportStatusList: [1, 2],
        },
      },
    ]"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :expandedRowsChanges="[expandChange]"
    :requests="[getLists as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <Button v-hasAuth="180040002000001" :disabled="disCheck(operationSelectedRows)" type="primary" @click="openModal">
        {{ t('审核') }}
      </Button>
    </template>
    <template #tableHeaderToolbar0>
      <Button
        v-hasAuth="180040002000002"
        :disabled="rowSelections[0]?.selectedRowKeys?.length !== 1 || disPrint(operationSelectedRows[0])"
        :loading="loading"
        @click="printReport(operationSelectedRows[0])">
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
  <AuditModal ref="auditModalRef" @submitSuccess="submitSuccess" />
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
  import { AuditModal, UnqualifiedModal } from '../index';
  import { t } from '@bmos/i18n';
  import { fileStreamDownload } from '@bmos/utils';
  import { message } from 'ant-design-vue';

  defineOptions({
    name: 'InspectionReportReview',
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

  const disCheck = (rows: any) => {
    if (!rows || rows.length == 0) {
      return true;
    } else {
      return rows.some((item: any) => item.reportStatus?.value !== 1);
    }
  };

  const disPrint = (row: any) => {
    if (!row?.id) {
      return true;
    } else {
      return row?.reportStatus?.value !== 2;
    }
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

  const operationSelectedRows = ref<any>([]);

  // 单选
  const rowSelections = reactive([
    {
      type: 'checkbox',
      hideSelectAll: false,
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
          rowSelections[0].selectedRowKeys = selectedRowKeys;
          operationSelectedRows.value = selectedRows;
        }
      },
    },
    null,
  ]);

  // 生成报告
  const auditModalRef = ref();
  const openModal = () => {
    auditModalRef.value?.openModal(operationSelectedRows.value);
  };

  // 打印报告
  // const printReport = async () => {
  //   try {
  //     await printInspectionReportManagement(operationSelectedRows.value?.id);
  //   } catch (error: any) {
  //     error.message && message.error(error.message);
  //   }
  // };

  const submitSuccess = () => {
    if (rowSelections[0]?.selectedRowKeys) {
      rowSelections[0].selectedRowKeys = [];
      operationSelectedRows.value = [];
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
