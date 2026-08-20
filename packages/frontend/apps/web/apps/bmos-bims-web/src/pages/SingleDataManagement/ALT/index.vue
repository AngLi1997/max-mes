<!-- ALT -->
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
    :rowClassNames="[rowClassName]"
    :expandedRowsChanges="[expandChange]"
    :paginations="[paginationBig]"
    :requests="[getLists as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <div class="table-header">
        <Button
          v-hasAuth="180020002000001"
          :disabled="disPublish()"
          type="primary"
          style="margin-right: 8px"
          @click="openReceive(operationSelectedRows, 'publish')">
          {{ t('发布') }}
        </Button>
        <Button v-hasAuth="180020002000002" type="primary" style="margin-right: 8px" @click="openBatch('publish')">
          {{ t('批量发布') }}
        </Button>
        <Button
          v-hasAuth="180020002000003"
          :disabled="disCheck()"
          style="margin-right: 8px"
          @click="openReceive(operationSelectedRows, 'check')">
          {{ t('核对') }}
        </Button>
        <Button v-hasAuth="180020002000004" type="primary" @click="openBatch('check')">
          {{ t('批量核对') }}
        </Button>
      </div>
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
              sampleOrgNo: record.sampleOrgNo,
            },
          },
        ]"
        :isExtraParamsChangeQuerys="[false]"
        :paginations="[paginationBig]"
        :showHeader="[false]"
        :showToolBars="[false]"
        :requests="[getALTSecondList as DataRequestFn]"
        :columns="[expandMap[record.sampleOrgNo].columnsFirst]"></BMPageComponent>
    </template>
  </BMPageComponent>
  <BatchModal ref="batchRef" @submitSuccess="nextModal" />
  <Receive
    ref="receiveRef"
    @submitSuccess="
      () => {
        if (rowSelections[0]?.selectedRowKeys) {
          rowSelections[0].selectedRowKeys = [];
        }
        operationSelectedRows = [];
        pageRef?.fetchData();
      }
    " />
  <!-- <ImportModal ref="importRef" /> -->
</template>

<script setup lang="ts">
  // import ImportModal from './components/ImportModal/index.vue';
  import { getALTList, getALTSecondList } from '@/services';
  import { paginationBig } from '@/utils/paginationConfig';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { BatchModal, Receive } from './components';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'ALT',
  });

  // 查询操作
  const getLists = async (params: any) => {
    const datas = {
      ...params,
    };
    const res = await getALTList(datas);

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
