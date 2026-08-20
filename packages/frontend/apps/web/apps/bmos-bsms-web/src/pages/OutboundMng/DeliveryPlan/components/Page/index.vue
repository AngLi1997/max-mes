<!-- 出库计划 -- 列表 -->
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
    :requests="[getDeliveryPlanList as DataRequestFn]"
    :paginations="[paginationFirst]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <div class="table-header">
        <Button v-hasAuth="170100001000001" type="primary" style="margin-right: 8px" @click="openAddDialog">
          {{ t('新增') }}
        </Button>
        <Button
          v-hasAuth="170100001000002"
          :disabled="disApply"
          style="margin-right: 8px"
          @click="openOperateModal(operationSelectedRow, 'apply')">
          {{ t('计划申请') }}
        </Button>
        <Button
          v-hasAuth="170100001000003"
          :disabled="disApply"
          @click="openOperateModal(operationSelectedRow, 'change')">
          {{ t('更改出库批次') }}
        </Button>
      </div>
    </template>
    <template #tableHeaderToolbar0>
      <div class="table-header">
        <Button
          v-hasAuth="170100001000004"
          :disabled="rowSelections[0]?.selectedRowKeys?.length === 0"
          style="margin-right: 8px"
          :loading="loading"
          @click="printFile">
          {{ t('打印出库单') }}
        </Button>
        <Button
          v-hasAuth="170100001000005"
          :disabled="rowSelections[0]?.selectedRowKeys?.length === 0"
          style="margin-right: 8px"
          :loading="loading2"
          @click="printFile2">
          {{ t('打印出库单附页') }}
        </Button>
        <Button
          v-hasAuth="170100001000006"
          :disabled="rowSelections[0]?.selectedRowKeys?.length === 0"
          type="primary"
          :loading="exportLoading"
          @click="exportFile">
          {{ t('导出') }}
        </Button>
      </div>
    </template>
  </BMPageComponent>
  <!-- 新增 -->
  <AddDialog ref="addDialogRef" @submitSuccess="submitSuccess" />
  <!-- 计划申请/更改 -->
  <OperateModal
    ref="operateModalRef"
    @submitSuccess="
      () => {
        if (rowSelections[0]?.selectedRowKeys) {
          rowSelections[0].selectedRowKeys = [];
          operationSelectedRow.value = {};
        }
        pageRef?.fetchData();
      }
    " />
</template>

<script setup lang="ts">
  import { getDeliveryPlanList, exportDeliveryPlanList, printDeliveryPlan, printDeliveryPlanDetail } from '@/services';
  import { useTable } from './hooks/useTable';
  import { AddDialog, OperateModal } from '../index';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { fileStreamDownload } from '@bmos/utils';
  import { t } from '@bmos/i18n';
  import { message } from 'ant-design-vue';

  defineOptions({
    name: 'DeliveryPlan',
  });

  const router = useRouter();

  // 是否禁用计划申请
  const disApply = computed(() => {
    if (rowSelections[0]?.selectedRowKeys?.length === 0) {
      return true;
    } else {
      return operationSelectedRow.value?.approveStatus?.value !== 0;
    }
  });

  // --------------------计划申请/更改出库批次----------------------
  const operateModalRef = ref<InstanceType<typeof OperateModal>>();

  const openOperateModal = (row: any, type: 'apply' | 'change') => {
    operateModalRef.value?.openModal(row, type);
  };

  // -------------------新增------------------------
  const addDialogRef = ref<InstanceType<typeof AddDialog>>();

  const openAddDialog = () => {
    addDialogRef.value?.openModal();
  };
  // 操作成功
  const submitSuccess = (id: string) => {
    enterEdit(id);
  };

  // ----------------------详情-----------------------
  const enterDetail = (data: any) => {
    router.push({
      name: 'DeliveryPlanViewCom',
      query: {
        rowData: JSON.stringify(data),
      },
    });
  };

  // ----------------------编辑-----------------------
  const enterEdit = (id: string) => {
    router.push({ name: 'DeliveryPlanEditCom', params: { id } });
  };

  const { pageRef, columnsFirst, formFirstProps, paginationFirst } = useTable(enterDetail, enterEdit);

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
      getCheckboxProps: (record: any) => {
        return {
          disabled: record.batchNo == 'CKL-0513-01',
        };
      },
      onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
        if (rowSelections[0]?.selectedRowKeys) {
          rowSelections[0].selectedRowKeys = selectedRowKeys.length
            ? [selectedRowKeys[selectedRowKeys.length - 1]]
            : [];
          operationSelectedRow.value = selectedRows[selectedRows.length - 1];
        }
      },
    },
    null,
  ]);

  // 导出相关
  const exportLoading = ref(false);

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

  const exportFile = async () => {
    exportLoading.value = true;
    const res = await exportDeliveryPlanList({
      batchNo: operationSelectedRow.value?.batchNo,
    });
    let fileName = res.headers['content-disposition']?.split("filename*=utf-8''")[1];
    // 文件名解码
    fileName && fileName.indexOf('%') > -1 && (fileName = decodeURI(fileName));
    downloadFn(res.data, fileName);
    exportLoading.value = false;
  };

  // 打印出库单
  const loading = ref(false);
  const printFile = async () => {
    try {
      loading.value = true;
      const res = await printDeliveryPlan(operationSelectedRow.value?.batchNo);
      let fileName = res.headers['content-disposition']?.split("filename*=utf-8''")[1];
      // 文件名解码
      fileName && fileName.indexOf('%') > -1 && (fileName = decodeURI(fileName));
      downloadFn(res.data, fileName);
    } finally {
      loading.value = false;
    }
  };
  // 打印出库单附页
  const loading2 = ref(false);
  const printFile2 = async () => {
    try {
      loading2.value = true;
      const res = await printDeliveryPlanDetail(operationSelectedRow.value?.batchNo);
      let fileName = res.headers['content-disposition']?.split("filename*=utf-8''")[1];
      // 文件名解码
      fileName && fileName.indexOf('%') > -1 && (fileName = decodeURI(fileName));
      downloadFn(res.data, fileName);
    } finally {
      loading2.value = false;
    }
  };
</script>

<style scoped></style>
