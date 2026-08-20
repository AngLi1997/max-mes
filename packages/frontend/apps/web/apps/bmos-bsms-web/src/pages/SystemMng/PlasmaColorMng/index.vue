<!-- 库存血浆颜色管理 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[false]"
    :hideRightTree="true"
    :showHeader="[false]"
    :showToolBars="[false]"
    :requests="[getPlasmaColorList as DataRequestFn]"
    :paginations="[paginationBig]"
    :columns="[columnsFirst]"></BMPageComponent>
  <EditColor ref="editColor" @submitSuccess="() => pageRef.fetchData()" />
</template>

<script setup lang="ts">
  import EditColor from './components/EditColor/index.vue';
  import { BMPageComponent, DataRequestFn } from '@bmos/components';
  import { useTable } from './hooks/useTable';
  import { getPlasmaColorList } from '@/services';
  import { paginationBig } from '@/utils/paginationConfig';

  defineOptions({
    name: 'PlasmaColorMng',
  });

  const editColor = ref();

  const { pageRef, columnsFirst } = useTable(editColor);
</script>

<style lang="less" scoped></style>
