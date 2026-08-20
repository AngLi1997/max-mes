<!-- 静态数据 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :showAllAddIcon="false"
    :showAction="false"
    :treeData="treeData"
    :search="[false]"
    :fieldNames="{
      title: 'menuName',
      key: 'id',
      children: 'childMenuList',
    }"
    :dragSorts="[true]"
    :treeField="{
      field: {
        menuIdentify: 'menuIdentify',
      },
    }"
    :showEmpty="true"
    :paginations="[paginationBig]"
    :rowSelections="[rowSelection]"
    :requests="[getConfigPage as any]"
    :columns="[columnsFirst]"
    @tableSortChange="tableSortChange">
    <template #tableHeaderTitle0="{ treeNode }">
      <Space>
        <Button
          v-hasAuth="210080001000003"
          type="primary"
          :disabled="!treeNode.id"
          @click="() => openAddEditModal(OperationStatusMap.ADD, treeNode)">
          {{ t('添加') }}
        </Button>
        <Button
          v-hasAuth="210080001000002"
          :disabled="!selectedRows.length"
          @click="() => deleteConfig(selectedRows, treeNode)">
          {{ t('删除') }}
        </Button>
      </Space>
    </template>
    <template #tableHeaderToolbar0="{ treeNode }">
      <span>
        <AddEditModal
          v-model:modalOpen="addEditModalOpen"
          :treeNode="treeNode"
          :status="operationStatus"
          :rowData="firstRowData"
          @ok="updateTableData" />
      </span>
      <Button
        v-hasAuth="210080001000001"
        type="primary"
        :disabled="!hasTableSortChange"
        @click="() => editSort(treeNode)">
        {{ t('提交排序') }}
      </Button>
    </template>
  </BMPageComponent>
</template>

<script setup lang="tsx">
  import { t } from '@bmos/i18n';
  import { BMPageComponent, Recordable } from '@bmos/components';
  import { useTable } from './hooks';
  import { useRowSelection } from '@/hooks';
  import { postStaticDataConfigPage, postStaticDataConfigSortEdit } from '@/services';
  import { isEmpty } from '@bmos/utils';
  import { OperationStatusMap } from '@/types';
  import AddEditModal from './components/AddEditModal.vue';
  import { message } from 'ant-design-vue';
  import { paginationBig } from '@/utils';
  import { useDict } from '@/stores';

  defineOptions({
    name: 'StaticData',
    inheritAttrs: false,
  });
  const { rowSelection, selectedRows, clearSelect } = useRowSelection({
    hideSelectAll: false,
    getCheckboxProps: () => {
      return {
        disabled: false,
      };
    },
  });
  const getConfigPage = async (params: any) => {
    const { menuIdentify }: any = params;
    if (isEmpty(menuIdentify)) {
      return [];
    }
    return await postStaticDataConfigPage({
      ...params,
    });
  };

  const addEditModalOpen = ref<boolean>(false);
  const operationStatus = ref<OperationStatusMap>(OperationStatusMap.ADD);

  const { columnsFirst, pageRef, treeData, deleteConfig, firstRowData, updateTableData } = useTable({
    addEditModalOpen,
    operationStatus,
    clearSelect,
  });

  const openAddEditModal = (status: OperationStatusMap, treeNode: Recordable) => {
    if (!treeNode?.menuName) {
      return message.error(t('请选择菜单'));
    }
    addEditModalOpen.value = true;
    operationStatus.value = status;
  };

  const hasTableSortChange = ref<boolean>(false);
  const tableSortChange = () => {
    hasTableSortChange.value = true;
  };
  const { setDict } = useDict();
  const editSort = async (treeNode: any) => {
    try {
      const tableData = pageRef.value?.getTableRef()?.getTableData();
      await postStaticDataConfigSortEdit({
        editDTOList: tableData.map((item: any, index: number) => {
          return {
            id: item.id,
            sort: index + 1,
          };
        }),
        staticDataType: treeNode.menuName,
      });
      updateTableData();
      setDict(treeNode.menuName);
      hasTableSortChange.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
</script>

<style lang="less" scoped>
  :deep(.lisms-tree .lisms-tree-treenode-disabled .lisms-tree-node-content-wrapper) {
    color: var(--bmos-third-level-text-color);
  }
</style>
