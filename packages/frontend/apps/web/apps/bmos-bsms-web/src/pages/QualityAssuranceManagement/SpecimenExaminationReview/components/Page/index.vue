<!-- 标本请验审核 -- 列表 -->
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
      <div class="table-header">
        <Button
          v-hasAuth="170060001000002"
          :disabled="rowSelections[0]?.selectedRowKeys?.length === 0"
          type="primary"
          style="margin-right: 8px"
          @click="openAudit(1)">
          {{ t('审核') }}
        </Button>
        <Button
          v-hasAuth="170060001000001"
          :disabled="rowSelections[0]?.selectedRowKeys?.length === 0"
          @click="openAudit(2)">
          {{ t('退回') }}
        </Button>
      </div>
    </template>
    <template #expandColumnTitle0>{{}}</template>
    <template #expandedRowRender0="{ record, instance }">
      <BMPageComponent
        :ref="el => setExpandRef(record.id, el)"
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
        :requests="[getSampleExaminationInfoDetailList as DataRequestFn]"
        :columns="[expandMap[record.id].columnsFirst]"></BMPageComponent>
    </template>
  </BMPageComponent>
  <AuditCom ref="auditComRef" @submitSuccess="submitSuccess" />
</template>

<script setup lang="ts">
  import { getSpecimenExaminationList, getSampleExaminationInfoDetailList } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { AuditCom } from '../index';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'SpecimenExaminationReview',
  });

  const router = useRouter();

  const enterView = (row: any) => {
    router.push({
      name: 'SpecimenExaminationReviewViewCom',
      params: { inspectionBatchNo: row?.inspectionBatchNo },
    });
  };

  const { pageRef, columnsFirst, formFirstProps, expandMap, expandedRowKeys, expandChange } = useTable(enterView);

  // 查询操作
  const getLists = async (params: any) => {
    const datas = {
      ...params,
    };

    const res = await getSpecimenExaminationList(datas);

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
          disabled: record?.auditStatus?.value !== 0,
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

  // 打开审核
  const auditComRef = ref();
  const openAudit = (status: number) => {
    auditComRef.value?.openModal(operationSelectedRows.value, status);
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
