<template>
  <div class="expression-config-table">
    <BMTable
      ref="tableInstance"
      :data-request="loadData"
      :columns="columns"
      row-key="id"
      :auto-height="true"
      :autoHeightOffset="24"
      :form-props="formProps"
      :scroll="{ x: 1380 }"
      :pagination="{
        pageSize: 20,
      }"
      :extraParams="extraParams">
      <template #toolbar>
        <Button type="primary" @click="handleAdd">
          {{ t('新建公式') }}
        </Button>
      </template>
    </BMTable>
  </div>
  <AddExpressionModal
    v-model:open="addExpressionModalOpen"
    :treeData="treeData"
    :selectCategory="selectCategory"
    :rowData="rowData"
    :status="modalStatus"
    @updateTable="updateTable" />
</template>

<script lang="ts" setup>
  import type { DataRequestFn, Recordable } from '@bmos/components';
  import { BMTable } from '@bmos/components';
  import { useTable } from './hooks/useTable';
  import { reqPageUsingGET } from '@/api';
  import { DataNode } from 'ant-design-vue/es/tree';
  import AddExpressionModal from '../AddExpressionModal.vue';
  import { MODAL_STATUS, ALL_TYPE } from '../../types';
  import { t } from '@bmos/i18n';

  const props = withDefaults(
    defineProps<{
      treeData: DataNode[];
      selectCategory: string;
    }>(),
    {},
  );

  watch(
    () => props.selectCategory,
    (val: string) => {
      if (val === ALL_TYPE.ALL) {
        extraParams.value = {};
        return;
      }
      tableInstance.value?.getQueryFormRef()?.resetForm();
      extraParams.value = {
        expressionCategoryId: props.selectCategory,
      };
    },
  );

  const extraParams = ref<Recordable>({});

  const loadData: DataRequestFn = async (params): Promise<any> => {
    return reqPageUsingGET(params as API.PageUsingGET);
  };

  // 新建公式
  const addExpressionModalOpen = ref<boolean>(false);
  const { tableInstance, columns, formProps, rowData, modalStatus } = useTable({
    addExpressionModalOpen,
  });

  // 更新表格
  const updateTable = () => {
    tableInstance.value?.fetchData();
  };

  const handleAdd = () => {
    modalStatus.value = MODAL_STATUS.ADD;
    addExpressionModalOpen.value = true;
  };
</script>

<style lang="less" scoped>
  .expression-config-table {
    padding: var(--bmos-padding-small);
    background-color: var(--bmos-primary-color-white);
    height: 100%;
    .bmos-table {
      height: 100%;
    }
  }
</style>
