<!-- 不合格血浆库存管理 -->
<template>
  <DubRowTable ref="dubTableRef" :leftTableProps="leftTableProps" :rightTableProps="rightTableProps">
    <template #leftexpandColumnTitle>{{}}</template>
    <template #leftexpandedRowRender="{ record, instance }">
      <BMPageComponent
        :ref="el => setLeftExpandRef(record.batchNo, el)"
        :rowKeys="['containerNo']"
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
        :scrolls="[{ x: 700, y: 300 }]"
        :showHeader="[false]"
        :showToolBars="[false]"
        :bordereds="[false]"
        :requests="[getWmsUnqualifiedPlasmaOutDetailList as DataRequestFn]"
        :columns="[expandedLeftTableMap[record.batchNo].columnsFirst]" />
    </template>
    <template #rightexpandColumnTitle>{{}}</template>
    <template #rightexpandedRowRender="{ record, instance }">
      <BMPageComponent
        :ref="el => setRightExpandRef(record.uniqueValue, el)"
        :rowKeys="['plasmaOrgNo']"
        :search="[false]"
        :hideRightTree="true"
        :tableFields="[
          {
            default: {
              ...instance.getQueryFormRef()?.handleFormValues(instance.getQueryFormRef().formModel),
              batchNo: record.batchNo,
              bigContainerNo: record.bigContainerNo,
            },
          },
        ]"
        :isExtraParamsChangeQuerys="[false]"
        :paginations="[paginationBig]"
        :scrolls="[{ x: 800, y: 300 }]"
        :showHeader="[false]"
        :showToolBars="[false]"
        :bordereds="[false]"
        :requests="[getWmsUnqualifiedPlasmaInDetailList as DataRequestFn]"
        :columns="[expandedRightTableMap[record.uniqueValue].columnsFirst]" />
    </template>
  </DubRowTable>
  <OutputModal ref="outputRef" @submitSuccess="submitSuccess" />
</template>

<script setup lang="ts">
  import DubRowTable from '@/components/DubRowTable/index.vue';
  import { useDubTable } from './hooks/useDubTable';
  import { OutputModal } from './components';
  import { paginationBig } from '@/utils/paginationConfig';
  import { getWmsUnqualifiedPlasmaOutDetailList, getWmsUnqualifiedPlasmaInDetailList } from '@/services';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';

  defineOptions({
    name: 'UnqualifiedPlasmaStock',
  });

  const outputRef = ref();

  const openOutputModal = (row: any) => {
    outputRef.value.openModal(row);
  };

  const {
    dubTableRef,
    leftTableProps,
    rightTableProps,
    fetchDubData,
    removeExpandedRightRowKeys,
    expandedLeftTableMap,
    expandedRightTableMap,
  } = useDubTable(openOutputModal);

  // 设置二级列表的ref
  const setLeftExpandRef = (key: any, ref: any) => {
    expandedLeftTableMap[key].setRef(ref);
  };

  const setRightExpandRef = (key: any, ref: any) => {
    expandedRightTableMap[key].setRef(ref);
  };

  const submitSuccess = (data: any) => {
    removeExpandedRightRowKeys(data.batchNo);
    fetchDubData();
  };
</script>

<style lang="less" scoped></style>
