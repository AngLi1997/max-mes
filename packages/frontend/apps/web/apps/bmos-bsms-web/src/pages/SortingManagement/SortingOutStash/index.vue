<!-- 分拣出库 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['checkNo']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :showToolBars="[true]"
    :titles="[t('分拣出库')]"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :expandedRowsChanges="[expandChange]"
    :requests="[getLists as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #expandColumnTitle0>{{}}</template>
    <template #expandedRowRender0="{ record, instance }">
      <BMPageComponent
        :ref="el => setExpandRef(record.checkNo, el)"
        :rowKeys="['id']"
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
        :requests="[getSortingOutWarehouseDetailList as DataRequestFn]"
        :columns="[expandMap[record.checkNo].columnsFirst]"></BMPageComponent>
    </template>
  </BMPageComponent>
  <!-- 数量明细侧边框 -->
  <Cnt ref="cntRef" />
</template>

<script setup lang="ts">
  import { getSortingOutWarehouseList, getSortingOutWarehouseDetailList } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { Cnt } from './components/index';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'SortingOutStash',
  });

  // 查看数量明细
  const cntRef = ref();

  const openCnt = (row: any) => {
    cntRef.value?.showDrawer(row);
  };

  const { pageRef, columnsFirst, formFirstProps, expandMap, expandedRowKeys, expandChange } = useTable(openCnt);

  // 查询操作
  const getLists = async (params: any) => {
    const datas = {
      ...params,
    };
    const res = await getSortingOutWarehouseList(datas);

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
