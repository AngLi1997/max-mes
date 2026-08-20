<!-- 血浆检测记录表 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :titles="[t('血浆检测记录表')]"
    :showToolBars="[true]"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :requests="[getPlasmaTestRecordList as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderToolbar0>
      <Button v-hasAuth="170120001000001" type="primary" @click="showPrintModal">
        {{ t('打印血浆检测记录表') }}
      </Button>
    </template>
  </BMPageComponent>
  <printModal ref="printRef" />
</template>

<script setup lang="ts">
  import { getPlasmaTestRecordList } from '@/services';
  import { useTable } from './hooks';
  import { paginationBig } from '@/utils/paginationConfig';
  import { printModal } from './components';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';

  defineOptions({
    name: 'PlasmaTestRecord',
    inheritAttrs: false,
  });

  const { pageRef, columnsFirst, formFirstProps } = useTable();

  const printRef = ref();

  const showPrintModal = () => {
    printRef.value.openModal();
  };
</script>

<style lang="less" scoped></style>
