<!-- 待入库血浆管理 -- 列表 -->
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
          v-hasAuth="170040002000001"
          :disabled="rowSelections[0]?.selectedRowKeys?.length === 0"
          type="primary"
          style="margin-right: 8px"
          @click="openAccept(operationSelectedRows)">
          {{ t('入库验收') }}
        </Button>
        <Button
          v-hasAuth="170040002000002"
          :disabled="rowSelections[0]?.selectedRowKeys?.length === 0"
          @click="openTransport">
          {{ t('获取运输信息') }}
        </Button>
      </div>
    </template>
    <template #expandColumnTitle0>{{}}</template>
    <template #expandedRowRender0="{ record, instance }">
      <BMPageComponent
        :ref="el => setExpandRef(record.syncBatchNo, el)"
        :rowKeys="['sampleNo']"
        :search="[false]"
        :hideRightTree="true"
        :tableFields="[
          {
            default: {
              ...instance.getQueryFormRef()?.handleFormValues(instance.getQueryFormRef().formModel),
              syncBatchNo: record.syncBatchNo,
            },
          },
        ]"
        :isExtraParamsChangeQuerys="[false]"
        :paginations="[paginationBig]"
        :showHeader="[false]"
        :showToolBars="[false]"
        :requests="[getPlasmaToStorageDetailList as DataRequestFn]"
        :columns="[expandMap[record.syncBatchNo].columnsFirst]"></BMPageComponent>
    </template>
  </BMPageComponent>
  <Accept ref="acceptRef" @submitSuccess="submitSuccess" />
  <Transport ref="transportRef" :rows="operationSelectedRows" />
</template>

<script setup lang="ts">
  import { getPlasmaToStorageList, getPlasmaToStorageDetailList } from '@/services';
  import { useTable } from './hooks';
  import { Transport, Accept } from '../index';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'PlasmaInStoredMng',
  });

  const router = useRouter();

  const enterView = (row: any) => {
    router.push({
      name: 'PlasmaInStoredMngViewCom',
      params: {
        syncBatchNo: row.syncBatchNo,
      },
    });
  };

  const { pageRef, columnsFirst, formFirstProps, expandMap, expandedRowKeys, expandChange } = useTable(enterView);

  // 查询操作
  const getLists = async (params: any) => {
    const datas = {
      ...params,
    };
    const res = await getPlasmaToStorageList(datas);

    const keys = res?.data?.list?.map((item: any) => item.syncBatchNo) || [];

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
      getCheckboxProps: (record: any) => {
        return {
          disabled: record?.acceptanceStatus?.value !== 0,
        };
      },
      onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
        if (rowSelections[0]?.selectedRowKeys) {
          rowSelections[0].selectedRowKeys = selectedRowKeys;
        }
        operationSelectedRows.value = selectedRows;
      },
    },
    null,
  ]);

  // 入库验收弹窗
  const acceptRef = ref();
  const openAccept = (rows: any) => {
    acceptRef.value?.openModal(rows);
  };

  // 打开获取运输信息弹窗
  const transportRef = ref();
  const openTransport = () => {
    transportRef.value?.openModal();
  };

  // 提交成功
  const submitSuccess = () => {
    if (rowSelections[0]?.selectedRowKeys) {
      rowSelections[0].selectedRowKeys = [];
    }
    operationSelectedRows.value = [];

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
