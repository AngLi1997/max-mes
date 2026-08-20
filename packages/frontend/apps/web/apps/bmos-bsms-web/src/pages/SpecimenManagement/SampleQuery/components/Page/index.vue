<!-- 已入库标本查询 -- 列表 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['orgSampleNo']"
    :search="[true]"
    :hideRightTree="true"
    :rowSelections="rowSelections"
    :showHeader="[false]"
    :showToolBars="[true]"
    :formProps="[formFirstProps]"
    :requests="[loadData as DataRequestFn]"
    :paginations="[paginationFirst]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <Button
        v-hasAuth="170020006000001"
        type="primary"
        :disabled="operationSelectedRows.length === 0"
        @click="openUphold(operationSelectedRows)">
        {{ t('标本维护') }}
      </Button>
    </template>
  </BMPageComponent>
  <!-- 维护弹框 -->
  <UpholdModal ref="upholdModalRef" @submitSuccess="submitSuccess" />
</template>

<script setup lang="ts">
  import { getSampleInWarehouseQueryList } from '@/services';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { UpholdModal } from '../index';
  import { t } from '@bmos/i18n';

  // 维护操作
  const upholdModalRef = ref();

  const openUphold = (data: any) => {
    upholdModalRef.value?.openModal(data);
  };

  const { pageRef, columnsFirst, formFirstProps, paginationFirst } = useTable();

  const loadData: DataRequestFn = async (params: any): Promise<any> => {
    const datas = { ...params };
    return getSampleInWarehouseQueryList(datas);
  };

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
    operationSelectedRows.value = [];
    if (rowSelections[0]?.selectedRowKeys) {
      rowSelections[0].selectedRowKeys = [];
    }
    pageRef.value?.fetchData();
  };
</script>

<style scoped></style>
