<!-- 标本出库计划 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['outPlanBatchNo']"
    :search="[true]"
    :hideRightTree="true"
    :rowSelections="rowSelections"
    :showHeader="[false]"
    :showToolBars="[true]"
    :tableFields="[
      {
        default: {
          pageFlag: 1,
        },
      },
    ]"
    :formProps="[formFirstProps]"
    :requests="[getSampleDeliveryPlanList as DataRequestFn]"
    :paginations="[paginationFirst]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <div class="table-header">
        <Button v-hasAuth="170020009000001" type="primary" style="margin-right: 8px" @click="openAddDialog">
          {{ t('新增') }}
        </Button>
        <Button
          v-hasAuth="170020009000002"
          :disabled="rowSelections[0]?.selectedRowKeys?.length === 0"
          style="margin-right: 8px"
          @click="openOperateModel(operationSelectedRow, 'apply')">
          {{ t('计划申请') }}
        </Button>
        <Button
          v-hasAuth="170020009000003"
          :disabled="rowSelections[0]?.selectedRowKeys?.length === 0"
          @click="openOperateModel(operationSelectedRow, 'editNo')">
          {{ t('更改出库批次') }}
        </Button>
      </div>
    </template>
  </BMPageComponent>
  <AddDialog ref="addDialogRef" @submitSuccess="submitSuccess" />
  <OperateModel
    ref="operateModel"
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
  import { getSampleDeliveryPlanList } from '@/services';
  import { useTable } from './hooks/useTable';
  import { AddDialog, OperateModel } from '../index';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'SpecimenDeliveryPlan',
    inheritAttrs: false,
  });

  const router = useRouter();

  // -------------------新增------------------------
  const addDialogRef = ref<InstanceType<typeof AddDialog>>();

  const openAddDialog = () => {
    addDialogRef.value?.openModal();
  };

  // 操作成功
  const submitSuccess = (outPlanBatchNo: string) => {
    enterEdit(outPlanBatchNo);
  };

  // ----------------------详情-----------------------
  const enterDetail = (outPlanBatchNo: string) => {
    router.push({
      name: 'SpecimenDeliveryPlanViewCom',
      params: {
        outPlanBatchNo,
      },
    });
  };

  // ----------------------编辑-----------------------
  const enterEdit = (outPlanBatchNo: string) => {
    router.push({
      name: 'SpecimenDeliveryPlanEditCom',
      params: {
        outPlanBatchNo,
      },
    });
  };

  // -----------------------计划申请 / 更改出库批次------------------------

  const operateModel = ref<InstanceType<typeof OperateModel>>();
  const openOperateModel = (row: any, newtype: 'apply' | 'editNo') => {
    operateModel.value?.openModal(row, newtype);
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
          disabled: record?.applyStatus?.value === 1,
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
</script>

<style scoped></style>
