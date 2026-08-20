<!-- 分拣计划 -- 列表 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :showAction="false"
    :showHeader="[false]"
    :showToolBars="[true]"
    :rowSelections="rowSelections"
    :isExtraParamsChangeQuerys="[false]"
    :formProps="[formFirstProps]"
    :paginations="[paginationFirst]"
    :tableFields="[
      {
        default: { itemType: tableType },
      },
    ]"
    :requests="[getSortingPlanList as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <div style="display: flex; align-items: center; justify-content: flex-start">
        <Button v-hasAuth="170080002000001" type="primary" style="margin-right: 8px" @click="openAddDialog">
          {{ t('新增') }}
        </Button>
        <Button v-hasAuth="170080002000002" :disabled="rowSelections[0]?.selectedRowKeys?.length === 0" @click="finish">
          {{ t('结束') }}
        </Button>
      </div>
    </template>
    <template #tableHeaderToolbar0>
      <Segmented v-model:value="tableType" :options="typeOpts" @change="changeTableType"></Segmented>
    </template>
  </BMPageComponent>
  <AddDialog ref="addDialogRef" @submitSuccess="submitSuccess" />
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { BMPageComponent, DataRequestFn } from '@bmos/components';
  import { useTable } from './hooks/useTable';
  import { sortingPlanFinish, getSortingPlanList } from '@/services';
  import { AddDialog } from '../index';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { Modal, message } from 'ant-design-vue';

  defineOptions({
    name: 'SortingPlan',
    inheritAttrs: false,
  });

  // -------------------新增------------------------
  const addDialogRef = ref<InstanceType<typeof AddDialog>>();

  const openAddDialog = () => {
    addDialogRef.value?.openModal(tableType.value);
  };

  // 操作成功
  const submitSuccess = (planBatchNo: string) => {
    enterEdit(planBatchNo);
  };

  const router = useRouter();

  // ----------------------详情-----------------------
  const enterDetail = (planBatchNo: any) => {
    router.push({
      name: 'SortingPlanViewCom',
      query: {
        planBatchNo,
        itemType: tableType.value,
      },
    });
  };

  // ----------------------编辑-----------------------
  const enterEdit = (planBatchNo: any) => {
    router.push({
      name: 'SortingPlanEditCom',
      query: {
        planBatchNo,
        itemType: tableType.value,
      },
    });
  };

  const { pageRef, columnsFirst, formFirstProps, paginationFirst, changeType } = useTable(enterDetail, enterEdit);

  const tableType = ref<any>(1);

  const typeOpts = [
    {
      label: t('血浆'),
      value: 1,
    },
    {
      label: t('标本'),
      value: 2,
    },
  ];

  // 选项变化时重新获取数据
  const changeTableType = async (val: any) => {
    changeType(val);
    await pageRef.value?.fetchData();
  };

  // 选中的数据
  const operationSelectedRows = ref<any>({});

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

  // 结束
  const finish = async () => {
    Modal.confirm({
      title: t('是否结束这些分拣计划?'),
      icon: h(ExclamationCircleOutlined),
      async onOk() {
        try {
          const data = {
            planBatchNoList: operationSelectedRows.value.map((item: any) => item.batchNo),
          };
          await sortingPlanFinish(data);
          message.success(t('操作成功'));
          pageRef.value?.fetchData();
        } catch (error: any) {
          error.message && message.error(error.message);
          return Promise.reject();
        }
      },
      onCancel() {},
    });
  };
</script>

<style lang="less" scoped>
  :deep(.bsms-segmented) {
    .bsms-segmented-item {
      padding: 0 8px;
      flex-grow: 1;
    }
  }
</style>
