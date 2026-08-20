<!-- 待入库标本管理 -- 列表 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['syncBatchNo']"
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
      <div class="table-header">
        <Button
          v-hasAuth="170020002000001"
          :disabled="isAccept(operationSelectedRows)"
          type="primary"
          style="margin-right: 8px"
          @click="openAccept(operationSelectedRows)">
          {{ t('入库验收') }}
        </Button>
        <Button
          v-hasAuth="170020002000002"
          type="primary"
          :disabled="isTransport(operationSelectedRows)"
          @click="openTransport(operationSelectedRows)">
          {{ t('获取运输信息') }}
        </Button>
      </div>
    </template>
    <template #expandColumnTitle0>{{}}</template>
    <template #expandedRowRender0="{ record, instance }">
      <BMPageComponent
        :ref="el => setExpandRef(record.syncBatchNo, el)"
        :rowKeys="['id']"
        :search="[false]"
        :hideRightTree="true"
        :tableFields="[
          {
            default: {
              ...instance.getQueryFormRef()?.handleFormValues(instance.getQueryFormRef().formModel),
              batchNo: record.syncBatchNo,
            },
          },
        ]"
        :isExtraParamsChangeQuerys="[false]"
        :paginations="[paginationBig]"
        :showHeader="[false]"
        :showToolBars="[false]"
        :requests="[getWaitInStorageDetailList as DataRequestFn]"
        :columns="[expandMap[record.syncBatchNo].columnsFirst]"></BMPageComponent>
    </template>
  </BMPageComponent>
  <AcceptModal ref="acceptModalRef" @submitSuccess="submitSuccess" />
  <Transport ref="transportRef" @submitSuccess="submitSuccess" />
</template>

<script setup lang="ts">
  import { getWaitInStorageList, getWaitInStorageDetailList } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { AcceptModal, Transport } from '../index';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'SampleInStoredMngPage',
    inheritAttrs: false,
  });

  const router = useRouter();

  const enterView = (row: any) => {
    // emits('enterView', row);
    router.push({
      name: 'SampleInStoredMngViewCom',
      params: { syncBatchNo: row?.syncBatchNo },
    });
  };

  // 是否可以验收
  const isAccept = (rows: any) => {
    return rows.length == 0 || rows.some((item: any) => item.acceptanceStatus?.value != 0);
  };

  // 是否可以获取运输信息
  const isTransport = (rows: any) => {
    return rows.length == 0;
  };

  const { pageRef, columnsFirst, formFirstProps, expandMap, expandedRowKeys, expandChange } = useTable(enterView);

  // 查询操作
  const getLists = async (params: any) => {
    const datas = {
      ...params,
    };

    const res = await getWaitInStorageList(datas);

    const keys = res?.data?.list?.map((item: any) => item.syncBatchNo) || [];

    // await nextTick();
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

  // 多选
  const operationSelectedRows = ref<any>([]);
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

  // 打开获取运输信息
  const transportRef = ref();
  const openTransport = (rows: any) => {
    transportRef.value?.openModal(rows);
  };

  // 打开验收
  const acceptModalRef = ref();
  const openAccept = (rows: any) => {
    acceptModalRef.value?.openModal(rows);
  };

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
