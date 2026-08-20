<!-- 标本请验 -- 列表 -->
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
          v-hasAuth="170030001000001"
          type="primary"
          :disabled="operationSelectedRows.length === 0"
          @click="openPleaseVerify(operationSelectedRows)">
          {{ t('标本请验') }}
        </Button>
      </div>
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
        :requests="[getSampleExaminationInfoDetailList as DataRequestFn]"
        :columns="[expandMap[record.inspectionBatchNo].columnsFirst]"></BMPageComponent>
    </template>
  </BMPageComponent>
  <!-- 请验弹框 -->
  <PleaseVerifyModal
    ref="pleaseVerifyRef"
    @submitSuccess="
      () => {
        if (rowSelections[0]?.selectedRowKeys) {
          rowSelections[0].selectedRowKeys = [];
        }
        operationSelectedRows = [];
        pageRef?.fetchData();
      }
    " />
</template>

<script setup lang="ts">
  import { getSampleExaminationInfoPageList, getSampleExaminationInfoDetailList } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { PleaseVerifyModal } from '../index';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'InspectionRequest',
  });

  // 请验操作
  const pleaseVerifyRef = ref();

  const openPleaseVerify = (data: any) => {
    pleaseVerifyRef.value?.openModal(data);
  };

  const router = useRouter();

  const enterView = (row: any) => {
    router.push({
      name: 'InspectionRequestViewCom',
      params: {
        inspectionBatchNo: row.inspectionBatchNo,
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
          disabled: record?.inspectionStatus?.value === 1,
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
    const res = await getSampleExaminationInfoPageList(datas);

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
</script>

<style lang="less" scoped></style>
