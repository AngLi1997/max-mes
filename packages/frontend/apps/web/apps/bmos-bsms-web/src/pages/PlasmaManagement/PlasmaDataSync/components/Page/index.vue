<!-- 血浆数据同步 --列表 -->
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
    :requests="[loadData as DataRequestFn]"
    :paginations="[paginationFirst]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <div class="table-header">
        <Button
          v-hasAuth="170040001000003"
          :disabled="disReceive(operationSelectedRows)"
          type="primary"
          style="margin-right: 8px"
          @click="openReceive(operationSelectedRows, 'receive')">
          {{ t('确认接收') }}
        </Button>
        <Button
          v-hasAuth="170040001000004"
          :disabled="disCancelSync(operationSelectedRows)"
          @click="cancelSync(operationSelectedRows)">
          {{ t('撤销同步') }}
        </Button>
      </div>
    </template>
    <template #tableHeaderToolbar0>
      <div class="table-header">
        <Button
          v-hasAuth="170040001000002"
          type="primary"
          :disabled="operationSelectedRows.length !== 1"
          style="margin-right: 8px"
          :loading="loading"
          @click="exportExcel(operationSelectedRows[0])">
          {{ t('导出') }}
        </Button>
        <Button v-hasAuth="170040001000001" @click="enterImport">
          {{ t('导入') }}
        </Button>
      </div>
    </template>
  </BMPageComponent>
  <Receive ref="receiveRef" @submitSuccess="submitSuccess" />
  <DetailCount ref="detailCountRef" />
</template>

<script setup lang="ts">
  import { getPlasmaDataSyncList, plasmaDataSyncCancel, plasmaDataSyncExport } from '@/services';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { Receive, DetailCount } from '../index';
  import { t } from '@bmos/i18n';
  import { fileStreamDownload } from '@bmos/utils';
  import { Modal, message } from 'ant-design-vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

  defineOptions({
    name: 'PlasmaDataSyncPage',
    inheritAttrs: false,
  });

  const downloadFn = (data: any, fileName: string) => {
    try {
      const uint8Array = new Uint8Array(data);
      const decoder = new TextDecoder();
      const jsonString = decoder.decode(uint8Array);
      const error = JSON.parse(jsonString);
      error.message && message.error(error.message);
    } catch (error) {
      fileStreamDownload(data, fileName);
    }
  };

  const loading = ref(false);

  const exportExcel = async (data: any) => {
    try {
      loading.value = true;
      const res = await plasmaDataSyncExport({ syncHistoryId: data.id });
      let fileName = res.headers['content-disposition']?.split("filename*=utf-8''")[1];
      // 文件名解码
      fileName && fileName.indexOf('%') > -1 && (fileName = decodeURI(fileName));
      downloadFn(res.data, fileName);
    } finally {
      loading.value = false;
    }
  };

  const loadData: DataRequestFn = async (params: any): Promise<any> => {
    const datas = { ...params };
    return getPlasmaDataSyncList(datas);
  };

  const receiveRef = ref();

  // 导入相关
  const emit = defineEmits(['enterImport']);

  const enterImport = () => {
    emit('enterImport');
  };

  // 能否接收
  const disReceive = (list: any) => {
    return list?.length === 0 || list?.some((item: any) => item?.receiveStatus?.value != 0);
  };
  // 能否撤销同步
  const disCancelSync = (list: any) => {
    return (
      list?.length === 0 || list?.some((item: any) => !(item?.syncType?.value == 2 && item?.receiveStatus?.value == 1))
    );
  };

  // 确认接收
  const openReceive = (rows: any, type: 'receive' | 'edit') => {
    receiveRef.value?.openModal(JSON.parse(JSON.stringify(rows)), type);
  };

  // 撤销同步
  const cancelSync = async (rows: any) => {
    Modal.confirm({
      title: t('确定要撤销同步吗'),
      icon: h(ExclamationCircleOutlined),
      async onOk() {
        try {
          await plasmaDataSyncCancel(rows.map((item: { id: any }) => item.id));
          message.success(t('操作成功'));
          pageRef.value?.fetchData();
        } catch (error: any) {
          console.log(error);
          error.message && message.error(error.message);
          return Promise.reject();
        }
      },
      onCancel() {},
    });
  };

  const detailCountRef = ref();

  // 查看成功、失败数据
  const openCount = (row: any, type: any) => {
    detailCountRef.value?.openModal(row, type);
  };

  const { pageRef, columnsFirst, formFirstProps, paginationFirst } = useTable(openReceive, cancelSync, openCount);

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
    if (rowSelections[0]?.selectedRowKeys) {
      rowSelections[0].selectedRowKeys = [];
    }
    operationSelectedRows.value = [];
    pageRef.value?.fetchData();
  };
</script>

<style lang="less" scoped>
  .table-header {
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }
</style>
