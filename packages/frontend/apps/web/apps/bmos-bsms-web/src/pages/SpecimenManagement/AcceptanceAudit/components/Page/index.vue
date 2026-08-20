<!-- 验收审核 -- 列表 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['auditId']"
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
          v-hasAuth="170020007000001"
          :disabled="operationSelectedRows.length === 0"
          style="margin-right: 8px"
          @click="openAudit(operationSelectedRows, 'return')">
          {{ t('退回') }}
        </Button>
        <Button
          v-hasAuth="170020007000002"
          type="primary"
          :disabled="operationSelectedRows.length === 0"
          @click="openAudit(operationSelectedRows, 'audit')">
          {{ t('验收审核') }}
        </Button>
      </div>
    </template>
    <template #expandColumnTitle0>{{}}</template>
    <template #expandedRowRender0="{ record, instance }">
      <BMPageComponent
        :ref="el => setExpandRef(record.auditId, el)"
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
        :columns="[expandMap[record.auditId].columnsFirst]"></BMPageComponent>
    </template>
  </BMPageComponent>
  <!-- 审核弹框 -->
  <AuditModal ref="auditModalRef" @submitSuccess="submitSuccess" />
</template>

<script setup lang="ts">
  import { getSpecimenAcceptanceAuditList, getWaitInStorageDetailList } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { AuditModal } from '../index';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'AcceptanceAudit',
    inheritAttrs: false,
  });

  // 审核操作
  const auditModalRef = ref();

  const openAudit = (data: any, type: 'audit' | 'return') => {
    auditModalRef.value?.openModal(data, type);
  };

  const router = useRouter();

  const enterView = (row: any) => {
    router.push({
      name: 'AcceptanceAuditViewCom',
      params: {
        syncBatchNo: row.syncBatchNo,
      },
    });
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
      getCheckboxProps: (record: any) => {
        return {
          disabled: record?.auditResult?.value != 0,
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

  // 查询操作
  const getLists = async (params: any) => {
    const datas = {
      ...params,
    };
    const res = await getSpecimenAcceptanceAuditList(datas);

    const keys = res?.data?.list?.map((item: any) => item.auditId) || [];

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

  const submitSuccess = () => {
    if (rowSelections[0]?.selectedRowKeys) {
      rowSelections[0].selectedRowKeys = [];
    }
    operationSelectedRows.value = [];
    pageRef.value?.fetchData();
  };
</script>

<style lang="less" scoped></style>
