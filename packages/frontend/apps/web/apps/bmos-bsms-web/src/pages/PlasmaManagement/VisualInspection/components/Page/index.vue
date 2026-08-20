<!-- 血浆外观检验 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['inWarehouseBatchNo']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :showToolBars="[true]"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :tableFields="[
      {
        default: { appearanceDetection: tableType },
      },
    ]"
    :expandedRowsChanges="[expandChange]"
    :requests="[getLists as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <Segmented
        v-model:value="tableType"
        :options="typeOpts"
        @change="
          val => {
            changeType(val);
          }
        "></Segmented>
    </template>
    <template #tableHeaderToolbar0>
      <Button
        v-hasAuth="170040006000001"
        type="primary"
        :disabled="rowSelectionAll?.length === 0"
        style="margin-right: 8px"
        @click="openInspection">
        {{ t('外观检验') }}
      </Button>
    </template>
    <template #expandColumnTitle0>{{}}</template>
    <template #expandedRowRender0="{ instance, record }">
      <BMPageComponent
        :ref="el => setExpandRef(record.inWarehouseBatchNo, el)"
        :rowKeys="['plasmaOrgNo']"
        :search="[false]"
        :hideRightTree="true"
        :tableFields="[
          {
            default: {
              ...instance.getQueryFormRef()?.handleFormValues(instance.getQueryFormRef().formModel),
              appearanceDetection: tableType,
              inWarehouseBatchNo: record.inWarehouseBatchNo,
            },
          },
        ]"
        :isExtraParamsChangeQuerys="[false]"
        :paginations="[paginationBig]"
        :rowSelections="expandMap[record.inWarehouseBatchNo].rowSelections"
        :showHeader="[false]"
        :showToolBars="[false]"
        :requests="[getAppearanceDetailList as DataRequestFn]"
        :columns="[expandMap[record.inWarehouseBatchNo].columnsFirst]"></BMPageComponent>
    </template>
  </BMPageComponent>
  <Inspection ref="inspectionRef" @submitSuccess="submitSuccess" />
</template>

<script setup lang="ts">
  import { getAppearanceList, getAppearanceDetailList } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { Inspection } from '../index';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'VisualInspection',
  });

  const tableType = ref(0);

  const typeOpts = [
    {
      label: t('未检测'),
      value: 0,
    },
    {
      label: t('已检测'),
      value: 1,
    },
  ];

  // 外观检验
  const inspectionRef = ref<any>();

  const openInspection = () => {
    inspectionRef.value.openModal(rowSelectionAll.value);
  };

  const changeType = (val: any) => {
    // tableType.value = val;
    columnsFirst[columnsFirst.length - 1].hideInTable = val === 1;
  };

  const router = useRouter();

  const enterView = (row: any) => {
    router.push({
      name: 'VisualInspectionViewCom',
      params: {
        plasmaOrgNo: row?.plasmaOrgNo,
      },
    });
  };

  const { pageRef, columnsFirst, formFirstProps, expandMap, expandedRowKeys, expandChange } = useTable(enterView);

  // 查询操作
  const getLists = async (params: any) => {
    const datas = {
      ...params,
    };
    const res = await getAppearanceList(datas);

    const keys = res?.data?.list?.map((item: any) => item.inWarehouseBatchNo) || [];

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

  // 所有二级列表的rowSelection
  const rowSelectionAll = computed(() => {
    const ans = [] as any;
    for (const key in expandMap) {
      if (expandMap[key].operationSelectedRows?.length > 0) {
        expandMap[key].operationSelectedRows.forEach((item: any) => {
          ans.push(item);
        });
      }
    }
    return ans;
  });

  const submitSuccess = async () => {
    for (const key in expandMap) {
      expandMap[key].rowSelections[0].selectedRowKeys = [];
      expandMap[key].operationSelectedRows = [];
    }
    await pageRef.value?.fetchData();
  };
</script>

<style lang="less" scoped>
  :deep(.bsms-segmented) {
    .bsms-segmented-item {
      padding: 0 8px;
      flex-grow: 1;
    }
  }
</style>
