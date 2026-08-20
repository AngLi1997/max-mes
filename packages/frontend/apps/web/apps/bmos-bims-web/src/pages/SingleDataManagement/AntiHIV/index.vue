<!-- 抗-HIV -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['sampleOrgNo']"
    :search="[true]"
    :hideRightTree="true"
    :rowSelections="rowSelections"
    :showHeader="[false]"
    :showToolBars="[true]"
    :formProps="[formFirstProps]"
    :rowExpandables="[rowExpandable]"
    :expandedRowsChanges="[expandChange]"
    :tableFields="[
      {
        default: { checkItem: 3 },
      },
    ]"
    :rowClassNames="[rowClassName]"
    :paginations="[paginationBig]"
    :requests="[getLists as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <div class="table-header">
        <Button
          v-hasAuth="180020005000001"
          :disabled="disPublish()"
          type="primary"
          style="margin-right: 8px"
          @click="openReceive(operationSelectedRows, 'publish')">
          {{ t('发布') }}
        </Button>
        <Button v-hasAuth="180020005000002" type="primary" style="margin-right: 8px" @click="openBatch('publish')">
          {{ t('批量发布') }}
        </Button>
        <Button
          v-hasAuth="180020005000003"
          :disabled="disCheck()"
          style="margin-right: 8px"
          @click="openReceive(operationSelectedRows, 'check')">
          {{ t('核对') }}
        </Button>
        <Button v-hasAuth="180020005000004" type="primary" @click="openBatch('check')">
          {{ t('批量核对') }}
        </Button>
      </div>
    </template>
    <template #tableHeaderToolbar0>
      <Button v-hasAuth="180020005000005" @click="openImport">
        {{ t('读取') }}
      </Button>
    </template>
    <template #expandColumnTitle0>{{}}</template>
    <template #expandedRowRender0="{ record, instance }">
      <BMPageComponent
        :ref="el => setExpandRef(record.sampleOrgNo, el)"
        :rowKeys="['id']"
        :search="[false]"
        :hideRightTree="true"
        :tableFields="[
          {
            default: {
              ...instance.getQueryFormRef()?.handleFormValues(instance.getQueryFormRef().formModel),
              checkItem: 3,
              sampleOrgNo: record.sampleOrgNo,
            },
          },
        ]"
        :isExtraParamsChangeQuerys="[false]"
        :paginations="[paginationBig]"
        :showHeader="[false]"
        :showToolBars="[false]"
        :requests="[getMonoidalSecondList as DataRequestFn]"
        :columns="[expandMap[record.sampleOrgNo].columnsFirst]"></BMPageComponent>
    </template>
  </BMPageComponent>
  <BatchModal ref="batchRef" @submitSuccess="nextModal" />
  <Receive ref="receiveRef" @submitSuccess="submitSuccess" />
  <ImportModal ref="importRef" @submitSuccess="submitSuccess" />
</template>

<script setup lang="ts">
  // import ImportModal from './components/ImportModal/index.vue';
  import { getMonoidalList, getMonoidalSecondList } from '@/services';
  import { paginationBig } from '@/utils/paginationConfig';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { BatchModal, Receive, ImportModal } from './components';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'AntiHIV',
  });

  // 查询操作
  const getLists = async (params: any) => {
    const datas = {
      ...params,
    };
    const res = await getMonoidalList(datas);

    const keys = res?.data?.list?.map((item: any) => item.sampleOrgNo) || [];

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

  // 是否禁止发布
  const disPublish = () => {
    return (
      operationSelectedRows.value?.length === 0 ||
      operationSelectedRows.value?.some((item: any) => item?.publishStatus?.value != 1)
    );
  };

  // 是否禁止核对
  const disCheck = () => {
    return (
      operationSelectedRows.value?.length === 0 ||
      operationSelectedRows.value?.some((item: any) => item?.publishStatus?.value != 2)
    );
  };

  const receiveRef = ref();
  // 发布/核对
  const openReceive = (rows: any, type: 'publish' | 'check') => {
    receiveRef.value?.openModal(JSON.parse(JSON.stringify(rows)), type);
  };

  // 批量发布/核对
  const batchRef = ref();
  const openBatch = (type: 'publish' | 'check') => {
    batchRef.value?.openModal(type);
  };

  const nextModal = (dataList: any, type: 'publish' | 'check') => {
    openReceive(dataList, type);
  };

  // 读取
  const importRef = ref();
  const openImport = () => {
    importRef.value?.openModal();
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
      getCheckboxProps: (_record: any) => {
        return {
          disabled: false,
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

  const submitSuccess = () => {
    if (rowSelections[0]?.selectedRowKeys) {
      rowSelections[0].selectedRowKeys = [];
    }
    operationSelectedRows.value = [];
    pageRef.value?.fetchData();
  };

  const { pageRef, columnsFirst, formFirstProps, expandMap, expandedRowKeys, rowExpandable, expandChange } = useTable();

  const rowClassName = (record: any) => {
    return record?.checkResult?.value == 0 ? 'danger' : '';
  };
</script>

<style lang="less" scoped>
  .table-header {
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }
  :deep(.danger) {
    color: red;
  }
</style>
