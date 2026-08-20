<!-- 标本出库 -- 列表 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['outPlanBatchNo']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :showToolBars="[true]"
    :tableFields="[
      {
        default: {
          pageFlag: 1,
        },
      },
    ]"
    :titles="[t('标本出库')]"
    :rowSelections="rowSelections"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :expandedRowsChanges="[expandChange]"
    :requests="[getLists as DataRequestFn]"
    :columns="[columnsFirst]">
    <!-- <template #tableHeaderTitle0>
      <div class="table-header">
        <Button
          type="primary"
          :disabled="rowSelections[0]?.selectedRowKeys.length === 0"
          style="margin-right: 8px"
          @click="enterView(operationSelectedRow)">
          {{ t('查看详情') }}
        </Button>
      </div>
    </template> -->
    <template #tableHeaderToolbar0>
      <Button
        v-hasAuth="170020011000002"
        type="primary"
        :disabled="rowSelections[0]?.selectedRowKeys.length === 0"
        style="margin-right: 8px"
        :loading="loading"
        @click="exportExcel(operationSelectedRow)">
        {{ t('导出') }}
      </Button>
    </template>
    <template #expandColumnTitle0>{{}}</template>
    <template #expandedRowRender0="{ record, instance }">
      <BMPageComponent
        :ref="el => setExpandRef(record.outPlanBatchNo, el)"
        :rowKeys="['id']"
        :search="[false]"
        :hideRightTree="true"
        :tableFields="[
          {
            default: {
              ...instance.getQueryFormRef()?.handleFormValues(instance.getQueryFormRef().formModel),
              outPlanBatchNo: record.outPlanBatchNo,
            },
          },
        ]"
        :isExtraParamsChangeQuerys="[false]"
        :paginations="[paginationBig]"
        :showHeader="[false]"
        :showToolBars="[false]"
        :requests="[getSampleOutWarehouseByBatchNo as DataRequestFn]"
        :columns="[expandMap[record.outPlanBatchNo].columnsFirst]"></BMPageComponent>
    </template>
  </BMPageComponent>
  <CntModal ref="cntModalRef" />
</template>

<script setup lang="ts">
  import { getSampleOutWarehouseList, getSampleOutWarehouseByBatchNo, sampleOutWarehouseExport } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { CntModal } from '../index';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { fileStreamDownload } from '@bmos/utils';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'SpecimenRelease',
    inheritAttrs: false,
  });

  const router = useRouter();

  const enterView = (row: any) => {
    router.push({
      name: 'SpecimenDeliveryPlanViewCom',
      params: {
        outPlanBatchNo: row.outPlanBatchNo,
      },
    });
  };

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
      const res = await sampleOutWarehouseExport({ outPlanBatchNo: data.outPlanBatchNo });
      let fileName = res.headers['content-disposition']?.split("filename*=utf-8''")[1];
      // 文件名解码
      fileName && fileName.indexOf('%') > -1 && (fileName = decodeURI(fileName));
      downloadFn(res.data, fileName);
    } finally {
      loading.value = false;
    }
  };

  // 数量查看
  const cntModalRef = ref();

  const openCnt = (data: any) => {
    cntModalRef.value?.openModal(data);
  };

  const { pageRef, columnsFirst, formFirstProps, expandMap, expandedRowKeys, expandChange } = useTable(
    openCnt,
    enterView,
  );

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
          rowSelections[0].selectedRowKeys = selectedRowKeys.length
            ? [selectedRowKeys[selectedRowKeys.length - 1]]
            : [];
          operationSelectedRow.value = selectedRows[selectedRows.length - 1];
        }
      },
    },
    null,
  ]);

  // 查询操作
  const getLists = async (params: any) => {
    const datas = {
      ...params,
    };
    const res = await getSampleOutWarehouseList(datas);

    const keys = res?.data?.list?.map((item: any) => item.outPlanBatchNo) || [];

    // 查询二级列表（如果展开了的话）
    expandedRowKeys.value?.forEach((key: any) => {
      if (keys.includes(key)) {
        expandMap[key].fetchData();
      }
    });
    return res;
  };

  // 设置二级列表的ref
  const setExpandRef = (key: any, ref: any) => {
    expandMap[key].setRef(ref);
  };
</script>

<style lang="less" scoped></style>
