<!-- 标本库存管理 -->
<template>
  <DubRowTable ref="dubTableRef" :leftTableProps="leftTableProps" :rightTableProps="rightTableProps">
    <template #leftexpandColumnTitle>{{}}</template>
    <template #leftexpandedRowRender="{ record, instance }">
      <BMPageComponent
        :ref="el => setLeftExpandRef(record.uniqueValue, el)"
        :rowKeys="['id']"
        :search="[false]"
        :hideRightTree="true"
        :tableFields="[
          {
            default: {
              ...instance.getQueryFormRef()?.handleFormValues(instance.getQueryFormRef().formModel),
              batchNo: record.batchNo,
              palletNo: record.palletNo,
              currentInventoryStatus: record.currentInventoryStatus?.value,
              sampleStatus: 1,
            },
          },
        ]"
        :isExtraParamsChangeQuerys="[false]"
        :paginations="[paginationBig]"
        :showHeader="[false]"
        :showToolBars="[false]"
        :bordereds="[false]"
        :requests="[getWmsQualifiedSampleSecondList as DataRequestFn]"
        :columns="[expandedLeftTableMap[record.uniqueValue].columnsFirst]" />
    </template>
    <template #rightexpandColumnTitle>{{}}</template>
    <template #rightexpandedRowRender="{ record, instance }">
      <BMPageComponent
        :ref="el => setRightExpandRef(record.uniqueValue, el)"
        :rowKeys="['id']"
        :search="[false]"
        :hideRightTree="true"
        :tableFields="[
          {
            default: {
              ...instance.getQueryFormRef()?.handleFormValues(instance.getQueryFormRef().formModel),
              batchNo: record.batchNo,
              palletNo: record.palletNo,
              currentInventoryStatus: record.currentInventoryStatus?.value,
              sampleStatus: 1,
            },
          },
        ]"
        :isExtraParamsChangeQuerys="[false]"
        :paginations="[paginationBig]"
        :showHeader="[false]"
        :showToolBars="[false]"
        :bordereds="[false]"
        :requests="[getWmsQualifiedSampleSecondList as DataRequestFn]"
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
  import { getWmsQualifiedSampleSecondList } from '@/services';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';

  defineOptions({
    name: 'SpecimenStockMng',
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
    expandedLeftTableMap,
    expandedRightTableMap,
    removeExpandedRightRowKeys,
  } = useDubTable(openOutputModal);

  // 设置二级列表的ref
  const setLeftExpandRef = (key: any, ref: any) => {
    expandedLeftTableMap[key].setRef(ref);
  };

  const setRightExpandRef = (key: any, ref: any) => {
    expandedRightTableMap[key].setRef(ref);
  };

  const submitSuccess = (batchNo: any) => {
    removeExpandedRightRowKeys(batchNo);
    fetchDubData();
  };
</script>

<style lang="less" scoped></style>
