<template>
  <BMTable
    ref="tableInstance"
    :data-request="loadData"
    :columns="columns"
    row-key="id"
    headerTitle="测试表格"
    :scroll="{ x: 1144, y: 400 }"
    :showRefresh="true"
    :formProps="formProps"
    :isSelect="true"
    :rowSelection="{
      selectedRowKeys: [2],
    }"
    :extraParams="{
      aaaa: '123',
    }"
    :show-tool-bar="true"
    @change="handleTableChange"
    @handleClickRow="handleClickRow"
    showSearchBorder>
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'name'">
        {{ record.name }}
        <a>[测试]</a>
      </template>
    </template>
    <!-- <template #expandedRowRender="{ record }">
      <p>
        {{ record.date }}
      </p>
    </template> -->
    <template #toolbar="{ reload, fetchData, setProps }">
      <a-button type="primary">新增</a-button>
      <a-button>导入</a-button>
      <a-button @click="reload">刷新</a-button>
    </template>
  </BMTable>
  <Modal v-model:open="open" title="Basic Modal" @ok="handleOk">
    <p>Some contents...</p>
    <p>Some contents...</p>
    <p>Some contents...</p>
  </Modal>
</template>

<script lang="ts" setup>
  import type {
    DataRequestFn,
    FormProps,
    TableInstance,
  } from '@bmos/components';
  import { BMTable } from '@bmos/components';
  import { GetRowKey } from 'ant-design-vue/es/table/interface';
  import { reactive, ref } from 'vue';
  import { Modal } from 'ant-design-vue';
  import { useTable } from './hooks/useTable';

  const open = ref<boolean>(false);

  const { columns, tableData } = useTable({ open });

  const handleOk = () => {
    open.value = false;
  };
  const tableInstance = ref<TableInstance>();

  const formProps = reactive<Partial<FormProps>>({
    actionColOptions: {
      // span: 6,
    },
    baseColProps: {
      span: 8,
    },
    showAdvancedButton: false,
    showAdvancedButtonBadge: true,
    advancedBadgeCount: 2,
  });

  const loadData: DataRequestFn = async (
    params,
    onChangeParams,
  ): Promise<any> => {
    console.log('params', params);
    console.log('onChangeParams', onChangeParams);

    return new Promise(resolve => {
      setTimeout(() => {
        resolve({
          data: {
            list: tableData,
            total: 30,
            pageNum: 1,
          },
          ...params,
        });
        // tableInstance.value?.updatePagination?.({
        //   total: 30,
        // });
        // 手动设置搜索表单的搜索项
        tableInstance.value?.getQueryFormRef()?.updateSchema?.([
          {
            field: 'price',
            componentProps: {
              options: [
                {
                  label: '0-199',
                  value: '0-199',
                },
                {
                  label: '200-999',
                  value: '200-999',
                },
              ],
            },
          },
        ]);
      }, 500);
    });
  };
  const handleTableChange = (params: any) => {
    console.log('params', params);
  };
  const handleClickRow = (
    record: any,
    key: string | GetRowKey<any>,
    selectedRowKeys: (string | GetRowKey<any>)[],
  ) => {
    console.log('record', record, key, selectedRowKeys);
  };
</script>
