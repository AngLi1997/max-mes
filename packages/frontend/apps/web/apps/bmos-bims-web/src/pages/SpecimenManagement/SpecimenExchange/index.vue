<!-- 标本交接 -->
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
      <div class="table-header">
        <Button
          v-hasAuth="180010001000001"
          type="primary"
          :disabled="disableAccept()"
          @click="openAccept(operationSelectedRows)">
          {{ t('接收') }}
        </Button>
      </div>
    </template>
    <template #tableHeaderToolbar0>
      <Button
        v-hasAuth="180010001000002"
        :disabled="rowSelections[0]?.selectedRowKeys.length !== 1"
        :loading="exportLoading"
        @click="exportFile">
        {{ t('导出') }}
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
            },
          },
        ]"
        :isExtraParamsChangeQuerys="[false]"
        :paginations="[paginationBig]"
        :showHeader="[false]"
        :showToolBars="[false]"
        :requests="[getSampleBatchInfoList as DataRequestFn]"
        :columns="[expandMap[record.inspectionBatchNo].columnsFirst]"></BMPageComponent>
    </template>
  </BMPageComponent>
  <!-- 接收弹框 -->
  <AcceptModal ref="acceptModalRef" @submitSuccess="submitSuccess" />
</template>

<script setup lang="ts">
  import { getSampleBatchList, getSampleBatchInfoList, sampleDataExport } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { AcceptModal } from './components';
  import { fileStreamDownload } from '@bmos/utils';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'SpecimenExchange',
  });

  // 是否禁止接收
  const disableAccept = () => {
    if (rowSelections[0]?.selectedRowKeys.length === 0) {
      return true;
    }
    return operationSelectedRows.value?.some((item: any) => item?.receiveStatus?.value !== 1);
  };

  // 接收操作
  const acceptModalRef = ref();

  const openAccept = (data: any) => {
    acceptModalRef.value?.openModal(data);
  };

  const emits = defineEmits(['enterView']);

  const enterView = (row: any) => {
    emits('enterView', row);
  };

  const { pageRef, columnsFirst, formFirstProps, expandMap, expandedRowKeys, expandChange } = useTable(enterView);

  // 选中的数据
  const operationSelectedRows = ref<any>([]);

  // 多选
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

  // 查询操作
  const getLists = async (params: any) => {
    const datas = {
      ...params,
    };
    const res = await getSampleBatchList(datas);

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
  const setExpandRef = (key: any, ref: any) => {
    expandMap[key].setRef(ref);
  };

  const submitSuccess = () => {
    if (rowSelections[0]?.selectedRowKeys) {
      rowSelections[0].selectedRowKeys = [];
      operationSelectedRows.value = [];
    }
    pageRef.value?.fetchData();
  };

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
    const res = await sampleDataExport({
      inspectionBatchNo: operationSelectedRows.value.map((item: any) => item.inspectionBatchNo)?.[0],
    });
    let fileName = res.headers['content-disposition']?.split("filename*=utf-8''")[1];
    // 文件名解码
    fileName && fileName.indexOf('%') > -1 && (fileName = decodeURI(fileName));
    downloadFn(res.data, fileName);
    exportLoading.value = false;
  };
</script>

<style lang="less" scoped></style>
