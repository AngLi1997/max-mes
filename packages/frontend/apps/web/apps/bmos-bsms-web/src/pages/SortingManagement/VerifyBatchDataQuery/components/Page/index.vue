<!-- 核查批次数据查询 -- 列表 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['checkNo']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :showToolBars="[false]"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :expandedRowsChanges="[expandChange]"
    :requests="[getLists as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #expandColumnTitle0>{{}}</template>
    <template #expandedRowRender0="{ record, instance }">
      <BMPageComponent
        :ref="el => setExpandRef(record.checkNo, el)"
        :rowKeys="['planBatchNo']"
        :search="[false]"
        :hideRightTree="true"
        :tableFields="[
          {
            default: {
              ...instance.getQueryFormRef()?.handleFormValues(instance.getQueryFormRef().formModel),
              checkNo: record.checkNo,
            },
          },
        ]"
        :isExtraParamsChangeQuerys="[false]"
        :paginations="[paginationBig]"
        :showHeader="[false]"
        :showToolBars="[false]"
        :requests="[getSortingCheckPlanList as DataRequestFn]"
        :columns="[expandMap[record.checkNo].columnsFirst]"></BMPageComponent>
    </template>
  </BMPageComponent>
  <!-- 数量详情 -->
  <Cnt ref="cntRef" />
</template>

<script setup lang="ts">
  import { getSortingCheckList, getSortingCheckPlanList } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { Cnt } from '../index';

  defineOptions({
    name: 'VerifyBatchDataQuery',
    inheritAttrs: false,
  });

  // 数量详情
  const cntRef = ref();

  const openCnt = (data: any) => {
    cntRef.value?.showDrawer(data);
  };

  const router = useRouter();

  // ----------------------详情-----------------------
  const enterDetail = (data: any) => {
    router.push({
      name: 'SortingPlanViewCom',
      query: {
        planBatchNo: data.planBatchNo,
        itemType: data.itemType,
      },
    });
  };

  const { pageRef, columnsFirst, formFirstProps, expandMap, expandedRowKeys, expandChange } = useTable(
    enterDetail,
    openCnt,
  );

  // 查询操作
  const getLists = async (params: any) => {
    const datas = {
      ...params,
    };
    const res = await getSortingCheckList(datas);

    const keys = res?.data?.list?.map((item: any) => item.checkNo) || [];

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
