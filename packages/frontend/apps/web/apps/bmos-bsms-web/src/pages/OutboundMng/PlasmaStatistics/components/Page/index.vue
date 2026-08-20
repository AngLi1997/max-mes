<!-- 出库血浆统计 -- 列表 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['inWarehouseBatchNo']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :showToolBars="[true]"
    :rowSelections="rowSelections"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :expandedRowsChanges="[expandChange]"
    :requests="[getLists as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <div class="table-header">
        <Button
          v-hasAuth="170100012000002"
          :disabled="rowSelections[0]?.selectedRowKeys?.length === 0"
          :loading="exportLoading"
          type="primary"
          @click="exportFile">
          {{ t('导出') }}
        </Button>
      </div>
    </template>
    <template #expandColumnTitle0>{{}}</template>
    <template #expandedRowRender0="{ record, instance }">
      <BMPageComponent
        :ref="el => setExpandRef(record.inWarehouseBatchNo, el)"
        :rowKeys="['id']"
        :search="[false]"
        :hideRightTree="true"
        :tableFields="[
          {
            default: {
              ...instance.getQueryFormRef()?.handleFormValues(instance.getQueryFormRef().formModel),
              inWarehouseBatchNo: record.inWarehouseBatchNo,
            },
          },
        ]"
        :isExtraParamsChangeQuerys="[false]"
        :paginations="[paginationBig]"
        :showHeader="[false]"
        :showToolBars="[false]"
        :requests="[getOutboundStatisticsOutboundPage as DataRequestFn]"
        :columns="[expandMap[record.inWarehouseBatchNo].columnsFirst]"></BMPageComponent>
    </template>
  </BMPageComponent>
</template>

<script setup lang="ts">
  import { getOutboundStatisticsPage, getOutboundStatisticsOutboundPage, exportOutboundStatistics } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { fileStreamDownload } from '@bmos/utils';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { message } from 'ant-design-vue';

  defineOptions({
    name: 'PlasmaStatistics',
  });

  const router = useRouter();

  const enterView = (row: any) => {
    router.push({
      name: 'DeliveryPlanViewCom',
      query: { rowData: JSON.stringify(row) },
    });
  };

  const { pageRef, columnsFirst, formFirstProps, expandMap, expandedRowKeys, expandChange } = useTable(enterView);

  // 查询操作
  const getLists = async (params: any) => {
    const datas = {
      ...params,
    };

    const res = await getOutboundStatisticsPage(datas);

    const keys = res?.data?.list?.map((item: any) => item.inWarehouseBatchNo) || [];

    // 查询二级列表（如果展开了的话）
    expandedRowKeys.value?.forEach((key: any) => {
      if (keys.includes(key)) {
        expandMap[key].fetchData();
      }
    });
    return res;
  };

  // 选中的数据
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
      getCheckboxProps: (_record: any) => {
        return {
          disabled: false,
        };
      },
      onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
        if (rowSelections[0]?.selectedRowKeys) {
          rowSelections[0].selectedRowKeys = selectedRowKeys?.length
            ? [selectedRowKeys[selectedRowKeys.length - 1]]
            : [];
          operationSelectedRow.value = selectedRows[selectedRows.length - 1];
        }
      },
    },
    null,
  ]);

  // 设置二级列表的ref
  const setExpandRef = (key: any, ref: any) => {
    expandMap[key].setRef(ref);
  };

  // 导出相关
  const exportLoading = ref(false);

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

  const exportFile = async () => {
    exportLoading.value = true;
    const res = await exportOutboundStatistics({
      inWarehouseBatchNo: operationSelectedRow.value?.inWarehouseBatchNo,
    });
    let fileName = res.headers['content-disposition']?.split("filename*=utf-8''")[1];
    // 文件名解码
    fileName && fileName.indexOf('%') > -1 && (fileName = decodeURI(fileName));
    downloadFn(res.data, fileName);
    exportLoading.value = false;
  };
</script>

<style lang="less" scoped>
  .table-header {
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }
</style>
