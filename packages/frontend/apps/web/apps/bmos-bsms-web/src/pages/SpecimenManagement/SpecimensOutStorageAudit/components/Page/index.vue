<!-- 标本出库审核 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['outPlanBatchNo']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :showToolBars="[true]"
    :rowSelections="rowSelections"
    :formProps="[formFirstProps]"
    :tableFields="[
      {
        default: {
          pageFlag: 2,
        },
      },
    ]"
    :paginations="[paginationBig]"
    :expandedRowsChanges="[expandChange]"
    :requests="[getLists as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <div class="table-header">
        <Button
          v-hasAuth="170020010000001"
          :disabled="rowSelections[0]?.selectedRowKeys.length === 0"
          style="margin-right: 8px"
          @click="openAudit(operationSelectedRow, 'return')">
          {{ t('退回') }}
        </Button>
        <Button
          v-hasAuth="170020010000002"
          type="primary"
          :disabled="rowSelections[0]?.selectedRowKeys.length === 0"
          @click="openAudit(operationSelectedRow, 'audit')">
          {{ t('审核') }}
        </Button>
      </div>
    </template>
    <template #expandColumnTitle0>{{}}</template>
    <template #expandedRowRender0="{ record, instance }">
      <BMPageComponent
        :ref="el => setExpandRef(record.outPlanBatchNo, el)"
        :rowKeys="['id']"
        :search="[false]"
        :hideRightTree="true"
        :tableFields="[
          {
            default: {
              ...instance.getQueryFormRef()?.handleFormValues(instance.getQueryFormRef().formModel),
              outPlanNo: record.outPlanBatchNo,
            },
          },
        ]"
        :isExtraParamsChangeQuerys="[false]"
        :paginations="[paginationBig]"
        :showHeader="[false]"
        :showToolBars="[false]"
        :requests="[getSampleDeliveryPlanAuditDetail as DataRequestFn]"
        :columns="[expandMap[record.outPlanBatchNo].columnsFirst]"></BMPageComponent>
    </template>
  </BMPageComponent>
  <!-- 审核弹框 -->
  <AuditModal
    ref="auditModalRef"
    @submitSuccess="
      () => {
        if (rowSelections[0]?.selectedRowKeys) {
          rowSelections[0].selectedRowKeys = [];
          operationSelectedRow = {};
        }
        pageRef?.fetchData();
      }
    " />
</template>

<script setup lang="ts">
  import { getSampleDeliveryPlanAuditDetail, getSampleDeliveryPlanList } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { AuditModal } from '../index';
  import { t } from '@bmos/i18n';

  // 审核操作
  const auditModalRef = ref();

  const openAudit = (data: any, type: 'audit' | 'return') => {
    auditModalRef.value?.openModal(data, type);
  };

  const router = useRouter();

  const enterView = (outPlanBatchNo: any) => {
    router.push({
      name: 'SpecimenDeliveryPlanViewCom',
      params: {
        outPlanBatchNo,
      },
    });
  };

  const { pageRef, columnsFirst, formFirstProps, expandMap, expandedRowKeys, expandChange } = useTable(enterView);

  // 选中的数据
  const operationSelectedRow = ref<any>([]);

  // 多选
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
          disabled: record?.auditStatus?.value !== 0,
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

  // 查询操作
  const getLists = async (params: any) => {
    const datas = {
      ...params,
    };
    const res = await getSampleDeliveryPlanList(datas);

    const keys = res?.data?.list?.map((item: any) => item.outPlanBatchNo) || [];

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
</script>

<style lang="less" scoped></style>
