<!-- 蛋白质含量 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :rowSelections="rowSelections"
    :showHeader="[false]"
    :showToolBars="[true]"
    :formProps="[formFirstProps]"
    :rowClassNames="[rowClassName]"
    :requests="[getProteinContentList as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <div class="table-header">
        <Button
          v-hasAuth="180020001000001"
          :disabled="disPublish()"
          type="primary"
          style="margin-right: 8px"
          @click="openReceive(operationSelectedRows, 'publish')">
          {{ t('发布') }}
        </Button>
        <Button v-hasAuth="180020001000002" type="primary" style="margin-right: 8px" @click="openBatch('publish')">
          {{ t('批量发布') }}
        </Button>
        <Button
          v-hasAuth="180020001000003"
          :disabled="disCheck()"
          style="margin-right: 8px"
          @click="openReceive(operationSelectedRows, 'check')">
          {{ t('核对') }}
        </Button>
        <Button v-hasAuth="180020001000004" type="primary" @click="openBatch('check')">
          {{ t('批量核对') }}
        </Button>
      </div>
    </template>
    <template #tableHeaderToolbar0>
      <Button v-hasAuth="180020001000005" @click="enterImport">
        {{ t('读取') }}
      </Button>
    </template>
  </BMPageComponent>
  <BatchModal ref="batchRef" @submitSuccess="nextModal" />
  <Receive
    ref="receiveRef"
    @submitSuccess="
      () => {
        if (rowSelections[0]?.selectedRowKeys) {
          rowSelections[0].selectedRowKeys = [];
        }
        operationSelectedRows = [];
        pageRef?.fetchData();
      }
    " />
  <!-- <ImportModal ref="importRef" /> -->
</template>

<script setup lang="ts">
  // import ImportModal from './components/ImportModal/index.vue';
  import { getProteinContentList } from '@/services';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { BatchModal, Receive } from '../index';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'ProteinContent',
  });

  const router = useRouter();

  const enterImport = () => {
    router.push({
      name: 'ImportExcelComponent',
      params: {
        templateFile: t('蛋白质含量上传'),
      },
    });
  };

  // 是否禁止发布
  const disPublish = () => {
    return (
      operationSelectedRows.value?.length === 0 ||
      operationSelectedRows.value?.some((item: any) => item?.publishStatus?.value != 1)
    );
  };

  // 是否禁止核对
  const disCheck = () => {
    return (
      operationSelectedRows.value?.length === 0 ||
      operationSelectedRows.value?.some((item: any) => item?.publishStatus?.value != 2)
    );
  };

  const receiveRef = ref();
  // 发布/核对
  const openReceive = (rows: any, type: 'publish' | 'check') => {
    receiveRef.value?.openModal(JSON.parse(JSON.stringify(rows)), type);
  };

  // 批量发布/核对
  const batchRef = ref();
  const openBatch = (type: 'publish' | 'check') => {
    batchRef.value?.openModal(type);
  };

  const nextModal = (dataList: any, type: 'publish' | 'check') => {
    openReceive(dataList, type);
  };

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

  const { pageRef, columnsFirst, formFirstProps } = useTable();

  const rowClassName = (record: any) => {
    return record?.checkResult?.value == 0 ? 'danger' : '';
  };
</script>

<style lang="less" scoped>
  .table-header {
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }
  :deep(.danger) {
    color: red;
  }
</style>
