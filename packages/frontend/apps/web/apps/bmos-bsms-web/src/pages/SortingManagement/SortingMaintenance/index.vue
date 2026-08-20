<!-- 分拣维护 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['itemOrgNo']"
    :hideRightTree="true"
    :showAllAddIcon="false"
    :showAction="false"
    :showHeader="[false]"
    :showToolBars="[true]"
    :rowSelections="rowSelections"
    :formProps="[formFirstProps]"
    :paginations="[paginationFirst]"
    :requests="[getLists as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <div>
        <Button
          v-hasAuth="170080008000002"
          :disabled="rowSelections[0]?.selectedRowKeys.length === 0"
          type="primary"
          @click="back">
          {{ t('撤销') }}
        </Button>
        <Button v-hasAuth="170080008000001" style="margin-left: 8px" @click="printBox">
          {{ t('打印箱号') }}
        </Button>
      </div>
    </template>
    <template #tableHeaderToolbar0>
      <Segmented v-model:value="tableType" :options="typeOpts" @change="changeType"></Segmented>
    </template>
  </BMPageComponent>
  <PrintBoxModal ref="printBoxModalRef" />
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { BMPageComponent, DataRequestFn } from '@bmos/components';
  import PrintBoxModal from '@/components/PrintBoxModal/index.vue';
  import { useTable } from './hooks/useTable';
  import {
    getSortingMaintainPlasmaList,
    getSortingMaintainSampleList,
    sortingMaintainPlasmaRevocation,
    sortingMaintainSampleRevocation,
  } from '@/services';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { Modal, message } from 'ant-design-vue';

  defineOptions({
    name: 'SortingMaintenance',
    inheritAttrs: false,
  });

  const { pageRef, columnsFirst, formFirstProps, paginationFirst, getSortingMaintainPersonList } = useTable();

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

  const printBoxModalRef = ref<any>();

  const printBox = () => {
    printBoxModalRef.value?.openModal(tableType.value);
  };

  // 选项变化时重新获取数据
  const changeType = async (_val: any) => {
    if (rowSelections[0]?.selectedRowKeys) {
      rowSelections[0].selectedRowKeys = [];
      operationSelectedRow.value = [];
    }
    await getSortingMaintainPersonList(tableType.value);
    await pageRef.value?.fetchData();
  };

  const getLists = async (params: any) => {
    const datas = {
      ...params,

      manageType: tableType.value,
    };
    if (tableType.value === 1) {
      return await getSortingMaintainPlasmaList(datas);
    } else {
      return await getSortingMaintainSampleList(datas);
    }
  };

  // 选中的数据
  const operationSelectedRow = ref<any>({});

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
          operationSelectedRow.value = selectedRows;
        }
      },
    },
    null,
  ]);

  // 撤销
  const back = () => {
    Modal.confirm({
      title: t('是否进行撤销操作?'),
      icon: h(ExclamationCircleOutlined),
      async onOk() {
        try {
          if (tableType.value === 1) {
            await sortingMaintainPlasmaRevocation(rowSelections[0]?.selectedRowKeys);
          } else {
            await sortingMaintainSampleRevocation(rowSelections[0]?.selectedRowKeys);
          }

          message.success(t('操作成功'));
          await changeType(tableType.value);
        } catch (error: any) {
          error.message && message.error(error.message);
          return Promise.reject();
        }
      },
      onCancel() {},
    });
  };

  onMounted(async () => {
    await getSortingMaintainPersonList(tableType.value);
  });
</script>

<style lang="less" scoped>
  :deep(.bsms-segmented) {
    .bsms-segmented-item {
      padding: 0 8px;
      flex-grow: 1;
    }
  }
</style>
