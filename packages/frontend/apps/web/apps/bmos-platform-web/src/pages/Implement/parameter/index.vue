<template>
  <div class="parameter-config-table">
    <BMTable
      ref="tableInstance"
      :data-request="loadData"
      :columns="columns"
      row-key="id"
      :auto-height="true"
      :autoHeightOffset="24"
      :form-props="formProps"
      :show-refresh="false"
      :scroll="{ x: 1380 }"
      :extraParams="extraParams">
      <template #toolbar>
        <Button type="primary" @click="handleRefresh">{{ t('刷新') }}</Button>
      </template>
    </BMTable>
  </div>
  <ParameterModal
    v-model:open="editParameterModalOpen"
    :rowData="rowData"
    :businessTypeOptions="businessTypeOptions"
    :valueTypeOptions="valueTypeOptions"
    :belongOptions="belongOptions"
    @updateTable="updateTable" />
</template>

<script lang="ts" setup>
  import type { DataRequestFn, Recordable } from '@bmos/components';
  import { BMTable } from '@bmos/components';
  import { useTable } from './hooks/useTable';
  import { reqBusinessParameterGET, reqBusinessParameterRefreshPUT } from '@/api';
  import { message } from 'ant-design-vue';
  import ParameterModal from './components/ParameterModal.vue';
  import { t } from '@bmos/i18n';

  const extraParams = ref<Recordable>({});

  const loadData: DataRequestFn = async (params): Promise<any> => {
    return reqBusinessParameterGET(params as API.PageUsingGET);
  };

  // 新建公式
  const editParameterModalOpen = ref<boolean>(false);
  const { tableInstance, columns, formProps, rowData, businessTypeOptions, valueTypeOptions, belongOptions } = useTable(
    {
      editParameterModalOpen,
    },
  );

  const handleRefresh = async () => {
    try {
      await reqBusinessParameterRefreshPUT();
      tableInstance.value?.reload();
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const updateTable = () => {
    tableInstance.value?.fetchData();
  };
</script>

<style lang="less" scoped>
  .parameter-config-table {
    padding: var(--bmos-padding-small);
    background-color: var(--bmos-primary-color-white);
    height: 100%;
    .bmos-table {
      height: 100%;
    }
  }
</style>
