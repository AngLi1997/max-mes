<!-- 血浆销毁出库 -- 列表 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :showToolBars="[true]"
    :titles="[t('血浆销毁出库')]"
    :rowSelections="rowSelections"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :tableFields="[
      {
        default: { type: 4 },
      },
    ]"
    :expandedRowsChanges="[expandChange]"
    :requests="[getLists as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderToolbar0>
      <div class="table-header">
        <Button
          v-hasAuth="170100007000005"
          :disabled="rowSelections[0]?.selectedRowKeys.length === 0"
          style="margin-right: 8px"
          :loading="loading"
          @click="printFile">
          {{ t('打印出库单') }}
        </Button>
        <Button
          v-hasAuth="170100007000006"
          :disabled="rowSelections[0]?.selectedRowKeys.length === 0"
          style="margin-right: 8px"
          :loading="loading2"
          @click="printFile2">
          {{ t('打印出库单附页') }}
        </Button>
        <!-- <Button type="primary" :disabled="rowSelections[0].selectedRowKeys.length === 0">
          {{ t('导出') }}
        </Button> -->
      </div>
    </template>
    <template #expandColumnTitle0>{{}}</template>
    <template #expandedRowRender0="{ record, instance }">
      <BMPageComponent
        :ref="el => setExpandRef(record.id, el)"
        :rowKeys="['id']"
        :search="[false]"
        :hideRightTree="true"
        :tableFields="[
          {
            default: {
              ...instance.getQueryFormRef()?.handleFormValues(instance.getQueryFormRef().formModel),
              batchNo: record.batchNo,
            },
          },
        ]"
        :isExtraParamsChangeQuerys="[false]"
        :paginations="[paginationBig]"
        :showHeader="[false]"
        :showToolBars="[false]"
        :requests="[getOutboundSortingPage as DataRequestFn]"
        :columns="[expandMap[record.id].columnsFirst]"></BMPageComponent>
    </template>
  </BMPageComponent>
  <!-- 数量明细侧边框 -->
  <Cnt ref="cntRef" />
  <!-- 出库确认 -->
  <Check
    ref="checkRef"
    @submitSuccess="
      () => {
        if (rowSelections[0]?.selectedRowKeys) {
          rowSelections[0].selectedRowKeys = [];
          operationSelectedRow = {};
        }
        pageRef?.fetchData();
      }
    " />
</template>

<script setup lang="ts">
  import { getOutboundPage, getOutboundSortingPage, printDeliveryPlan, printDeliveryPlanDetail } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { Cnt, Check } from '../index';
  import { t } from '@bmos/i18n';
  import { fileStreamDownload } from '@bmos/utils';
  import { message } from 'ant-design-vue';

  defineOptions({
    name: 'PlasmaFeedOutOfDestruction',
  });

  // 查看数量明细
  const cntRef = ref();

  const openCnt = (row: any) => {
    cntRef.value?.showDrawer(row);
  };

  // 出库确认
  const checkRef = ref();
  const openCheck = (row: any, type: 'batchNo' | 'trayNo') => {
    checkRef.value?.openModal({ ...row, typeValue: 4 }, type);
  };

  const router = useRouter();

  const enterView = (row: any) => {
    router.push({
      name: 'DestroyWarehouseAuditViewCom',
      query: { rowData: JSON.stringify(row) },
    });
  };

  const { pageRef, columnsFirst, formFirstProps, expandMap, expandedRowKeys, expandChange } = useTable(
    openCnt,
    openCheck,
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
          rowSelections[0].selectedRowKeys = selectedRowKeys?.length
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
    const res = await getOutboundPage(datas);

    const keys = res?.data?.list?.map((item: any) => item.id) || [];

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
  // 打印出库单
  const loading = ref(false);
  const printFile = async () => {
    try {
      loading.value = true;
      const res = await printDeliveryPlan(operationSelectedRow.value?.batchNo);
      let fileName = res.headers['content-disposition']?.split("filename*=utf-8''")[1];
      // 文件名解码
      fileName && fileName.indexOf('%') > -1 && (fileName = decodeURI(fileName));
      downloadFn(res.data, fileName);
    } finally {
      loading.value = false;
    }
  };
  // 打印出库单附页
  const loading2 = ref(false);
  const printFile2 = async () => {
    try {
      loading2.value = true;
      const res = await printDeliveryPlanDetail(operationSelectedRow.value?.batchNo);
      let fileName = res.headers['content-disposition']?.split("filename*=utf-8''")[1];
      // 文件名解码
      fileName && fileName.indexOf('%') > -1 && (fileName = decodeURI(fileName));
      downloadFn(res.data, fileName);
    } finally {
      loading2.value = false;
    }
  };
</script>

<style lang="less" scoped></style>
